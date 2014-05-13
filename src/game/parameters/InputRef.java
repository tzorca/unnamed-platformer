package game.parameters;

import java.util.HashMap;

import org.newdawn.slick.Input;

import app.InputManager.PlayerGameKey;

public class InputRef {

	public enum GameKey {
		left, right, up, down, a, b, start, scrollIn, scrollOut, sizePlus, sizeMinus, restartApp, toggleFullscreen, startRandomGame,
	}

	public static final HashMap<Integer, PlayerGameKey> DEFAULT_GAME_KEYS = new HashMap<Integer, PlayerGameKey>();
	static {

		// just an alias
		HashMap<Integer, PlayerGameKey> dgk = DEFAULT_GAME_KEYS;

		dgk.put(Input.KEY_F2, new PlayerGameKey(1, GameKey.restartApp));
		dgk.put(Input.KEY_F11, new PlayerGameKey(1, GameKey.toggleFullscreen));
		dgk.put(Input.KEY_F3, new PlayerGameKey(1, GameKey.startRandomGame));

		dgk.put(Input.KEY_LEFT, new PlayerGameKey(1, GameKey.left));
		dgk.put(Input.KEY_RIGHT, new PlayerGameKey(1, GameKey.right));
		dgk.put(Input.KEY_UP, new PlayerGameKey(1, GameKey.up));
		dgk.put(Input.KEY_DOWN, new PlayerGameKey(1, GameKey.down));

		dgk.put(Input.KEY_LSHIFT, new PlayerGameKey(1, GameKey.a));
		dgk.put(Input.KEY_RSHIFT, new PlayerGameKey(1, GameKey.a));
		dgk.put(Input.KEY_RCONTROL, new PlayerGameKey(1, GameKey.b));
		dgk.put(Input.KEY_ENTER, new PlayerGameKey(1, GameKey.start));

		dgk.put(Input.KEY_LBRACKET, new PlayerGameKey(1, GameKey.scrollOut));
		dgk.put(Input.KEY_RBRACKET, new PlayerGameKey(1, GameKey.scrollIn));

		dgk.put(Input.KEY_MINUS, new PlayerGameKey(1, GameKey.sizeMinus));
		dgk.put(Input.KEY_EQUALS, new PlayerGameKey(1, GameKey.sizePlus));
	}
}
