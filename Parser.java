public abstract class Parser
{
    public int IndexChar;
    public String Code;
    
    public boolean IsCharEOF() {return IndexChar < 0 || IndexChar >= Code.length(); }
    public char ReadChar() { return IsCharEOF() ? '\0' : Code.charAt(IndexChar);}
    public char ReadAndMoveChar() 
    {
        char c = ReadChar();
        IndexChar++;
        return c;
    }

    public boolean MoveMatch(String keyword)
    {
        if(Match(keyword))
        {
            IndexChar+=keyword.length();
            return true;
        }
        return false;
    }
    public boolean Match(String keyword)
    {
        if(IndexChar+keyword.length() > Code.length()){ return false;}

        for(int i = 0;i<keyword.length();i++)
        {
            if(Code.charAt(IndexChar+i) != keyword.charAt(i)){ return false;}
        }
        return true;
    }

    public Parser(String code)
    {
        Code = code;
        IndexChar = 0;
    }
    
    public Element Parse()
    {
        IndexChar = 0;
        return _Parse();
    }
    public abstract Element _Parse();

    public Element ErrorUnexceptedChar()
    {
        if(IsCharEOF()) { IndexChar = Math.max(0, IndexChar-1);}
        Useful.crash("Unexpected char: '"+ReadChar()+"' at index "+IndexChar);
        return null;
    }
}