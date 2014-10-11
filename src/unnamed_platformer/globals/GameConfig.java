package unnamed_platformer.globals;

import org.newdawn.slick.Input;

import unnamed_platformer.app.InputManager.GameKey;
import unnamed_platformer.app.InputManager.PlrGameKey;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class GameConfig
{
	public final static boolean DEFAULT_FULLSCREEN = true;

	public final static String PRIMARY_WORLD_NAME = "Example Levels";

	public static final Multimap<Integer, PlrGameKey> GAME_KEYS = HashMultimap
			.create();
	static {

		GAME_KEYS.put(Input.KEY_UP, new PlrGameKey(1, GameKey.UP));
		GAME_KEYS.put(Input.KEY_LEFT, new PlrGameKey(1, GameKey.LEFT));
		GAME_KEYS.put(Input.KEY_DOWN, new PlrGameKey(1, GameKey.DOWN));
		GAME_KEYS.put(Input.KEY_RIGHT, new PlrGameKey(1, GameKey.RIGHT));

		GAME_KEYS.put(Input.KEY_X, new PlrGameKey(1, GameKey.A));
		GAME_KEYS.put(Input.KEY_Z, new PlrGameKey(1, GameKey.B));

		GAME_KEYS.put(Input.KEY_ENTER, new PlrGameKey(1, GameKey.START));

		GAME_KEYS.put(Input.KEY_ESCAPE, new PlrGameKey(1, GameKey.EXIT));
		GAME_KEYS.put(Input.KEY_F2, new PlrGameKey(1, GameKey.RESTART));
		GAME_KEYS.put(Input.KEY_F11, new PlrGameKey(1,
				GameKey.TOGGLE_FULLSCREEN));
		GAME_KEYS
				.put(Input.KEY_F12, new PlrGameKey(1, GameKey.SAVE_SCREENSHOT));

		GAME_KEYS
				.put(Input.KEY_LSHIFT, new PlrGameKey(1, GameKey.MULTI_SELECT));
		GAME_KEYS
				.put(Input.KEY_RSHIFT, new PlrGameKey(1, GameKey.MULTI_SELECT));

		GAME_KEYS.put(Input.KEY_W, new PlrGameKey(1, GameKey.SECONDARY_UP));
		GAME_KEYS.put(Input.KEY_A, new PlrGameKey(1, GameKey.SECONDARY_LEFT));
		GAME_KEYS.put(Input.KEY_S, new PlrGameKey(1, GameKey.SECONDARY_DOWN));
		GAME_KEYS.put(Input.KEY_D, new PlrGameKey(1, GameKey.SECONDARY_RIGHT));

		GAME_KEYS
				.put(Input.KEY_LBRACKET, new PlrGameKey(1, GameKey.SCROLL_OUT));
		GAME_KEYS.put(Input.KEY_RBRACKET, new PlrGameKey(1, GameKey.SCROLL_IN));

	}
}
