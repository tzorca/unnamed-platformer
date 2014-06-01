package unnamed_platformer.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import unnamed_platformer.game.parameters.Ref;
import unnamed_platformer.game.parameters.RegexRef;

public class FileHelper {

	public static class RenameFailedException extends Exception {
		private static final long serialVersionUID = 2628470090472936861L;

	}

	public static Collection<String> listFilenames(String dirName,
			boolean excludeExtensions) {
		return listFileNames(new File(dirName), excludeExtensions);
	}

	public static Collection<String> listFileNames(File dir,
			boolean excludeExtensions) {
		List<String> filenames = new ArrayList<String>();

		if (!dir.canRead()) {
			System.out.println("Warning: cannot read from directory "
					+ dir.getAbsolutePath());
			return filenames;
		}

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				filenames.addAll(listFileNames(files[i], excludeExtensions));
			} else {
				if (excludeExtensions) {
					filenames.add(FilenameUtils.removeExtension(files[i]
							.getName()));
				} else {
					filenames.add(files[i].getName());
				}
			}
		}

		return filenames;
	}

	public static boolean isValidFilename(String filename) {
		try {
			Paths.get(filename);
		} catch (InvalidPathException e) {
			return false;
		}
		return true;
	}

	public static Map<File, String> getFileNameMap(String dirName) {
		return getFileNameMap(new File(dirName));
	}

	public static Map<File, String> getFileNameMap(File dir) {
		File[] files = dir.listFiles();

		Map<File, String> map = new HashMap<File, String>();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				map.putAll(getFileNameMap(files[i]));
			} else {
				map.put(files[i],
						FilenameUtils.removeExtension(files[i].getName()));
			}
		}

		return map;
	}

	public static boolean copyFileInSameDir(File file, String suffix) {
		String ext = FilenameUtils.getExtension(file.getAbsolutePath());
		String newName = FilenameUtils.removeExtension(file.getAbsolutePath())
				+ suffix + "." + ext;

		File newFile = new File(newName);

		if (newFile.exists()) {
			return false;
		}

		try {
			FileUtils.copyFile(file, newFile);
			return true;
		} catch (IOException e) {
			System.out.println(e);
			return false;
		}
	}

	public static void renameKeepExtension(File file, String newName)
			throws Exception {
		String dir = file.getParent();
		String ext = FilenameUtils.getExtension(file.getAbsolutePath());
		String newFilename = dir + File.separator + newName + "." + ext;

		if (!FileHelper.isValidFilename(newFilename)) {
			throw new InvalidPathException(newFilename, "Invalid name.");
		}
		if (!file.renameTo(new File(newFilename))) {
			throw new RenameFailedException();
		}

	}

	public static File getLastMatchingFileInDir(String dirFilename,
			Pattern pattern) {

		File dirFile = new File(dirFilename);
		List<String> filenames = Arrays.asList(dirFile.list());
		Collections.sort(filenames);
		Collections.reverse(filenames);

		// Enumerate through files in reverse alphabetical order
		for (String filename : filenames) {
			if (pattern.matcher(filename).matches()) {
				return new File(filename);
			}
		}

		// Didn't find a match
		return null;
	}

	public static boolean mkDir(String absolutePath) {
		try {
			new File(absolutePath).mkdirs();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String determineScreenshotFilename() {
		mkDir(Ref.SCREENSHOT_DIR);

		Integer number = 0;
		File lastFile = getLastMatchingFileInDir(Ref.SCREENSHOT_DIR,
				RegexRef.SCREENSHOT_NAME);
		if (lastFile != null) {
			String lastFilename = FilenameUtils.removeExtension(lastFile
					.getName());
			try {
				String numericSuffix = RegexRef.findMatch(lastFilename,
						RegexRef.NUMERIC_SUFFIX);
				number = new Integer(numericSuffix) + 1;
			} catch (Exception e) {
				System.out.println("Screenshot failed: " + e.getMessage());
				return null;
			}
		}
		return Ref.SCREENSHOT_DIR + "scr" + String.format("%04d", number)
				+ ".png";

	}
}
