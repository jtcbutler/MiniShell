import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.Ansi;

public class Clear extends ShellCommand {
	@Override
	public String execute() throws ShellException{
		return Ansi.ansi().eraseScreen().cursor(0, 0).toString();
	}
}
