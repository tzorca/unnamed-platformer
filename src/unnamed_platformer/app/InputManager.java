package unnamed_platformer.app;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
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
import unnamed_platformer.view.ViewManager;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public final class InputManager {
	private static HashMap<Integer, Boolean> rawKeyStates = Maps.newHashMap();
	private static HashMap<PlayerGameKey, Boolean> playerGameKeyStates = Maps
			.newHashMap(), lastPlayerGameKeyStates = Maps.newHashMap(),
			playerGameKeyPressEvents = Maps.newHashMap();
	private static Multimap<Integer, PlayerGameKey> gamekeyMappings = InputRef.GAME_KEY_SETUP;

	public static void mapKey(int playerNo, GameKey gk, int key) {
		gamekeyMappings.put(key, new PlayerGameKey(playerNo, gk));
	}

	public static void unmapAllKeyMappings(int key) {
		gamekeyMappings.removeAll(key);
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

	public static void setEventHandler(InputEventType inputEventType,
			Runnable runnable) {
		eventHandlers.put(inputEventType, runnable);
	}

	private static void readKeys() {
		while (Keyboard.next()) {
			int keycode = Keyboard.getEventKey();
			boolean state = Keyboard.getEventKeyState();
			assignKeyState(keycode, state);
		}
	}

	private static void assignKeyState(int keycode, boolean state) {
		rawKeyStates.put(keycode, state);
		if (gamekeyMappings.containsKey(keycode)) {
			Collection<PlayerGameKey> mappings = gamekeyMappings.get(keycode);

			for (PlayerGameKey mapping : mappings) {
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

	public static boolean keyPressOccuring(GameKey gk, int playerNo) {
		PlayerGameKey pgk = new PlayerGameKey(playerNo, gk);
		if (!playerGameKeyStates.containsKey(pgk)) {
			playerGameKeyStates.put(pgk, false);
			return false;
		}

		return playerGameKeyStates.get(pgk);
	}

	public static boolean keyPressOccurred(GameKey gk, int playerNo) {
		PlayerGameKey pgk = new PlayerGameKey(playerNo, gk);
		if (playerGameKeyPressEvents.containsKey(pgk)) {
			boolean returnValue = playerGameKeyPressEvents.get(pgk);
			playerGameKeyPressEvents.put(pgk, false);

			return returnValue;
		}
		return false;
	}

	public static class PlayerGameKey {
		private GameKey gameKey;
		private int playerNo;

		public PlayerGameKey(int playerNo, GameKey gk) {
			this.playerNo = playerNo;
			this.gameKey = gk;
		}

		public int hashCode() {
			// two randomly chosen prime numbers
			return new HashCodeBuilder(23, 11).append(playerNo).append(gameKey)
					.toHashCode();
		}

		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (obj == this)
				return true;
			if (!(obj instanceof PlayerGameKey))
				return false;

			PlayerGameKey rhs = (PlayerGameKey) obj;
			return new EqualsBuilder().append(playerNo, rhs.playerNo)
					.append(gameKey, rhs.gameKey).isEquals();
		}

	}

	public static boolean mouseIntersects(Rectangle box) {
		return mouseBox.intersects(box);
	}

}
