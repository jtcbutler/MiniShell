import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.Ansi;

import java.io.FileNotFoundException;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Arrays;
import java.io.File;

public class Cat extends ShellCommand {
	private static final HashMap<String, String> ABBREVIATION_MAP  = new HashMap<>();

	private final HashMap<String, Boolean> flagMap = new HashMap<>();

	static{
		ABBREVIATION_MAP.put("-b", "--number-nonblank");
		ABBREVIATION_MAP.put("-s", "--squeeze-blank");
		ABBREVIATION_MAP.put("-E", "--show-ends");
		ABBREVIATION_MAP.put("-n", "--number");
		ABBREVIATION_MAP.put("-h", "--help");
	}

	public Cat(){
		flagMap.put("--number-nonblank", false);
		flagMap.put("--squeeze-blank", false);
		flagMap.put("--show-ends", false);
		flagMap.put("--number", false);
		flagMap.put("--help", false);
	}

	@Override
	public String execute() throws ShellException {
		if(arguments.length == 0) {
			throw new ShellException("cat: no arguments found");
		}

		String[] inputs = setFlags();

		if(inputs.length == 0){
			throw new ShellException("cat: no inputs found");
		}
		else{
			return generateOutput(inputs);
		}
	}

	private String[] setFlags() throws ShellException {
		flagMap.forEach((k, v)->{flagMap.put(k, false);});

		int index = 0;
		while(index < arguments.length && arguments[index].startsWith("-")){
			if(arguments[index].length() == 1){
				throw new ShellException("");
			}
			else if(arguments[index].charAt(1) == '-'){
				if(flagMap.containsKey(arguments[index])){
					flagMap.put(arguments[index], true);
				}
				else{
					throw new ShellException("");
				}
			}
			else{
				String key = ABBREVIATION_MAP.get(arguments[index]);
				if(key != null){
					flagMap.put(key, true);
				}
				else{
					throw new ShellException("");
				}
			}
			index++;
		}
		return Arrays.copyOfRange(arguments, index, arguments.length);
	}

	private String generateOutput(String[] inputs) throws ShellException {
		if(flagMap.get("--help")){
			return help();
		}

		String[] lines = assembleBase(inputs);

		if(flagMap.get("--squeeze-blank")){
			squeezeBlank(lines);
		}

		if(flagMap.get("--number-nonblank")){
			number(lines, true);
			flagMap.put("--number", false);
		}

		if(flagMap.get("--number")){
			number(lines, false);
		}

		if(flagMap.get("--show-ends")){
			showEnds(lines);
		}

		return recombine(lines);
	}


	private String[] assembleBase(String[] inputs) throws ShellException {

		ArrayList<ArrayList<String>> inputLines = new ArrayList<>();
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
		int baseIndex;
		if(isPiped){
			base = inputs[inputs.length - 1].split("\n");
			base = Arrays.copyOf(base, base.length + numberOfLines);
			baseIndex = base.length - numberOfLines;
		}
		else{
			base = new String[numberOfLines];
			baseIndex = 0;
		}

		for(int i = 0; i < inputLines.size(); i++){
			for(String line : inputLines.get(i)){
				base[baseIndex] = line;
				baseIndex++;
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

	private void number(String[] lines, boolean nonblank){
		int lineNumber = 0;
		for(String line : lines){
			if(line != null){
				lineNumber++;
			}
		}

		int indent = String.valueOf(lineNumber).length() + MiniShell.INDENT.length();
		StringBuilder builder = new StringBuilder();
		lineNumber = 1;

		for(int i = 0; i < lines.length; i++){
			if(lines[i] != null && (nonblank ? !lines[i].isEmpty() : true)){
				builder.setLength(0);
				String lineNumberText = String.valueOf(lineNumber);
				for(int j = 0; j < indent - lineNumberText.length(); j++){
					builder.append(" ");
				}
				builder.append(lineNumberText);
				lines[i] = builder.toString() + MiniShell.INDENT + lines[i];
				lineNumber++;
			}
		}
	}

	protected String help(){
		return Ansi.ansi().fgYellow().render(""
		+ "Usage: cat [OPTION]... [FILE]...\n"
		+ "Concatenate FILE(s) to standard output.\n"
		+ "\n"
		+ "-b, --number-nonblank      number nonempty output lines, overrides -n\n"
		+ "-s, --squeeze-blank        suppress repeated empty output lines\n"
		+ "-E, --show-ends            display $ at end of each line\n"
		+ "-n, --number               number all output lines\n"
		+ "\n"
		+ "Examples:\n"
		+ MiniShell.INDENT + "cat a      Output a's contents\n"
		+ MiniShell.INDENT + "cat a b    Output a's contents concatentated with b's contents\n"
		).fgDefault().toString();
	}
}
