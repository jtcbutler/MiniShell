import java.io.IOException;
import java.io.File;

/**
 * A stripped down version of the Bash command 'touch'
 *
 * @author	Jackson Butler
 * @date 	Feb 26, 2025
 */
public class Touch extends ShellCommand {

	/**
	 * Create a new Touch command
	 */
	public Touch(){}

	@Override
	protected String processCommand() throws ShellException {

		if(arguments.length == 0){
			throw new ShellException("touch: missing file operand");
		}

		for(String filename : arguments){
			try {
				File file = new File(ShellPath.buildPath(filename));

				// if the file does not always exist
				// create file
				if(!file.exists()){
					file.createNewFile();
				}
				else{
					throw new ShellException("touch: " + filename + ": file already exists");
				}
			} 
			catch (IOException e) {
				throw new ShellException("touch: failed to create file '" + filename + "'");
			}
		}

		// this command does not print any text
		return "";
	}

	@Override
	protected String getHelpText(){
		return ""
		+ "Usage: touch FILE...\n"
		+ "Create a new empty file\n";
	}
}
