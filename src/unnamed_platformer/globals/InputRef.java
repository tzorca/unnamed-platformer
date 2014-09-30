package unnamed_platformer.globals;

import org.newdawn.slick.Input;

import unnamed_platformer.app.InputManager.PlayerGameKey;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public final class InputRef {

	public enum GameKey {
		left, right, up, down, /* */
		jump, shoot, /* */
		ok, back, /* */
		restartApp, toggleFullscreen, saveScreenshot, /* */
		extraLeft, extraRight, extraUp, extraDown, /* */
		scrollIn, scrollOut, /* */
		multiselect, /* */
	}

	public static final Multimap<Integer, PlayerGameKey> GAME_KEY_SETUP = HashMultimap
			.create();
	static {

		// just an alias
		Multimap<Integer, PlayerGameKey> gk = GAME_KEY_SETUP;

		gk.put(Input.KEY_UP, new PlayerGameKey(1, GameKey.up));
		gk.put(Input.KEY_LEFT, new PlayerGameKey(1, GameKey.left));
		gk.put(Input.KEY_DOWN, new PlayerGameKey(1, GameKey.down));
		gk.put(Input.KEY_RIGHT, new PlayerGameKey(1, GameKey.right));

		gk.put(Input.KEY_X, new PlayerGameKey(1, GameKey.jump));
		gk.put(Input.KEY_Z, new PlayerGameKey(1, GameKey.shoot));

		gk.put(Input.KEY_ENTER, new PlayerGameKey(1, GameKey.ok));
		gk.put(Input.KEY_ESCAPE, new PlayerGameKey(1, GameKey.back));

		gk.put(Input.KEY_F2, new PlayerGameKey(1, GameKey.restartApp));
		gk.put(Input.KEY_F11, new PlayerGameKey(1, GameKey.toggleFullscreen));
		gk.put(Input.KEY_F12, new PlayerGameKey(1, GameKey.saveScreenshot));

		gk.put(Input.KEY_W, new PlayerGameKey(1, GameKey.extraUp));
		gk.put(Input.KEY_A, new PlayerGameKey(1, GameKey.extraLeft));
		gk.put(Input.KEY_S, new PlayerGameKey(1, GameKey.extraDown));
		gk.put(Input.KEY_D, new PlayerGameKey(1, GameKey.extraRight));

		gk.put(Input.KEY_LBRACKET, new PlayerGameKey(1, GameKey.scrollOut));
		gk.put(Input.KEY_RBRACKET, new PlayerGameKey(1, GameKey.scrollIn));

		gk.put(Input.KEY_LSHIFT, new PlayerGameKey(1, GameKey.multiselect));
		gk.put(Input.KEY_RSHIFT, new PlayerGameKey(1, GameKey.multiselect));

	}
}
