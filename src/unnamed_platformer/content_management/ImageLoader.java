package unnamed_platformer.content_management;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageLoader extends ContentLoader {

	protected ImageLoader() {
		super(".png");
	}
	
	@Override
	protected Object load(String directory, String name, String ext) throws Exception {
		File file = new File(getFilename(directory, name, ext));
		BufferedImage image = ImageIO.read(file);
		return image;
	}
}
