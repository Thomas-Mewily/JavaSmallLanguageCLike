import java.util.*;

public class Cpu
{
    public List<Double> Stack;
    public Environement Env;
    public Map<Double, List<Double>> Malloc;
    private double MallocIndex;

    public static final double NULL_PTR = 0;

    public Cpu()
    {
        Stack = new ArrayList<Double>();
        Env = new Environement(null);
        Malloc = new HashMap<Double, List<Double>>();
        MallocIndex = NULL_PTR+1;
    }

    public double Peek(){ return Stack.get(Stack.size()-1); }
    public double Pop() { return Stack.remove(Stack.size()-1); }
    public void Push(double d) { Stack.add(d); }
    public void Push(boolean b) { Push(b ? 1 : 0); }
    public void Dup(double d) { Push(Peek()); }

    public double DeRef(double ptr, double offset)
    {
        return Malloc.get(ptr).get((int)offset);
    }
    public void DeRefAssign(double ptr, double offset, double value)
    {
        Malloc.get(ptr).set((int)offset, value);
    }
    public double Size(double ptr)
    {
        if(ptr == NULL_PTR){ return -1;}
        return Malloc.get(ptr).size();
    }

    public double Malloc(int nbValue)
    {
        if(nbValue <= 0) {return NULL_PTR;}
        while(Malloc.containsKey(MallocIndex)){MallocIndex++;}
        List<Double> a = new ArrayList<Double>(nbValue);
        for(int i = 0;i<nbValue;i++)
        {
            a.add(0.0);
        }
        Malloc.put(MallocIndex, a);
        return MallocIndex;
    }

    public double MallocString(String Value)
    {
        var ptr = Malloc(Value.length());
        for(int i =0;i<Value.length();i++)
        {
            DeRefAssign(ptr, i, Value.charAt(i));
        }
        return ptr;
    }

    public int NbDoubleAllocated()
    {
        int nb = 0;
        for(var v : Malloc.values())
        {
            nb+=v.size();
        }
        return nb;
    }

    public double Realloc(double ptr, int nbValue)
    {
        if(nbValue == 0) { Free(ptr); }
        if(Malloc.containsKey(ptr) == false){ return Malloc(nbValue); }
        
        var a = Malloc.get(ptr);

        if(a.size() > nbValue)
        {
            for(int i = a.size()-nbValue; i > 0; i--)
            {
                a.remove(a.size()-1);
            }
        }else
        {
            for(int i = a.size()-nbValue; i > 0; i--)
            {
                a.add(0.0);
            }
        }
        return ptr;
    }

    public void Free(double ptr)
    {
        if(Malloc.containsKey(ptr) == false) { Useful.crash("Invalid Free Pointer: "+ptr);}
        Malloc.remove(ptr);
    }

    public @Override String toString()
    {
        return "stack: "+Stack.toString()+"\nenv: "+Env.toString()+"\nmalloc (index: "+MallocIndex+", nb double: "+NbDoubleAllocated()+"): "+Malloc.toString();
    }

    public void Execute(Element e){e.Eval(this); }
}