package unnamed_platformer.res_mgt;

import java.awt.image.BufferedImage;

import unnamed_platformer.app.ImageHelper;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.structures.BinaryPixelGrid;

public class BinaryPixelGridResLoader extends ResLoader<BinaryPixelGrid> {

	protected BinaryPixelGridResLoader() {
		super("img", ".png");
	}

	@Override
	public BinaryPixelGrid load(String name) throws Exception {
		return new BinaryPixelGrid(ImageHelper.resizeImageWithinCanvas(
				ImageHelper.blurImage(ResManager.get(BufferedImage.class, name), Ref.MASK_BLUR_ITERATIONS),
				Ref.MASK_SIZE_PERCENT));
	}
}
