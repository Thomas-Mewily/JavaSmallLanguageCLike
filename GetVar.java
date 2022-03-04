public class GetVar extends Exp
{
    String Identifier;

    public GetVar(String identifier)
    {
        Identifier = identifier;
    }

    public @Override void Eval(Cpu c)
    {
        c.Push(c.Env.GetVar(Identifier));
    }

    public @Override String toString()
    {
        return Identifier;
    }
}