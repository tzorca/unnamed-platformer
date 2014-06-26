package unnamed_platformer.app;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.HashMap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.globals.InputRef;
import unnamed_platformer.globals.InputRef.GameKey;

public class InputManager {
	private static HashMap<Integer, Boolean> rawKeyStates = new HashMap<Integer, Boolean>();
	private static HashMap<PlayerGameKey, Boolean> playerGameKeyStates = new HashMap<PlayerGameKey, Boolean>();
	private static HashMap<Integer, PlayerGameKey> rawKeyToPlayerGameKeyMapping = new HashMap<Integer, PlayerGameKey>();

	public static void init() {
		// add default key mapping
		rawKeyToPlayerGameKeyMapping.putAll(InputRef.DEFAULT_GAME_KEYS);
	}

	public static void mapKey(int playerNo, GameKey gk, int key) {
		rawKeyToPlayerGameKeyMapping.put(key, new PlayerGameKey(playerNo, gk));
	}

	public static void unmapKey(int key) {
		rawKeyToPlayerGameKeyMapping.remove(key);
	}

	private static Point getMousePosInWindow() {
		Point mousePos = MouseInfo.getPointerInfo().getLocation();
		Point contentLocation = ViewManager.getFrame().getContentPane().getLocation();
		Point windowLocation = ViewManager.getFrame().getLocation();
		mousePos.x -= contentLocation.x + windowLocation.x;
		mousePos.y -= contentLocation.y + windowLocation.y;

		return mousePos;
	}

	public static Vector2f getGameMousePos() {
		Point awtMousePoint = getMousePosInWindow();
		Vector2f awtMouseVector = new Vector2f(awtMousePoint.x, awtMousePoint.y);

		Vector2f gameMousePos = new Vector2f();

		gameMousePos.x = ViewManager.getViewportX() - Display.getX() + awtMouseVector.x;
		gameMousePos.y = ViewManager.getViewportY() - Display.getY() + awtMouseVector.y;

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
		getKeys();
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
				if (!nowState && prevState) {
					switch (button) {
					case left:
						eventHandlers.get(InputEventType.leftClick).run();
						break;
					case right:
						eventHandlers.get(InputEventType.rightClick).run();
						break;
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
		leftClick, rightClick, mouseMotion
	}

	private static EnumMap<InputEventType, Runnable> eventHandlers = new EnumMap<InputEventType, Runnable>(
			InputEventType.class);

	static {
		// setup event handlers to be non-null initially
		resetEventHandlers();
	}

	public static void resetEventHandlers() {
		for (InputEventType iet : InputEventType.values()) {
			eventHandlers.put(iet, new Runnable() {
				public void run() {
				}
			});
		}
	}

	public static void setEventHandler(InputEventType inputEventType, Runnable runnable) {
		eventHandlers.put(inputEventType, runnable);
	}

	private static void getKeys() {
		while (Keyboard.next()) {
			int keycode = Keyboard.getEventKey();
			boolean state = Keyboard.getEventKeyState();
			setKey(keycode, state);
		}
	}

	private static void setKey(int keycode, boolean state) {
		rawKeyStates.put(keycode, state);
		if (rawKeyToPlayerGameKeyMapping.containsKey(keycode)) {
			playerGameKeyStates.put(rawKeyToPlayerGameKeyMapping.get(keycode), state);
		}
	}

	public static boolean getKeyState(Integer keycode) {
		if (!rawKeyStates.containsKey(keycode)) {
			return false;
		}

		return rawKeyStates.get(keycode);
	}

	public static boolean getGameKeyState(GameKey gk, int playerNo) {
		PlayerGameKey pgk = new PlayerGameKey(playerNo, gk);

		if (!playerGameKeyStates.containsKey(pgk)) {
			return false;
		}

		return playerGameKeyStates.get(pgk);
	}

	public static void resetGameKey(GameKey gk, int playerNo) {
		PlayerGameKey pgk = new PlayerGameKey(playerNo, gk);

		if (!playerGameKeyStates.containsKey(pgk)) {
			return;
		}

		playerGameKeyStates.put(pgk, false);
	}

	public static class PlayerGameKey {
		private GameKey gameKey;
		private int playerNo;

		public PlayerGameKey(int playerNo, GameKey gk) {
			this.playerNo = playerNo;
			this.gameKey = gk;
		}

		public int hashCode() {
			return new HashCodeBuilder(23, 11). // two randomly chosen prime
												// numbers
					append(playerNo).append(gameKey).toHashCode();
		}

		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (obj == this)
				return true;
			if (!(obj instanceof PlayerGameKey))
				return false;

			PlayerGameKey rhs = (PlayerGameKey) obj;
			return new EqualsBuilder().append(playerNo, rhs.playerNo).append(gameKey, rhs.gameKey).isEquals();
		}

	}

	public static boolean mouseIntersects(Rectangle box) {
		return mouseBox.intersects(box);
	}

	public static boolean isArrowKey(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
			return true;
		}
		return false;
	}

}
