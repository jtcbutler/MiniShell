import java.io.File;

public class Cd extends ShellCommand{
	public String execute() throws ShellCommandException{
		if(this.arguments.length > 1){
			throw new ShellCommandException("cd: too many arguments");
		}

		if(this.arguments.length == 0){
			System.setProperty("user.dir", System.getProperty("user.home"));
		}
		else if(this.arguments[0].startsWith("~")){
		}
		else if(this.arguments[0].startsWith(MiniShell.root)){
		}
		else{
		}

		return "";
	}

	private void attemptMove(String path) throws ShellCommandException{
		File file = new File(path);
		if(!file.exists()){
			throw new ShellCommandException("cd: " + this.arguments[0] + ": No such file or directory");
		}
		else if(!file.isDirectory()){
			throw new ShellCommandException("cd: " + this.arguments[0] + ": Not a directory");
		}
		else{
			System.setProperty("user.dir", path);
		}
	}
}
