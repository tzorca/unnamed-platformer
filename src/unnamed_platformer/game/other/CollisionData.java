package unnamed_platformer.game.other;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.other.TextureSetup.CollisionShapeOption;

public class CollisionData
{
	private boolean[][] opacityGrid;
	private Rectangle autoCropRect;
	private final int canvasWidth;
	private final int canvasHeight;

	private static final int COLOR_TRANSPARENT = (new Color(0.0f, 0.0f, 0.0f, 0.0f)).getRGB();

	public CollisionData(final BufferedImage img) {
		readData(img);
		canvasWidth = img.getWidth();
		canvasHeight = img.getHeight();
		autoCrop();
	}

	private void readData(BufferedImage img) {
		opacityGrid = new boolean[img.getWidth()][];

		for (int x = 0; x < img.getWidth(); x++) {
			opacityGrid[x] = new boolean[img.getHeight()];

			for (int y = 0; y < img.getHeight(); y++) {
				opacityGrid[x][y] = img.getRGB(x, y) != COLOR_TRANSPARENT;
			}
		}
	}

	private void autoCrop() {
		int leftEdge = 0, rightEdge = opacityGrid.length - 1;
		int topEdge = 0, bottomEdge = opacityGrid[0].length - 1;
		boolean foundEdge;

		foundEdge = false;
		for (int x = leftEdge; x <= rightEdge && !foundEdge; x++) {
			for (int y = topEdge; y <= bottomEdge; y++) {
				if (opacityGrid[x][y]) {
					leftEdge = x;
					foundEdge = true;
					break;
				}
			}
		}

		foundEdge = false;
		for (int x = rightEdge; x >= leftEdge && !foundEdge; x--) {
			for (int y = topEdge; y <= bottomEdge; y++) {
				if (opacityGrid[x][y]) {
					rightEdge = x;
					foundEdge = true;
					break;
				}
			}
		}

		foundEdge = false;
		for (int y = topEdge; y <= bottomEdge && !foundEdge; y++) {
			for (int x = leftEdge; x <= rightEdge; x++) {
				if (opacityGrid[x][y]) {
					topEdge = y;
					foundEdge = true;
					break;
				}
			}
		}

		foundEdge = false;
		for (int y = bottomEdge; y <= topEdge && !foundEdge; y++) {
			for (int x = leftEdge; x <= rightEdge; x++) {
				if (opacityGrid[x][y]) {
					bottomEdge = y;
					foundEdge = true;
					break;
				}
			}
		}
		autoCropRect = new Rectangle(leftEdge, topEdge, rightEdge - leftEdge + 1, bottomEdge - topEdge + 1);
	}

	private Rectangle getScaledRect(final Rectangle entityBox) {
		final float wRatio = entityBox.getWidth() / canvasWidth;
		final float hRatio = entityBox.getHeight() / canvasHeight;

		final float xPos = autoCropRect.getX() * wRatio + entityBox.getX();
		final float yPos = autoCropRect.getY() * hRatio + entityBox.getY();
		final float width = autoCropRect.getWidth() * wRatio;
		final float height = autoCropRect.getHeight() * hRatio;

		return new Rectangle(xPos, yPos, width, height);
	}

	public Shape getScaledShape(final Rectangle entityBox, final CollisionShapeOption collisionShape) {

		final Rectangle scaledRect = getScaledRect(entityBox);

		switch (collisionShape) {
		case circle:
			final float radius = MathHelper.getRectInnerRadius(scaledRect.getWidth(), scaledRect.getHeight());
			final float centerX = scaledRect.getCenterX();
			final float centerY = scaledRect.getCenterY();
			return new Circle(centerX, centerY, radius);
		case none:
			return new Rectangle(0, 0, 0, 0);
		case rectangle:
			return scaledRect;
		case polygon:
			// System.err
			// .println("Error: Polygon collision shape not yet implemented.");
			return scaledRect;
		default:
			System.err.println("Error: Collision shape cannot be null.");
			return scaledRect;
		}
	}

}
