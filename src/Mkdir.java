import java.io.File;
import java.io.IOException;

public class Mkdir extends ShellCommand{
	public String execute() throws ShellException{
		if(arguments.length == 0){
			throw new ShellException("mkdir: Missing operand");
		}

		if(isHelp()){
			return help();
		}
		else{
			for(String filename : arguments){
				if(!(new File(ShellPath.buildPath(filename)).mkdir())){
					throw new ShellException("mkdir: Failed to create directory '" + filename + "'");
				}
			}
		}
		return "";
	}

	protected String help(){
		return null;
	}
}
