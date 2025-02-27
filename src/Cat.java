import java.io.FileNotFoundException;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Arrays;
import java.io.File;

/**
 * A stripped down version of the Bash command 'cd'
 *
 * @author	Jackson Butler
 * @date	Feb 27, 2025
 */
public class Cat extends ShellCommand {

	/**
	 * Used to convert abbreviated flags to their normal equivalent
	 *
	 * Ex. "-n" ==> "--number"
	 */
	private static final HashMap<String, String> ABBREVIATION_MAP  = new HashMap<>();

	/**
	 * Used to easily access and set the value of flags
	 */
	private final HashMap<String, Boolean> flagMap = new HashMap<>();

	static{
		ABBREVIATION_MAP.put("-b", "--number-nonblank");
		ABBREVIATION_MAP.put("-s", "--squeeze-blank");
		ABBREVIATION_MAP.put("-E", "--show-ends");
		ABBREVIATION_MAP.put("-n", "--number");
	}

	/**
	 * Create a new Cat command
	*/
	public Cat(){
		flagMap.put("--number-nonblank", false);
		flagMap.put("--squeeze-blank", false);
		flagMap.put("--show-ends", false);
		flagMap.put("--number", false);
	}

	@Override
	protected String processCommand() throws ShellException {
		if(arguments.length == 0) {
			throw new ShellException("cat: no arguments found");
		}

		// iterate through arguments
		// if any flags are found, set them to true in flagMap
		// setFlags() returns all arguments after the final flag (inputs)
		String[] inputs = setFlags();

		if(inputs.length == 0){
			throw new ShellException("cat: no inputs found");
		}
		else{
			return generateOutput(inputs);
		}
	}

	@Override
	protected String getHelpText(){
		return ""
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
		+ MiniShell.INDENT + "cat a b    Output a's contents concatentated with b's contents\n";
	}

	/**
	 * Iterate through arguments searching for flags
	 * If a flag is found, set its value to true within flagMap
	 *
	 * @return a truncated version of arguments (with flags removed)
	 * @throws ShellException if any of the flags are invalid
	*/
	private String[] setFlags() throws ShellException {

		// set all flags to false
		flagMap.forEach((k, v)->{flagMap.put(k, false);});

		int index = 0;

		// continue reading flags as
		// 1.) the bounds of arguments have not been exceeded
		// 2.) the current argument starts with a dash
		while(index < arguments.length && arguments[index].startsWith("-")){

			// ensure that the length of the argument is more than 1 character
			// this just prevents an exception from being thrown in the next block
			if(arguments[index].length() == 1){
				throw new ShellException("cat: '" + arguments[index] + "': invalid flag");
			}
			
			// if the current flag starts with "--" it is assumed to be a complete flag
			else if(arguments[index].charAt(1) == '-'){

				// look up the flag in flagMap
				// if it exists set its value to true
				if(flagMap.containsKey(arguments[index])){
					flagMap.put(arguments[index], true);
				}
				else{
					throw new ShellException("cat: '" + arguments[index] + "': invalid flag");
				}
			}

			// the current flag is assumed to be abbreviated
			else{

				// look up the flag in ABBREVIATION_MAP
				// if it exists set its value within flagMap to true
				String key = ABBREVIATION_MAP.get(arguments[index]);
				if(key != null){
					flagMap.put(key, true);
				}
				else{
					throw new ShellException("cat: '" + arguments[index] + "': invalid flag");
				}
			}

			// increment the index
			index++;
		}

		// return a truncated version of arguments with the flags removed
		return Arrays.copyOfRange(arguments, index, arguments.length);
	}

	/**
	 * Generate the commands output based off of previously set flag values within flagMap
	 *
	 * @param inputs a truncated list of arguments (all flags must be removed)
	 * @return the cat commands output
	 * @throws ShellException if assembleBase() fails
	*/
	private String generateOutput(String[] inputs) throws ShellException {

		// generate the base output by concatenating the output of inputs
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

		// compine lines into a single String and return
		return recombine(lines);
	}

