public class Pwd extends ShellCommand{
	@Override
	public String execute() throws ShellCommandException {
		return System.getProperty("user.dir") + "\n";
	}
}
