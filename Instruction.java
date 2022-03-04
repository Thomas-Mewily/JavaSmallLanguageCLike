import java.util.*;

public class Instruction extends Statement
{
    public String Type;
    public List<Exp> Parameters;

    private static List<Exp> ExpToList(Exp exp) { var a = new ArrayList<Exp>(); a.add(exp); return a; }
    public Instruction(String type, Exp exp) { this(type, ExpToList(exp)); }
    public Instruction(String type, List<Exp> parameters)
    {
        Type = type;
        Parameters = parameters;
    }
    
    public @Override String toStringStmt()
    {
        String arg = "";
        for(int i= 0;i<Parameters.size();i++)
        {
            arg+=Parameters.get(i);
            if(i!=Parameters.size()-1){arg+=ColoredToken(", ");}
        }
        return ColoredKeyword(Type)+ColoredToken("(")+arg+ColoredToken(")")+ColoredToken(";");
    }

    private void putchar(char c){ System.out.print(c); }

    public @Override void EvalStmt(Cpu c)
    {
        for(var p : Parameters)
        {
            c.Execute(p);
        }
        switch(Type)
        {
            case "free": c.Free(c.Pop()); break;
            case "print": System.out.print(c.Pop()); break;
            case "putchar": putchar((char)c.Pop()); break;
            case "array_set": c.DeRefAssign(c.Pop(), c.Pop(), c.Pop()); break;
            case "printstr": 
            {
                for(var v : c.Malloc.get(c.Pop()))
                {
                    putchar((char)(double)v);
                }
            }break;
            case "printstrnf": 
            {
                var ptr = c.Pop();
                for(var v : c.Malloc.get(ptr))
                {
                    putchar((char)(double)v);
                }
                c.Free(ptr);
            }break;
            default: Useful.crash("Unknow instruction : '"+Type+"'"); break;
        }
    }
}