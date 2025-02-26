abstract public class ShellCommand {
	protected String[] arguments;
	protected boolean isPiped;

	public String execute() throws ShellException{
		if(helpNeeded())
		{
			return help();
		}
		else{
			return processCommand();
		}
	}

	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

	public void setPiped(boolean isPiped){
		this.isPiped = isPiped;
	}

	protected boolean helpNeeded(){
		if(arguments.length == 0) return false;
		else return arguments[0].equals("--help") || arguments[0].equals("-h");
	}

	abstract protected String help();
	abstract protected String processCommand() throws ShellException;
}
