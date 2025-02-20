import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.Ansi;
import java.io.File;

public class Ls extends ShellCommand{
// finish ls
// finish ls-a
// finish ls-l

    @Override
    public String execute () throws ShellCommandException {
        // current directory
        String currentPath = System.getProperty("user.dir");
        // create an object for that directory
        File directory = new File(currentPath);
        // get all the files 
        File[] files = directory.listFiles();

        // print the files and directories
        if(files != null){
            for(File file : files){
                System.out.print(file.getName() + " ");
        }
            System.err.println("\n");
    }
        return "";
    }
}