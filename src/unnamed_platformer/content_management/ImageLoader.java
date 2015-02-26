package unnamed_platformer.content_management;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageLoader extends ContentLoader {

	protected ImageLoader() {
		super(".png");
	}

	@Override
	public Object load(String directory, String name) throws Exception {
		File file = new File(getFilename(directory, name));
		BufferedImage image = ImageIO.read(file);
		return image;
	}

}
