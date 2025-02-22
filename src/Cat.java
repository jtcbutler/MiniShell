import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.Ansi;
import java.io.FileNotFoundException;
import java.lang.StringBuilder;
import java.util.ListIterator;
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
		"-s", "--squeeze-blank", 
		"-n", "--number", 
		"-b", "--number-nonblank",
		"-E", "--show-ends",
	};

	private static final String INDENT = "  ";

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
			String[] lines = assembleBase(inputs);
			if(flags[1]){
				squeezeBlank(lines);
			}
			if(flags[2]){
				if(flags[3]){
					numberNonblank(lines);
				}
				else{
					number(lines);
				}
			}
			else if(flags[3]){
				numberNonblank(lines);
			}
			if(flags[4]){
				showEnds(lines);
			}
			return recombine(lines);
		}
	}

	protected String help(){
		return Ansi.ansi().fgYellow().render(""
		+ "Usage: cat [OPTION]... [FILE]...\n"
		+ "Concatenate FILE(s) to standard output.\n"
		+ "\n"
		+ "-s, --squeeze-blank        suppress repeated empty output lines\n"
		+ "-n, --number               number all output lines\n"
		+ "-b, --number-nonblank      number nonempty output lines, overrides -n\n"
		+ "-E, --show-ends            display $ at end of each line\n"
		+ "\n"
		+ "-p, --pipe-position=INDEX  the INDEX (relative to other inputs) at which piped input should be inserted\n"
		+ "                           by default, piped input will be inserted before all other inputs\n"
		+ "\n"
		+ "Examples:\n"
		+ "  cat a      Output a's contents\n"
		+ "  cat a b    Output a's contents concatentated with b's contents\n").fgDefault().toString();
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

			throw new ShellException("cat: invalid option '" + arguments[index] + "'\n");
		}

		return Arrays.copyOfRange(arguments, index, arguments.length);
	}

	private String[] assembleBase(String[] inputs) throws ShellException {
		ArrayList<ArrayList<String>> inputLines = new ArrayList<>(isPiped ? inputs.length - 1 : inputs.length);
		int numberOfLines = 0;

		for(int i = 0; i < (isPiped ? inputs.length - 1 : inputs.length); i++){
			ArrayList<String> lines = new ArrayList<>();

			File file;
			if(inputs[i].startsWith(ShellPath.root)){
				file = new File(inputs[i]);
			}
			else{
				file = new File(ShellPath.buildPath(inputs[i]));
			}

			if(file.isDirectory()){
				throw new ShellException("cat: " + inputs[i] + ": is a directory");
			}

			try{
				Scanner scanner = new Scanner(file);
				while(scanner.hasNextLine()){
					lines.add(scanner.nextLine());
					numberOfLines++;
				}
				scanner.close();
			}
			catch(FileNotFoundException e){
				throw new ShellException("cat: \"" + inputs[i] + "\" does not exist");
			}

			inputLines.add(lines);
		}

		String[] base;
		if(isPiped){
			if(parameters[0] < 0) parameters[0] = 0;
			else if(parameters[0] > inputLines.size()) parameters[0] = inputLines.size();

			ArrayList<String> pipedInputLines = new ArrayList<>();
			for(String line : inputs[inputs.length - 1].split("\n")){
				pipedInputLines.add(line);
				numberOfLines++;
			}

			base = new String[numberOfLines];

			int baseIndex = 0;
			for(int i = 0; i < parameters[0]; i++){
				for(String line : inputLines.get(i)){
					base[baseIndex] = line;
					baseIndex++;
				}
			}

			for(String line : pipedInputLines){
				base[baseIndex] = line;
				baseIndex++;
			}

			for(int i = parameters[0]; i < inputLines.size(); i++){
				for(String line : inputLines.get(i)){
					base[baseIndex] = line;
					baseIndex++;
				}
			}
		}
		else{
			base = new String[numberOfLines];
			int baseIndex = 0;
			for(int i = 0; i < inputLines.size(); i++){
				for(String line : inputLines.get(i)){
					base[baseIndex] = line;
					baseIndex++;
				}
			}
		}

		return base;
	}

	private String recombine(String[] base){
		StringBuilder builder = new StringBuilder();
		for(String line : base){
			if(line != null){
				builder.append(line);
				builder.append("\n");
			}
		}
		return builder.toString();
	}

	private void squeezeBlank(String[] lines){
		boolean previousLineWasBlank = false;

		for(int i = 0; i < lines.length; i++){
			if(lines[i].isEmpty()){
				if(previousLineWasBlank){
					lines[i] = null;
				}
				else{
					previousLineWasBlank = true;
				}
			}
			else{
				previousLineWasBlank = false;
			}
		}
	}

	private void showEnds(String[] lines){
		for(int i = 0; i < lines.length; i++){
			if(lines[i] != null){
				lines[i] = lines[i] + "$";
			}
		}
	}

	private void number(String[] lines){
		int lineNumber = 0;
		for(String line : lines){
			if(line != null){
				lineNumber++;
			}
		}

		int indent = String.valueOf(lineNumber).length() + INDENT.length();
		StringBuilder builder = new StringBuilder();
		lineNumber = 1;

		for(int i = 0; i < lines.length; i++){
			if(lines[i] != null){
				builder.setLength(0);
				String lineNumberText = String.valueOf(lineNumber);
				for(int j = 0; j < indent - lineNumberText.length(); j++){
					builder.append(" ");
				}
				builder.append(lineNumberText);
				lines[i] = builder.toString() + INDENT + lines[i];
				lineNumber++;
			}
		}
	}

	private void numberNonblank(String[] lines){
		int lineNumber = 0;
		for(String line : lines){
			if(line != null && !line.isEmpty()){
				lineNumber++;
			}
		}

		int indent = String.valueOf(lineNumber).length() + INDENT.length();
		StringBuilder builder = new StringBuilder();
		lineNumber = 1;

		for(int i = 0; i < lines.length; i++){
			if(lines[i] != null && !lines[i].isEmpty()){
				builder.setLength(0);
				String lineNumberText = String.valueOf(lineNumber);
				for(int j = 0; j < indent - lineNumberText.length(); j++){
					builder.append(" ");
				}
				builder.append(lineNumberText);
				lines[i] = builder.toString() + INDENT + lines[i];
				lineNumber++;
			}
		}
	}
}
