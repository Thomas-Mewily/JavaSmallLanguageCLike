public abstract class Statement extends Element
{
    public Statement Next = null;

    public @Override String toString()
    {
        return toStringStmt()+(Next == null ? "": "\n"+Next.toString());
    }
    public String toStringFormatAfterCond() { return (this instanceof Scope ? "\n":" ")+this+"\n"; }
    
    public @Override void Eval(Cpu c)
    {
        EvalStmt(c);
        if(Next != null)
        {
            Next.Eval(c);
        }
    }

    public abstract void EvalStmt(Cpu c);
    public abstract String toStringStmt();
}