	/**
	 * Read the contents of an array of filepaths into a String array
	 *
	 * @param inputs the array of filepaths
	 * @return contents of the specified files as lines
	 * @throws ShellException if ShellPath.buildPath() fails
	 * @throws ShellException if any of the specified files are directories
	 * @throws ShellException if any of the specified files do not exist or cannot be opened
	*/
	private String[] assembleBase(String[] inputs) throws ShellException {
		ArrayList<ArrayList<String>> inputLines = new ArrayList<>();

		int numberOfLines = 0;

		// if isPiped is set
		// the final element should be treated as a String litteral (not a file)
		// alter the bounds accordingly
		for(int i = 0; i < (isPiped ? inputs.length - 1 : inputs.length); i++){
			ArrayList<String> lines = new ArrayList<>();

			// open this argument as a file
			File file = new File(ShellPath.buildPath(inputs[i]));

			if(file.isDirectory()){
				throw new ShellException("cat: " + inputs[i] + ": is a directory");
			}

			// read the entire file into lines
			// increment numberOfLines with each added line
			try(Scanner scanner = new Scanner(file)){
				while(scanner.hasNextLine()){
					lines.add(scanner.nextLine());
					numberOfLines++;
				}
			}
			catch(FileNotFoundException e){
				throw new ShellException("cat: '" + inputs[i] + "': does not exist");
			}

			// add lines (the contents of the current file) to inputLines
			inputLines.add(lines);
		}

		String[] base;
		int baseIndex;

		// if the final element is a String litteral
		// split it on the newlines
		// store the contents in an array large enough to accomodate all other lines
		if(isPiped){
			base = inputs[inputs.length - 1].split("\n");
			base = Arrays.copyOf(base, base.length + numberOfLines);
			baseIndex = base.length - numberOfLines;
		}

		// otherwise, create an array large enough to accomodate the contents of inputLines
		else{
			base = new String[numberOfLines];
			baseIndex = 0;
		}

		// copy the content of inputLines into base
		for(int i = 0; i < inputLines.size(); i++){
			for(String line : inputLines.get(i)){
				base[baseIndex] = line;
				baseIndex++;
			}
		}

		return base;
	}

	/**
	 * Remove contiguous empty lines from an array of Strings
	 *
	 * @param lines the array of Strings
	 *
	 * WARNING: after calling this command, some elements of lines may be null
	 *			from this point on, there will be a null check in all commands dealling with this return value
	*/
	private void squeezeBlank(String[] lines){
		boolean previousLineWasBlank = false;

		for(int i = 0; i < lines.length; i++){

			if(lines[i].isEmpty()){

				// if the current lines is empty and the previous line was empty
				if(previousLineWasBlank){
					lines[i] = null;
				}
				else{
					previousLineWasBlank = true;
				}
			}

			// if the current line is not empty
			else{
				previousLineWasBlank = false;
			}
		}
	}

	/**
	 * Append '$' to each element in a String array
	 *
	 * @param lines the String array
	*/
	private void showEnds(String[] lines){
		for(int i = 0; i < lines.length; i++){
			if(lines[i] != null){
				lines[i] = lines[i] + "$";
			}
		}
	}

	/**
	 * Prepend ascending numbers to the elements of a String array
	 *
	 * @param lines the String array
	 * @param nonblank should blank lines be skipped (true if so
	*/
	private void number(String[] lines, boolean nonblank){

		// determine the number of lines
		// this is used to determine the size of indentation
		int lineNumber = 0;
		for(String line : lines){
			if(line != null){
				lineNumber++;
			}
		}

		// calculate the size of the indent (enough to accomodate all line numbers + MiniShell.INDENT)
		int indent = String.valueOf(lineNumber).length() + MiniShell.INDENT.length();
		

		StringBuilder builder = new StringBuilder();
		lineNumber = 1;
		for(int i = 0; i < lines.length; i++){

			// if nonblank is true
			// check that the current line is not empty
			// otherwise, proceed without a check
			if(lines[i] != null && (nonblank ? !lines[i].isEmpty() : true)){
				builder.setLength(0);
				String lineNumberText = String.valueOf(lineNumber);

				// append the proper number of spaces prior to the line number
				for(int j = 0; j < indent - lineNumberText.length(); j++){
					builder.append(" ");
				}

				// append the line number
				builder.append(lineNumberText);

				// prepend the indented line number to this line
				lines[i] = builder.toString() + MiniShell.INDENT + lines[i];

				lineNumber++;
			}
		}
	}

	/**
	 * Combine an array of Strings into a single String
	 * Seperate each String with a newline
	 *
	 * @param base the array of Strings
	 * @return the combined String
	*/
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
}
