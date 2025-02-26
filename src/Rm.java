import java.io.File;

public class Rm extends ShellCommand{

	@Override
	protected String processCommand() throws ShellException{
		if(helpNeeded()){
			return help();
		}
		else{
			for(String argument : arguments){
				File file = new File(argument);
				if(file.exists() && file.isFile()){
					file.delete();
				}
				else{
					throw new ShellException("");
				}
			}
			return "";
		}
	}

	@Override
	protected String help(){
		return null;
	}
}
