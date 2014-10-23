package unnamed_platformer.app;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;

/**
 * Translates LWJGL/JInput keycodes to Java KeyEvent keycodes. Allows
 * InputManager to use the same keyboard bindings across LWJGL and Swing.
 */
public class KeyCodeTranslator
{

	private final static HashMap<Integer, Integer> translationTable = new HashMap<Integer, Integer>();
	static {
		translationTable.put(KeyEvent.VK_0, Keyboard.KEY_0);
		translationTable.put(KeyEvent.VK_1, Keyboard.KEY_1);
		translationTable.put(KeyEvent.VK_2, Keyboard.KEY_2);
		translationTable.put(KeyEvent.VK_3, Keyboard.KEY_3);
		translationTable.put(KeyEvent.VK_4, Keyboard.KEY_4);
		translationTable.put(KeyEvent.VK_5, Keyboard.KEY_5);
		translationTable.put(KeyEvent.VK_6, Keyboard.KEY_6);
		translationTable.put(KeyEvent.VK_7, Keyboard.KEY_7);
		translationTable.put(KeyEvent.VK_8, Keyboard.KEY_8);
		translationTable.put(KeyEvent.VK_9, Keyboard.KEY_9);
		translationTable.put(KeyEvent.VK_A, Keyboard.KEY_A);
		translationTable.put(KeyEvent.VK_ADD, Keyboard.KEY_ADD);
		translationTable.put(KeyEvent.VK_AT, Keyboard.KEY_AT);
		translationTable.put(KeyEvent.VK_B, Keyboard.KEY_B);
		translationTable.put(KeyEvent.VK_BACK_SLASH, Keyboard.KEY_BACKSLASH);
		translationTable.put(KeyEvent.VK_C, Keyboard.KEY_C);
		translationTable.put(KeyEvent.VK_CLEAR, Keyboard.KEY_CLEAR);
		translationTable.put(KeyEvent.VK_COLON, Keyboard.KEY_COLON);
		translationTable.put(KeyEvent.VK_COMMA, Keyboard.KEY_COMMA);
		translationTable.put(KeyEvent.VK_D, Keyboard.KEY_D);
		translationTable.put(KeyEvent.VK_DECIMAL, Keyboard.KEY_DECIMAL);
		translationTable.put(KeyEvent.VK_DELETE, Keyboard.KEY_DELETE);
		translationTable.put(KeyEvent.VK_DIVIDE, Keyboard.KEY_DIVIDE);
		translationTable.put(KeyEvent.VK_DOWN, Keyboard.KEY_DOWN);
		translationTable.put(KeyEvent.VK_E, Keyboard.KEY_E);
		translationTable.put(KeyEvent.VK_END, Keyboard.KEY_END);
		translationTable.put(KeyEvent.VK_EQUALS, Keyboard.KEY_EQUALS);
		translationTable.put(KeyEvent.VK_ESCAPE, Keyboard.KEY_ESCAPE);
		translationTable.put(KeyEvent.VK_F, Keyboard.KEY_F);
		translationTable.put(KeyEvent.VK_F1, Keyboard.KEY_F1);
		translationTable.put(KeyEvent.VK_F10, Keyboard.KEY_F10);
		translationTable.put(KeyEvent.VK_F11, Keyboard.KEY_F11);
		translationTable.put(KeyEvent.VK_F12, Keyboard.KEY_F12);
		translationTable.put(KeyEvent.VK_F13, Keyboard.KEY_F13);
		translationTable.put(KeyEvent.VK_F14, Keyboard.KEY_F14);
		translationTable.put(KeyEvent.VK_F15, Keyboard.KEY_F15);
		translationTable.put(KeyEvent.VK_F16, Keyboard.KEY_F16);
		translationTable.put(KeyEvent.VK_F17, Keyboard.KEY_F17);
		translationTable.put(KeyEvent.VK_F18, Keyboard.KEY_F18);
		translationTable.put(KeyEvent.VK_F19, Keyboard.KEY_F19);
		translationTable.put(KeyEvent.VK_F2, Keyboard.KEY_F2);
		translationTable.put(KeyEvent.VK_F3, Keyboard.KEY_F3);
		translationTable.put(KeyEvent.VK_F4, Keyboard.KEY_F4);
		translationTable.put(KeyEvent.VK_F5, Keyboard.KEY_F5);
		translationTable.put(KeyEvent.VK_F6, Keyboard.KEY_F6);
		translationTable.put(KeyEvent.VK_F7, Keyboard.KEY_F7);
		translationTable.put(KeyEvent.VK_F8, Keyboard.KEY_F8);
		translationTable.put(KeyEvent.VK_F9, Keyboard.KEY_F9);
		translationTable.put(KeyEvent.VK_G, Keyboard.KEY_G);
		translationTable.put(KeyEvent.VK_H, Keyboard.KEY_H);
		translationTable.put(KeyEvent.VK_HOME, Keyboard.KEY_HOME);
		translationTable.put(KeyEvent.VK_I, Keyboard.KEY_I);
		translationTable.put(KeyEvent.VK_INSERT, Keyboard.KEY_INSERT);
		translationTable.put(KeyEvent.VK_J, Keyboard.KEY_J);
		translationTable.put(KeyEvent.VK_K, Keyboard.KEY_K);
		translationTable.put(KeyEvent.VK_L, Keyboard.KEY_L);
		translationTable.put(KeyEvent.VK_OPEN_BRACKET, Keyboard.KEY_LBRACKET);
		translationTable.put(KeyEvent.VK_CONTROL, Keyboard.KEY_LCONTROL);
		translationTable.put(KeyEvent.VK_LEFT, Keyboard.KEY_LEFT);
		translationTable.put(KeyEvent.VK_CONTEXT_MENU, Keyboard.KEY_LMENU);
		translationTable.put(KeyEvent.VK_SHIFT, Keyboard.KEY_LSHIFT);
		translationTable.put(KeyEvent.VK_M, Keyboard.KEY_M);
		translationTable.put(KeyEvent.VK_MINUS, Keyboard.KEY_MINUS);
		translationTable.put(KeyEvent.VK_MULTIPLY, Keyboard.KEY_MULTIPLY);
		translationTable.put(KeyEvent.VK_N, Keyboard.KEY_N);
		translationTable.put(KeyEvent.VK_NUM_LOCK, Keyboard.KEY_NUMLOCK);
		translationTable.put(KeyEvent.VK_NUMPAD0, Keyboard.KEY_NUMPAD0);
		translationTable.put(KeyEvent.VK_NUMPAD1, Keyboard.KEY_NUMPAD1);
		translationTable.put(KeyEvent.VK_NUMPAD2, Keyboard.KEY_NUMPAD2);
		translationTable.put(KeyEvent.VK_NUMPAD3, Keyboard.KEY_NUMPAD3);
		translationTable.put(KeyEvent.VK_NUMPAD4, Keyboard.KEY_NUMPAD4);
		translationTable.put(KeyEvent.VK_NUMPAD5, Keyboard.KEY_NUMPAD5);
		translationTable.put(KeyEvent.VK_NUMPAD6, Keyboard.KEY_NUMPAD6);
		translationTable.put(KeyEvent.VK_NUMPAD7, Keyboard.KEY_NUMPAD7);
		translationTable.put(KeyEvent.VK_NUMPAD8, Keyboard.KEY_NUMPAD8);
		translationTable.put(KeyEvent.VK_NUMPAD9, Keyboard.KEY_NUMPAD9);
		translationTable.put(KeyEvent.VK_O, Keyboard.KEY_O);
		translationTable.put(KeyEvent.VK_P, Keyboard.KEY_P);
		translationTable.put(KeyEvent.VK_PAUSE, Keyboard.KEY_PAUSE);
		translationTable.put(KeyEvent.VK_PERIOD, Keyboard.KEY_PERIOD);
		translationTable.put(KeyEvent.VK_Q, Keyboard.KEY_Q);
		translationTable.put(KeyEvent.VK_R, Keyboard.KEY_R);
		translationTable.put(KeyEvent.VK_CLOSE_BRACKET, Keyboard.KEY_RBRACKET);
		translationTable.put(KeyEvent.VK_CONTROL, Keyboard.KEY_RCONTROL);
		translationTable.put(KeyEvent.VK_ENTER, Keyboard.KEY_RETURN);
		translationTable.put(KeyEvent.VK_RIGHT, Keyboard.KEY_RIGHT);
		translationTable.put(KeyEvent.VK_CONTEXT_MENU, Keyboard.KEY_RMENU);
		translationTable.put(KeyEvent.VK_SHIFT, Keyboard.KEY_RSHIFT);
		translationTable.put(KeyEvent.VK_S, Keyboard.KEY_S);
		translationTable.put(KeyEvent.VK_SEMICOLON, Keyboard.KEY_SEMICOLON);
		translationTable.put(KeyEvent.VK_SLASH, Keyboard.KEY_SLASH);
		translationTable.put(KeyEvent.VK_SPACE, Keyboard.KEY_SPACE);
		translationTable.put(KeyEvent.VK_SUBTRACT, Keyboard.KEY_SUBTRACT);
		translationTable.put(KeyEvent.VK_T, Keyboard.KEY_T);
		translationTable.put(KeyEvent.VK_TAB, Keyboard.KEY_TAB);
		translationTable.put(KeyEvent.VK_U, Keyboard.KEY_U);
		translationTable.put(KeyEvent.VK_UP, Keyboard.KEY_UP);
		translationTable.put(KeyEvent.VK_V, Keyboard.KEY_V);
		translationTable.put(KeyEvent.VK_W, Keyboard.KEY_W);
		translationTable.put(KeyEvent.VK_X, Keyboard.KEY_X);
		translationTable.put(KeyEvent.VK_Y, Keyboard.KEY_Y);
		translationTable.put(KeyEvent.VK_Z, Keyboard.KEY_Z);
	}

	public static Integer translateJavaVKToJInputCode(Integer jInputKeyCode) {
		if (translationTable.containsKey(jInputKeyCode)) {
			return translationTable.get(jInputKeyCode);
		} else {
			// Keycode not in translation table
			return -1;
		}
	}

}
