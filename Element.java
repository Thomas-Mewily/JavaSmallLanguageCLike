public abstract class Element
{
    public abstract void Eval(Cpu c);
    
    public static Token Error() {return new Token("error","\0"); }

    public boolean IsExp()  { return this instanceof Exp; }
    public Exp AsExp()      { return IsExp() ? (Exp)this : null; }

    public boolean  IsConstExp() { return this instanceof ConstExp; }
    public ConstExp AsConstExp() { return IsConstExp() ? (ConstExp)this : null; }

    public boolean  IsBinExp()   { return this instanceof BinOp; }
    public BinOp    AsBinExp()   { return IsBinExp() ? (BinOp)this : null; }

    public boolean   IsStmt()   { return this instanceof Statement; }
    public Statement AsStmt()   { return IsStmt() ? (Statement)this : null; }

    public boolean IsToken()   { return this instanceof Token; }
    public Token   AsToken()   { return IsToken() ? (Token)this : Token.Error(); }

    public static String ColoredKeyword(String keyword)
    {
        String prefix = ConsoleColor.ANSI_GREEN;
        switch(keyword)
        {
            case "if": case "else": case "while":
            prefix = ConsoleColor.ANSI_PURPLE; break;
            default:break;
        }
        return prefix+keyword+ConsoleColor.ANSI_WHITE;
    }
    public static String ColoredToken(String token)
    {
        String prefix = ConsoleColor.ANSI_WHITE;
        switch(token)
        {
            case ";": prefix = ConsoleColor.ANSI_WHITE; break;
            case "?": case "{": case "}": prefix = ConsoleColor.ANSI_PURPLE; break;
            case "(": case ")": prefix = ConsoleColor.ANSI_CYAN; break;
            default:break;
        }
        return prefix+token+ConsoleColor.ANSI_WHITE;
    }
}