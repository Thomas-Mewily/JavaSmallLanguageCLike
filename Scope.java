import java.util.*;

public class Scope extends Statement
{
    public List<Statement> Inside;

    public Scope()
    {
        Inside = new ArrayList<Statement>();
    }

    public @Override void EvalStmt(Cpu c)
    {
        var env = new Environement(c.Env);
        c.Env = env;

        for(var s : Inside)
        {
            c.Execute(s);
        }

        c.Env = c.Env.Parent;
    }
    public @Override String toStringStmt() 
    { 
        StringBuilder sb = new StringBuilder();
        sb.append(ColoredToken("{"));
        sb.append("\n");
        for(var stmt : Inside)
        {
            for(var s : stmt.toString().split("\n"))
            {
                sb.append("    ");
                sb.append(s.toString());
                sb.append("\n");
            }
        }
        sb.append(ColoredToken("}"));
        var str = sb.toString();
        return str;
    }
}