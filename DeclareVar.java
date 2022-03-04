public class DeclareVar extends Statement
{
    public String Name;
    public Exp Value;

    public DeclareVar(String name, Exp defaultValue)
    {
        Name = name;
        Value = defaultValue;
    }

    public @Override String toStringStmt()
    {
        return ColoredKeyword("var") +" "+ Name + " " + ColoredToken("=") + " " + Value + ColoredToken(";");
    }

    public void EvalStmt(Cpu c)
    {
        c.Execute(Value);
        c.Env.DeclareVar(Name, c.Pop());
    }
}