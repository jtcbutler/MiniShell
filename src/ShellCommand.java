abstract public class ShellCommand {
	protected String[] arguments;

	abstract public String execute() throws ShellException;
	// abstract protected String help();
	
	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}
}
