public class ConsoleColor {
    private ConsoleColor(){}

    public static void UseColor(boolean useColor)
    {
        if(useColor){return;}
        ANSI_RESET = ANSI_BLACK = ANSI_RED = ANSI_GREEN = ANSI_YELLOW = ANSI_BLUE = ANSI_PURPLE = ANSI_CYAN = ANSI_WHITE = "";
        ANSI_BLACK_BACKGROUND = ANSI_RED_BACKGROUND = ANSI_GREEN_BACKGROUND = ANSI_YELLOW_BACKGROUND = ANSI_BLUE_BACKGROUND = ANSI_PURPLE_BACKGROUND = ANSI_CYAN_BACKGROUND = ANSI_WHITE_BACKGROUND = "";
    }

    // Thank to https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    public static String ANSI_RESET  = "\u001B[0m";
    public static String ANSI_BLACK  = "\u001B[30m";
    public static String ANSI_RED    = "\u001B[31m";
    public static String ANSI_GREEN  = "\u001B[32m";
    public static String ANSI_YELLOW = "\u001B[33m";
    public static String ANSI_BLUE   = "\u001B[34m";
    public static String ANSI_PURPLE = "\u001B[35m";
    public static String ANSI_CYAN   = "\u001B[36m";
    public static String ANSI_WHITE  = "\u001B[37m";

    public static String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static String ANSI_RED_BACKGROUND   = "\u001B[41m";
    public static String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static String ANSI_YELLOW_BACKGROUND= "\u001B[43m";
    public static String ANSI_BLUE_BACKGROUND  = "\u001B[44m";
    public static String ANSI_PURPLE_BACKGROUND= "\u001B[45m";
    public static String ANSI_CYAN_BACKGROUND  = "\u001B[46m";
    public static String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static void Set(String color) { System.out.print(color); }
    public static void ResetForeground(){ Set(ANSI_WHITE); }
    public static void ResetBackground(){ Set(ANSI_BLACK_BACKGROUND); }
    public static void Reset()
    {
        ResetForeground();
        ResetBackground();
    }
}