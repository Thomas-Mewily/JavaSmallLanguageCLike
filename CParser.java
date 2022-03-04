import java.util.*;

public class CParser extends Parser
{
    private List<Element> Elems;
    public int IndexElem;

    public int   nbToken(){return Elems.size();}
    public void  PushEnd(Element e){ Elems.add(e); }
    public Element PopEndToken ()       { return Elems.remove(nbToken()-1); }
    public Element PeekEndToken()       { return Elems.get(nbToken()-1); }

    public boolean IsElemError() {return IndexElem < 0 || IndexElem >= CountElem(); }
    public Element PeekElem() { return IsElemError() ? Element.Error() : Elems.get(IndexElem);}
    public Element PopElem()  { return Elems.remove(IndexElem--); }
    public void    PushElem(Element e){ Elems.add(++IndexElem, e); }
    public Element PeekAndMoveElem() 
    {
        Element t = PeekElem();
        IndexElem++;
        return t;
    }
    public int CountElem(){return Elems.size();}

    public CParser(String code)
    {
        super(code);
        Elems = new ArrayList<Element>();
    }
    
    public Element _Parse()
    {
        IndexElem = -1;
        LookAheadElement = new ArrayDeque<Element>();

        while(IsCharEOF() == false || LookAheadElement.size() != 0)
        {
            ParseStmt();
        }
        
        Scope s = new Scope();
        if(CountElem() == 0){ return s;}
        if(CountElem() == 1)
        {
             var stmt = Elems.get(0);
             Elems.clear();
             return stmt;
        }

        for(var stmt : Elems)
        {
            if(stmt.IsStmt() == false){ ErrorUnexceptedElem(stmt);}
            s.Inside.add(stmt.AsStmt());
        }
        Elems.clear();
        return s;
    }

    public static final String TYPE_UNKNOW = "";
    public static final String TYPE_BIN_OP = "bin_op";
    public static final String TYPE_BASIC  = "basic";
    public static final String TYPE_KEYWORD  = "keyword";
    public static final String TYPE_IDENTIFIER = "identifier";

    public static boolean IdentifierChar(char c)
    {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == '_';
    }

    private void ParseUnary(String op)
    {
        ParseStmt(); // push the second operand
        var right = PopElem();
        if("+".equals(op) == false)
        {
            if(right.IsExp() == false){ ErrorUnexceptedElem(right); }
            PushElem(new UnaryOp(op, right.AsExp()));
        }else { PushElem(right);}
    }

    private Queue<Element> LookAheadElement;

