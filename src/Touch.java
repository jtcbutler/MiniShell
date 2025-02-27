import org.fusesource.jansi.Ansi;
import java.io.IOException;
import java.io.File;

/**
 * A stripped down version of the Bash command 'touch'
 *
 * @author	Jackson Butler
 * @date 	Feb 26, 2025
 */
public class Touch extends ShellCommand {

	/**
	 * Create a new Touch command
	 */
	public Touch(){}

	/**
	 * Iterate through arguments
	 * Interpret each argument as a filepath
	 * Create a new new file for each filepath
	 *
	 * @return an empty String
	 * @throws ShellException If no arguments are supplied
	 * @throws ShellException If ShellPath.buildPath() fails
	 * @throws ShellException If any of the supplied files already exist
	 * @throws ShellException If any of the supplied files are unable to be created for any other reason
	*/
	@Override
	protected String processCommand() throws ShellException {

		// if no arguments were provided
		// throw a new ShellException
		if(arguments.length == 0){
			throw new ShellException("touch: missing file operand");
		}

		// for each argument in arguments
		for(String filename : arguments){
			try {
				File file = new File(ShellPath.buildPath(filename));

				// if the file does not always exist
				// create file
				if(!file.exists()){
					file.createNewFile();
				}

				// otherwise, throw new ShellException
				else{
					throw new ShellException("touch: " + filename + ": file already exists");
				}
			} 

			// if an error occured during file creation
			// throw a new ShellException
			catch (IOException e) {
				throw new ShellException("touch: failed to create file '" + filename + "'");
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
			+ "Usage: touch FILE...\n"
			+ "Create a new empty file\n"
		).fgDefault().toString();
	}
}
