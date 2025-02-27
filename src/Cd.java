import java.io.File;

/**
 * A stripped down version of the Bash command 'cd'
 *
 * @author	Jackson Butler
 * @date	Feb 26, 2025
 */
public class Cd extends ShellCommand{

	/**
	 * Create a new Cd command
	*/
	public Cd(){}

	@Override
	protected String processCommand() throws ShellException{

		if(arguments.length == 0){
			System.setProperty("user.dir", System.getProperty("user.home"));
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

		// this command does not print any text
		return "";
	}

	@Override
	protected String getHelpText(){
		return ""
		+ "Usage: cd DIR\n"
		+ "Change the shell working directory.\n"
		+ "\n"
		+ "Change the current directory to DIR. The default DIR is the value of the HOME shell variable.\n";
	}
}
