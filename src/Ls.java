import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Set;

/**
 * A stripped down version of the Bash command 'ls'
 *
 * @author	Jiafeng Gu
 * @date 	Feb 27, 2025
 */
public class Ls extends ShellCommand{

	/**
	 * Create a new Ls command
	 */
	public Ls(){}

    /**
     * This method is used to execute the ls command. It takes in an array of arguments and returns a string of the output of the ls command.
     * @return A string of the output 
     * @throws ShellException If there is an error in the ls command.
     */
    @Override
    protected String processCommand() throws ShellException {
        String total_return = "";
        if(this.arguments.length >2){
            throw new ShellException("ls: too many arguments");
        }
        else if (this.arguments.length ==0) {
            // current directory
            String currentPath = System.getProperty("user.dir");
            File directory = new File(currentPath);
            File[] files = directory.listFiles();
            Arrays.sort(files);

            // print the files and directories
            if(files != null){
                for(File file : files){
                    if(file.getName().charAt(0) != '.' ){
                        total_return += file.getName() + " ";
                    }         
            }
            }
            return total_return + "\n";
		}else if (this.arguments[0].equals("-A")) {
            return execute_Ls_A();
        } else if (this.arguments[0].equals("-l")) {
            return execute_Ls_l();
        } else if (this.arguments[0].equals("-lh")) {
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

    /**
     * This method is used to print all the files and directories in the current directory.(including hidden files)
     * @return A string of the output with color.
     * @throws ShellException If there is an error in the ls command.
     */
    public String execute_Ls_A() throws ShellException {
        String total_return = "";
        String currentPath="";
        if (this.arguments.length == 2){
            String givenPath = this.arguments[1];
            currentPath =ShellPath.buildPath(givenPath);
            // how to check if it is a currect path

        }else{
            currentPath = System.getProperty("user.dir");
        }

        File directory = new File(currentPath);
        File[] files = directory.listFiles();
        Arrays.sort(files);
        
        if(!directory.exists()){
            throw new ShellException("No such file or directory");
        }

		Arrays.sort(files);
		if(files != null){
			for(File file : files){
				if (file.isDirectory()) {
					total_return += ShellFormatter.boldText(ShellFormatter.colorText(file.getName(), 0, 0, 255));
				} else if (file.canExecute()) {
					total_return += ShellFormatter.boldText(ShellFormatter.colorText(file.getName(), 0, 255, 0));
				} else {
					total_return += file.getName();
				}

				total_return += " ";
			}
		}

        return total_return + "\n";
    }

    
    /**
     * This method is used to print the files and directories with more information.(not including hidden files)
     * @return strings of the output 
     * @throws ShellException If there is an error in the ls command.
     */
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

        Arrays.sort(files);

        if(!directory.exists()){
            throw new ShellException("No such file or directory");
        }


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
                    total_return+=" ";




                    // number of links
                    int linkCount = (Integer) Files.getAttribute(file_path, "unix:nlink");
                    total_return+=linkCount+" ";     
                    total_return+=" ";


                    //owner
                    total_return+=Files.getOwner(file_path);
                    total_return+=" ";

                    //group
                    String group = Files.getAttribute(file_path, "posix:group").toString();
                    total_return+=group;
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


    /**
     * This method is used to print the files and directories with more information and sizes in human readable format.(not including hidden files)
     * @return strings of the output 
     * @throws ShellException If there is an error in the ls command
     */

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
        if(!directory.exists()){
            throw new ShellException("No such file or directory");
        }
        File[] files = directory.listFiles();

        Arrays.sort(files);

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

                    total_return+=" ";

                    // number of links
                    int linkCount = (Integer) Files.getAttribute(file_path, "unix:nlink");
                    total_return+=linkCount+" ";
                    total_return+=" ";

                    //owner
                    total_return+=Files.getOwner(file_path);
                    total_return+=" ";

                    //group
                    String group = Files.getAttribute(file_path, "posix:group").toString();
                    total_return+=group;
                    total_return+=" ";

                    //size
                    DecimalFormat df = new DecimalFormat("#.#");
                    if(file.length()<1024){
                        total_return+=Files.size(file_path);
                        total_return+="B";
                        }else if(file.length()<1024*1024){
                            total_return+=df.format(Files.size(file_path)/1024.0);
                            total_return+="K";
                            }else{
                                total_return+=df.format(Files.size(file_path)/(1024.0*1024.0));
                                total_return+="M";
                            }
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

	@Override
	protected String getHelpText(){
		return ""
		+ "Usage: ls [OPTION]...\n"
		+ "List information about the FILEs (the current directory by default).\n"
		+ "Example: ls -l\n"
		+ "\n"
		+ "Word selection and interpretation:\n"
		+ MiniShell.INDENT + "-A,  --all the files          list all files including hidden files\n"
		+ MiniShell.INDENT + "-l,  --more information       use a long listing format\n"
		+ MiniShell.INDENT + "-lh, --human-readable         with -l, print sizes in human readable format (e.g., 1K 234M 2G)\n";
	}
}
