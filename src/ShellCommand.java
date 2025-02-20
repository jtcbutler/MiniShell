abstract public class ShellCommand{
	protected String[] arguments;

	abstract public String execute() throws ShellCommandException;
	
	public void setArguments(String[] arguments){
		this.arguments = arguments;
	}

	public static class ShellCommandException extends Exception{
		public ShellCommandException(String message){
			super(message);
		}
	}
}
