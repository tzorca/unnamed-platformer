package unnamed_platformer.app;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.globals.Ref;
import unnamed_platformer.view.ViewManager;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public final class InputManager
{

	public enum GameKey {
		LEFT, RIGHT, UP, DOWN, /* */
		A, B, /* */
		START, /* */
		RESTART, TOGGLE_FULLSCREEN, SAVE_SCREENSHOT, EXIT, /* */
		MULTI_SELECT, /* */
		SECONDARY_LEFT, SECONDARY_RIGHT, SECONDARY_UP, SECONDARY_DOWN, /* */
		SCROLL_IN, SCROLL_OUT /* */,
	}

	private static HashMap<Integer, Boolean> rawKeyStates = Maps.newHashMap(),
			lastRawKeyStates = Maps.newHashMap(), rawKeyPressEvents = Maps
					.newHashMap();;

	private static HashMap<PlrGameKey, Boolean> playerGameKeyStates = Maps
			.newHashMap(), lastPlayerGameKeyStates = Maps.newHashMap(),
			playerGameKeyPressEvents = Maps.newHashMap();
	private static Multimap<Integer, PlrGameKey> gameKeyMappings;

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

	private static Point getMousePosInWindow() {
		Point mousePos = MouseInfo.getPointerInfo().getLocation();
		Point contentLocation = ViewManager.getFrame().getContentPane()
				.getLocation();

		Point windowLocation = ViewManager.getFrame().getLocation();

		mousePos.x -= contentLocation.x + windowLocation.x;
		mousePos.y -= contentLocation.y + windowLocation.y;

		return mousePos;
	}

	public static Vector2f getGameMousePos() {
		Point awtMousePoint = getMousePosInWindow();
		Vector2f awtMouseVector = new Vector2f(awtMousePoint.x, awtMousePoint.y);

		Vector2f gameMousePos = new Vector2f();

		Vector2f cameraPos = ViewManager.getCameraPos();

		gameMousePos.x = cameraPos.x - Display.getX() + awtMouseVector.x;
		gameMousePos.y = cameraPos.y - Display.getY() + awtMouseVector.y;

		return gameMousePos;
	}

	public boolean getMouseButtonState(MouseButton mb) {
		return nowMouseButtonStates.get(mb);
	}

	public enum MouseButton {
		left, right
	}

	private static Rectangle mouseBox = new Rectangle();

	public static void update() {
		detectMouseButtons();
		detectMousePos();
		readKeys();
	}

	private static EnumMap<MouseButton, Boolean> nowMouseButtonStates = new EnumMap<MouseButton, Boolean>(
			MouseButton.class);
	private static EnumMap<MouseButton, Boolean> prevMouseButtonStates = new EnumMap<MouseButton, Boolean>(
			MouseButton.class);
	private static Point lastMousePos = new Point(0, 0);

	private static void detectMouseButtons() {
		nowMouseButtonStates.put(MouseButton.left, Mouse.isButtonDown(0));
		nowMouseButtonStates.put(MouseButton.right, Mouse.isButtonDown(1));

		if (!prevMouseButtonStates.isEmpty()) {
			for (MouseButton button : MouseButton.values()) {
				boolean nowState = nowMouseButtonStates.get(button);
				boolean prevState = prevMouseButtonStates.get(button);

				if (nowState) {
					if (button == MouseButton.left) {
						eventHandlers.get(InputEventType.leftMouseDown).run();
					} else if (button == MouseButton.right) {
						eventHandlers.get(InputEventType.rightMouseDown).run();
					}
				}

				if (!nowState && prevState) {
					if (button == MouseButton.left) {
						eventHandlers.get(InputEventType.leftClick).run();
					} else if (button == MouseButton.right) {
						eventHandlers.get(InputEventType.rightClick).run();
					}
				}
			}
		}

		prevMouseButtonStates.putAll(nowMouseButtonStates);
	}

	private static void detectMousePos() {
		Vector2f vectMousePos = getGameMousePos();
		Point mousePos = new Point((int) vectMousePos.x, (int) vectMousePos.y);

		if (!mousePos.equals(lastMousePos)) {
			eventHandlers.get(InputEventType.mouseMotion).run();
		}

		lastMousePos = mousePos;

		mouseBox = new Rectangle(mousePos.x - 8, mousePos.y - 8, 16, 16);
	}

	public enum InputEventType {
		leftClick, rightClick, leftMouseDown, rightMouseDown, mouseMotion
	}

	private static EnumMap<InputEventType, Runnable> eventHandlers = new EnumMap<InputEventType, Runnable>(
			InputEventType.class);

	static {
		// load game key mappings from settings
		gameKeyMappings = Settings.generateGameKeyMappings();

		// setup event handlers to be non-null initially
		resetEvents();
	}

	/**
	 * Prevents previous key events from being passed into new frames.
	 */
	public static void resetEvents() {
		for (InputEventType iet : InputEventType.values()) {
			eventHandlers.put(iet, new Runnable() {
				public void run() {
				}
			});
		}
		for (PlrGameKey pgk : playerGameKeyStates.keySet()) {
			playerGameKeyStates.put(pgk, false);
			lastPlayerGameKeyStates.put(pgk, false);
			playerGameKeyPressEvents.put(pgk, false);

		}
		for (Integer keyCode : rawKeyStates.keySet()) {
			rawKeyStates.put(keyCode, false);
			lastRawKeyStates.put(keyCode, false);
			rawKeyPressEvents.put(keyCode, false);
		}
	}

	public static void setEventHandler(InputEventType inputEventType,
			Runnable runnable) {
		eventHandlers.put(inputEventType, runnable);
	}

	private static void readKeys() {
		while (Keyboard.next()) {
			int keycode = Keyboard.getEventKey();
			Keyboard.getEventCharacter();
			boolean state = Keyboard.getEventKeyState();
			linkKeyState(keycode, state);
		}
	}

	private static void linkKeyState(int keycode, boolean state) {
		rawKeyStates.put(keycode, state);
		if (gameKeyMappings.containsKey(keycode)) {
			Collection<PlrGameKey> mappings = gameKeyMappings.get(keycode);

			if (!lastRawKeyStates.containsKey(keycode)) {
				lastRawKeyStates.put(keycode, false);
			}

			if (state && !lastRawKeyStates.containsKey(keycode)) {
				rawKeyPressEvents.put(keycode, true);

			}
			for (PlrGameKey mapping : mappings) {
				playerGameKeyStates.put(mapping, state);
				if (!lastPlayerGameKeyStates.containsKey(mapping)) {
					lastPlayerGameKeyStates.put(mapping, false);
				}

				if (state && !lastPlayerGameKeyStates.get(mapping)) {
					playerGameKeyPressEvents.put(mapping, true);
				}
				lastPlayerGameKeyStates.put(mapping, state);

			}
		}
	}

	public static boolean keyPressOccurring(GameKey gk, int playerNo) {
		PlrGameKey pgk = new PlrGameKey(playerNo, gk);
		createPlrGameKeyEntry(pgk);

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
		createPlrGameKeyEntry(pgk);

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

	private static void createPlrGameKeyEntry(PlrGameKey pgk) {
		if (!playerGameKeyStates.containsKey(pgk)) {
			playerGameKeyStates.put(pgk, false);
		}
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

	public static boolean mouseIntersects(Rectangle box) {
		return mouseBox.intersects(box);
	}

	public static int getAssociatedKeyCode(PlrGameKey plrGameKey) {
		for (Entry<Integer, PlrGameKey> entry : gameKeyMappings.entries()) {
			if (entry.getValue().equals(plrGameKey)) {
				return entry.getKey();
			}
		}
		return -1;
	}

	public static List<Integer> getRawKeyPresses() {
		List<Integer> pressedKeyCodes = new ArrayList<Integer>();
		for (Integer keyCode : rawKeyPressEvents.keySet()) {
			if (rawKeyPressEvents.get(keyCode)) {
				pressedKeyCodes.add(keyCode);
			}
		}

		return pressedKeyCodes;
	}

}
