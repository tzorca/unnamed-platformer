package unnamed_platformer.game.logic;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class StringHelper {
	public static boolean isValidFilename(String filename) {
		try {

			Paths.get(filename);
		} catch (InvalidPathException e) {
			return false;
		}
		return true;
	}
}
