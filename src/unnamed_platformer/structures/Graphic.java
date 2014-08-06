package unnamed_platformer.structures;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.res_mgt.ResManager;

public class Graphic implements Serializable {
	private static final long serialVersionUID = 3743979572361086098L;

	public Color color;
	public String textureName;

	public Graphic(Color color) {
		this.color = color;
	}

	public Graphic(String textureName, Color color) {
		this.textureName = textureName;
		this.color = color;
	}

	public Graphic(String textureName) {
		this.textureName = textureName;
	}

	public Texture getTexture() {
		if (textureName == null) {
			System.out.println("Warning: Unable to return texture from a null textureName");
			return null;
		}
		return ResManager.get(Texture.class, textureName);
	}

	public CollisionData getCollisionData() {
		return ResManager.get(CollisionData.class, textureName);
	}

	public Rectangle getCroppedRectangle(Rectangle box) {
		return getCollisionData().getCroppedRectangle(box);
	}

	public BufferedImage getTextureImage() {
		return ResManager.get(BufferedImage.class, textureName);
	}

	public boolean hasTextureName() {
		return textureName != null;
	}
}
