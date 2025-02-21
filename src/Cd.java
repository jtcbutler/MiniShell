import java.io.File;

public class Cd extends ShellCommand{

	@Override
	public String execute() throws ShellException{

		if(arguments.length == 0){
			System.setProperty("user.dir", System.getProperty("user.home"));
		}
		else if(arguments.length == 1){
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
		else{
			throw new ShellException("cd: too many arguments");
		}

		return "";
	}
}
