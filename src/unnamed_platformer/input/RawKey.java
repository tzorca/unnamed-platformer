package unnamed_platformer.input;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.lwjgl.input.Keyboard;

public class RawKey
{
	public enum KeyType {
		GAMEPAD, KEYBOARD_JINPUT
	}

	private KeyType type;
	private int code;
	private int controllerIndex;

	public RawKey(KeyType type, int keyCode) {
		this.type = type;
		this.code = keyCode;
	}

	public RawKey(KeyType type, int controllerIndex, int keyCode) {
		this.type = type;
		this.controllerIndex = controllerIndex;
		this.code = keyCode;
	}

	public static RawKey fromAWTKeyCode(int keycode) {
		return new RawKey(KeyType.KEYBOARD_JINPUT,
				KeyCodeTranslator.awtCodeToJInputCode(keycode));
	}

	public String toString() {
		switch (type) {
		case GAMEPAD:
			return GAMEPAD_PREFIX + controllerIndex + "_" + code;
		case KEYBOARD_JINPUT:
			return Keyboard.getKeyName(code);
		default:
			return "UNKNOWN_KEY";
		}
	}

	private final static String GAMEPAD_SEPARATOR = "_";
	private final static String GAMEPAD_PREFIX = "JOY" + GAMEPAD_SEPARATOR;

	public static RawKey fromString(String keyName) throws Exception {
		int jinputKeyIndex = Keyboard.getKeyIndex(keyName);
		if (jinputKeyIndex > 0) {
			return new RawKey(KeyType.KEYBOARD_JINPUT, jinputKeyIndex);
		} else if (keyName.startsWith(GAMEPAD_PREFIX)) {
			String[] parts = keyName.split(GAMEPAD_SEPARATOR);

			if (parts.length != 3) {
				throw new Exception("Invalid key name " + keyName);
			}

			return new RawKey(KeyType.GAMEPAD, Integer.parseInt(parts[1]),
					Integer.parseInt(parts[2]));
		} else {
			throw new Exception("Invalid key name " + keyName);
		}
	}

	public int hashCode() {
		// two randomly chosen prime numbers
		return new HashCodeBuilder(3, 13).append(type).append(controllerIndex)
				.append(code).toHashCode();
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof RawKey))
			return false;

		RawKey rhs = (RawKey) obj;
		return new EqualsBuilder()
		/* */.append(type, rhs.type)
		/* */.append(controllerIndex, rhs.controllerIndex)
		/* */.append(code, rhs.code)
		/* */.isEquals();
	}

}
