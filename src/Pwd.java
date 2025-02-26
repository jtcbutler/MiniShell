import org.fusesource.jansi.Ansi;

public class Pwd extends ShellCommand {
	@Override
	protected String processCommand() throws ShellException {
		if(arguments.length > 0 && (arguments[0].equals("--help") || arguments[0].equals("-h"))){
			return help();
		}
		else{
			return System.getProperty("user.dir") + "\n";
		}
	}

	protected String help(){
		return Ansi.ansi().fgYellow().render(""
		+ "pwd: pwd\n"
		+ "Print the name of the current working directory.\n"
		).fgDefault().toString();
	}
}
