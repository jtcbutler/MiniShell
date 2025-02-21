import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Arrays;

public class Cat extends ShellCommand {
	private static final String[] VALID_FLAGS = {
		"-h", "--help",
		"-n", "--number", 
		"-b", "--number-nonblank",
		"-s", "--squeeze-blank", 
		"-E", "--show-ends"
	};

	private final boolean[] flags = new boolean[VALID_FLAGS.length / 2];

	@Override
	public String execute() throws ShellException {
		if(arguments.length == 0) {
			throw new ShellException("cat: no arguments found");
		}

		String[] inputs = setFlags();

		if(flags[0]){
			return help();
		}
		else if(inputs.length == 0){
			throw new ShellException("cat: no inputs found");
		}
		else{
			//ArrayList<StringBuilder> base = assembleBase(inputs);
			return "";
		}
	}

	protected String help(){
		return ""
		+ "Usage: cat [OPTION]... [FILE]...\n"
		+ "Concatenate FILE(s) to standard output.\n"
		+ "\n"
		+ "-n, --number             number all output lines\n"
		+ "-b, --number-nonblank    number nonempty output lines, overrides -n\n"
		+ "-s, --squeeze-blank      suppress repeated empty output lines\n"
		+ "-E, --show-ends          display $ at end of each line\n"
		+ "\n"
		+ "Examples:\n"
		+ "  cat a      Output a's contents\n"
		+ "  cat a b    Output a's contents concatentated with b's contents\n";
	}

	private String[] setFlags() throws ShellException {
		int index = 0;

		while(index < arguments.length){
			if (!arguments[index].startsWith("-")) break;

			int flagIndex = -1;
			for(int i = 0; i < VALID_FLAGS.length; i++){
				if(arguments[index].equals(VALID_FLAGS[i])) flagIndex = i;
			}

			if(flagIndex < 0){
				throw new ShellException("cat: invalid option '" + arguments[index] + "'\nTry 'cat --help' for more information");
			}
			else{
				flags[flagIndex / 2] = true;
			}

			index++;
		}

		return Arrays.copyOfRange(arguments, index, arguments.length);
	}

	/*
	private ArrayList<StringBuilder> assembleBase(String[] inputs){
		for(int i = 0; i < inputs.length - 1; i++){
			File file;
			if(inputs[i].startsWith(ShellPath.root)){
				file = new File(ShellPath.buildPathRoot(inputs[i]));
			}
			else{
				file = new File(Shell)
			}
		}
		return null;
	}
	*/
}
