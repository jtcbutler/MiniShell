abstract public class ShellCommand{
	protected String pipedInput;

	abstract public String execute() throws ShellCommandException;

	public void setPipedInput(String pipedInput){
		this.pipedInput = pipedInput;
	}

	public static class ShellCommandException extends Exception{
		public ShellCommandException(String message){
			super(message);
		}
	}
}
..