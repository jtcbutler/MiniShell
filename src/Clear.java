/**
 * A stripped down version of the Bash command 'clear'
 *
 * @author	Jackson Butler
 * @date 	Feb 26, 2025
 */
public class Clear extends ShellCommand {

	/**
	 * Create a new Clear command
	 */
	public Clear(){}

	@Override
	protected String processCommand() throws ShellException{
		return ShellFormatter.clearTerminal().toString() + ShellFormatter.setCursorPosition(0, 0).toString();
	}

	@Override
	protected String getHelpText(){
		return ""
		+ "Usage: clear\n"
		+ "Clears all text from the terminal\n";
	}
}
