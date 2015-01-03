package unnamed_platformer.input;

import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.lwjgl.input.Keyboard;

import unnamed_platformer.app.Settings;
import unnamed_platformer.app.TimeManager;
import unnamed_platformer.globals.Ref;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public final class InputManager
{
	private static Multimap<Integer, PlrGameKey> gameKeyMappings;

	private static HashMap<PlrGameKey, Boolean>
	/* */playerGameKeyStates = Maps.newHashMap(),
	/* */lastPlayerGameKeyStates = Maps.newHashMap(),
	/* */playerGameKeyPressEvents = Maps.newHashMap();

	public static Collection<PlrGameKey> getGameKeysMatchingKeyEvent(
			KeyEvent keyEvent) {

		int translatedKeyCode = KeyCodeTranslator
				.translateJavaVKToJInputCode(keyEvent.getKeyCode());

		if (gameKeyMappings.containsKey(translatedKeyCode)) {
			return gameKeyMappings.get(translatedKeyCode);
		}

		return new HashSet<PlrGameKey>();
	}

	public static void mapKey(int playerNo, GameKey gk, int key) {
		gameKeyMappings.put(key, new PlrGameKey(playerNo, gk));
	}

	public static void unmapAllKeyMappings(int key) {
		gameKeyMappings.removeAll(key);
	}

	public static void update() {
		MouseInputManager.update();
		readKeys();
	}

	public static void init() {
		// set up key code translator
		KeyCodeTranslator.init();

		// init mouse input manager
		MouseInputManager.init();

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
			if (gameKeyMappings.containsKey(keycode)) {
				linkKeyState(gameKeyMappings.get(keycode), state);
			}
		}
	}

	private static void linkKeyState(Collection<PlrGameKey> mappings,
			boolean state) {

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

	public static int getAssociatedKeyCode(PlrGameKey plrGameKey) {
		for (Entry<Integer, PlrGameKey> entry : gameKeyMappings.entries()) {
			if (entry.getValue().equals(plrGameKey)) {
				return entry.getKey();
			}
		}
		return -1;
	}

}
