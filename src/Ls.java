import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.Ansi;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.*;
//import java.nio.file.Files;
import java.util.Set;

public class Ls extends ShellCommand{
// finish ls
// finish ls-a
// finish ls-l

    @Override
    public String execute () throws ShellException {
        String total_return = "";
        if(this.arguments.length >2){
            throw new ShellException("ls: too many arguments");
        }
        else if (this.arguments.length ==0) {
            // current directory
            String currentPath = System.getProperty("user.dir");
            File directory = new File(currentPath);
            File[] files = directory.listFiles();

            // print the files and directories
            if(files != null){
                for(File file : files){
                    if(file.getName().charAt(0) != '.' ){
                        total_return += file.getName() + " ";
                    }         
            }
            }
            return total_return + "\n";

        }else if (this.arguments[0].equals("--help") || this.arguments[0].equals("-h")) {
            return help();
		}else if (this.arguments[0].equals("-A")) {
            return execute_Ls_A();
        } else if (this.arguments[0].equals("-l")) {
            return execute_Ls_l();
        } else if (this.arguments[0].equals("-ln")) {
            return execute_Ls_ln();
        } else{
            if(this.arguments.length>1){
                throw new ShellException("ls: too many arguments");
            }else{
                // print the given directory
                String givenPath = this.arguments[0];
                String fullPath =ShellPath.buildPath(givenPath);
                File directory = new File(fullPath);
                File[] files = directory.listFiles();
                if(files != null){
                    for(File file : files){
                        if(!file.isHidden() ){
                            total_return += file.getName() + "\n";
                        }         
                    }
                }
                return total_return + "\n";
            }
        } 
    }


    public String execute_Ls_A() throws ShellException {
        String total_return = "";
        String currentPath="";
        if (this.arguments.length == 2){
            String givenPath = this.arguments[1];
            currentPath =ShellPath.buildPath(givenPath);

        }else{
            currentPath = System.getProperty("user.dir");
        }
        File directory = new File(currentPath);
        File[] files = directory.listFiles();

        if(files != null){
            for(File file : files){
                total_return += file.getName() + " ";
            }
        }

        return total_return + "\n";
    }

    
    public String execute_Ls_l() throws ShellException{
        String total_return = "";
        String fullPath = "";
        if(this.arguments.length ==2){
            String givenPath = this.arguments[1];
            fullPath =ShellPath.buildPath(givenPath);
        }else{
            fullPath = System.getProperty("user.dir");
        }
            
        File directory = new File(fullPath);
        File[] files = directory.listFiles();
        if(files != null){
            for(File file : files){
                if(!file.isHidden()){
                    Path file_path = file.toPath();
                    try{
                    Set<PosixFilePermission> attributes = Files.getPosixFilePermissions(file_path);
                    total_return+=attributes.toString()+file.getName()+"\n";
                    }catch(Exception e){
                        throw new ShellException("ls: cannot access " + file.getName());
                    }
                }



                }
        }


        return total_return +"\n";
}

    public String execute_Ls_ln() throws ShellException{

        return "";
    }

	protected String help(){
		return Ansi.ansi().fgYellow().render(""
		).fgDefault().toString();
	}
}
