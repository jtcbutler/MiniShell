import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.Ansi;

public class Clear extends ShellCommand {
	@Override
	protected String processCommand() throws ShellException{
		if(arguments.length > 0 && (arguments[0].equals("--help") || arguments[0].equals("-h"))){
			return help();
		}
		else{
			return Ansi.ansi().eraseScreen().cursor(0, 0).toString();
		}
	}

	protected String help(){
		return Ansi.ansi().fgYellow().render(""
		+ "Usage: clear\n"
		+ "Clears all text from the terminal\n"
		).fgDefault().toString();
	}
}
