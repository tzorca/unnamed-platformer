package unnamed_platformer.app;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public final class ImageHelper
{
	public static BufferedImage load(InputStream input) throws IOException {
		return ImageIO.read(input);
	}

	public static BufferedImage blur(BufferedImage originalImage,
			int repetitions) {
		BufferedImage modImage = clone(originalImage);

		for (int i = 0; i < repetitions; i++) {
			BufferedImage filteredImage = new BufferedImage(
					modImage.getWidth(null), modImage.getHeight(null),
					BufferedImage.TYPE_BYTE_GRAY);

			Dimension size = new Dimension();
			size.width = modImage.getWidth(null);
			size.height = modImage.getHeight(null);

			Graphics g = filteredImage.getGraphics();
			g.drawImage(modImage, 455, 255, null);

			float[] blurKernel = { 1 / 9f, 1 / 9f, 1 / 9f, 1 / 9f, 1 / 9f,
					1 / 9f, 1 / 9f, 1 / 9f, 1 / 9f };

			BufferedImageOp blur = new ConvolveOp(new Kernel(3, 3, blurKernel));
			modImage = blur.filter(modImage,
					new BufferedImage(modImage.getWidth(),
							modImage.getHeight(), modImage.getType()));

			g.dispose();
		}

		return modImage;
	}

	static BufferedImage clone(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public static BufferedImage percentScaleWithinCanvas(BufferedImage img,
			double percent) {
		int scaleX = (int) (img.getWidth() * percent);
		int scaleY = (int) (img.getHeight() * percent);
		int startX = (int) (img.getWidth() * (1 - percent) / 2);
		int startY = (int) (img.getHeight() * (1 - percent) / 2);

		Image scaled = img
				.getScaledInstance(scaleX, scaleY, Image.SCALE_SMOOTH);
		BufferedImage buffered = new BufferedImage(img.getWidth(),
				img.getHeight(), img.getType());

		buffered.getGraphics().drawImage(scaled, startX, startY, null);
		return buffered;
	}

	public static BufferedImage resizeCanvas(BufferedImage img, Dimension size) {
		BufferedImage surface = new BufferedImage((int) size.getWidth(),
				(int) size.getHeight(), img.getType());

		int startX = (int) (size.getWidth() / 2 - img.getWidth() / 2);
		int startY = (int) (size.getHeight() / 2 - img.getHeight() / 2);

		surface.getGraphics().drawImage(img, startX, startY, null);
		return surface;
	}

	public static ImageIcon toImageIcon(Image image) {
		return new ImageIcon(image);
	}

	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		BufferedImage surface = new BufferedImage(image.getWidth(null),
				image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D graphics2D = surface.createGraphics();
		graphics2D.drawImage(image, 0, 0, null);
		graphics2D.dispose();

		return surface;
	}

	public static BufferedImage scaleWidth(BufferedImage img, int width,
			Integer hints) {
		float ratio = (float) img.getWidth() / img.getHeight();
		int newHeight = (int) (width / ratio);

		if (img.getWidth() < width) {
			return resizeCanvas(img, new Dimension(width, newHeight));
		}

		return toBufferedImage(img.getScaledInstance(width, newHeight, hints));
	}
}
