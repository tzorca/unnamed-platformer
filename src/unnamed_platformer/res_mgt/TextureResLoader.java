package unnamed_platformer.res_mgt;

import java.awt.image.BufferedImage;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

public class TextureResLoader extends ResLoader<Texture>
{

	protected TextureResLoader() {
		super("img", ".png");
	}

	@Override
	public Texture load(String name) throws Exception {
		Texture tex = null;
		BufferedImage img = ResManager.get(BufferedImage.class, name);
		tex = BufferedImageUtil.getTexture(name, img);
		return tex;
	}

}
