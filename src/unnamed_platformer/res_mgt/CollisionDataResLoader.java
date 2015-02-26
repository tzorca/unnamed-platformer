package unnamed_platformer.res_mgt;

import unnamed_platformer.app.ImageHelper;
import unnamed_platformer.game.other.CollisionData;
import unnamed_platformer.globals.GameGlobals;
import unnamed_platformer.res_mgt.types.ObjectImage;

public class CollisionDataResLoader extends ResLoader
{

	protected CollisionDataResLoader() {
		super("img", ".png");
	}

	@Override
	public CollisionData load(String name) throws Exception {
		return new CollisionData(ImageHelper.percentScaleWithinCanvas(
				ImageHelper.blur(ResManager.get(ObjectImage.class, name),
						GameGlobals.TEXTURE_COLLISION_BLUR_ITERATIONS),
				GameGlobals.TEXTURE_COLLISION_SIZE_MODIFIER));
	}
}
