package unnamed_platformer.app;

import java.awt.Desktop.Action;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import unnamed_platformer.game.parameters.InputRef;
import unnamed_platformer.game.parameters.InputRef.GameKey;
import unnamed_platformer.gui.GUIManager;

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

	public static Point getMousePos() {
		return new Point(Mouse.getX(), ViewManager.height - Mouse.getY());
	}

	public boolean leftMouseClicked() {
		return leftMouseClicked;
	}

	public static boolean leftMouseDown;
	private static boolean leftMouseClicked;

	private static Rectangle mouseBox = new Rectangle();

	public static void update() {
		getMouseActions();
		getKeys();
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
		// if LWJGL has keys for us
		while (Keyboard.next()) {
			int keycode = Keyboard.getEventKey();
			boolean state = Keyboard.getEventKeyState();
			setKey(keycode, state);
		}
	}

	private static void setKey(int keycode, boolean state) {
		rawKeyStates.put(keycode, state);
		if (rawKeyToPlayerGameKeyMapping.containsKey(keycode)) {
			playerGameKeyStates.put(rawKeyToPlayerGameKeyMapping.get(keycode),
					state);
		}
		GUIManager.pushKeyEvent(keycode, Keyboard.getEventCharacter(), state);
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

	public static boolean leftMouseWasClicked() {
		return leftMouseClicked;
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
