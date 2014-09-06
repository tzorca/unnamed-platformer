package unnamed_platformer.res_mgt;

import java.awt.image.BufferedImage;

import unnamed_platformer.app.ImageHelper;
import unnamed_platformer.game.other.CollisionData;
import unnamed_platformer.globals.Ref;

public class CollisionDataResLoader extends ResLoader<CollisionData> {

	protected CollisionDataResLoader() {
		super("img", ".png");
	}

	@Override
	public CollisionData load(String name) throws Exception {
		return new CollisionData(ImageHelper.resizeImageWithinCanvas(
				ImageHelper.blurImage(ResManager.get(BufferedImage.class, name), Ref.MASK_BLUR_ITERATIONS),
				Ref.MASK_SIZE_PERCENT));
	}
}
