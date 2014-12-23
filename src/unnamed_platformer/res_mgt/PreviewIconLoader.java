package unnamed_platformer.res_mgt;

import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class PreviewIconLoader extends ResLoader<ImageIcon>
{
	protected PreviewIconLoader() {
		super("game-preview", ".png");
	}

	@Override
	public ImageIcon load(String name) throws Exception {
		File file = new File(getFilename(name));
		return new ImageIcon(ImageIO.read(file));
	}

}
