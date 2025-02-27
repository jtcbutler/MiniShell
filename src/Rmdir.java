import org.fusesource.jansi.Ansi;
import java.io.File;

/**
 * A stripped down version of the Bash command 'rmdir'
 *
 * @author	Jackson Butler
 * @date 	Feb 26, 2025
 */
public class Rmdir extends ShellCommand {

	/**
	 * Create a new Rmdir command
	 */
	public Rmdir(){}

	/**
	 * Iterate through arguments
	 * Interpret each argument as a filepath
	 * Attempt to remove the directory indicated by each filepath
	 *
	 * @return an empty String
	 * @throws ShellException If ShellPath.buildPath() fails
	 * @throws ShellException If any of the supplied arguments do not exist in the users filesystem
	 * @throws ShellException If any of the supplied arguments are not directories
	 * @throws ShellException If any of the supplied directories are not empty
	 * @throws ShellException If any of the supplied directories are unable to be deleted for any other reason
	*/
	@Override
	protected String processCommand() throws ShellException {

		// for every argument
		for (String argument : arguments) {
			File file = new File(ShellPath.buildPath(argument));

			// if the specified directory does not exist
			// throw a new ShellException
			if(!file.exists()){
				throw new ShellException("rmdir: cannot remove '" + argument + "': No such file or directory");
			}

			// if the specified directory is not a directory
			// throw a new ShellException
			else if(!file.isDirectory()){
				throw new ShellException("rmdir: cannot remove '" + argument + "': Is not a directory");
			}

			// if the specified directory is not empty
			// throw a new ShellException
			else if(file.list().length == 0){
				throw new ShellException("rmdir: cannot remove '" + argument + "': Directory not empty");
			}

			// if the specified directory cannot be removed for any other reason
			// throw a new ShellException
			else if(!file.delete()){
				throw new ShellException("rmdir: cannot remove '" + argument + "': Permission denied");
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
			+ "Usage: rmdir DIRECTORY...\n"
			+ "Remove the DIRECTORY(ies), if they are empty.\n"
		).fgDefault().toString();
	}
}
