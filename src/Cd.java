import org.fusesource.jansi.Ansi;
import java.io.File;

/**
 * A stripped down version of the Bash command 'cd'
 *
 * @author	Jackson Butler
 * @since	Feb 26, 2025
 */
public class Cd extends ShellCommand{

	/**
	 * Create a new Cd command
	*/
	public Cd(){}

	/**
	 * Given a path, alter the current working directory within the shell
	 *
	 * @return an empty String
	 * @throws ShellException if ShellPath.buildPath fails
	 * @throws ShellException if the provided path does not exist
	 * @throws ShellException if the provided path does not lead to a directory
	*/
	@Override
	protected String processCommand() throws ShellException{

		// if no arguments were provided
		// set "user.home" as the current directory
		if(arguments.length == 0){
			System.setProperty("user.dir", System.getProperty("user.home"));
		}

		// otherwise, build an absolute path from the provided path
		else{
			String path = ShellPath.buildPath(arguments[0]);
			File file = new File(path);

			// if the provided path does exist
			// throw a new ShellException
			if(!file.exists()){
				throw new ShellException("cd: " + arguments[0] + ": No such file or directory");
			}

			// if the provided path does not lead to a directory
			// throw a new ShellException
			else if(!file.isDirectory()){
				throw new ShellException("cd: " + arguments[0] + ": Not a directory");
			}

			// otherwise, set path as the current directory
			else{
				System.setProperty("user.dir", path);
			}
		}

		// this command does not print any text
		return "";
	}

	/**
	 * Explain the intended use of this command
	 *
	 * @return an explanation of the command
	*/
	protected String help(){
		return Ansi.ansi().fgYellow().render(""
		+ "Usage: cd DIR\n"
		+ "Change the shell working directory.\n"
		+ "\n"
		+ "Change the current directory to DIR. The default DIR is the value of the HOME shell variable.\n"
		).fgDefault().toString();
	}
}
