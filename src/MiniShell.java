import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.Ansi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Arrays;

public class MiniShell{
	private static final String NAME = "minishell";
	private static final String PROMPT = NAME + "> ";

	private final HashMap<String, ShellCommand> commandMap = new HashMap<>();

	public MiniShell(){
		this.commandMap.put("clear", new Clear());
		this.commandMap.put("pwd", new Pwd());
	}

	public void run(){

		//allocate resources
		AnsiConsole.systemInstall();
		Scanner scanner = new Scanner(System.in);

		while(true){
			try{
				// print prompt and read from stdin, splitting the input along whitespace
				System.out.print(PROMPT);
				String[] inputSegments = (scanner.nextLine().split("\\s+"));

				if(inputSegments[0].equals("exit")){
					break;
				}

				// if the input is piped (contained '|' character)
				// split the input along pipe characters and execute
				// print the result
				else if(isPiped(inputSegments)){
					String result = executePiped(splitPipedInput(inputSegments));
					System.out.print(result);
				}

				// if the input is not piped (contained no '|' character)
				// execute and print the result
				else{
					String result = execute(inputSegments, null);
					System.out.print(result);
				}
			}

			// if an error occures
			// color the prompt of the command that caused the error red
			// print error message
			catch(ShellCommand.ShellCommandException e){
				System.out.print(Ansi.ansi().cursorUp(1).fgRed().render(PROMPT).fgDefault().cursorDown(1).cursorLeft(PROMPT.length()));
				System.out.println(e.getMessage());
			}
		}

		// free resources
		scanner.close();
		AnsiConsole.systemInstall();
	}

	// execute a single ShellCommand
	private String execute(String[] command, String pipedInput) throws ShellCommand.ShellCommandException {

		// look up the executable name
		// if executable is available, set values
		// if the executable is not available, throw exception
		ShellCommand executable = this.commandMap.get(command[0]);
		if(executable == null){
			throw new ShellCommand.ShellCommandException(NAME + ": " + command[0] + ": command not found");
		}
		else{
			executable.setArguments(Arrays.copyOfRange(command, 1, command.length));
			executable.setPipedInput(pipedInput);
		}

		// run executable and return its output
		return executable.execute();
	}

	// execute multiple ShellCommands
	private String executePiped(String[][] commands) throws ShellCommand.ShellCommandException {
		String output = null;

		// for each command in <commands>
		// set its piped input as the output of the last command and execute
		for(String[] command : commands){
			output = execute(command, output);
		}

		// return the output of the final command
		return output;
	}

	private boolean isPiped(String[] inputSegments){

		// if segment is the pipe character, inputSegments is piped
		for(String segment : inputSegments){
			if(segment.equals("|")) return true;
		}
		return false;
	}

	private static String[][] splitPipedInput(String inputSegments[]) throws ShellCommand.ShellCommandException {
		ArrayList<String[]> pipeSegments = new ArrayList<>();
		int segmentStart = 0;

		// for all elements in <inputSegments>
		// if the element is a pipe character, add a subarray spanning <segmentStart> - <i> to <pipeSegments>
		// update <segmentStart>
		for(int i = 0; i < inputSegments.length; i++){
			if(inputSegments[i].equals("|")){
				checkAndAddPipeSegment(pipeSegments, Arrays.copyOfRange(inputSegments, segmentStart, i));
				segmentStart = i+1;
			}
		}
		checkAndAddPipeSegment(pipeSegments, Arrays.copyOfRange(inputSegments, segmentStart, inputSegments.length));

		return pipeSegments.toArray(new String[0][0]);
	}

	private static void checkAndAddPipeSegment(ArrayList<String[]> pipeSegments, String[] pipeSegment) throws ShellCommand.ShellCommandException{

		// ensure that a pipe segment is not empty
		// this happens when two pipes are next two eachother, and is invalid syntax
		if(pipeSegment.length == 0){
			throw new ShellCommand.ShellCommandException(NAME + ": syntax error near unexpected token '|'");
		}
		else{
			pipeSegments.add(pipeSegment);
		}
	}

    public static void main(String[] args) {
		new MiniShell().run();
    }
}
