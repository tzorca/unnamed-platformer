package unnamed_platformer.res_mgt;

import unnamed_platformer.app.ImageHelper;
import unnamed_platformer.game.other.CollisionData;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.res_mgt.types.ObjectImage;

public class CollisionDataResLoader extends ResLoader {

	protected CollisionDataResLoader() {
		super("img", ".png");
	}

	@Override
	public CollisionData load(String name) throws Exception {
		return new CollisionData(ImageHelper.resizeWithinCanvas(
				ImageHelper.blur(ResManager.get(ObjectImage.class, name), Ref.MASK_BLUR_ITERATIONS),
				Ref.MASK_SIZE_PERCENT));
	}
}
