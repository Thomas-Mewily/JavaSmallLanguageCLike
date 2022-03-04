public class SetVar extends DeclareVar
{
    public SetVar(String name, Exp exp){ super(name, exp); }

    public @Override String toString()
    {
        return Name+" = "+Value+";";
    }

    public void EvalStmt(Cpu c)
    {
        c.Execute(Value);
        c.Env.SetVar(Name, c.Pop());
    }
}