package unnamed_platformer.view;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.game.other.TextureSetup;
import unnamed_platformer.globals.TextureRef;
import unnamed_platformer.res_mgt.ResManager;

public class Graphic implements Serializable
{
	private static final long serialVersionUID = 3743979572361086098L;

	public Color color;
	private String textureName;
	public TextureSetup cachedTextureSetup;

	public Graphic(final Color color) {
		this.color = color;
	}

	public Graphic(final String textureName, final Color color) {
		setTexture(textureName);

		this.color = color;
	}

	public void setTexture(final String textureName) {
		this.textureName = textureName;
		this.cachedTextureSetup = TextureRef.getSetup(textureName);
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
		return ResManager.get(Texture.class, textureName);
	}

	public BufferedImage getTextureImage() {
		return ResManager.get(BufferedImage.class, textureName);
	}

	public boolean hasTextureName() {
		return textureName != null;
	}

	public TextureSetup getTextureSetup() {
		return cachedTextureSetup;
	}

	public String getTextureName() {
		return textureName;
	}
}
