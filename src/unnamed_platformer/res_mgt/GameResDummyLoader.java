package unnamed_platformer.res_mgt;

import unnamed_platformer.game.other.World;

public class GameResDummyLoader extends ResLoader<World> {

	protected GameResDummyLoader() {
		super("game", ".game");
	}

	@Override
	protected World load(String name) throws Exception {
		throw new Exception("Cannot load games from a resource loader. Use Game class internal methods.");
	}

}
