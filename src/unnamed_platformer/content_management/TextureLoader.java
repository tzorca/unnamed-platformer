package unnamed_platformer.content_management;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

public class TextureLoader extends ContentLoader
{

	public TextureLoader() {
		super(".png");
	}

	@Override
	public Object load(String directory, String name, String ext) throws Exception {
		Texture tex = null;
		File file = new File(getFilename(directory, name, ext));
		BufferedImage image = ImageIO.read(file);

		tex = BufferedImageUtil.getTexture(name, image);
		return tex;
	}

}
