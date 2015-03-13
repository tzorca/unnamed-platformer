package unnamed_platformer.content_management;

import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public final class ContentManager
{
	public static String getFilename(Class<?> clazz, String directory,
			String name) {
		return selectContentLoader(clazz).getFilename(directory, name);
	}

	public static String getExtension(Class<?> clazz) {
		return selectContentLoader(clazz).getDefaultExtension();
	}

	private static ConcurrentHashMap<Class<?>, ContentLoader> contentLoaders = new ConcurrentHashMap<Class<?>, ContentLoader>();

	public static <T> void registerContentLoader(Class<T> clazz,
			ContentLoader resLoader) {
		contentLoaders.put(clazz, resLoader);
	}

	private static <T> ContentLoader selectContentLoader(Class<T> clazz) {
		return contentLoaders.get(clazz);
	}

	public static <T> T get(Class<T> clazz, String directory, String contentName) {
		return (T) selectContentLoader(clazz).get(directory, contentName);
	}

	public static <T> T get(Class<T> clazz, String directory,
			String contentName, String ext) {
		return (T) selectContentLoader(clazz).get(directory, contentName, ext);
	}

	public static boolean contentExists(Class<?> clazz, String directory,
			String contentName) {
		return selectContentLoader(clazz).contentExists(directory, contentName);
	}

	public static String humanizeName(String internalName) {
		StringBuilder sb = new StringBuilder("");
		// capitalize first letter
		sb.append(Character.toUpperCase(internalName.charAt(0)));

		// replace camelCase with spaces
		int state = 0;
		for (int i = 1; i < internalName.length(); i++) {
			char c = internalName.charAt(i);
			if (state == 0 && Character.isUpperCase(c)) {
				sb.append(' ');
			}

			sb.append(c);
			state = c == ' ' ? 1 : 0;
		}

		return sb.toString();
	}

}
