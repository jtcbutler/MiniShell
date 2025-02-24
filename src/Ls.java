import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.Ansi;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Files.*;
import java.nio.file.attribute.*;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
//import java.nio.file.Files;
import java.util.Set;

public class Ls extends ShellCommand{
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
        } else if (this.arguments[0].equals("-t")) {
            return execute_Ls_lh();
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
                    // 
                    if(file.isDirectory()){
                        total_return+="d";
                    }else{
                        total_return+="-";
                    }


                    if(attributes.contains(PosixFilePermission.OWNER_READ)){
                        total_return+="r";
                    }else{
                        total_return+="-";
                    }

                    if(attributes.contains(PosixFilePermission.OWNER_WRITE)){
                        total_return+="w";
                    }else{
                        total_return+="-";
                    }

                    if(attributes.contains(PosixFilePermission.OWNER_EXECUTE)){
                        total_return+="x";
                    } else{
                        total_return+="-";
                    }

                    if(attributes.contains(PosixFilePermission.GROUP_READ)){
                        total_return+="r";
                    }else{
                        total_return+="-";
                    }

                    if(attributes.contains(PosixFilePermission.GROUP_WRITE)){
                        total_return+="w";
                    }else{
                        total_return+="-";
                    }

                    if(attributes.contains(PosixFilePermission.GROUP_EXECUTE)){
                        total_return+="x";
                    } else{
                        total_return+="-";
                    }

                    if(attributes.contains(PosixFilePermission.OTHERS_READ)){
                        total_return+="r";
                    }else{
                        total_return+="-";
                    }

                    if(attributes.contains(PosixFilePermission.OTHERS_WRITE)){
                        total_return+="w";
                    }else{
                        total_return+="-";
                    }

                    if(attributes.contains(PosixFilePermission.OTHERS_EXECUTE)){
                        total_return+="x";
                    } else{
                        total_return+="-";
                    }

                    // number of links
                    // Path link =readSymbolicLink(file_path);
                    //total_return+=link.toString().length()+" ";

                    //total_return+=attributes.toString()+file.getName()+"\n";
                    total_return+=" ";
                    //owner
                    total_return+=Files.getOwner(file_path);
                    total_return+=" ";

                    //size
                    total_return+=Files.size(file_path);
                    total_return+=" ";

                    // last modified time
                    // https://web.cs.ucla.edu/classes/winter15/cs144/projects/java/simpledateformat.html
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM d HH:mm");
                    total_return +=sdf.format(Files.getLastModifiedTime(file_path).toMillis());
                    total_return+=" ";
                    //name
                    total_return+=file.getName()+"\n";
                    }catch(Exception e){
                        throw new ShellException("ls: cannot access " + file.getName());
                    }
                }



                }
        }


        return total_return +"\n";
}



    
    public String execute_Ls_lh() throws ShellException{
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
                    // 
                    if(file.isDirectory()){
                        total_return+="d";
                    }else{
                        total_return+="-";
                    }


                    if(attributes.contains(PosixFilePermission.OWNER_READ)){
                        total_return+="r";
                    }else{
                        total_return+="-";
                    }

                    if(attributes.contains(PosixFilePermission.OWNER_WRITE)){
                        total_return+="w";
                    }else{
                        total_return+="-";
                    }

                    if(attributes.contains(PosixFilePermission.OWNER_EXECUTE)){
                        total_return+="x";
                    } else{
                        total_return+="-";
                    }

                    if(attributes.contains(PosixFilePermission.GROUP_READ)){
                        total_return+="r";
                    }else{
                        total_return+="-";
                    }

                    if(attributes.contains(PosixFilePermission.GROUP_WRITE)){
                        total_return+="w";
                    }else{
                        total_return+="-";
                    }

                    if(attributes.contains(PosixFilePermission.GROUP_EXECUTE)){
                        total_return+="x";
                    } else{
                        total_return+="-";
                    }

                    if(attributes.contains(PosixFilePermission.OTHERS_READ)){
                        total_return+="r";
                    }else{
                        total_return+="-";
                    }

                    if(attributes.contains(PosixFilePermission.OTHERS_WRITE)){
                        total_return+="w";
                    }else{
                        total_return+="-";
                    }

                    if(attributes.contains(PosixFilePermission.OTHERS_EXECUTE)){
                        total_return+="x";
                    } else{
                        total_return+="-";
                    }

                    // number of links
                    total_return+=" ";


                    //owner
                    total_return+=Files.getOwner(file_path);
                    total_return+=" ";

                    //size
                    total_return+=Files.size(file_path);
                    total_return+="B";
                    total_return+=" ";

                    // last modified time
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM d HH:mm");
                    total_return +=sdf.format(Files.getLastModifiedTime(file_path).toMillis());
                    //.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().toString()
                    total_return+=" ";
                    //name
                    total_return+=file.getName()+"\n";
                    }catch(Exception e){
                        throw new ShellException("ls: cannot access " + file.getName());
                    }
                }

                }
        }

        return total_return +"\n";
    
    }

	protected String help(){
		return Ansi.ansi().fgYellow().render(""
		).fgDefault().toString();
	}
}
