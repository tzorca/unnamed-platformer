package unnamed_platformer.res_mgt;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

public class TextureResLoader extends ResLoader
{

	protected TextureResLoader(String directory) {
		super(directory, ".png");
	}
	

	@Override
	public Object load(String name) throws Exception {
		Texture tex = null;
		File file = new File(getFilename(name));
		BufferedImage image = ImageIO.read(file);
		
		tex = BufferedImageUtil.getTexture(name, image);
		return tex;
	}

}
