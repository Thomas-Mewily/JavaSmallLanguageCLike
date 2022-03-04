public class WhileLoop extends Loop
{
    public WhileLoop(Exp cond, Statement inside) { super(cond, inside); }

    public @Override String toStringStmt()
    {
        return ColoredKeyword("while")+" "+Cond+Inside.toStringFormatAfterCond();
    }

    public @Override void EvalStmt(Cpu c)
    {
        Cond.Eval(c);
        while(c.Pop() != 0)
        {
            Inside.Eval(c);
            Cond.Eval(c);
        }
    }
}