import org.fusesource.jansi.Ansi;
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

	/**
	 * Iterate through arguments
	 * Interpret each argument as a filepath
	 * Attempt to remove the file indicated by each filepath
	 *
	 * @return String an empty String
	 * @throws ShellException If ShellPath.buildPath() fails
	 * @throws ShellException If any of the supplied arguments do not exist in the users filesystem
	 * @throws ShellException If any of the supplied arguments are non-regular files (e.g. directory)
	 * @throws ShellException If any of the supplied arguments are unable to be deleted (likely due to a lack of permission)
	*/
	@Override
	protected String processCommand() throws ShellException{
		for(String argument : arguments){

			// create a new file object with the specified path
			File file = new File(ShellPath.buildPath(argument));

			// throw exception if the specified file does not exist
			if(!file.exists()){
				throw new ShellException("rm: cannot remove '" + argument + "': No such file or directory");
			}

			// throw exception if the specified file is a directory (or some other non-regular filetype)
			else if(!file.isFile()){
				throw new ShellException("rm: cannot remove '" + argument + "': Is a directory");
			}

			// otherwise, attempt to delete the file and continue
			// throw exception if the deletion is unsuccessful
			else if(!file.delete()){
				throw new ShellException("rm: cannot remove '" + argument + "': Permission denied");
			}
		}

		// this command does not print any text
		return "";
	}

	/**
	 * Return text explaining the intended use of this command
	 *
	 * @return String the explanation
	*/
	@Override
	protected String help(){
		return Ansi.ansi().fgYellow().render(""
			+ "Usage: rm [FILE]...\n"
			+ "Remove the FILE(s)...\n"
			+ "\n"
			+ "This command does not remove directories (see rmdir)\n"
		).fgDefault().toString();
	}
}
