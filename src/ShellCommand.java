abstract public class ShellCommand{
	protected String pipedInput;
	protected String[] arguments;

	abstract public String execute() throws ShellCommandException;
	
	public void setArguments(String[] arguments){
		this.arguments = arguments;
	}

	public void setPipedInput(String pipedInput){
		this.pipedInput = pipedInput;
	}

	public static class ShellCommandException extends Exception{
		public ShellCommandException(String message){
			super(message);
		}
	}
}
