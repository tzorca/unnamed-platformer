package unnamed_platformer.res_mgt;

import java.util.Collection;
import java.util.HashMap;

import javax.swing.ImageIcon;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.app.FileHelper;
import unnamed_platformer.game.other.CollisionData;
import unnamed_platformer.game.other.World;
import unnamed_platformer.res_mgt.types.BackgroundImage;
import unnamed_platformer.res_mgt.types.BackgroundTexture;
import unnamed_platformer.res_mgt.types.GUI_Image;
import unnamed_platformer.res_mgt.types.GUI_Texture;
import unnamed_platformer.res_mgt.types.ObjectImage;

@SuppressWarnings("unchecked")
public final class ResManager {

	public static String getFilename(Class<?> clazz, String name) {
		return getResloader(clazz).getFilename(name);
	}

	public static Collection<String> list(Class<?> clazz, boolean excludeExtensions) {
		return FileHelper.listFilenames(getResloader(clazz).getDir(), excludeExtensions);
	}

	public static String getExtension(Class<?> clazz) {
		return getResloader(clazz).getExt();
	}

	private static HashMap<Class<?>, ResLoader> resLoaders = new HashMap<Class<?>, ResLoader>();

	private static <T> void addResLoader(Class<T> clazz, ResLoader resLoader) {
		resLoaders.put(clazz, resLoader);
	}

	private static <T> ResLoader getResloader(Class<T> clazz) {
		return resLoaders.get(clazz);
	}

	public static <T> T get(Class<T> clazz, String contentName) {
		return (T) getResloader(clazz).get(contentName);
	}

	public static boolean contentExists(Class<?> clazz, String contentName) {
		return getResloader(clazz).contentExists(contentName);
	}

	public static void init() {
		addResLoader(BackgroundTexture.class, new TextureResLoader("img-bg"));
		addResLoader(BackgroundImage.class, new ImageResLoader("img-bg"));
		addResLoader(Texture.class, new TextureResLoader("img-obj"));
		addResLoader(ObjectImage.class, new ImageResLoader("img-obj"));
		addResLoader(GUI_Image.class, new ImageResLoader("img-gui"));
		addResLoader(GUI_Texture.class, new TextureResLoader("img-gui"));
		
		addResLoader(Audio.class, new SoundLoader());
		addResLoader(CollisionData.class, new CollisionDataResLoader());
		addResLoader(World.class, new WorldResDummyLoader());
		addResLoader(ImageIcon.class, new PreviewImageLoader());
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
