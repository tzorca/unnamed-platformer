package unnamed_platformer.content_management;

import java.awt.image.BufferedImage;

import unnamed_platformer.app.ImageHelper;
import unnamed_platformer.game.physics.CollisionData;
import unnamed_platformer.globals.GameGlobals;

public class CollisionDataLoader extends ContentLoader
{
	protected CollisionDataLoader() {
		super(".png");
	}

	@Override
	public CollisionData load(String directory, String name) throws Exception {
		return new CollisionData(ImageHelper.percentScaleWithinCanvas(
				ImageHelper.blur(ContentManager.get(BufferedImage.class,
						directory, name),
						GameGlobals.TEXTURE_COLLISION_BLUR_ITERATIONS),
				GameGlobals.TEXTURE_COLLISION_SIZE_MODIFIER));
	}
}