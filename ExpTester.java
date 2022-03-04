public class ExpTester
{
    private void Annonce(String title)
    {
        ConsoleColor.Set(ConsoleColor.ANSI_PURPLE);
        System.out.print("\n================= ");
        ConsoleColor.Set(ConsoleColor.ANSI_WHITE);
        System.out.print(Useful.PadRight(title,6));
        ConsoleColor.Set(ConsoleColor.ANSI_PURPLE);
        System.out.println(" =========");
        ConsoleColor.Set(ConsoleColor.ANSI_WHITE);
    }

    public void Test(TxtFile txt){ Test(txt.Content);}
    public void Test(TxtFile txt, Cpu cpu){ Test(txt.Content, cpu);}
    public void Test(String exp) { Test(exp, new Cpu()); }
    public void Test(String exp, Cpu cpu)
    {
        try
        {

        }catch (Exception e){}
        CParser p = new CParser(exp);
        
        Annonce("Parsing");
        Element e = p.Parse();
        System.out.println(e);

        Annonce("Output");
        cpu.Stack.clear();
        cpu.Execute(e);
        
        Annonce("Cpu");
        System.out.println(cpu);
        if(cpu.NbDoubleAllocated() != 0)
        {
            ConsoleColor.Set(ConsoleColor.ANSI_RED);
            System.out.println("Your program have a Memory Leak");
        }

        Annonce("End");
    }
}