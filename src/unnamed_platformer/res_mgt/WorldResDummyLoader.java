package unnamed_platformer.res_mgt;

import unnamed_platformer.game.other.World;

public class WorldResDummyLoader extends ResLoader<World> {

	protected WorldResDummyLoader() {
		super("game", ".json");
	}

	@Override
	protected World load(String name) throws Exception {
		throw new Exception("Cannot load worlds from a resource loader. Use World class internal methods.");
	}

}
