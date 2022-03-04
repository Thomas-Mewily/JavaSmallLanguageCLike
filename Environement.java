import java.util.*;

public class Environement
{
    public Environement Parent;
    private Map<String, Double> Variables;

    public Environement(Environement parent)
    {
        Parent = parent;
        Variables = new HashMap<String, Double>();
    }
    
    public boolean IsVarLocalyDeclared(String name){return Variables.containsKey(name);}
    public boolean IsVarDeclared(String name)
    {
        if(Variables.containsKey(name)) { return true;}
        if(Parent != null) {return Parent.IsVarDeclared(name); }
        return false;
    }

    public void SetVar(String name, double value)
    {
        if(Variables.containsKey(name)) { Variables.replace(name, value); return; }
        if(Parent == null)
        {
            Useful.crash("Variable <" + name + "> not defined");
        }
        Parent.SetVar(name, value);
    }

    public double GetVar(String name)
    {
        if(Variables.containsKey(name)){return Variables.get(name);}
        if(Parent != null){return Parent.GetVar(name); }
        Useful.crash("Variable <" + name + "> not defined");
        return 0;
    }

    public void DeclareVar(String name, double value)
    {
        if(IsVarLocalyDeclared(name))
        {
            Useful.crash("Variable <" + name + "> already localy defined");
        }
        Variables.put(name, value);
    }

    public @Override String toString()
    {
        return Variables.toString() + (Parent!= null ? "\n" + Parent.toString() : "");
    }
}