import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;

public class TxtFile
{
    public String Content, Path = null;
    private TxtFile(String content, String path)
    {
        Content = content;
        Path = path;
    }

    public @Override String toString() { return Content +"\nat: "+Path; }

    public static TxtFile FromPath(Path path){ return FromPath(path.toString()); }
    public static TxtFile FromPath(String path)
    {
        StringBuilder builder = new StringBuilder();
 
        try (BufferedReader buffer = new BufferedReader(new FileReader(path))) {
 
            String str;
            while ((str = buffer.readLine()) != null) {
                builder.append(str).append("\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            Useful.crash("TxtFile not found at "+path);
        }
 
        return new TxtFile(builder.toString(), path);
    }
}