package model.parameters;

import java.util.HashMap;
import java.util.Map;

import model.structures.AudioWrapper;
import model.structures.ContentDetails;

import org.newdawn.slick.Game;
import org.newdawn.slick.opengl.Texture;

import de.lessvoid.nifty.render.NiftyImage;

public class ContentRef {
	public enum ContentType {
		game, texture, audioSample, audioLoop, niftyImage
	}

	public static final Map<ContentType, ContentDetails> details = new HashMap<ContentType, ContentDetails>();
	static {
		details.put(ContentType.game, new ContentDetails("game/",
				".game", Game.class, false));
		details.put(ContentType.texture, new ContentDetails("img/",
				".png", Texture.class, true));
		details.put(ContentType.niftyImage, new ContentDetails("img/",
				".png", NiftyImage.class, true));
		details.put(ContentType.audioSample, new ContentDetails("snd/",
				".ogg", AudioWrapper.class, true));
		details.put(ContentType.audioLoop, new ContentDetails("snd/",
				".ogg", AudioWrapper.class, true));
	}
}