    private void ParseStmt()
    {
        if(LookAheadElement.size() != 0)
        {
            PushElem(LookAheadElement.poll());
            return;
        }

        char c = ReadAndMoveChar();
        switch(c)
        {
            case '+': case '-':
            case '*': case '/': case '%':
            case '=': case '!':
            case '&': case '|':
            case '>': case '<':
            {
                String op = c+"";
                switch(c)
                {
                    case '/':
                    {
                        //maybe a comment
                        c = ReadAndMoveChar();
                        switch(c)
                        {
                            // yep that a comment
                            case '/': while("\0\n".indexOf(ReadAndMoveChar()) == -1); ParseStmt(); return;
                            // yep that a comment too
                            case '*': while("\0*".indexOf(ReadAndMoveChar()) == -1 && "\0\\".indexOf(ReadChar()) == -1); ReadAndMoveChar(); ParseStmt(); return;
                            default: IndexChar--; break;
                        }
                    }break;
                    case '<': case '>':
                    {
                        if(ReadAndMoveChar() == '=')
                        {
                            op += "="; break;
                        }else{ IndexChar--;}
                    }
                    break;
                    case '!':
                    {
                        if(ReadAndMoveChar() == '=')
                        {
                            op = "!="; break;
                        } else
                        {
                            IndexChar--;
                            ParseUnary("!");
                            return;
                        }
                    }
                    case '=': // comparison or affectation
                    {
                        c = ReadAndMoveChar();
                        switch(c)
                        {
                            // comparison
                            case '=': op = "=="; break;
                            // affectation
                            default:
                            { 
                                IndexChar--;
                                var lvalue = PopElem();
                                if(lvalue.IsExp() == false){ErrorUnexceptedElem(lvalue); }
                
                                ParseExpAndSeparator();
                                ConsumeInstructionEndToken();
                                var right = PopElem();
                                if(right.IsExp() == false){ErrorUnexceptedElem(right); }
                
                                if(lvalue.IsBinExp() && lvalue.AsBinExp().Type == "array_get")
                                {
                                    var array_set = lvalue.AsBinExp();
                                    var a = new ArrayList<Exp>();
                                    a.add(right.AsExp()); a.add(array_set.Right); a.add(array_set.Left);
                                    PushElem(new Instruction("array_set", a));
                                }else if(lvalue instanceof GetVar getVar)
                                {
                                    PushElem(new SetVar(getVar.Identifier, right.AsExp()));
                                }
                                return;
                            }
                        }
                    }break;
                }

                if(PeekElem().IsExp())
                { //binary op
                    PushElem(new Token(TYPE_BIN_OP,  op));
                    ParseStmt(); // push the second operand
                    //ParseToken(); // push the after element
   
                    //Element after  = PopElem();

                    var right = PopElem();
                    if(right.AsExp() == null)
                    {
                        ErrorUnexceptedElem(right);
                    }

                    var middle = PopElem();
                    if(middle.AsToken().Type != TYPE_BIN_OP)
                    {
                        ErrorUnexceptedElem(middle);
                    }

                    var left = PopElem();
                    if(left.AsExp() == null)
                    {
                        ErrorUnexceptedElem(left);
                    }

                    BinOp operation = null;
                    if(left.IsBinExp() &&  GetBinOpPriority(left.AsBinExp().Type) < GetBinOpPriority(middle.AsToken().Symbol))
                    {
                        var leftBinOp = left.AsBinExp();
                        operation = new BinOp(middle.AsToken().Symbol, leftBinOp.Right, right.AsExp());
                        leftBinOp.Right = operation;
                        operation = leftBinOp;
                    }else
                    {
                        operation = new BinOp(middle.AsToken().Symbol, left.AsExp(), right.AsExp());
                    }
                    PushElem(operation);
                }else { ParseUnary(op); }
            }
            break;
            case '(':
            {
                PushElem(new Token(TYPE_BASIC,  "("));
                do
                {
                    ParseStmt();
                }while(PeekElem().AsToken().Is(TYPE_BASIC, ")") == false && IsCharEOF() == false);
                ConsumeToken(TYPE_BASIC, ")");

                var idx = IndexElem;
                while(PeekElem().AsToken().Is(TYPE_BASIC, "(") == false)
                {
                    IndexElem--;
                }
                ConsumeToken(TYPE_BASIC, "(");
                IndexElem = idx-1;
                /*
                if(PeekElem().AsToken().Is(TYPE_BASIC, "("))
                {
                    ConsumeToken(TYPE_BASIC, "(");
                }else
                {
                    IndexElem--;
                    ConsumeToken(TYPE_BASIC, "(");
                    IndexElem++;
                }*/
            }
            break;
            case '{':
            {
                PushElem(new Token(TYPE_BASIC,  "{"));
                do
                {
                    ParseStmt();
                }while(PeekElem().AsToken().Is(TYPE_BASIC, "}") == false && IsCharEOF() == false);
                ConsumeToken(TYPE_BASIC, "}");

                Scope sc = new Scope();
                while(PeekElem().AsToken().Is(TYPE_BASIC, "{") == false)
                {
                    var stmt = PopElem();
                    if(stmt instanceof Statement == false) { ErrorUnexceptedElem(stmt); }
                    sc.Inside.add((Statement)stmt);
                }
                ConsumeToken(TYPE_BASIC, "{");
                Collections.reverse(sc.Inside);
                PushElem(sc);
            }
            break;
            case '[':
            {
                PushElem(new Token(TYPE_BASIC,  "["));
                do
                {
                    ParseStmt();
                }while(PeekElem().AsToken().Is(TYPE_BASIC, "]") == false);

                ConsumeToken(TYPE_BASIC, "]");
                var right = PopElem();
                if(right.IsExp() == false){ErrorUnexceptedElem(right);}
                ConsumeToken(TYPE_BASIC, "[");
                var left = PopElem();
                if(left.IsExp() == false){ErrorUnexceptedElem(left);}
                PushElem(new BinOp("array_get", left.AsExp(), right.AsExp()));
            }
            break;
            case ')': case ']': case '}':
            case '.': case ',': case ';':
            PushElem(new Token(TYPE_BASIC,  c));
            break;
            case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
            {
                IndexChar--;
                double val = 0;
                while(IsCharEOF() == false && "0123456789".indexOf(ReadChar()) != -1)
                {
                    val = val*10+ ReadAndMoveChar()-'0';
                }
                if(ReadChar() == '.')
                {
                    ReadAndMoveChar();
                    double decimal = 0;
                    int nbDecimal = 0;
                    while(IsCharEOF() == false && "0123456789".indexOf(ReadChar()) != -1)
                    {
                        decimal = decimal*10+ ReadAndMoveChar()-'0';
                        nbDecimal++;
                    }
                    if(nbDecimal == 0){ ErrorUnexceptedChar(); }
                    val = val+decimal/Math.pow(10, nbDecimal);
                }
                PushElem(new ConstExp(val));
            } break;
            case ' ': case '\t': case '\r': case '\n': ParseStmt(); break; // Useless token
            case '\0': break;
            case '\'': 
            {
                c = ParseCharString();
                PushElem(new ConstExp(c));
                c = ReadAndMoveChar();
                if(c != '\'') { ErrorUnexceptedChar(); }
            }
            break;
            case '"':
            {
                String v = "";
                while(ReadChar() != '"')
                {
                    v += ParseCharString();
                    if(IsCharEOF()) { ErrorUnexceptedChar(); }
                }
                ReadAndMoveChar();
                PushElem(new StringExp(v));
            }
            break;
            default: 
            {
                if(IdentifierChar(c))
                {
                    String identifier = "";
                    do
                    {
                        identifier += c;
                        c = ReadAndMoveChar();
                    }while(IdentifierChar(c));
                    IndexChar--;
                    //PushElem(new Token(TYPE_IDENTIFIER,  identifier));
                    switch(identifier)
                    {
                        case "print" : case "printstr": case "printstrnf": case "putchar": case "free": 
                        {  // instruction
                            ParseExpAndSeparator();
                            ConsumeInstructionEndToken();
                            var exp = PopElem();
                            if(exp.IsExp() == false){ErrorUnexceptedElem(exp);}
                            PushElem(new Instruction(identifier, exp.AsExp()));
                        }break;
                        case "malloc": case "length":
                        case "abs":
                        case "cos":  case "sin":  case "tan":
                        case "acos": case "asin": case "atan":
                        case "cosh": case "sinh": case "tanh":
                        // Round :
                        case "floor": case "ceil": case "round":
                        // Various :
                        case "random":
                        case "log":
                        case "log10":
                        case "exp":
                        case "sqrt":
                        ParseUnary(identifier); break; // expression
                        case "pow":
                        case "min":
                        case "max":
                        case "atan2":
                        {
                            ParseFunctionArg(identifier, 2);
                            var right = PopElem();
                            var left = PopElem();
                            PushElem(new BinOp(identifier, left.AsExp(), right.AsExp()));
                        }
                        break;
                        case "var":
                        {
                            // declaration: var name; (todo)
                            // declaration & affectation: var name = exp;
                            do
                            {
                                ParseStmt();
                            }while(PeekElem() instanceof SetVar == false);

                            var setVar = (SetVar)PopElem();
                            PushElem(new DeclareVar(setVar.Name, setVar.Value));                            
                        }break;
                        case "ask":  case "askchar": case "askstr": PushElem(new AskExp(identifier)); break;
                        case "true":  PushElem(new ConstExp(1)); break;
                        case "false": case "null": PushElem(new ConstExp(0)); break;
                        case "else": PushElem(new Token(TYPE_KEYWORD, identifier)); break;
                        case "if":
                        {
                            ParseExpAndSeparator();
                            var stmt = PopElem();
                            if(stmt.IsStmt() == false) { ErrorUnexceptedElem(stmt); }
                            var cond = PopElem();
                            if(cond.IsExp() == false) { ErrorUnexceptedElem(cond); }
                            Statement elseStmt = null;
                            
                            ParseStmt();
                            Element nextElementIfNotElseStmt = null;
                            if(CountElem() > 0)
                            {
                                nextElementIfNotElseStmt = PopElem();
                                if(nextElementIfNotElseStmt.AsToken().Is(TYPE_KEYWORD, "else"))
                                {
                                    ParseStmt();
                                    if(PeekElem().IsStmt() == false){ErrorUnexceptedElem(PeekElem());}
                                    elseStmt = PopElem().AsStmt();
                                    nextElementIfNotElseStmt = null;
                                }
                            }

                            PushElem(new IfElseStmt(cond.AsExp(), stmt.AsStmt(), elseStmt));
                            if(nextElementIfNotElseStmt != null){ LookAheadElement.add(nextElementIfNotElseStmt); }
                        }
                        break;
                        case "while":
                        {
                            ParseExpAndSeparator();
                            var stmt = PopElem();
                            if(stmt.IsStmt() == false) { ErrorUnexceptedElem(stmt); }
                            var cond = PopElem();
                            if(cond.IsExp() == false) { ErrorUnexceptedElem(cond); }

                            PushElem(new WhileLoop(cond.AsExp(), stmt.AsStmt()));
                        }
                        break;
                        default: PushElem(new GetVar(identifier)); break;
                    }
                    break;
                }
                ErrorUnexceptedChar(); 
            }
            break;
        }
    }
    
