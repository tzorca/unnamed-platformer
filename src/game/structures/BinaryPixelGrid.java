package game.structures;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class BinaryPixelGrid {
	boolean[][] data;
	Rectangle croppedRectangle, originalRectangle;

	private static final int COLOR_TRANSPARENT = (new Color(0.0f, 0.0f, 0.0f,
			0.0f)).getRGB();

	public BinaryPixelGrid(BufferedImage img) {
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
		double wRatio = box.width / (double)originalRectangle.width;
		double hRatio = box.height / (double)originalRectangle.height;

		double x = croppedRectangle.x * wRatio + box.x;
		double y = croppedRectangle.y * hRatio + box.y;
		double w = croppedRectangle.width * wRatio;
		double h = croppedRectangle.height * hRatio;

		return new Rectangle((int) x, (int) y, (int) w, (int) h);
	}

}
