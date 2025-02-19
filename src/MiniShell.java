import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.Ansi;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Arrays;

//System.setProperty("user.dir", "/"); note: does not change working directory, but does change the globar variable
public class MiniShell{
	private static final String PROMPT = "msh> ";
	private final HashMap<String, ShellCommand> commandMap;

	public MiniShell(){

		commandMap = new HashMap<>();	
		commandMap.put("clear", new Clear());
		commandMap.put("pwd", new Pwd());

		AnsiConsole.systemInstall();
		inputLoop();
		AnsiConsole.systemInstall();
	}

	private void inputLoop(){
		Scanner scanner = new Scanner(System.in);

		while(true){
			System.out.print(PROMPT);

			String[] inputSegments = (splitAndTrim(scanner.nextLine()));

			if(inputSegments[0].equals("exit")){
				System.out.println("exiting...");
				break;
			}
			else{
				try{
					execute(inputSegments);
				}
				catch(ShellCommand.ShellCommandException e){
					System.out.print(Ansi.ansi().cursorUp(1).fgRed().render(PROMPT).fgDefault().cursorDown(1).cursorLeft(PROMPT.length()));
					System.out.println(e.getMessage());
				}
			}
		}

		scanner.close();
	}

	private void execute(String[] command) throws ShellCommand.ShellCommandException {
		ShellCommand executable = this.commandMap.get(command[0]);

		if(executable == null){
			throw new ShellCommand.ShellCommandException("msh: " + command[0] + ": command not found");
		}
		else{
			executable.setArguments(Arrays.copyOfRange(command, 1, command.length));
		}

		System.out.println(executable.execute());
	}

	private void executePipe(String[][] commands){
	}

	private String[] splitAndTrim(String input){
		String[] segments = input.split(" ");
		for(int i = 0; i < segments.length; i++){
			segments[i] = segments[i].trim();
		}
		return segments;
	}

    public static void main(String[] args) {
		new MiniShell();
    }
}
