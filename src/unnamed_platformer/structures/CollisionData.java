package unnamed_platformer.structures;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.newdawn.slick.geom.Rectangle;

import unnamed_platformer.app.MathHelper;

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

	public Rectangle getCroppedRectangle(Rectangle box) {
		float wRatio = box.getWidth() / originalRectangle.getWidth();
		float hRatio = box.getHeight() / originalRectangle.getHeight();

		float x = (croppedRectangle.getX() * wRatio + box.getX());
		float y = (croppedRectangle.getY() * hRatio + box.getY());
		float w = (croppedRectangle.getWidth() * wRatio);
		float h = (croppedRectangle.getHeight() * hRatio);

		Rectangle croppedRectangle = new Rectangle(x, y, w, h);

		return croppedRectangle;
	}

}
