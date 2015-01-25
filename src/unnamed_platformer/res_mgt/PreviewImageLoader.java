package unnamed_platformer.res_mgt;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class PreviewImageLoader extends ResLoader
{
	protected PreviewImageLoader() {
		super("img-preview", ".png");
	}

	@Override
	public Object load(String name) throws Exception {
		File file = new File(getFilename(name));
		return new ImageIcon(ImageIO.read(file));
	}

}
