import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import java.lang.StringBuilder;

public class ShellPath{
	public static final String root;
	public static final char fileSeparator;
	private static final String fileSeparatorPattern;

	static {
		fileSeparator = System.getProperty("file.separator").charAt(0);

		if(fileSeparator == '/'){
			fileSeparatorPattern = "/+";
			root = "/"; 
		}
		else{
			fileSeparatorPattern = "\\\\+";
			root = "C:\\";
		}
	}

	private ShellPath(){}

	public static String buildPath(String path) throws ShellException{
		String absolutePath;
		String relativePath;

		if(path.startsWith(root)){
			absolutePath = "";
			relativePath = path;
		}
		else if(path.startsWith("~")){
			absolutePath = "";
			relativePath = path.replace("~", System.getProperty("user.home"));
		}
		else{
			absolutePath = System.getProperty("user.dir");
			relativePath = path;
		}

		ArrayList<String> absolutePathComponents = new ArrayList<String>(Arrays.asList(absolutePath.split(fileSeparatorPattern)));
		ArrayList<String> relativePathComponents = new ArrayList<String>(Arrays.asList(relativePath.split(fileSeparatorPattern)));

		if(absolutePathComponents.size() == 0) absolutePathComponents.add("");

		for(String relativePathComponent : relativePathComponents){
			if(relativePathComponent.equals("") || relativePathComponent.equals(".")){
				continue;
			}
			else if(relativePathComponent.equals("..")){
				if(absolutePathComponents.size() > 1){
					absolutePathComponents.remove(absolutePathComponents.size() - 1);
				}
				else{
					throw new ShellException("invalid path \"" + path + "\" you may not ascend from root");
				}
			}
			else{
				absolutePathComponents.add(relativePathComponent);
			}
		}

		return joinPathComponents(absolutePathComponents);
	}

	private static String joinPathComponents(ArrayList<String> pathComponents){
		StringBuilder path = new StringBuilder();

		for(String pathComponent : pathComponents){
			path.append(pathComponent);
			path.append(fileSeparator);
		}

		if(path.length() == 1) return path.toString();
		else return path.deleteCharAt(path.length() - 1).toString();
	}
}
