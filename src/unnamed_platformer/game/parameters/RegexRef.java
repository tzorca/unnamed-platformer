package unnamed_platformer.game.parameters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.NotFoundException;

public class RegexRef {
	public static final Pattern NUMERIC_SUFFIX = Pattern.compile("\\d+$");
	public static final Pattern SCREENSHOT_NAME = Pattern.compile("scr\\d{4}\\.png");

	public static String findMatch(String text, Pattern pattern)
			throws NotFoundException {
		Matcher matcher = pattern.matcher(text);

		if (matcher.find()) {
			return matcher.group();
		} else {
			throw new NotFoundException("No match found for "
					+ pattern.toString() + " in " + text);
		}
	}
}
