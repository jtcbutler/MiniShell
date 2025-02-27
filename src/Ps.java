import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * A ShellCommand wrapper for the Bash command 'ps'
 *
 * @author	Jiafeng Gu
 * @date 	Feb 27, 2025
 */
public  class Ps extends ShellCommand {

	/**
	 * Create a new Ps command
	*/
	public Ps(){}

	@Override
	protected String processCommand() throws ShellException{
		Runtime runtime = Runtime.getRuntime();
		Process process;
		try {
			// common exec("ps") is deprecated, change ps to string[]
			String[] command = {"ps"};
			process = runtime.exec(command);
		} catch (IOException e) {
			throw new ShellException("Error executing ps command");
		}

		String line;
		StringBuilder output = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = reader.readLine())!= null) {
				output.append(line).append("\n");
			}
			reader.close();
			process.destroy();
		} catch (IOException e) {
			throw new ShellException("Error reading ps output");
		}


		return output.toString();
	}

	@Override
	protected String getHelpText(){
		return ""
		+ "ps: ps\n"
		+ "Prints a list of all running processes.\n";
	}
}
