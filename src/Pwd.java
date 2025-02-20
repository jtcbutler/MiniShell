public class Pwd extends ShellCommand {
	@Override
	public String execute() throws ShellException {
		return System.getProperty("user.dir") + "\n";
	}
}
