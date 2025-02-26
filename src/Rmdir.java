import java.io.File;

public class Rmdir extends ShellCommand {

	@Override
	protected String processCommand() throws ShellException {
		for (String argument : arguments) {
			File file = new File(argument);
			if (file.exists() && file.isDirectory() && file.list().length == 0) {
				file.delete();
			} else {
				throw new ShellException("");
			}
		}
		return "";
	}

	@Override
	protected String help() {
		return null;
	}
}
