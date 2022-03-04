public class UnaryOp extends Exp
{
    public Exp Right;
    public String Type;

    public UnaryOp(String type, Exp right)
    {
        Right = right;
        Type = type;
    }

    public @Override String toString()
    {
        return ColoredToken("(")+Type+" "+Right.toString()+ColoredToken(")");
    }

    public @Override void Eval(Cpu c)
    {
        c.Execute(Right);
        switch(Type)
        {
            case "-": c.Push(-c.Pop()); break;
            case "!": c.Push(1-c.Pop()); break; //boolean negation
            case "malloc": c.Push(c.Malloc((int)c.Pop())); break;
            case "length": c.Push(c.Size(c.Pop())); break;

            case "abs": c.Push(Math.abs(c.Pop()));  break;

            case "cos": c.Push(Math.cos(c.Pop()));  break;
            case "sin": c.Push(Math.sin(c.Pop()));  break;
            case "tan": c.Push(Math.tan(c.Pop()));  break;
            case "cosh": c.Push(Math.cosh(c.Pop()));  break;
            case "sinh": c.Push(Math.sinh(c.Pop()));  break;
            case "tanh": c.Push(Math.tanh(c.Pop()));  break;
            case "acos": c.Push(Math.acos(c.Pop()));  break;
            case "asin": c.Push(Math.asin(c.Pop()));  break;
            case "atan": c.Push(Math.atan(c.Pop()));  break;

            case "floor": c.Push(Math.floor(c.Pop()));  break;
            case "ceil" : c.Push(Math.ceil (c.Pop()));  break;
            case "round": c.Push(Math.round(c.Pop()));  break;

            case "random": c.Push(Math.random()*c.Pop()); break;
            case "log"   : c.Push(Math.log(c.Pop()));     break;
            case "log10" : c.Push(Math.log10(c.Pop()));   break;
            case "exp"   : c.Push(Math.exp(c.Pop()));     break;
            case "sqrt"  : c.Push(Math.sqrt(c.Pop()));     break;

            default: Useful.crash("Unknow unary operator : '"+Type+"'"); break;
        }
    }
}