public class IfElseStmt extends Statement
{
    public Exp Cond;
    public Statement CondTrue;
    public Statement CondFalse;

    public boolean IsIfElse(){return CondFalse != null;}

    public IfElseStmt(Exp cond, Statement condTrue, Statement condFalse)
    {
        Cond = cond;
        CondTrue = condTrue;
        CondFalse = condFalse;
    }

    public @Override String toStringStmt()
    {
        return ColoredKeyword("if")+" "+Cond+CondTrue.toStringFormatAfterCond()+(IsIfElse() ? ColoredKeyword("else")+" "+CondFalse:"");
    }
    
    public @Override void EvalStmt(Cpu c)
    {
        Cond.Eval(c);
        if(c.Pop() != 0){ CondTrue.Eval(c);}
        else if(IsIfElse())
        {
            CondFalse.Eval(c);
        }
    }
}