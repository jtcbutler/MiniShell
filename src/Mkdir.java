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

	protected String processCommand() throws ShellException {

		if (arguments.length == 0) {
			throw new ShellException("mkdir: missing operand");
		}

		for (String filename : arguments) {
			if (!(new File(ShellPath.buildPath(filename)).mkdir())) {
				throw new ShellException("mkdir: failed to create directory '" + filename + "'");
			}
		}

		// this command does not print any text
		return "";
	}

	@Override
	protected String getHelpText(){
		return ""
		+ "Usage: mkdir [OPTION]... DIRECTORY..."
		+ "Create the DIRECTORY(ies), if they do not already exist.";
	}
}
