import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Arrays;

/**
 * A barebones recreation of Bash
 *
 * @authors Jackson Butler, Jiafeng Gu
 * @date	Feb 27, 2025
 */
public class MiniShell {

	/**
	 * The name of the shell, as displayed in the shell
	 */
	public static final String NAME = "msh";

	/**
	 * The standard indent within the shell
	 */
	public static final String INDENT = "  ";

	/**
	 * The shells command prompt
	 */
	public static final String PROMPT = NAME+"> ";

	/**
	 * HashMap for easy access to implemented commands
	 */
	private final HashMap<String, ShellCommand> commandMap = new HashMap<>();

	/**
	 * Create a new MiniShell
	 */
	public MiniShell(){
		this.commandMap.put("clear", new Clear());
		this.commandMap.put("mkdir", new Mkdir());
		this.commandMap.put("rmdir", new Rmdir());
		this.commandMap.put("touch", new Touch());
		this.commandMap.put("grep", new Grep());
		this.commandMap.put("pwd", new Pwd());
		this.commandMap.put("cat", new Cat());
		this.commandMap.put("cd", new Cd());
		this.commandMap.put("ls", new Ls());
		this.commandMap.put("rm", new Rm());
		this.commandMap.put("mv", new Mv());
		this.commandMap.put("cp", new Cp());
		this.commandMap.put("ps",new Ps());
	}

	/**
	 * Run the shell. 
	 * This will enter a loop where the user can interact with the shell.
	*/
	public void run(){

		//allocate resources
		Scanner scanner = new Scanner(System.in);

		while(true){
			try{
				String output;

				// read input from the user
				// split the input on whitespace
				// store the result in an array
				System.out.print(ShellFormatter.boldText(PROMPT));
				String[] inputSegments = (scanner.nextLine().split("\\s+"));

				// if the first word of the input was "exit"
				// break out of the input loop
				if(inputSegments[0].equals("exit")){
					break;
				}


				// if the input is piped (contains at least one '|' character)
				// seperate into individual commands by splitting at '|' character
				// execute the resulting list of commands, piping their results
				else if(Arrays.asList(inputSegments).contains("|")){
					String[][] pipedInputSegments = splitPipedInput(inputSegments);
					output = executePipe(pipedInputSegments);
				}

				// if the input is not piped (does not contain any '|' characters)
				// execute the command
				else{
					output = executeSolo(inputSegments, false);
				}

				System.out.print(ShellFormatter.moveCursorUp(1));
				System.out.print(ShellFormatter.boldText(ShellFormatter.colorText(PROMPT, 0, 255, 0)));
				System.out.print(ShellFormatter.moveCursorDown(1));
				System.out.print(ShellFormatter.moveCursorLeft(PROMPT.length()));
				System.out.print(output);
			}

			// if an error occurs at any point, it will be propagated to this catch block
			// color the prompt of the command that caused the error red
			// print the errors message and then continue with the loop
			catch(ShellException e){
				System.out.print(ShellFormatter.moveCursorUp(1));
				System.out.print(ShellFormatter.boldText(ShellFormatter.colorText(PROMPT, 255, 0, 0)));
				System.out.print(ShellFormatter.moveCursorDown(1));
				System.out.print(ShellFormatter.moveCursorLeft(PROMPT.length()));
				System.out.println(NAME + ": " + e.getMessage());
			}
		}

		// free resources
		scanner.close();
	}

	/**
	 * Execute a single command.
	 *
	 * @param command an array holding the commands name and its arguments
	 * @param isPiped indication of whether or not this command is piped
	 * @return the String output of the command that was run 
	 * @throws ShellException if the command was not found or failed
	*/
	private String executeSolo(String[] command, boolean isPiped) throws ShellException {

		// look up the commands name in the ShellCommand hash table
		ShellCommand executable = this.commandMap.get(command[0]);

		// if the executable is not available, throw exception
		if(executable == null){
			throw new ShellException(command[0] + ": command not found");
		}

		// if the command is available, set its arguments (truncate the commands name) 
		else{
			executable.setArguments(Arrays.copyOfRange(command, 1, command.length));
			executable.setPiped(isPiped);
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
			if(output != null){
				command = Arrays.copyOf(command, command.length+1);
				command[command.length-1] = output;
				output = executeSolo(command, true);
			}
			else{
				output = executeSolo(command, false);
			}
		}

		// return the output of the final command
		return output;
	}

	/**
	 * Split an array of arguments into a 2d array of seperate commands
	 *
	 * @param inputSegments the array of aruments
	 * @return the array of arguments seperated into commands
	 * @throws ShellException if a syntax error is found
	*/
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

	/**
	 * Run the program
	 *
	 * @param args command line arguments
	*/
    public static void main(String[] args) {
		new MiniShell().run();
    }
}
