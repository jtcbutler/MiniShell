import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.Ansi;
import java.io.File;

public class Ls extends ShellCommand{
// finish ls
// finish ls-a
// finish ls-l

    @Override
    public String execute () throws ShellException {
        String total_return = "";
        if (this.arguments.length <1) {
            // current directory
            String currentPath = System.getProperty("user.dir");
            // create an object for that directory
            File directory = new File(currentPath);
            // get all the files 
            File[] files = directory.listFiles();

            // print the files and directories
            if(files != null){
                for(File file : files){
                    if(file.getName().charAt(0) != '.' ){
                        total_return += file.getName() + " ";
                    }         
            }
        }

        } else if (this.arguments[0].equals("-A")) {
            // print all files and directories
            String currentPath = System.getProperty("user.dir");
            File directory = new File(currentPath);
            File[] files = directory.listFiles();

            if(files != null){
                for(File file : files){
                    total_return += file.getName() + " ";
                }
            }
        }
        return total_return + "\n";
    }



}
