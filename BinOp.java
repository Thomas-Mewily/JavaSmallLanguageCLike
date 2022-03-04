public class BinOp extends Exp
{
    public Exp Left;
    public Exp Right;
    public String Type;

    public BinOp(String type, Exp left, Exp right)
    {
        Left = left;
        Right = right;
        Type = type;
    }

    public @Override String toString()
    {
        return Type.length() <= 2 ? ColoredToken("(")+Left.toString()+" "+ Type+" "+Right.toString()+ColoredToken(")")
                                  : Type+ColoredToken("(")+Left.toString()+", "+Right.toString()+ColoredToken(")");
    }


    public void Eval(Cpu c)
    {
        c.Execute(Right);
        c.Execute(Left);
        switch(Type)
        {
            case "+": c.Push(c.Pop()+c.Pop()); break;
            case "-": c.Push(c.Pop()-c.Pop()); break;
            case "*": c.Push(c.Pop()*c.Pop()); break;
            case "/": c.Push(c.Pop()/c.Pop()); break;
            case "==": c.Push(c.Pop() == c.Pop()); break;
            case ">=": c.Push(c.Pop() >= c.Pop()); break;
            case ">" : c.Push(c.Pop() >  c.Pop()); break;
            case "<" : c.Push(c.Pop() <  c.Pop()); break;
            case "<=": c.Push(c.Pop() <= c.Pop()); break;
            case "!=": c.Push(c.Pop() != c.Pop()); break;
            case "&" : c.Push(c.Pop() != 0 & c.Pop() != 0); break;
            case "|" : c.Push(c.Pop() != 0 | c.Pop() != 0); break;

            case "array_get": c.Push(c.DeRef(c.Pop(), c.Pop())); break;

            case "pow" : c.Push(Math.pow(c.Pop(), c.Pop())); break;
            case "min" : c.Push(Math.min(c.Pop(), c.Pop())); break;
            case "max" : c.Push(Math.max(c.Pop(), c.Pop())); break;
            case "atan2" : c.Push(Math.atan2(c.Pop(), c.Pop())); break;
            default: Useful.crash("Unknow binary operator : '"+Type+"'"); break;
        }
    }
}