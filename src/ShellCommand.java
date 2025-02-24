abstract public class ShellCommand {
	protected String[] arguments;
	protected boolean isPiped;

	abstract public String execute() throws ShellException;
	abstract protected String help();
	
	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

	public void setPiped(boolean isPiped){
		this.isPiped = isPiped;
	}

	protected boolean isHelp(){
		if(arguments.length == 0) return false;
		else return arguments[0].equals("--help") || arguments[0].equals("-h");
	}
}
