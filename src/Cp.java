import java.nio.file.StandardCopyOption;
import org.fusesource.jansi.Ansi;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.File;

/**
 * A stripped down version of the Bash command 'cp'
 *
 * @author	Jackson Butler
 * @date 	Feb 26, 2025
 */
public class Cp extends ShellCommand{

	/**
	 * Create a new Cp command
	 */
	public Cp(){}

	/**
	 * Copy a single file/directory or a list of files/directories to a specified path
	 *
	 * @return an empty String
	 */
	@Override
	protected String processCommand() throws ShellException{

		// if no arguments were provided
		// throw a new ShellException
		if(arguments.length == 0){
			throw new ShellException("cp: Missing file operand");
		}

		// if only one argument was provided
		// throw a new ShellException
		else if(arguments.length == 1){
			throw new ShellException("cp: Missing destination file operand after '" + arguments[0] + "'");
		}

		// if exactly two arguments were provided
		// allow for the destination to be a directory or file
		else if(arguments.length == 2){
			String source = ShellPath.buildPath(arguments[0]);
			String destination = ShellPath.buildPath(arguments[1]);

			// if the source does not exist
			// throw a new ShellException
			if(!Files.exists(Paths.get(source))){
				throw new ShellException("cp: '" + arguments[0] + "' is not a file or directory");
			}

			// if the destination is a directory
			// the destination file will retain the source files name
			// extract the source files name and append it to the destination path
			if(Files.isDirectory(Paths.get(destination))){
				destination = ShellPath.buildPath(arguments[1]) + ShellPath.FILE_SEPARATOR + ShellPath.getLast(arguments[0]);
			}

			// attempt to copy the file
			// if the copy fails, throw a new ShellException
			try{
				Files.copy(Paths.get(source), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
			}
			catch(Exception e){
				throw new ShellException("cp: Failed to copy '" + arguments[0] + "' to '" + arguments[1] + "'");
			}
		}

		// if more than two arguments were provided
		// the destination must be a directory
		else{

			// the destination directory is the final argument
			String destinationPath = ShellPath.buildPath(arguments[arguments.length - 1]);

			// if the destination is not a directory
			// throw a new ShellException
			if(!Files.isDirectory(Paths.get(destinationPath))){
				throw new ShellException("cp: '" + arguments[arguments.length - 1] + "' is not a directory");
			}

			// for each argument (excluding the final argument)
			for(int i = 0; i < arguments.length - 1; i++){

				// append the name of the current file to the destination path
				Path destination = Paths.get(destinationPath + ShellPath.FILE_SEPARATOR + ShellPath.getLast(arguments[i]));

				// attempt to copy the file
				// if the copy fails, throw a new ShellException
				try{
					Files.copy(Paths.get(ShellPath.buildPath(arguments[i])), destination, StandardCopyOption.REPLACE_EXISTING);
				}
				catch(Exception e){
					throw new ShellException("cp: Failed to copy '" + arguments[i] + "' to '" + arguments[arguments.length - 1] + "'");
				}
			}
		}

		// this command does not print any text
		return "";
	}

	/**
	 * Return text explaining the intended use of this command
	 *
	 * @return String the explanation
	*/
	protected String help(){
		return Ansi.ansi().fgYellow().render(""
			+ "Usage: cp SOURCE DEST or cp SOURCE... DIRECTORY\n"
			+ "Copy SOURCE to DEST, or multiple SOURCE(s) to DIRECTORY.\n"
		).fgDefault().toString();
	}
}
