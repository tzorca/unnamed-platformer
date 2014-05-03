package app;

import java.io.IOException;
import java.util.HashMap;

import model.parameters.AudioRef.AudioType;
import model.parameters.ContentRef;
import model.parameters.ContentRef.ContentType;
import model.structures.AudioWrapper;
import model.structures.ContentDetails;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import app.gui.GUIManager;

public class ContentManager {

	public static String getFilename(ContentType contentType, String name) {
		return ContentRef.details.get(contentType).getFilename(name);
	}

	public static String[] list(ContentType contentType,
			boolean excludeExtensions) {
		return ContentRef.details.get(contentType).listFilenames(
				excludeExtensions);
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
		ContentDetails contentDetails =  ContentRef.details.get(contentType);

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
		return  ContentRef.details.get(contentType).ext;
	}

	public static void init() {
		for (ContentType type : ContentType.values()) {
			resourceCache.put(type, new HashMap<String, Object>());
		}
	}

}
