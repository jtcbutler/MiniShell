import org.fusesource.jansi.Ansi;

/**
 * A stripped down version of the Bash command 'clear'
 *
 * @author	Jackson Butler
 * @since 	Feb 26, 2025
 */
public class Clear extends ShellCommand {

	/**
	 * Create a new Clear command
	 */
	public Clear(){}

	/**
	 * Return an Ansi encoded String
	 * When printed, this string will clear the terminal and reposition the cursor to the top left
	 *
	 * @return the Ansi encoded String
	*/
	@Override
	protected String processCommand() throws ShellException{
		return Ansi.ansi().eraseScreen().cursor(0, 0).toString();
	}

	/**
	 * Explain the intended use of this command
	 *
	 * @return an explanation of the command
	*/
	protected String help(){
		return Ansi.ansi().fgYellow().render(""
		+ "Usage: clear\n"
		+ "Clears all text from the terminal\n"
		).fgDefault().toString();
	}
}
