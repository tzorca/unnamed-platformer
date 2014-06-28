package unnamed_platformer.res_mgt;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.game.World;
import unnamed_platformer.structures.BinaryPixelGrid;

@SuppressWarnings("unchecked")
public class ResManager {

	public static String getFilename(Class<?> clazz, String name) {
		return getResloader(clazz).getFilename(name);
	}

	public static Collection<String> list(Class<?> clazz, boolean excludeExtensions) {
		return FileHelper.listFilenames(getResloader(clazz).getDir(), excludeExtensions);
	}

	public static String getExtension(Class<?> clazz) {
		return getResloader(clazz).getExt();
	}

	private static HashMap<Class<?>, ResLoader<?>> resLoaders = new HashMap<Class<?>, ResLoader<?>>();

	private static <T> void addResLoader(Class<T> clazz, ResLoader<T> resLoader) {
		resLoaders.put(clazz, resLoader);
	}

	private static <T> ResLoader<T> getResloader(Class<T> clazz) {
		return (ResLoader<T>) resLoaders.get(clazz);
	}

	public static <T> T get(Class<T> clazz, String contentName) {
		return getResloader(clazz).get(contentName);
	}

	public static boolean contentExists(Class<?> clazz, String contentName) {
		return getResloader(clazz).contentExists(contentName);
	}

	public static void init() {
		addResLoader(Texture.class, new TextureResLoader());
		addResLoader(BufferedImage.class, new ImageResLoader());
		addResLoader(BinaryPixelGrid.class, new BinaryPixelGridResLoader());
		addResLoader(World.class, new GameResDummyLoader());
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