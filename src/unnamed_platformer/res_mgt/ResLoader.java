package unnamed_platformer.res_mgt;

import java.io.File;
import java.util.HashMap;

import unnamed_platformer.globals.Ref;

public abstract class ResLoader
{
	private HashMap<String, Object> cache = new HashMap<String, Object>();

	private String dir, ext;

	private static final String FALLBACK_RESOURCE = "default";

	protected ResLoader(String dir, String ext) {
		this.dir = dir;
		this.ext = ext;
	}

	public String getFilename(String name) {
		return Ref.RESOURCE_DIR + dir + File.separator + name + ext;
	}

	public String getDir() {
		return Ref.RESOURCE_DIR + dir;
	}

	public Object get(String name) {
		if (cache.containsKey(name)) {
			return cache.get(name);
		}
		Object res = null;
		try {
			res = load(name);
		} catch (Exception e) {
			System.out.println("Resource '" + name
					+ "' not found. Defaulting to fallback.");
			try {
				res = load(FALLBACK_RESOURCE);
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

	protected abstract Object load(String name) throws Exception;

	public boolean contentExists(String name) {
		Object data = null;
		try {
			data = get(name);
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