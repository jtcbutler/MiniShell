import java.io.File;

/**
 * A stripped down version of the Bash command 'rm'
 *
 * @author	Jackson Butler
 * @date 	Feb 25, 2025
 */
public class Rm extends ShellCommand{

	/**
	 * Create a new Rm command
	 */
	public Rm(){}

	@Override
	protected String processCommand() throws ShellException{
		for(String argument : arguments){
			File file = new File(ShellPath.buildPath(argument));

			if(!file.exists()){
				throw new ShellException("rm: cannot remove '" + argument + "': No such file or directory");
			}
			else if(!file.isFile()){
				throw new ShellException("rm: cannot remove '" + argument + "': Is a directory");
			}
			else if(!file.delete()){
				throw new ShellException("rm: cannot remove '" + argument + "': Permission denied");
			}
		}

		// this command does not print any text
		return "";
	}

	@Override
	protected String getHelpText(){
		return ""
		+ "Usage: rm [FILE]...\n"
		+ "Remove the FILE(s)...\n"
		+ "\n"
		+ "This command does not remove directories (see rmdir)\n";
	}
}
