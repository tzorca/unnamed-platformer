package unnamed_platformer.content_management;

import java.io.File;
import java.util.HashMap;

public abstract class ContentLoader
{
	private HashMap<String, Object> cache = new HashMap<String, Object>();

	private String ext;

	private static final String FALLBACK_RESOURCE = "default";

	protected ContentLoader(String ext) {
		this.ext = ext;
	}

	public String getFilename(String directory, String name) {
		return directory + File.separator + name + ext;
	}

	public Object get(String directory, String name) {
		if (cache.containsKey(name)) {
			return cache.get(name);
		}
		Object res = null;
		try {
			res = load(directory, name);
		} catch (Exception e) {
			System.out.println("Resource '" + name
					+ "' not found. Defaulting to fallback.");
			try {
				res = load(directory, FALLBACK_RESOURCE);
			} catch (Exception e1) {
				System.err.println("Could not load fallback resource.");
				e1.printStackTrace();
			}
		}

		if (res == null) {
			// Can't load yet. Will try again later. (But don't cache)
			return null;
		}

		cache(directory, name, res);
		return res;
	}

	protected void cache(String directory, String name, Object res) {
		cache.put(name, res);
	}

	protected abstract Object load(String directory, String name)
			throws Exception;

	public boolean contentExists(String directory, String name) {
		Object data = null;
		try {
			data = get(directory, name);
		} catch (Exception e) {
			return false;
		}
		return data != null;
	}

	public String getExt() {
		return ext;
	}

	public String getMetaData(String type, String name) {
		return "";
	}
}