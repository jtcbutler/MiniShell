abstract public class ShellCommand {
	protected String[] arguments;

	abstract public String execute() throws ShellException;
	
	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}
}
