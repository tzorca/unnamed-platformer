// Tim Zorca
// CPSC 3520
package app;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Input;

import app.gui.GUIManager;

public class InputManager {
	private static HashMap<Integer, Boolean> keystates = new HashMap<Integer, Boolean>();
	private static HashMap<PlayerGameKey, Boolean> gamekeystates = new HashMap<PlayerGameKey, Boolean>();
	private static HashMap<Integer, PlayerGameKey> gamekeymap = new HashMap<Integer, PlayerGameKey>();

	public static void init() {
		mapGamekeys();
	}

	public static void mapGamekeys() {
		mapKey(1, GameKey.restartApp, Input.KEY_F2);
		mapKey(1, GameKey.toggleFullscreen, Input.KEY_F11);

		mapKey(1, GameKey.left, Input.KEY_LEFT);
		mapKey(1, GameKey.right, Input.KEY_RIGHT);
		mapKey(1, GameKey.up, Input.KEY_UP);
		mapKey(1, GameKey.down, Input.KEY_DOWN);

		mapKey(1, GameKey.a, Input.KEY_LSHIFT);
		mapKey(1, GameKey.a, Input.KEY_RSHIFT);
		mapKey(1, GameKey.b, Input.KEY_RCONTROL);
		mapKey(1, GameKey.start, Input.KEY_ENTER);

		mapKey(1, GameKey.scrollOut, Input.KEY_LBRACKET);
		mapKey(1, GameKey.scrollIn, Input.KEY_RBRACKET);
		mapKey(1, GameKey.sizeMinus, Input.KEY_MINUS);
		mapKey(1, GameKey.sizePlus, Input.KEY_EQUALS);
	}

	public static void mapKey(int playerNo, GameKey gk, int key) {
		gamekeymap.put(key, new PlayerGameKey(playerNo, gk));
	}

	public static void unmapKey(int key) {
		gamekeymap.remove(key);
	}

	public static Point getMousePos() {
		return new Point(Mouse.getX(), ViewManager.height - Mouse.getY());
	}

	public static int getMouseX() {
		return Mouse.getX();
	}

	public static int getMouseY() {
		return ViewManager.height - Mouse.getY();
	}

	public boolean leftMouseClicked() {
		return leftMouseClicked;
	}

	public static boolean leftMouseDown;
	@SuppressWarnings("unused")
	private static boolean rightMouseDown;
	private static boolean leftMouseClicked;
	@SuppressWarnings("unused")
	private static boolean rightMouseClicked;

	private static Rectangle mouseBox = new Rectangle();

	public static void update() {
		getMouseActions();
		getKeys();
		processSpecialKeys();
	}

	private static void processSpecialKeys() {
		if (InputManager.getGameKeyState(GameKey.restartApp, 1)) {
			GUIManager.setStateHeld(false);
			App.restart();
		}

		/*
		 * if (InputManager.getGameKeyState(GameKey.toggleFullscreen, 1)) {
		 * ViewManager.toggleFullscreen();
		 * resetGameKeyState(GameKey.toggleFullscreen, 1); }
		 */
	}

	private static void getMouseActions() {
		if (Mouse.isButtonDown(0)) {
			if (!leftMouseDown) {
				leftMouseClicked = true;
			}
			leftMouseDown = true;
		} else {
			leftMouseDown = false;
			leftMouseClicked = false;
		}

		Point mousePos = getMousePos();
		mouseBox = new Rectangle(mousePos.x - 8, mousePos.y - 8, 16, 16);
	}

	private static void getKeys() {
		while (Keyboard.next()) {
			int key = Keyboard.getEventKey();
			boolean keystate = Keyboard.getEventKeyState();
			keystates.put(key, keystate);
			if (gamekeymap.containsKey(key)) {
				gamekeystates.put(gamekeymap.get(key), keystate);
			}
			GUIManager
					.pushKeyEvent(key, Keyboard.getEventCharacter(), keystate);
		}
	}

	public static boolean getKeyState(Integer keycode) {
		if (!keystates.containsKey(keycode)) {
			return false;
		}

		return keystates.get(keycode);
	}

	public static boolean getGameKeyState(GameKey gk, int playerNo) {
		PlayerGameKey pgk = new PlayerGameKey(playerNo, gk);

		if (!gamekeystates.containsKey(pgk)) {
			return false;
		}

		return gamekeystates.get(pgk);
	}

	public static void resetGameKeyState(GameKey gk, int playerNo) {
		PlayerGameKey pgk = new PlayerGameKey(playerNo, gk);

		if (!gamekeystates.containsKey(pgk)) {
			return;
		}

		gamekeystates.put(pgk, false);
	}

	public static boolean leftMouseWasClicked() {
		return leftMouseClicked;
	}

	public enum GameKey {
		left, right, up, down, a, b, start, scrollIn, scrollOut, sizePlus, sizeMinus, restartApp, toggleFullscreen,
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
			return new EqualsBuilder().append(playerNo, rhs.playerNo)
					.append(gameKey, rhs.gameKey).isEquals();
		}

	}

	public static boolean mouseIntersects(Rectangle box) {
		return mouseBox.intersects(box);
	}

}
