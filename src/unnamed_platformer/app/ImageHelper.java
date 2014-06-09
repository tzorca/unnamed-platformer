package unnamed_platformer.app;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import unnamed_platformer.game.parameters.ContentRef.ContentType;

public class ImageHelper {
	public static BufferedImage loadImage(InputStream input) throws IOException {
		return ImageIO.read(input);
	}

	public static BufferedImage blurImage(BufferedImage originalImage,
			int repetitions) {
		BufferedImage modImage = cloneBufferedImage(originalImage);

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

	static BufferedImage cloneBufferedImage(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public static BufferedImage resizeImageWithinCanvas(BufferedImage img,
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

	public static void saveScreenshot() {
		String newFilename = FileHelper.determineScreenshotFilename();
		if (newFilename == null) {
			return;
		}

		BufferedImage image = ViewManager.getScreenshot();

		try {
			ImageIO.write(image, "PNG", new File(newFilename));
		} catch (IOException e) {
			System.out.println("Screenshot failed: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static ImageIcon getImageIconContent(String name) {
		return new ImageIcon((BufferedImage) ContentManager.get(
				ContentType.image, name));
	}

	public static ImageIcon getImageIconContentScaleDown(String name, int size) {
		BufferedImage img = (BufferedImage) ContentManager.get(
				ContentType.image, name);

		if (img.getWidth() < size) {
			return new ImageIcon(img);
		}

		float ratio = img.getWidth() / img.getHeight();

		return new ImageIcon(img.getScaledInstance(size, (int) (size / ratio),
				java.awt.Image.SCALE_SMOOTH));
	}
}