    public char ParseCharString() // Todo Bad support for ' and " (for char and string)
    {
        char c = ReadAndMoveChar();
        if(c == '\\')
        {
            switch(ReadAndMoveChar())
            {
                case '\'': c = '\''; break;
                case '\\': c = '\\'; break;
                case  'n': c = '\n'; break;
                case  'r': c = '\r'; break;
                case  't': c = '\t'; break;
                case  '0': c = '\0'; break;
                default: ErrorUnexceptedChar(); break;
            }
        }else if(c== '\''){ ErrorUnexceptedChar();}
        return c;
    }

    public void ParseFunctionArg(String funcName, int nbArg)
    {
        var idx = IndexElem;
        ParseStmt(); // Parenthesis
        var targetIdx = idx + Math.max(0, nbArg*2-1);
        if(targetIdx != IndexElem)
        {
            Useful.crash("Error expected: "+nbArg+" argument for function "+funcName+"()");
        }
        for(int i = 0;i<nbArg-1;i++)
        {
            if(PeekElem().IsExp() == false){ ErrorUnexceptedElem(PeekElem()); }
            IndexElem--;
            ConsumeToken(TYPE_BASIC, ",");
        }
        IndexElem += nbArg-1;
    }

    public void ParseExpAndSeparator()
    {
        do
        {
            ParseStmt();
        }while(PeekElem().IsExp() && IsCharEOF() == false);
    }
    public void ParseNCommaSeparatedExp(int n)
    {
        if(n <= 0) { return; }
        ParseExpAndSeparator();

        if(n-1 != 0)
        {
            ConsumeToken(TYPE_BASIC, ",");
        }
        ParseNCommaSeparatedExp(n-1);
    }

    public void ConsumeInstructionEndToken()
    {
        ConsumeToken(TYPE_BASIC, ";");
    }

    public void ConsumeToken(String type, String symbol)
    {
        var e = PopElem();
        if(e.AsToken().Is(type, symbol) == false)
        {
            ErrorUnexceptedElem(e);
        }
    }

    public int GetBinOpPriority(String s)
    {
        switch(s)
        {
            case "*": case "/": case "%": return 2;
            case "+": case "-": return 1;
            case "==": case "!=": return 0;
            default: return -1;
        }
    }

    public Element ErrorUnexceptedElem(Element elem)
    {
        Useful.crash("Unexpected element : "+elem);
        return null;
    }
}