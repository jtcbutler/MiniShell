import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.Ansi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Arrays;

public class MiniShell {
	public static final String NAME = "minishell";
	public static final String PROMPT = NAME+"> ";

	private final HashMap<String, ShellCommand> commandMap = new HashMap<>();


	public MiniShell(){
		this.commandMap.put("clear", new Clear());
		this.commandMap.put("grep", new Grep());
		this.commandMap.put("pwd", new Pwd());
		this.commandMap.put("cd", new Cd());
		this.commandMap.put("ls", new Ls());
	}

	/**
	 * Run the shell. 
	 * This will enter a loop where the user can interact with the shell.
	*/
	public void run(){

		//allocate resources
		AnsiConsole.systemInstall();
		Scanner scanner = new Scanner(System.in);

		while(true){
			try{

				// read input from the user
				// split the input on whitespace
				// store the result in an array
				System.out.print(PROMPT);
				String[] inputSegments = (scanner.nextLine().split("\\s+"));

				// if the first word of the input was "exit"
				// break out of the input loop
				if(inputSegments[0].equals("exit")){
					break;
				}

				// if the input is piped (contains at least one '|' character)
				// seperate into individual commands by splitting at '|' character
				// execute the resulting list of commands, piping their results
				// print the final commands result
				else if(Arrays.asList(inputSegments).contains("|")){
					String[][] pipedInputSegments = splitPipedInput(inputSegments);
					String result = executePipe(pipedInputSegments);
					System.out.print(result);
				}

				// if the input is not piped (does not contain any '|' characters)
				// execute the command and print its result
				else{
					String result = executeSolo(inputSegments);
					System.out.print(result);
				}
			}

			// if an error occurs at any point, it will be propagated to this catch block
			// color the prompt of the command that caused the error red
			// print the errors message and then continue with the loop
			catch(ShellException e){
				System.out.print(Ansi.ansi().cursorUp(1).fgRed().render(PROMPT).fgDefault().cursorDown(1).cursorLeft(PROMPT.length()));
				System.out.println(NAME + ": " + e.getMessage());
			}
		}

		// free resources
		scanner.close();
		AnsiConsole.systemInstall();
	}

	/**
	 * Execute a single command.
	 *
	 * @param command an array holding the commands name and its arguments
	 * @return the String output of the command that was run 
	 * @throws ShellException if the command was not found or failed
	*/
	private String executeSolo(String[] command) throws ShellException {

		// look up the commands name in the ShellCommand hash table
		ShellCommand executable = this.commandMap.get(command[0]);

		// if the executable is not available, throw exception
		if(executable == null){
			throw new ShellException(command[0] + ": command not found");
		}

		// if the command is available, set its arguments (truncate the commands name) 
		else{
			executable.setArguments(Arrays.copyOfRange(command, 1, command.length));
		}

		// run command and return its output
		return executable.execute();
	}

	/**
	 * Execute several commands.
	 * Pipe the output of each command into the input of its successor.
	 *
	 * @param commands a array of commands, each command being an array holding its own name and arguments
	 * @return the String output of the final command that was run 
	 * @throws ShellException if the any command was not found or failed
	*/
	private String executePipe(String[][] commands) throws ShellException {
		String output = null;

		// run each command in the pipe
		for(String[] command : commands){

			// if this command is not the first
			// append the output of the last command to its arguments
			if(output != null){
				String[] extendedCommand = Arrays.copyOf(command, command.length+1);
				extendedCommand[command.length] = output;

				// System.out.println(Arrays.toString(extendedCommand));
				output = executeSolo(extendedCommand);
			}
			else{
				output = executeSolo(command);

				// String[] tempCommand = Arrays.copyOf(command, command.length+1);
				// tempCommand[command.length] = output;
				// command = tempCommand;

			}

			// execute the command, capturing its output
			//output = executeSolo(command);
		}

		// return the output of the final command
		return output;
	}

	private static String[][] splitPipedInput(String inputSegments[]) throws ShellException {
		ArrayList<String[]> pipeSegments = new ArrayList<>();
		int segmentStart = 0;

		for(int i = 0; i < inputSegments.length+1; i++){
			if(i == inputSegments.length || inputSegments[i].equals("|")){
				String[] pipeSegment = Arrays.copyOfRange(inputSegments, segmentStart, i);
				if(pipeSegment.length == 0){
					throw new ShellException("syntax error near unexpected token '|'");
				}
				else{
					pipeSegments.add(pipeSegment);
				}
				segmentStart = i+1;
			}
		}

		return pipeSegments.toArray(new String[0][0]);
	}


    public static void main(String[] args) {
		new MiniShell().run();
    }
}
