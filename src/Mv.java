import org.fusesource.jansi.Ansi;
import java.io.File;

/**
 * A stripped down version of the Bash command 'mv'
 *
 * @author	Jackson Butler
 * @since 	Feb 26, 2025
 */
public class Mv extends ShellCommand{

	/**
	 * Create a new Mv command
	 */
	public Mv(){}

	/**
	 * Move a single file/directory or a list of files/directories to a specified path
	 *
	 * @return an empty String
	 */
	@Override
	protected String processCommand() throws ShellException{

		// if no arguments were provided
		// throw a new ShellException
		if(arguments.length == 0){
			throw new ShellException("mv: Missing file operand");
		}

		// if only one argument was provided
		// throw a new ShellException
		else if(arguments.length == 1){
			throw new ShellException("mv: Missing destination file operand after '" + arguments[0] + "'");
		}

		// if exactly two arguments were provided
		// allow for the destination to be a directory or file
		else if(arguments.length == 2){
			File source = new File(ShellPath.buildPath(arguments[0]));
			File destination = new File(ShellPath.buildPath(arguments[1]));

			// if the source does not exist
			// throw a new ShellException
			if(!source.exists()){
				throw new ShellException("mv: '" + arguments[0] + "' is not a file or directory");
			}

			// if the destination is a directory
			// the destination file will retain the source files name
			// extract the source files name and append it to the destination path
			if(destination.isDirectory()){
				destination = new File(ShellPath.buildPath(arguments[1]) + ShellPath.FILE_SEPARATOR + ShellPath.getLast(arguments[0]));
			}

			// attempt to move/rename the file
			// if the move fails, throw a new ShellException
			if(!source.renameTo(destination)){
				throw new ShellException("mv: Failed to move '" + arguments[0] + "' to '" + arguments[1] + "'");
			}
		}

		// if more than two arguments were provided
		// the destination must be a directory
		else{

			// the destination directory is the final argument
			String destinationPath = ShellPath.buildPath(arguments[arguments.length - 1]);

			// if the destination is not a directory
			// throw a new ShellException
			if(!(new File(destinationPath).isDirectory())){
				throw new ShellException("mv: '" + arguments[arguments.length - 1] + "' is not a directory");
			}

			// for each argument (excluding the final argument)
			for(int i = 0; i < arguments.length - 1; i++){

				// interpret the argument as a file
				File source = new File(ShellPath.buildPath(arguments[i]));

				// if the file does not exist
				// throw a new ShellException
				if(!source.exists()){
					throw new ShellException("mv: '" + arguments[i] + "' is not a file or directory");
				}

				// the destination file will retain the source files name
				// extract the source files name and append it to the destination path
				File destination = new File(destinationPath + ShellPath.FILE_SEPARATOR + ShellPath.getLast(arguments[i]));

				// attempt to move/rename the file
				// if the move fails, throw an exception
				if(!source.renameTo(destination)){
					throw new ShellException("mv: Failed to move '" + arguments[i] + "' to '" + arguments[arguments.length - 1] + "'");
				}
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
			+ "Usage: mv SOURCE DEST or mv SOURCE... DIRECTORY\n"
			+ "Rename SOURCE to DEST, or move SOURCE(s) to DIRECTORY.\n"
		).fgDefault().toString();
	}
}
