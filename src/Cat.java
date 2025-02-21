import java.io.FileNotFoundException;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Arrays;
import java.io.File;

public class Cat extends ShellCommand {
	private static final String[] VALID_PARAMETERS = {
		"-p", "--pipe-position"
	};

	private static final String[] VALID_FLAGS = {
		"-h", "--help",
		"-n", "--number", 
		"-b", "--number-nonblank",
		"-s", "--squeeze-blank", 
		"-E", "--show-ends",
	};

	private final int[] parameters = new int[VALID_PARAMETERS.length / 2];
	private final boolean[] flags = new boolean[VALID_FLAGS.length / 2];

	@Override
	public String execute() throws ShellException {
		if(arguments.length == 0) {
			throw new ShellException("cat: no arguments found");
		}

		String[] inputs = setOptions();

		if(flags[0]){
			return help();
		}
		else if(inputs.length == 0){
			throw new ShellException("cat: no inputs found");
		}
		else{
			ArrayList<StringBuilder> lines = assembleBase(inputs);
			for(StringBuilder line : lines){
				System.out.println(line);
			}
			return "";
		}
	}

	protected String help(){
		return ""
		+ "Usage: cat [OPTION]... [FILE]...\n"
		+ "Concatenate FILE(s) to standard output.\n"
		+ "\n"
		+ "-n, --number               number all output lines\n"
		+ "-b, --number-nonblank      number nonempty output lines, overrides -n\n"
		+ "-s, --squeeze-blank        suppress repeated empty output lines\n"
		+ "-p, --pipe-position=INDEX  the INDEX (relative to other inputs) at which piped input should be inserted\n"
		+ "-E, --show-ends            display $ at end of each line\n"
		+ "\n"
		+ "Examples:\n"
		+ "  cat a      Output a's contents\n"
		+ "  cat a b    Output a's contents concatentated with b's contents\n";
	}

	private String[] setOptions() throws ShellException {

		for(int i = 0; i < parameters.length; i++){
			parameters[i] = 0;
		}
		for(int i = 0; i < flags.length; i++){
			flags[i] = false;
		}

		int index = 0;

		while(index < arguments.length){
			if (!arguments[index].startsWith("-")) break;

			int parameterIndex = -1;
			for(int i = 0; i < VALID_PARAMETERS.length; i++){
				if(arguments[index].equals(VALID_PARAMETERS[i])) parameterIndex = i;
			}
			if(parameterIndex >= 0){
				if(index+1 == arguments.length || arguments[index+1].startsWith("-")){
					throw new ShellException("cat: " + arguments[index] + ": no value provided");
				}

				try{
					parameters[parameterIndex / 2] = Integer.parseInt(arguments[index+1]);
					index += 2;
					continue;
				}
				catch(NumberFormatException e){
					throw new ShellException("cat: " + arguments[index] + ": invalid value provided");
				}
			}

			int flagIndex = -1;
			for(int i = 0; i < VALID_FLAGS.length; i++){
				if(arguments[index].equals(VALID_FLAGS[i])) flagIndex = i;
			}
			if(flagIndex >= 0){
				flags[flagIndex / 2] = true;
				index++;
				continue;
			}

			throw new ShellException("cat: invalid option '" + arguments[index] + "'\nTry 'cat --help' for more information");
		}

		return Arrays.copyOfRange(arguments, index, arguments.length);
	}

	private ArrayList<StringBuilder> assembleBase(String[] inputs) throws ShellException {
		LinkedList<ArrayList<StringBuilder>> inputLines = new LinkedList<>();

		for(int i = 0; i < inputs.length - 1; i++){
			inputLines.add(parseFileToStringBuilderLines(inputs[i]));
		}

		try{
			inputLines.add(parseFileToStringBuilderLines(inputs[inputs.length - 1]));
		}
		catch(ShellException e){
			inputLines.add(parameters[0], parseStringToStringBuilderLines(inputs[inputs.length - 1]));
		}

		ArrayList<StringBuilder> concatenatedLines = new ArrayList<>();
		for(ArrayList<StringBuilder> inputLine : inputLines){
			for(StringBuilder line : inputLine){
				concatenatedLines.add(line);
			}
		}

		return concatenatedLines;
	}

	private ArrayList<StringBuilder> parseFileToStringBuilderLines(String input) throws ShellException{
		ArrayList<StringBuilder> lines = new ArrayList<>();

		File file;
		if(input.startsWith(ShellPath.root)){
			file = new File(input);
		}
		else{
			file = new File(ShellPath.buildPath(input));
		}

		if(file.isDirectory()){
			throw new ShellException("cat: " + input + ": is a directory");
		}

		try{
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()){
				lines.add(new StringBuilder(scanner.nextLine()));
			}
			scanner.close();
		}
		catch(FileNotFoundException e){
			throw new ShellException("cat: \"" + input + "\" does not exist");
		}

		return lines;
	}

	private ArrayList<StringBuilder> parseStringToStringBuilderLines(String input){
		ArrayList<StringBuilder> stringBuilderLines = new ArrayList<>();
		String[] lines = input.split("\n");
		for(String line : lines){
			stringBuilderLines.add(new StringBuilder(line));
		}
		return stringBuilderLines;
	}
}
