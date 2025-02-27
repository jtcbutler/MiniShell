import org.fusesource.jansi.Ansi;

/**
 * A stripped down version of the Bash command 'cp'
 *
 * @author	Jackson Butler
 * @date 	Feb 26, 2025
 */
public class Pwd extends ShellCommand {

	/**
	 * Create a new Pwd command
	 */
	public Pwd(){}

	/**
	 * Retrieve the String path of the current working directory
	 *
	 * @return the path of the current working directory
	 */
	@Override
	protected String processCommand() throws ShellException {
		return System.getProperty("user.dir") + "\n";
	}

	/**
	 * Return text explaining the intended use of this command
	 *
	 * @return String the explanation
	*/
	protected String help(){
		return Ansi.ansi().fgYellow().render(""
		+ "pwd: pwd\n"
		+ "Print the name of the current working directory.\n"
		).fgDefault().toString();
	}
}
