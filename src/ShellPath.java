import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Non-instantiable class that provides utilities for path manipulation
 *
 * @author	Jackson Butler
 * @since	Feb 26, 2025	
 */
public class ShellPath{

	/**
	 * The systems root directory
	 */
	public static final String ROOT;					
	
	/**
	 * The systems file separator
	 */
	public static final String FILE_SEPARATOR;			
	
	/**
	 * A regex pattern for matching the systems file separator
	 */
	private static final String FILE_SEPARATOR_PATTERN; 

	static {

		// query the systems file separator
		// ROOT and FILE_SEPARATOR_PATTERN will be assigned based on this
		FILE_SEPARATOR = System.getProperty("file.separator");

		// unix
		if(FILE_SEPARATOR.equals("/")){
			FILE_SEPARATOR_PATTERN = "/+";
			ROOT = "/"; 
		}

		// windows
		else{
			FILE_SEPARATOR_PATTERN = "\\\\+";
			ROOT = "C:\\";
		}
	}

	/**
	 * Private constructor
	 * This is a non-instantiable class
	*/
	private ShellPath(){}

	/**
	 * Given a path, construct the smallest equivalent absolute path
	 * If used on an absolute path, this function will sanatize the path
	 *
	 * Ex. "./Main.java" ==> "/home/your_username/Main.java" (assuming you are currently in "/home/your_username")
	 * Ex. "~/Main.java" ==> "/home/your_username/Main.java"
	 * Ex. "/home/.././home/your_username/./../your_username/Main.java" ==> "/home/your_username/Main.java"
	 *
	 * @param path the path that should be made absolute
	 * @return the absolute equiavalent of path
	 * @throws ShellException if path attempts to ascend from root (e.g. "/..", "C:\..")
	*/
	public static String buildPath(String path) throws ShellException{
		String absolutePath;
		String relativePath;

		// if the provided path starts with ROOT
		// the base absolutePath will be empty
		if(path.startsWith(ROOT)){
			absolutePath = "";
			relativePath = path;
		}

		// if the provided path starts with '~'
		// the base absolutePath will be empty
		// replace the '~' with the users home directory path
		else if(path.startsWith("~")){
			absolutePath = "";
			relativePath = path.replace("~", System.getProperty("user.home"));
		}

		// otherwise, the provided path is assumed to be relative
		// the base absolutePath will be set to the current directory
		else{
			absolutePath = System.getProperty("user.dir");
			relativePath = path;
		}

		// split both the absolute and relative path on file separators and convert the results to array lists
		ArrayList<String> absolutePathComponents = new ArrayList<String>(Arrays.asList(absolutePath.split(FILE_SEPARATOR_PATTERN)));
		ArrayList<String> relativePathComponents = new ArrayList<String>(Arrays.asList(relativePath.split(FILE_SEPARATOR_PATTERN)));

		// add a placeholder for ROOT if necessary
		if(absolutePathComponents.size() == 0) absolutePathComponents.add("");

		// for each element in relativePathComponents
		for(String relativePathComponent : relativePathComponents){

			// do nothing if the component is empty or "."
			if(relativePathComponent.equals("") || relativePathComponent.equals(".")){
				continue;
			}

			// if the component is ".." remove one element from the end of absolutePathComponents
			// if absolutePathComponents has only one element (ROOT), throw and exception
			else if(relativePathComponent.equals("..")){
				if(absolutePathComponents.size() > 1){
					absolutePathComponents.remove(absolutePathComponents.size() - 1);
				}
				else{
					throw new ShellException(path + ": You may not ascend from root");
				}
			}

			// otherwise, add the component to the end of absolutePathComponents
			else{
				absolutePathComponents.add(relativePathComponent);
			}
		}

		// join absolutePathComponents with FILE_SEPARATOR and return the result
		return joinPathComponents(absolutePathComponents);
	}

	/**
	 * Extract the final element from a given path
	 *
	 * @param path the path whos final element should be extracted
	 * @return the extracted element or null if unavailable
	*/
	public static String getLast(String path){

		// split the path on FILE_SEPARATOR
		String[] components = path.split(FILE_SEPARATOR_PATTERN);

		// return the final element of the resulting array or null
		if(components.length > 0){
			return components[components.length - 1];
		}
		else{
			return null;
		}
	}

	/**
	 * Join an ArrayList of Strings into a single String
	 * Place a FILE_SEPARATOR between each element
	 *
	 * @param pathComponents the ArraysList to join
	 * @return the joined String
	*/
	private static String joinPathComponents(ArrayList<String> pathComponents){
		StringBuilder path = new StringBuilder();

		// for each element in pathComponents
		// add the element to path
		// add a FILE_SEPARATOR to path
		for(String pathComponent : pathComponents){
			path.append(pathComponent);
			path.append(FILE_SEPARATOR);
		}

		// if the paths length is one (unix ROOT) return it
		if(path.length() == 1) return path.toString();

		// otherwise, truncate the final FILE_SEPARATOR
		else return path.deleteCharAt(path.length() - 1).toString();
	}
}
