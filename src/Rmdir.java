import org.fusesource.jansi.Ansi;
import java.io.File;

/**
 * A stripped down version of the Bash command 'rmdir'
 *
 * @author	Jackson Butler
 * @date 	Feb 26, 2025
 */
public class Rmdir extends ShellCommand {

	/**
	 * Create a new Rmdir command
	 */
	public Rmdir(){}

	@Override
	protected String processCommand() throws ShellException {

		for (String argument : arguments) {
			File file = new File(ShellPath.buildPath(argument));

			if(!file.exists()){
				throw new ShellException("rmdir: cannot remove '" + argument + "': No such file or directory");
			}
			else if(!file.isDirectory()){
				throw new ShellException("rmdir: cannot remove '" + argument + "': Is not a directory");
			}
			else if(file.list().length == 0){
				throw new ShellException("rmdir: cannot remove '" + argument + "': Directory not empty");
			}
			else if(!file.delete()){
				throw new ShellException("rmdir: cannot remove '" + argument + "': Permission denied");
			}
		}

		// this command does not print any text
		return "";
	}

	@Override
	protected String getHelpText(){
		return ""
		+ "Usage: rmdir DIRECTORY...\n"
		+ "Remove the DIRECTORY(ies), if they are empty.\n";
	}
}
