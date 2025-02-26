import java.io.File;

public class Mkdir extends ShellCommand {
	protected String processCommand() throws ShellException {
		if (arguments.length == 0) {
			throw new ShellException("mkdir: Missing operand");
		}

		for (String filename : arguments) {
			if (!(new File(ShellPath.buildPath(filename)).mkdir())) {
				throw new ShellException("mkdir: Failed to create directory '" + filename + "'");
			}
		}
		return "";
	}

	protected String help() {
		return null;
	}
}