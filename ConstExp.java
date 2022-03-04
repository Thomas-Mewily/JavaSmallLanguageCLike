public class ConstExp extends Exp
{
    double Value;

    public ConstExp(double value)
    {
        Value = value;
    }

    public void Eval(Cpu c)
    {
        c.Push(Value);
    }

    public @Override String toString()
    {
        return ConsoleColor.ANSI_YELLOW+Double.toString(Value)+ConsoleColor.ANSI_WHITE;
    }
}