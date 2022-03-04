public class AskExp extends Exp
{
    public String Type;

    public AskExp(String type)
    {
        Type = type;
    }

    private double Ask()
    {
        try
        {
            return Double.parseDouble(ConsoleInput.scanner.nextLine());
        }catch (Exception e){ return Ask(); }
    }

    public void Eval(Cpu c)
    {
        switch(Type)
        {
            case "ask": c.Push(Ask()); break;
            case "askchar": c.Push(ConsoleInput.scanner.next().charAt(0)); break;
            case "askstr": c.Push(c.MallocString(ConsoleInput.scanner.nextLine())); break;
            default: Useful.crash("Unknow AskExp: "+Type);
        }
    }

    public @Override String toString()
    {
        return ColoredKeyword(Type);
    }
}