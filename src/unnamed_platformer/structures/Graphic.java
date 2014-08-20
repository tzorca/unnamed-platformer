package unnamed_platformer.structures;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.globals.TextureRef;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.structures.TextureSetup.CollisionShapeOption;

public class Graphic implements Serializable {
	private static final long serialVersionUID = 3743979572361086098L;

	public Color color;
	public String textureName;
	public TextureSetup cachedTextureSetup;

	public Graphic(Color color) {
		this.color = color;
	}

	public Graphic(String textureName, Color color) {
		setTexture(textureName);

		this.color = color;
	}

	public void setTexture(String textureName) {
		this.textureName = textureName;
		this.cachedTextureSetup = TextureRef.getSetup(textureName);
	}

	public Graphic(String textureName) {
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

	public CollisionData getCollisionData() {
		return ResManager.get(CollisionData.class, textureName);
	}

	public BufferedImage getTextureImage() {
		return ResManager.get(BufferedImage.class, textureName);
	}

	public boolean hasTextureName() {
		return textureName != null;
	}

	public Rectangle getCollisionRectangle(Rectangle entityBox) {
		return (Rectangle) getCollisionData().getCollisionShape(entityBox,
				CollisionShapeOption.rectangle);
	}

	public Shape getCollisionShape(Rectangle entityBox) {
		return getCollisionData().getCollisionShape(entityBox,
				cachedTextureSetup.getCollisionShape());
	}
}
