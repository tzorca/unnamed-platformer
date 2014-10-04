package unnamed_platformer.globals;

import org.newdawn.slick.Input;

import unnamed_platformer.app.InputManager.PlayerGameKey;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public final class InputRef
{

	public enum GameKey {
		LEFT, RIGHT, UP, DOWN, /* */
		A, B, /* */
		START, /* */
		RESTART_APP, TOGGLE_FULLSCREEN, SAVE_SCREENSHOT, /* */
		MULTI_SELECT, /* */
		SECONDARY_LEFT, SECONDARY_RIGHT, SECONDARY_UP, SECONDARY_DOWN, /* */
		SCROLL_IN, SCROLL_OUT /* */
	}

	public static final Multimap<Integer, PlayerGameKey> GAME_KEY_SETUP = HashMultimap
			.create();
	static {

		// just an alias
		Multimap<Integer, PlayerGameKey> gk = GAME_KEY_SETUP;

		gk.put(Input.KEY_UP, new PlayerGameKey(1, GameKey.UP));
		gk.put(Input.KEY_LEFT, new PlayerGameKey(1, GameKey.LEFT));
		gk.put(Input.KEY_DOWN, new PlayerGameKey(1, GameKey.DOWN));
		gk.put(Input.KEY_RIGHT, new PlayerGameKey(1, GameKey.RIGHT));

		gk.put(Input.KEY_X, new PlayerGameKey(1, GameKey.A));
		gk.put(Input.KEY_Z, new PlayerGameKey(1, GameKey.B));

		gk.put(Input.KEY_ENTER, new PlayerGameKey(1, GameKey.START));

		gk.put(Input.KEY_F2, new PlayerGameKey(1, GameKey.RESTART_APP));
		gk.put(Input.KEY_F11, new PlayerGameKey(1, GameKey.TOGGLE_FULLSCREEN));
		gk.put(Input.KEY_F12, new PlayerGameKey(1, GameKey.SAVE_SCREENSHOT));

		gk.put(Input.KEY_LSHIFT, new PlayerGameKey(1, GameKey.MULTI_SELECT));
		gk.put(Input.KEY_RSHIFT, new PlayerGameKey(1, GameKey.MULTI_SELECT));

		gk.put(Input.KEY_W, new PlayerGameKey(1, GameKey.SECONDARY_UP));
		gk.put(Input.KEY_A, new PlayerGameKey(1, GameKey.SECONDARY_LEFT));
		gk.put(Input.KEY_S, new PlayerGameKey(1, GameKey.SECONDARY_DOWN));
		gk.put(Input.KEY_D, new PlayerGameKey(1, GameKey.SECONDARY_RIGHT));

		gk.put(Input.KEY_LBRACKET, new PlayerGameKey(1, GameKey.SCROLL_OUT));
		gk.put(Input.KEY_RBRACKET, new PlayerGameKey(1, GameKey.SCROLL_IN));

	}
}
