import java.io.File;
import java.io.IOException;
import org.fusesource.jansi.Ansi;

public class Touch extends ShellCommand {
	@Override
	protected String processCommand() throws ShellException {

		if(arguments.length < 1){
			throw new ShellException("touch: Missing file operand");
		}

		if(arguments[0].equals("--help") || arguments[0].equals("-h")){
			return help();
		}
		else{
			for(String filename : arguments){
				try {
					File file = new File(ShellPath.buildPath(filename));
					if(!file.exists()){
						file.createNewFile();
					}
					else{
						throw new ShellException("touch: " + filename + ": File already exists");
					}
				} 
				catch (IOException e) {
					throw new ShellException("touch: Failed to create file '" + filename + "'");
				}
			}
			return "";
		}
	}

	@Override
	protected String help(){
		return Ansi.ansi().fgYellow().render(""
			+ "Usage: touch FILE...\n"
			+ "Create a new empty file\n"
		).fgDefault().toString();
	}
}
