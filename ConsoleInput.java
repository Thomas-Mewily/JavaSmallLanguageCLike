import java.nio.file.*;
import java.util.*;

public class ConsoleInput {

    public static Scanner scanner;
    public static Cpu cpu;
    public ConsoleInput(){}

    public void Run() 
    {
        scanner = new Scanner(System.in);
        cpu = new Cpu();
        while(true)
        {
            ConsoleColor.Reset();
            ConsoleColor.Set(ConsoleColor.ANSI_WHITE);
            System.out.println("Enter clike Code or Filename (inside programs folder). Type 'help' to get help. 'reset' to Reset");
            ConsoleColor.Set(ConsoleColor.ANSI_YELLOW);
            System.out.print("> ");
            ConsoleColor.Reset();

            String cmd = "";
            cmd = ReadCommand();

            if(cmd.endsWith(".clike"))
            {
                TestCodeFromPath(cmd);
            }else
            {
                switch(cmd)
                {
                    case "help": 
                    {
                        TestCodeFromPath("help.clike");
                    } break;
                    case "reset": 
                    {
                        cpu = new Cpu(); 
                        ConsoleColor.Set(ConsoleColor.ANSI_YELLOW);
                        System.out.println("Cpu Reset");
                    } break;
                    default: TestCode(cmd); break;
                }
            }
            System.out.println();
        }
    }

    public String ReadCommand()
    {
        String cmd = scanner.nextLine();
        return cmd;
    }

    public void TestCodeFromPath(String name)
    {
        try
        {
            var path = Paths.get(System.getProperty("user.dir"), "programs", name);
            var code = TxtFile.FromPath(path).Content;
            System.out.print("file at: ");
            ConsoleColor.Set(ConsoleColor.ANSI_GREEN);
            System.out.println(path);
            ConsoleColor.Set(ConsoleColor.ANSI_WHITE);

            int i = 0;
            for(var s : code.split("\n"))
            {
                ConsoleColor.Set(ConsoleColor.ANSI_PURPLE);
                System.out.print(Useful.PadRight(Integer.toString(++i), 2)+"| ");
                ConsoleColor.Set(ConsoleColor.ANSI_WHITE);
                System.out.println(s);
            }
            TestCode(code);
        }catch (Exception e)
        {
            ConsoleColor.Set(ConsoleColor.ANSI_RED);
            System.out.println(e.getMessage());
        }
    }

    public void TestCode(String code)
    {
        try
        {
            ExpTester e = new ExpTester();
            e.Test(code, cpu);
        }catch (Exception e)
        {
            ConsoleColor.Set(ConsoleColor.ANSI_RED);
            System.out.println(e.getMessage());
        }
    }
}