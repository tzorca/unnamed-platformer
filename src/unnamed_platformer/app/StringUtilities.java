package unnamed_platformer.app;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.Lists;

public class StringUtilities
{

	public static boolean equalsAnyIgnoreCase(String str,
			Collection<String> options) {
		if (str == null) {
			return false;
		}
		
		for (String option : options) {
			if (str.equalsIgnoreCase(option)) {
				return true;
			}
		}
		return false;
	}

	private static final List<String> TRUE_OPTIONS = Lists.newArrayList("true",
			"on", "yes");
	private static final List<String> FALSE_OPTIONS = Lists.newArrayList(
			"false", "off", "no");

	public static Object inferStringAsType(String str) {
		if (StringUtilities.equalsAnyIgnoreCase(str, TRUE_OPTIONS)) {
			return true;
		}

		if (StringUtilities.equalsAnyIgnoreCase(str, FALSE_OPTIONS)) {
			return false;
		}

		if (NumberUtils.isNumber(str)) {

			Number numberValue;
			try {
				numberValue = NumberFormat.getInstance().parse(str);
				return numberValue;
			} catch (ParseException e) {
				System.err.println("Error parsing " + str + " as a number.");
				e.printStackTrace();
			}
		}

		// default to plain string if nothing else worked
		return str;
	}
}
