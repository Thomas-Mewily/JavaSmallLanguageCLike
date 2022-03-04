public class Useful 
{
    public static boolean inside(int value, int min, int max)
    {
        return value >= min && value <= max;
    }

    // Thank to https://www.baeldung.com/java-pad-string
    public static String PadRight(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(inputString);
        while (sb.length() < length) {
            sb.append(' ');
        }
        return sb.toString();
    }

    // Thank to https://stackoverflow.com/questions/4519557/is-there-a-way-to-throw-an-exception-without-adding-the-throws-declaration/4519576
    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void crash(Throwable exception, Object dummy) throws T
    {
        throw (T) exception;
    }

    public static void crash(String msg)
    {
        Useful.<RuntimeException>crash(new Exception(msg), null);
    }
}