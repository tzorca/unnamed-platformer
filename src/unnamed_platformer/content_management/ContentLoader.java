package unnamed_platformer.content_management;

import java.io.File;
import java.util.HashMap;

public abstract class ContentLoader
{
	private HashMap<String, Object> cache = new HashMap<String, Object>();

	private String defaultExtension;

	private static final String FALLBACK_RESOURCE = "default";

	protected ContentLoader(String defaultExtension) {
		this.defaultExtension = defaultExtension;
	}

	public String getFilename(String directory, String name) {
		return getFilename(directory, name, defaultExtension);
	}

	public String getFilename(String directory, String name, String ext) {
		return directory + File.separator + name + ext;
	}

	public Object get(String directory, String name) {
		return get(directory, name, defaultExtension);
	}

	public Object get(String directory, String name, String ext) {
		if (cache.containsKey(name)) {
			return cache.get(name);
		}
		Object res = null;
		try {
			res = load(directory, name, ext);
		} catch (Exception e) {
			System.out.println("Resource '" + name
					+ "' not found. Defaulting to fallback.");
			try {
				res = load(directory, FALLBACK_RESOURCE, ext);
			} catch (Exception e1) {
				System.err.println("Could not load fallback resource.");
				e1.printStackTrace();
			}
		}

		if (res == null) {
			// Can't load yet. Will try again later. (But don't cache)
			return null;
		}

		cache(name, res);
		return res;
	}

	protected void cache(String name, Object res) {
		cache.put(name, res);
	}

	protected Object load(String directory, String name) throws Exception {
		return load(directory, name, defaultExtension);
	}

	protected abstract Object load(String directory, String name, String ext)
			throws Exception;

	public boolean contentExists(String directory, String name) {
		return contentExists(directory, name, defaultExtension);
	}

	public boolean contentExists(String directory, String name, String ext) {
		Object data = null;
		try {
			data = get(directory, name);
		} catch (Exception e) {
			return false;
		}
		return data != null;
	}

	public String getDefaultExtension() {
		return defaultExtension;
	}

	public String getMetaData(String type, String name) {
		return "";
	}

}