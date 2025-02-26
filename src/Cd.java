import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.Ansi;
import java.io.File;

public class Cd extends ShellCommand{

	@Override
	protected String processCommand() throws ShellException{

		if(arguments.length == 0){
			System.setProperty("user.dir", System.getProperty("user.home"));
		}
		else{
			if(arguments[0].equals("--help") || arguments[0].equals("-h")){
				return help();	
			}
			else{
				String path = ShellPath.buildPath(arguments[0]);
				File file = new File(path);

				if(!file.exists()){
					throw new ShellException("cd: " + arguments[0] + ": No such file or directory");
				}
				else if(!file.isDirectory()){
					throw new ShellException("cd: " + arguments[0] + ": Not a directory");
				}
				else{
					System.setProperty("user.dir", path);
				}
			}
		}
		return "";
	}

	protected String help(){
		return Ansi.ansi().fgYellow().render(""
		+ "Usage: cd DIR\n"
		+ "Change the shell working directory.\n"
		+ "\n"
		+ "Change the current directory to DIR. The default DIR is the value of the HOME shell variable.\n"
		).fgDefault().toString();
	}
}
