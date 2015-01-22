package unnamed_platformer.game.other;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.other.DirectionalEnums.Orientation;
import unnamed_platformer.globals.EntityRef.EntityParam;
import unnamed_platformer.view.Graphic;

public class EntitySetupSerializable
{
	Graphic graphic;
	Vector2f location;
	Orientation orientation;
	SizeStrategy sizeStrategy;

	public static EntitySetupSerializable get(EntitySetup setup) {
		EntitySetupSerializable newSerializable = new EntitySetupSerializable();
		for (EntityParam param : EntityParam.values()) {
			if (setup.has(param)) {
				switch (param) {
				case GRAPHIC:
					newSerializable.graphic = (Graphic) setup.get(param);
					break;
				case LOCATION:
					newSerializable.location = (Vector2f) setup.get(param);
					break;
				case ORIENTATION:
					newSerializable.orientation = (Orientation) setup
							.get(param);
					break;
				case SIZE_STRATEGY:
					newSerializable.sizeStrategy = (SizeStrategy) setup
							.get(param);
					break;
				default:
					break;

				}
			}
		}
		return newSerializable;

	}
}
