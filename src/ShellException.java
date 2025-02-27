/**
 * A barebones exception type used throughout the MiniShell to improve clarity
 * Serial warning is suppressed for this class as it is not intended to be serialized
 *
 * @authors Jackson Butler, Jiafeng Gu
 * @since	Feb 25, 2025
 */
@SuppressWarnings("serial")
public class ShellException extends Exception {

	/**
	 * Create a new ShellException with an explanation of what went wrong
	 *
	 * @param message an explanation of what went wrong
	*/
	public ShellException(String message) {
		super(message);
	}
}
