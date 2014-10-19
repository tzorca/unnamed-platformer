package unnamed_platformer.res_mgt;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageResLoader extends ResLoader<BufferedImage> {

	protected ImageResLoader() {
		super("img", ".png");
	}

	@Override
	public BufferedImage load(String name) throws Exception {
		File file = new File(getFilename(name));
		BufferedImage image = ImageIO.read(file);
		return image;
	}

}
