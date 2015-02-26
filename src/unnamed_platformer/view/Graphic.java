package unnamed_platformer.view;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.content_management.ContentManager;
import unnamed_platformer.game.config.TextureLookup;
import unnamed_platformer.game.config.TextureSetup;
import unnamed_platformer.globals.FileGlobals;

public class Graphic implements Serializable
{
	private static final long serialVersionUID = 3743979572361086098L;

	public Color color;
	private String textureName;
	transient private TextureSetup cachedTextureSetup;

	public Graphic(final Color color) {
		this.color = color;
	}

	public Graphic(final String textureName, final Color color) {
		setTexture(textureName);

		this.color = color;
	}

	public void setTexture(final String textureName) {
		this.textureName = textureName;
		this.cachedTextureSetup = TextureLookup.getSetup(textureName);
	}

	public Graphic(final String textureName) {
		setTexture(textureName);
	}

	public Texture getTexture() {
		if (textureName == null) {
			System.out
					.println("Warning: Unable to return texture from a null textureName");
			return null;
		}
		return ContentManager.get(Texture.class, FileGlobals.IMG_OBJ_DIR,
				textureName);
	}

	public BufferedImage getImage() {
		return ContentManager.get(BufferedImage.class, FileGlobals.IMG_OBJ_DIR,
				textureName);
	}

	public boolean hasTextureName() {
		return textureName != null;
	}

	public TextureSetup getTextureSetup() {
		if (cachedTextureSetup == null) {
			setTexture(textureName);
		}
		return cachedTextureSetup;
	}

	public String getTextureName() {
		return textureName;
	}

}
