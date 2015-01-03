package unnamed_platformer.input;

import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;

import unnamed_platformer.app.Settings;
import unnamed_platformer.app.TimeManager;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.input.RawKey.KeyType;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public final class InputManager
{
	private static Multimap<RawKey, PlrGameKey> gameKeyMappings;

	private static HashMap<PlrGameKey, Boolean>
	/* */playerGameKeyStates = Maps.newHashMap(),
	/* */lastPlayerGameKeyStates = Maps.newHashMap(),
	/* */playerGameKeyPressEvents = Maps.newHashMap();

	public static Collection<PlrGameKey> getGameKeysMatchingKeyEvent(
			KeyEvent keyEvent) {

		RawKey key = RawKey.fromAWTKeyCode(keyEvent.getKeyCode());

		if (gameKeyMappings.containsKey(key)) {
			return gameKeyMappings.get(key);
		} else {
			return new HashSet<PlrGameKey>();
		}
	}

	public static void mapKey(RawKey key, int playerNo, GameKey gk) {
		gameKeyMappings.put(key, new PlrGameKey(playerNo, gk));
	}

	public static void unmapAllKeyMappings(RawKey key) {
		gameKeyMappings.removeAll(key);
	}

	public static void update() {
		MouseInputManager.update();
		GamepadInputManager.update();
		readKeys();
	}

	public static void init() {
		// set up key code translator
		KeyCodeTranslator.init();

		// init mouse input manager
		MouseInputManager.init();

		// init gamepad input manager
		GamepadInputManager.init();

		// load game key mappings from settings
		loadMappingsFromSettings();

		// setup event handlers to be non-null initially
		resetEvents();
	}

	/**
	 * Prevent previous key events from being passed into new frames.
	 */
	public static void resetEvents() {
		MouseInputManager.resetEvents();
		Controllers.clearEvents();

		for (PlrGameKey pgk : gameKeyMappings.values()) {
			playerGameKeyStates.put(pgk, false);
			lastPlayerGameKeyStates.put(pgk, false);
			playerGameKeyPressEvents.put(pgk, false);
		}
	}

	public static void loadMappingsFromSettings() {
		gameKeyMappings = Settings.generateGameKeyMappings();

	}

	private static void readKeys() {
		while (Keyboard.next()) {
			int keycode = Keyboard.getEventKey();
			Keyboard.getEventCharacter();
			boolean state = Keyboard.getEventKeyState();
			RawKey key = new RawKey(KeyType.KEYBOARD_JINPUT, keycode);

			linkKeyState(key, state);
		}
	}

	public static void linkKeyState(RawKey key, boolean state) {
		if (!gameKeyMappings.containsKey(key)) {
			return;
		}

		Collection<PlrGameKey> mappings = gameKeyMappings.get(key);

		for (PlrGameKey mapping : mappings) {
			playerGameKeyStates.put(mapping, state);

			if (state && !lastPlayerGameKeyStates.get(mapping)) {
				playerGameKeyPressEvents.put(mapping, true);
			}
			lastPlayerGameKeyStates.put(mapping, state);

		}
	}

	public static boolean keyIsPressed(GameKey gk, int playerNo) {
		PlrGameKey pgk = new PlrGameKey(playerNo, gk);

		return playerGameKeyStates.get(pgk);
	}

	public static boolean keyPressOccurred(GameKey gk, int playerNo) {
		PlrGameKey pgk = new PlrGameKey(playerNo, gk);
		if (playerGameKeyPressEvents.containsKey(pgk)) {
			boolean returnValue = playerGameKeyPressEvents.get(pgk);
			playerGameKeyPressEvents.put(pgk, false);

			return returnValue;
		}
		return false;
	}

	private final static String REPETITION = "Repetition";

	public static boolean keyPressOccurredOrRepeating(GameKey gk, int playerNo) {

		PlrGameKey pgk = new PlrGameKey(playerNo, gk);

		// Check if occurred
		if (keyPressOccurred(gk, playerNo)) {
			TimeManager.sample(pgk);
			return true;
		}

		if (TimeManager.secondsSince(TimeManager.lastSample(pgk)) > Ref.INPUT_DELAY_TIME) {
			// Check if repeating
			if (TimeManager.periodElapsed(pgk, REPETITION,
					Ref.INPUT_REPEAT_TIME)) {
				return playerGameKeyStates.get(pgk);
			}
		}

		return false;
	}

	public static class PlrGameKey
	{
		private GameKey gameKey;
		private int playerNo;

		public PlrGameKey(int playerNo, GameKey gk) {
			this.playerNo = playerNo;
			this.gameKey = gk;
		}

		public int hashCode() {
			// two randomly chosen prime numbers
			return new HashCodeBuilder(23, 11).append(playerNo).append(gameKey)
					.toHashCode();
		}

		public GameKey getGameKey() {
			return gameKey;
		}

		public int getPlayerNo() {
			return playerNo;
		}

		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (obj == this)
				return true;
			if (!(obj instanceof PlrGameKey))
				return false;

			PlrGameKey rhs = (PlrGameKey) obj;
			return new EqualsBuilder().append(playerNo, rhs.playerNo)
					.append(gameKey, rhs.gameKey).isEquals();
		}

		public String toString() {
			return "PLR" + playerNo + "_" + gameKey.toString();
		}

		/***
		 * @param source
		 *            PLR{playerNo}_{gameKey}
		 */
		public static PlrGameKey fromString(String source) {
			try {
				Integer playerNo = Integer.parseInt(String.valueOf(source
						.charAt(3)));

				String gameKeyString = source.substring(5);

				GameKey gameKey = GameKey.valueOf(gameKeyString);

				return new PlrGameKey(playerNo, gameKey);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Invalid PlrGameKey string " + source
						+ ". Format should be PLR{playerNo}_{gameKey}");
				return null;
			}
		}
	}

	public static RawKey getAssociatedKey(PlrGameKey plrGameKey)
			throws Exception {
		for (Entry<RawKey, PlrGameKey> entry : gameKeyMappings.entries()) {
			if (entry.getValue().equals(plrGameKey)) {
				return entry.getKey();
			}
		}
		// Invalid key
		throw new Exception("No associated game key for "
				+ plrGameKey.toString());
	}

	public static void finish() {
		GamepadInputManager.finish();
	}

	/**
	 * Prevents a key from activating a press event
	 */
	public static void ghost(PlrGameKey pgk) {
		lastPlayerGameKeyStates.put(pgk, true);

	}

	public static void ghostAll() {
		for (PlrGameKey pgk : lastPlayerGameKeyStates.keySet()) {
			ghost(pgk);
		}
	}

}
