import java.io.File;

public class Cd extends ShellCommand{

	@Override
	public String execute() throws ShellException{

		if(this.arguments.length == 0){
			System.setProperty("user.dir", System.getProperty("user.home"));
		}
		else if(this.arguments.length == 1){
			String path;

			if(this.arguments[0].startsWith("~")){
				path = ShellPath.buildPathRoot(this.arguments[0].replace("~", System.getProperty("user.home")));
			}
			else if(this.arguments[0].startsWith(ShellPath.root)){
				path = ShellPath.buildPathRoot(this.arguments[0]);
			}
			else{
				path = ShellPath.buildPathCurrent(this.arguments[0]);
			}

			attemptMove(path);
		}
		else{
			throw new ShellException("cd: too many arguments");
		}

		return "";
	}

	private void attemptMove(String path) throws ShellException{
		File file = new File(path);
		if(!file.exists()){
			throw new ShellException("cd: " + this.arguments[0] + ": No such file or directory");
		}
		else if(!file.isDirectory()){
			throw new ShellException("cd: " + this.arguments[0] + ": Not a directory");
		}
		else{
			System.setProperty("user.dir", path);
		}
	}
}
