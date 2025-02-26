/**
 * Blueprint for a Bash command (e.g. cd, ls, cat, etc.)
 *
 * @authors	Jackson Butler, Jiafeng
 * @since	Feb 25, 2025
 */
abstract public class ShellCommand {
	protected String[] arguments; // arguments passed via the command line
	protected boolean isPiped; // true if the final element in <arguments> should be interpreted as piped input

	/**
	 * Run the ShellCommand
	 * Both <arguments> and <isPiped> must be set prior to calling this method
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
	 * Setter for the <this.arguments>
	 *
	 * @param arguments the value that <this.arguments> should be set to
	*/
	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

	/**
	 * Setter for the <this.isPiped>
	 *
	 * @param isPiped the value that <this.isPiped> should be set to
	*/
	public void setPiped(boolean isPiped){
		this.isPiped = isPiped;
	}

	/**
	 * Handle the interpretation and execution of <arguments>
	 *
	 * @return String the commands output
	 * @throws ShellException if an error occured during the commands execution
	*/
	abstract protected String processCommand() throws ShellException;

	/**
	 * Explain the intended use of this command
	 *
	 * @return String an explanation of the command
	*/
	abstract protected String help();

	/**
	 * Determine if <arguments> contains a help flag
	 *
	 * @return boolean true if the help flag was found
	*/
	private boolean helpNeeded(){

		// for each String in <arguments> check if it is "--help" or "-h"
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
