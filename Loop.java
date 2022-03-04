public abstract class Loop extends Statement
{
    public Exp Cond;
    public Statement Inside;

    public Loop(Exp cond, Statement inside)
    {
        Cond = cond;
        Inside = inside;
    }

    public @Override String toStringStmt()
    {
        return "loop: "+Cond+Inside;
    }
}