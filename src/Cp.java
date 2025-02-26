import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Cp extends ShellCommand{

	protected String processCommand() throws ShellException{
		if(arguments.length == 0){
			throw new ShellException("");
		}
		else if(arguments.length == 1){
			throw new ShellException("");
		}
		else if(arguments.length == 2){
			copyOne();
		}
		else{
			copyMany();
		}

		return "";
	}

	protected String help(){
		return "";
	}

	private void copyOne() throws ShellException{
		String source = ShellPath.buildPath(arguments[0]);
		String destination = ShellPath.buildPath(arguments[1]);

		if(new File(destination).isDirectory()){
			destination = ShellPath.buildPath(arguments[1]) + ShellPath.FILE_SEPARATOR + ShellPath.getLast(arguments[0]);
		}

		try{
			Files.copy(Paths.get(source), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
		}
		catch(Exception e){
			throw new ShellException("");
		}
	}

	private void copyMany() throws ShellException{
		String destinationPath = ShellPath.buildPath(arguments[arguments.length - 1]);

		if(!(new File(destinationPath).isDirectory())){
			throw new ShellException("");
		}

		for(int i = 0; i < arguments.length - 1; i++){
			Path source = Paths.get(ShellPath.buildPath(arguments[i]));
			Path destination = Paths.get(destinationPath + ShellPath.FILE_SEPARATOR + ShellPath.getLast(arguments[i]));

			try{
				Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
			}
			catch(Exception e){
				throw new ShellException("");
			}
		}
	}
}
