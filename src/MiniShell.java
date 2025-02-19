import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.Ansi;
import java.util.Scanner;

//System.setProperty("user.dir", "/"); note: does not change working directory, but does change the globar variable
public class MiniShell{
	private static final String PROMPT = "msh> ";

	public MiniShell(){
		AnsiConsole.systemInstall();
		inputLoop();
		AnsiConsole.systemInstall();
	}

	private void inputLoop(){
		Scanner scanner = new Scanner(System.in);
		boolean proceed = true;

		while(proceed){
			System.out.print(PROMPT);

			String[] inputSegments = (splitAndTrim(scanner.nextLine()));

			try{
				switch(inputSegments[0]){
					case "exit": 
					proceed = false;
					System.out.println("exiting...");
					break;

					case "clear":
					ShellCommand clear = new Clear();
					System.out.print(clear.execute());
					break;

					case "pwd":
					ShellCommand pwd = new Pwd();
					System.out.print(pwd.execute());
					break;

					default:
					throw new ShellCommand.ShellCommandException("minish: " + inputSegments[0] + ": command not found");
				}
			}
			catch(ShellCommand.ShellCommandException e){
				System.out.print(Ansi.ansi().cursorUp(1).fgRed().render(PROMPT).fgDefault().cursorDown(1).cursorLeft(PROMPT.length()));
				System.out.println(e.getMessage());
			}
		}

		scanner.close();
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
