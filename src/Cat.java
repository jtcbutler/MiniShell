import java.lang.StringBuilder;

public class Cat extends ShellCommand {

	// -n, --number             number all output lines
	// -b, --number-nonblank    number nonempty output lines, overrides -n
	// -s, --squeeze-blank      suppress repeated empty output lines
	// -E, --show-ends          display $ at end of each line
	
	private static final String[] VALID_ARGUMENTS = {"-n", "--number", "-b", "--number-nonblank","-s", "--squeeze-blank", "-E", "--show-ends"};
	private final boolean flags = new boolean[4];

	@Override
	public String execute() throws ShellException {
		if(this.arguments.length == 0) {
			throw new ShellException("cat: no arguments found");
		}

		int argumentIndex = 0;

		while(argumentIndex < this.arguments.length){
			if(argument.startsWith("--")){
			}
			else if (argument.startsWith("-")){
			}
			else{
				break;
			}

			argumentIndex++;
		}

		if(argumentIndex == this.arguments.length){
			throw new ShellException("cat: no inputs found");
		}

		return null;
	}
}
