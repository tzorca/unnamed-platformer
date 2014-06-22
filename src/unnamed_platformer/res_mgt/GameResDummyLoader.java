package unnamed_platformer.res_mgt;

import unnamed_platformer.game.Game;

public class GameResDummyLoader extends ResLoader<Game> {

	protected GameResDummyLoader() {
		super("game", ".game");
	}

	@Override
	protected Game load(String name) throws Exception {
		throw new Exception("Cannot load games from a resource loader. Use Game class internal methods.");
	}

}
