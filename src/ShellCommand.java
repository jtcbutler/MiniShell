/**
 * Blueprint for a Bash command (e.g. cd, ls, cat, etc.)
 *
 * @authors	Jackson Butler,Jiafeng Gu
 * @since	Feb 25, 2025
 */
abstract public class ShellCommand {

	/**
	 * An array of the arguments that were supplied via the command line
	 * The commands name should be removed from this array prior to setting it
	 */
	protected String[] arguments;

	/**
	 * Indicate whether or not the final element of arguments should be interpreted as piped input
	 * This should be set to true if the final element of arguments should be interpreted as piped input
	 */
	protected boolean isPiped;

	/**
	 * Create a new ShellCommand
	 */
	public ShellCommand(){}

	/**
	 * Run the ShellCommand
	 * Both arguments and isPiped must be set prior to calling this method
	 *
	 * @return String the output of the command
	 * @throws ShellException if an error occured within processCommand()
	*/
	public String execute() throws ShellException{
		if(helpNeeded())
		{
			return help();
		}
		else{
			return processCommand();
		}
	}

	/**
	 * Setter for the this.arguments
	 *
	 * @param arguments the value that this.arguments should be set to
	*/
	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

	/**
	 * Setter for the this.isPiped
	 *
	 * @param isPiped the value that this.isPiped should be set to
	*/
	public void setPiped(boolean isPiped){
		this.isPiped = isPiped;
	}

	/**
	 * Handle the interpretation and execution of arguments
	 *
	 * @return String the commands output
	 * @throws ShellException if an error occured during the commands execution
	*/
	abstract protected String processCommand() throws ShellException;

	/**
	 * Return a String of help text
	 *
	 * @return a String of help text
	 */
	abstract protected String getHelpText();

	/**
	 * Format a String of help text
	 *
	 * @return the formatted help text
	*/
	private String help(){
		return ShellFormatter.colorText(getHelpText(), 255, 255, 0);
	}

	/**
	 * Determine if arguments contains a help flag
	 *
	 * @return boolean true if the help flag was found
	*/
	private boolean helpNeeded(){

		// for each String in arguments check if it is "--help" or "-h"
		// return true if so
		for(String argument : arguments){
			if(argument.equals("--help") || argument.equals("-h")){
				return true;
			}
		}

		// none of the arguments were help flags
		return false;
	}
}
