package unnamed_platformer.game.parameters;

import java.util.EnumMap;

import org.newdawn.slick.Game;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.game.structures.AudioWrapper;
import unnamed_platformer.game.structures.BinaryPixelGrid;
import unnamed_platformer.game.structures.ContentDetails;
import de.lessvoid.nifty.render.NiftyImage;

public class ContentRef {
	public enum ContentType {
		game, texture, audioSample, audioLoop, niftyImage, binaryPixelGrid
	}

	public static final EnumMap<ContentType, ContentDetails> details = new EnumMap<ContentType, ContentDetails>(
			ContentType.class);
	static {
		details.put(ContentType.game, new ContentDetails("game/", ".game",
				Game.class, false));
		details.put(ContentType.texture, new ContentDetails("img/", ".png",
				Texture.class, true));
		details.put(ContentType.niftyImage, new ContentDetails("img/", ".png",
				NiftyImage.class, true));
		details.put(ContentType.audioSample, new ContentDetails("snd/", ".ogg",
				AudioWrapper.class, true));
		details.put(ContentType.audioLoop, new ContentDetails("snd/", ".ogg",
				AudioWrapper.class, true));
		details.put(ContentType.binaryPixelGrid, new ContentDetails("img/",
				".png", BinaryPixelGrid.class, true));
	}
}
