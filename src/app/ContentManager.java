

package app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.structures.AudioType;
import model.structures.AudioWrapper;
import model.structures.ContentDetails;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Game;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import app.gui.GUIManager;
import de.lessvoid.nifty.render.NiftyImage;

public class ContentManager {

	public enum ContentType {
		game, texture, audioSample, audioLoop, niftyImage
	}

	private static final Map<ContentType, ContentDetails> resDetails = new HashMap<ContentType, ContentDetails>();
	static {
		resDetails.put(ContentType.game, new ContentDetails("game/", ".game",
				Game.class, false));
		resDetails.put(ContentType.texture, new ContentDetails("img/", ".png",
				Texture.class, true));
		resDetails.put(ContentType.niftyImage, new ContentDetails("img/",
				".png", NiftyImage.class, true));
		resDetails.put(ContentType.audioSample, new ContentDetails("snd/",
				".ogg", AudioWrapper.class, true));
		resDetails.put(ContentType.audioLoop, new ContentDetails("snd/",
				".ogg", AudioWrapper.class, true));
	}

	public static String getFilename(ContentType contentType, String name) {
		return resDetails.get(contentType).getFilename(name);
	}

	public static String[] list(ContentType contentType,
			boolean excludeExtensions) {
		return resDetails.get(contentType).listFilenames(excludeExtensions);
	}

	private static HashMap<ContentType, HashMap<String, Object>> resourceCache = new HashMap<ContentType, HashMap<String, Object>>();

	public static void clearAll() {
		resourceCache.clear();
	}

	public static Object get(ContentType contentType, String contentName) {
		HashMap<String, Object> specificResourceMap = resourceCache
				.get(contentType);

		if (!specificResourceMap.containsKey(contentName)) {

			// cache content if not already in cache
			Object newResource = loadResource(contentType, contentName);

			specificResourceMap.put(contentName, newResource);
			return newResource;
		}
		return specificResourceMap.get(contentName);
	}

	private static Object loadResource(ContentType contentType,
			String contentName) {
		ContentDetails contentDetails = resDetails.get(contentType);

		if (!contentDetails.cacheable) {
			return null;
		}

		String filename = contentDetails.getFilename(contentName);
		Object newResource = null;
		try {
			switch (contentType) {
			case game:
				break;
			case texture:
				newResource = TextureLoader.getTexture("PNG",
						ResourceLoader.getResourceAsStream(filename), false,
						GL11.GL_LINEAR);
				break;
			case niftyImage:
				newResource = GUIManager.getImage(filename);
				break;
			case audioSample:
				newResource = loadAudio(AudioType.SAMPLE, contentName);
				break;
			case audioLoop:
				newResource = loadAudio(AudioType.LOOP, contentName);
				break;
			default:
				break;

			}
		} catch (Exception e) {
			App.log(e.toString());
			return null;
		}

		return newResource;
	}

	private static AudioWrapper loadAudio(AudioType audioType, String filename) {
		// extract file extension
		String mode = filename.substring(filename.lastIndexOf('.') + 1)
				.toUpperCase();

		Audio tmp = null;
		try {
			if (audioType == AudioType.SAMPLE) {
				tmp = AudioLoader.getAudio(mode,
						ResourceLoader.getResourceAsStream(filename));
			} else {
				tmp = AudioLoader.getStreamingAudio(mode,
						ResourceLoader.getResource(filename));
			}
		} catch (IOException e) {
			App.log(e.getMessage());
			return null;
		}
		return new AudioWrapper(audioType, tmp);
	}

	public static String getExtension(ContentType contentType) {
		return resDetails.get(contentType).ext;
	}

	public static void init() {
		for (ContentType type : ContentType.values()) {
			resourceCache.put(type, new HashMap<String, Object>());
		}
	}

}
