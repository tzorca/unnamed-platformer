package game.structures;

import java.io.Serializable;

import game.parameters.ContentRef.ContentType;
import game.parameters.Ref;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

import app.ContentManager;

public class Graphic implements Serializable {
	private static final long serialVersionUID = 3743979572361086098L;

	public FlColor color = Ref.DEFAULT_COLOR;
	private String textureName;
	private transient Texture texture;
	private transient BinaryPixelGrid binaryPixelGrid;

	// private boolean tempHighlight = false;

	public Graphic(FlColor color) {
		this.color = color;
	}

	public Graphic(String textureName, FlColor color) {
		setTexture(textureName);
		this.color = color;
	}

	public Graphic(String textureName) {
		setTexture(textureName);
	}

	public Graphic() {
	}

	public void removeTexture() {
		textureName = null;
		texture = null;
	}

	private void setTexture(String textureName) {
		this.textureName = textureName;

		texture = (Texture) ContentManager
				.get(ContentType.texture, textureName);
		binaryPixelGrid = (BinaryPixelGrid) ContentManager.get(
				ContentType.binaryPixelGrid, textureName);
	}

	public String getTextureName() {
		return textureName;
	}

	public Texture getTexture() {
		restoreTextureIfLost();

		return texture;
	}
	
	private void restoreTextureIfLost() {
		// Textures may have to be nullified during the process of serializing
		// and deserializing...
		if (textureName != null && texture == null) {
			setTexture(textureName);
		}

	}

	public Rectangle getCroppedRectangle(Rectangle box) {
		restoreTextureIfLost();
		
		return binaryPixelGrid.getCroppedRectangle(box);
	}
	// public BinaryPixelGrid getBinaryPixelGrid() {
	// return binaryPixelGrid;
	// }

	// GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
	// GL11.GL_REPEAT);
	// GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
	// GL11.GL_REPEAT);

	// public boolean isTempHighlight() {
	// boolean wasTempHighlight = tempHighlight;
	// tempHighlight = false;
	// return wasTempHighlight;
	// }
	//
	// public void setTempHighlight() {
	// this.tempHighlight = true;
	// }
}
