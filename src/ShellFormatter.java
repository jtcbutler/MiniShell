import org.fusesource.jansi.Ansi;

/**
 * Non-instantiable class that provides utilities for printing formatted text
 *
 * @author	Jackson Butler
 * @date	Feb 26, 2025	
 */
public class ShellFormatter{

	/**
	 * Private constructor
	 * This is a non-instantiable class
	*/
	private ShellFormatter(){}

	/**
	 * Color a String of text
	 *
	 * @param text the text to color
	 * @param r the red color value
	 * @param g the green color value
	 * @param b the blue color value
	 * @return the colored text
	*/
	public static String colorText(String text, int r, int g, int b){
		return Ansi.ansi().fgRgb(r, g, b).render(text).fgDefault().toString();
	}

	/**
	 * Bold a String of text
	 *
	 * @param text the text to make bold
	 * @return the bold text
	*/
	public static String boldText(String text){
		return Ansi.ansi().bold().render(text).boldOff().toString();
	}

	/**
	 * Format an Ansi String to reposition terminal cursor
	 *
	 * @param x the new x position of the cursor
	 * @param y the new y position of the cursor
	 * @return the formatted String
	*/
	public static String setCursorPosition(int x, int y){
		return Ansi.ansi().cursor(x, y).toString();
	}

	/**
	 * Format an Ansi String to move the terminal cursor up
	 *
	 * @param spaces how many lines should the cursor move up
	 * @return the formatted String
	*/
	public static String moveCursorUp(int spaces){
		return Ansi.ansi().cursorUp(spaces).toString();
	}

	/**
	 * Format an Ansi String to move the terminal cursor down
	 *
	 * @param spaces how many lines should the cursor move down
	 * @return the formatted String
	*/
	public static String moveCursorDown(int spaces){
		return Ansi.ansi().cursorDown(spaces).toString();
	}

	/**
	 * Format an Ansi String to move the terminal cursor left
	 *
	 * @param spaces how many characters should the cursor move left
	 * @return the formatted String
	*/
	public static String moveCursorLeft(int spaces){
		return Ansi.ansi().cursorLeft(spaces).toString();
	}

	/**
	 * Format an Ansi String to move the terminal cursor right
	 *
	 * @param spaces how many characters should the cursor move right
	 * @return the formatted String
	*/
	public static String moveCursorRight(int spaces){
		return Ansi.ansi().cursorRight(spaces).toString();
	}

	/**
	 * Format an Ansi String to clear all text from the terminal
	 *
	 * @return the formatted String
	*/
	public static String clearTerminal(){
		return Ansi.ansi().eraseScreen().toString();
	}
}
