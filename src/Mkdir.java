import java.io.File;

/**
 * A stripped down version of the Bash command 'mkdir'
 *
 * @author	Jackson Butler
 * @date 	Feb 26, 2025
 */
public class Mkdir extends ShellCommand {

	/**
	 * Create a new Mkdir command
	 */
	public Mkdir(){}

	/**
	 * Iterate through arguments
	 * Interpret each argument as a filepath
	 * Attempt to create the directory indicated by each filepath
	 *
	 * @return an empty String
	 * @throws ShellException If no arguments are provided
	 * @throws ShellException If ShellPath.buildPath() fails
	 * @throws ShellException If unable to create any of the supplied directories
	 * @throws ShellException If any of the supplied arguments are unable to be deleted (likely due to a lack of permission)
	*/
	protected String processCommand() throws ShellException {

		// if no arguments were provided
		// throw a new shell exception
		if (arguments.length == 0) {
			throw new ShellException("mkdir: missing operand");
		}

		// for each argument
		for (String filename : arguments) {

			// attempt to create new directory
			// throw a new ShellException upon failure
			if (!(new File(ShellPath.buildPath(filename)).mkdir())) {
				throw new ShellException("mkdir: failed to create directory '" + filename + "'");
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
	protected String help() {
		return null;
	}
}
