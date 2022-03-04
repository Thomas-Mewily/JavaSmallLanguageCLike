public class Token extends Element
{
    String Type;
    String Symbol;

    public Token(String type,  char symbol){this(type, symbol+"");}
    public Token(String type,  String symbol)
    {
        Type = type;
        Symbol = symbol;
    }

    public @Override String toString()
    {
        return "<"+Type+":\""+Symbol+"\">";
    }

    public @Override void Eval(Cpu c)
    {
        Useful.crash("Can't evaluate a token: "+toString());
    }

    public boolean IsType(String type){return Type.equals(type);}
    public boolean IsSymbol(String lexem){return Symbol.equals(lexem);}
    public boolean Is(String type, String lexem){return IsType(type) && IsSymbol(lexem);}

    public boolean IsUnknow(){ return IsType(CParser.TYPE_UNKNOW); }
    public boolean IsInstructionEnd(){ return Is(CParser.TYPE_BASIC, ";"); }
}