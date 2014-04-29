package model.structures;

import java.io.File;

import model.Ref;

public class ContentDetails {
	public String dir = "", ext;
	@SuppressWarnings("rawtypes")
	public Class classType;
	public boolean cacheable = false;

	public ContentDetails(String dir, String ext, @SuppressWarnings("rawtypes") Class classType,
			boolean cacheable) {
		this.dir = dir;
		this.ext = ext;
		this.classType = classType;
		this.cacheable = cacheable;
	}

	public String getFilename(String name) {
		return Ref.baseDir + dir + name + ext;
	}

	public String[] listFilenames(boolean excludeExtensions) {
		File pathFile = new File(Ref.baseDir + dir);

		String[] list = pathFile.list();

		if (excludeExtensions) {
			for (int i = 0; i < list.length; i++) {
				list[i] = removeExtension(list[i]);
			}
		}
		return list;

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