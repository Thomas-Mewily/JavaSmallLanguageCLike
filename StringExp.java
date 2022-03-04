public class StringExp extends Exp
{
    public String Value;
    public StringExp(String value){ Value = value; }

    public @Override String toString()
    {
        return ConsoleColor.ANSI_RED +'"'+Value.replace("\n", "\\n")+'"'+ ConsoleColor.ANSI_WHITE;
    }

    public void Eval(Cpu c)
    {
        c.Push(c.MallocString(Value));
    }
}