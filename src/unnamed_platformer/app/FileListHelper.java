package unnamed_platformer.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileListHelper {

	public static Collection<String> listFilenames(String dirName,
			boolean excludeExtensions) {
		return listFileNames(new File(dirName), excludeExtensions);
	}

	public static Collection<String> listFileNames(File dir,
			boolean excludeExtensions) {
		List<String> filenames = new ArrayList<String>();
		
		if (!dir.canRead()) {
			System.out.println("Warning: cannot read from directory " + dir.getAbsolutePath());
			return filenames;
		}

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				filenames.addAll(listFileNames(files[i], excludeExtensions));
			} else {
				if (excludeExtensions) {
					filenames.add(removeExtension(files[i].getName()));
				} else {
					filenames.add(files[i].getName());
				}
			}
		}

		return filenames;
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
				map.put(files[i], removeExtension(files[i].getName()));
			}
		}

		return map;
	}

	public static String removeExtension(String filename) {
		File f = new File(filename);

		// if it's a directory, don't remove the extension
		if (f.isDirectory()) {
			return filename;
		}

		String name = f.getName();

		final int lastPeriodPos = name.lastIndexOf('.');
		if (lastPeriodPos <= 0) {
			// No period after first character - return name as it was passed in
			return filename;
		} else {
			// Remove the last period and everything after it
			return name.substring(0, lastPeriodPos);
		}
	}

}
