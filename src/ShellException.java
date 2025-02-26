/**
 * A barebones exception type used throughout the MiniShell to improve clarity
 *
 * @authors Jackson Butler, Jiafeng
 * @since	Feb 25, 2025
 */
public class ShellException extends Exception {

	/**
	 * Create a new ShellException with an explanation of what went wrong
	 *
	 * @param message an explanation of what went wrong
	 * @return ShellException the newly created ShellException
	*/
	public ShellException(String message) {
		super(message);
	}
}
