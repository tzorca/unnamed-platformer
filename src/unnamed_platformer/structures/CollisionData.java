package unnamed_platformer.structures;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.structures.TextureSetup.CollisionShapeOption;

public class CollisionData {
	boolean[][] data;
	Rectangle croppedRectangle, originalRectangle;

	private static final int COLOR_TRANSPARENT = (new Color(0.0f, 0.0f, 0.0f,
			0.0f)).getRGB();

	public CollisionData(BufferedImage img) {
		readData(img);
		originalRectangle = new Rectangle(0, 0, data.length, data[0].length);
		generateCroppedRectangle();

	}

	private void readData(BufferedImage img) {
		data = new boolean[img.getWidth()][];

		for (int x = 0; x < img.getWidth(); x++) {
			data[x] = new boolean[img.getHeight()];

			for (int y = 0; y < img.getHeight(); y++) {
				data[x][y] = img.getRGB(x, y) != COLOR_TRANSPARENT;
			}
		}
	}

	private void generateCroppedRectangle() {
		int leftEdge = 0, rightEdge = data.length - 1;
		int topEdge = 0, bottomEdge = data[0].length - 1;
		boolean foundEdge;

		foundEdge = false;
		for (int x = leftEdge; x <= rightEdge && !foundEdge; x++) {
			for (int y = topEdge; y <= bottomEdge; y++) {
				if (data[x][y]) {
					leftEdge = x;
					foundEdge = true;
					break;
				}
			}
		}

		foundEdge = false;
		for (int x = rightEdge; x >= leftEdge && !foundEdge; x--) {
			for (int y = topEdge; y <= bottomEdge; y++) {
				if (data[x][y]) {
					rightEdge = x;
					foundEdge = true;
					break;
				}
			}
		}

		foundEdge = false;
		for (int y = topEdge; y <= bottomEdge && !foundEdge; y++) {
			for (int x = leftEdge; x <= rightEdge; x++) {
				if (data[x][y]) {
					topEdge = y;
					foundEdge = true;
					break;
				}
			}
		}

		foundEdge = false;
		for (int y = bottomEdge; y <= topEdge && !foundEdge; y++) {
			for (int x = leftEdge; x <= rightEdge; x++) {
				if (data[x][y]) {
					bottomEdge = y;
					foundEdge = true;
					break;
				}
			}
		}
		croppedRectangle = new Rectangle(leftEdge, topEdge, rightEdge
				- leftEdge + 1, bottomEdge - topEdge + 1);
	}

	private Rectangle getCroppedRectangle(Rectangle entityBox) {
		float wRatio = entityBox.getWidth() / originalRectangle.getWidth();
		float hRatio = entityBox.getHeight() / originalRectangle.getHeight();

		float x = (croppedRectangle.getX() * wRatio + entityBox.getX());
		float y = (croppedRectangle.getY() * hRatio + entityBox.getY());
		float w = (croppedRectangle.getWidth() * wRatio);
		float h = (croppedRectangle.getHeight() * hRatio);

		return new Rectangle(x, y, w, h);
	}

	public Shape getCollisionShape(Rectangle entityBox,
			CollisionShapeOption collisionShape) {

		Rectangle croppedRectangle = getCroppedRectangle(entityBox);

		switch (collisionShape) {
		case circle:
			float radius = MathHelper.getRectInnerRadius(
					croppedRectangle.getWidth(), croppedRectangle.getHeight());
			float centerX = croppedRectangle.getCenterX();
			float centerY = croppedRectangle.getCenterY();
			return new Circle(centerX, centerY, radius);
		case none:
			return new Rectangle(0, 0, 0, 0);
		case polygon:
//			System.err
//					.println("Error: Polygon collision shape not yet implemented.");
			return croppedRectangle;
		case rectangle:
			return croppedRectangle;
		default:
			System.err.println("Error: Collision shape should not be null.");
			return croppedRectangle;
		}
	}

}
