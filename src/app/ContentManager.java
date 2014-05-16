package app;

import game.parameters.AudioRef.AudioType;
import game.parameters.ContentRef;
import game.parameters.ContentRef.ContentType;
import game.structures.AudioWrapper;
import game.structures.ContentDetails;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

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

	public static Collection<String> list(ContentType contentType,
			boolean excludeExtensions) {
		return FileListHelper.listFilenames(ContentRef.details.get(contentType)
				.getDir(), excludeExtensions);
	}

	public static Map<File, String> getFileNameMap(ContentType contentType) {
		return FileListHelper.getFileNameMap(ContentRef.details
				.get(contentType).getDir());
	}

	private static EnumMap<ContentType, HashMap<String, Object>> resourceCache = new EnumMap<ContentType, HashMap<String, Object>>(ContentType.class);

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

	public static Object customCache(ContentType contentType,
			String contentName, File file) {
		HashMap<String, Object> specificResourceMap = resourceCache
				.get(contentType);

		Object newResource = loadResource(contentType, contentName, file);

		specificResourceMap.put(contentName, newResource);
		return newResource;
	}

	private static Object loadResource(ContentType contentType,
			String contentName) {
		ContentDetails contentDetails = ContentRef.details.get(contentType);

		if (!contentDetails.cacheable) {
			return null;
		}

		String filename = contentDetails.getFilename(contentName);
		return loadResource(contentType, contentName, new File(filename));

	}

	private static Object loadResource(ContentType contentType,
			String contentName, File file) {
		Object newResource = null;
		try {
			switch (contentType) {
			case game:
				break;
			case texture:
				newResource = TextureLoader.getTexture("PNG", ResourceLoader
						.getResourceAsStream(file.getAbsolutePath()), false,
						GL11.GL_LINEAR);
				break;
			case niftyImage:
				newResource = GUIManager.getImage(file.getAbsolutePath());
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
		return ContentRef.details.get(contentType).ext;
	}

	@SuppressWarnings("unused")
	private static void clearAll() {
		resourceCache.clear();
	}

	public static void init() {
		for (ContentType type : ContentType.values()) {
			resourceCache.put(type, new HashMap<String, Object>());
		}
	}

}
