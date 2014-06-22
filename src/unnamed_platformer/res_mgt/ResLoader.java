package unnamed_platformer.res_mgt;

import java.io.File;
import java.util.HashMap;

import unnamed_platformer.globals.Ref;

public abstract class ResLoader<T> {
	private HashMap<String, T> cache = new HashMap<String, T>();

	private String dir, ext;

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

	public T get(String name) {
		if (cache.containsKey(name)) {
			return cache.get(name);
		}
		T res = null;
		try {
			res = load(name);
		} catch (Exception e) {
			System.out.println("Error loading resource " + name + ": " + e.toString());
			return null;
		}
		
		if (res == null) {
			// Can't load yet. Will try again later. (But don't cache)
			return null;
		}

		cache(name, res);
		return res;
	}

	protected void cache(String name, T res) {
		cache.put(name, res);
	}

	protected abstract T load(String name) throws Exception;

	public boolean contentExists(String name) {
		return get(name) != null;
	}

	public String getExt() {
		return ext;
	}
	
	public String getMetaData(String type, String name) {
		return "";
	}
}