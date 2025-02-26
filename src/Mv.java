import java.io.File;

public class Mv extends ShellCommand{

	protected String processCommand() throws ShellException{
		if(arguments.length == 0){
			throw new ShellException("");
		}
		else if(arguments.length == 1){
			throw new ShellException("");
		}
		else if(arguments.length == 2){
			moveOne();
		}
		else{
			moveMany();
		}

		return "";
	}

	protected String help(){
		return "";
	}

	private void moveOne() throws ShellException{
		File source = new File(ShellPath.buildPath(arguments[0]));
		File destination = new File(ShellPath.buildPath(arguments[1]));

		if(destination.isDirectory()){
			destination = new File(ShellPath.buildPath(arguments[1]) + ShellPath.fileSeparator + ShellPath.getLast(arguments[0]));
		}

		if(!source.renameTo(destination)){
			throw new ShellException("");
		}
	}

	private void moveMany() throws ShellException{
		String destinationPath = ShellPath.buildPath(arguments[arguments.length - 1]);

		if(!(new File(destinationPath).isDirectory())){
			throw new ShellException("");
		}

		for(int i = 0; i < arguments.length - 1; i++){
			File source = new File(ShellPath.buildPath(arguments[i]));
			File destination = new File(destinationPath + ShellPath.fileSeparator + ShellPath.getLast(arguments[i]));

			if(!source.renameTo(destination)){
				throw new ShellException("");
			}
		}
	}
}
