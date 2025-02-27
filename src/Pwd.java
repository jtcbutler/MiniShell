import org.fusesource.jansi.Ansi;

/**
 * A stripped down version of the Bash command 'pwd'
 *
 * @author	Jackson Butler
 * @date 	Feb 26, 2025
 */
public class Pwd extends ShellCommand {

	/**
	 * Create a new Pwd command
	 */
	public Pwd(){}

	@Override
	protected String processCommand() throws ShellException {
		return System.getProperty("user.dir") + "\n";
	}

	@Override
	protected String getHelpText(){
		return ""
		+ "pwd: pwd\n"
		+ "Print the name of the current working directory.\n";
	}
}
