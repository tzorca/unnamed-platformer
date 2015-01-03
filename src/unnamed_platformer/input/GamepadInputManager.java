package unnamed_platformer.input;

import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

import unnamed_platformer.input.RawKey.KeyType;

import com.google.common.collect.Maps;

public class GamepadInputManager
{

	public static void update() {
		Controllers.poll();

		Map<RawKey, Boolean> keyStates = getGamepadKeyStates();

		for (Entry<RawKey, Boolean> keyState : keyStates.entrySet()) {
			RawKey key = keyState.getKey();
			Boolean state = keyState.getValue();
			InputManager.linkKeyState(key, state);
		}

	}

	private static final int DPAD_LEFT = 100;
	private static final int DPAD_RIGHT = 101;
	private static final int DPAD_UP = 102;
	private static final int DPAD_DOWN = 103;

	private static final int AXIS_BASE = 200;
	private static final int AXIS_IDX_MUL = 2;
	private static final int AXIS_POSITIVE = 1;
	private static final int AXIS_NEGATIVE = 0;

	private static final float ANALOG_DIGITAL_DEADZONE = 0.4f;

	private static Map<Integer, Float> axisInitialValues = Maps.newHashMap();
	private static Map<Integer, Boolean> axisMoved = Maps.newHashMap();

	public static Map<RawKey, Boolean> getGamepadKeyStates() {
		Map<RawKey, Boolean> keyStates = Maps.newHashMap();

		for (int idx = 0; idx < Controllers.getControllerCount(); idx++) {
			Controller gamepad = Controllers.getController(idx);

			// Keys
			for (int btn = 0; btn < gamepad.getButtonCount(); btn++) {
				RawKey key = new RawKey(KeyType.GAMEPAD, idx, btn);
				keyStates.put(key, gamepad.isButtonPressed(btn));
			}

			// D-Pad
			keyStates.put(new RawKey(KeyType.GAMEPAD, idx, DPAD_LEFT),
					gamepad.getPovX() < 0);
			keyStates.put(new RawKey(KeyType.GAMEPAD, idx, DPAD_RIGHT),
					gamepad.getPovX() > 0);
			keyStates.put(new RawKey(KeyType.GAMEPAD, idx, DPAD_UP),
					gamepad.getPovY() < 0);
			keyStates.put(new RawKey(KeyType.GAMEPAD, idx, DPAD_DOWN),
					gamepad.getPovY() > 0);

			// Axes
			for (int axis = 0; axis < gamepad.getAxisCount(); axis++) {
				float value = gamepad.getAxisValue(axis);

				RawKey positiveAxis = new RawKey(KeyType.GAMEPAD, idx,
						AXIS_BASE + axis * AXIS_IDX_MUL + AXIS_POSITIVE);
				RawKey negativeAxis = new RawKey(KeyType.GAMEPAD, idx,
						AXIS_BASE + axis * AXIS_IDX_MUL + AXIS_NEGATIVE);

				// We want to only notice axes values after first moved.
				// On some systems, axes appear to be -1 at start and
				// are corrected only after moving.
				int axisID = axis + (idx*100);
				if (!axisInitialValues.containsKey(axisID)) {
					axisInitialValues.put(axisID, value);
					axisMoved.put(axisID, false);
					keyStates.put(positiveAxis, false);
					keyStates.put(negativeAxis, false);
				} else {
					if (axisInitialValues.get(axisID) != value) {
						axisMoved.put(axisID, true);
					}
					
					if (axisMoved.get(axisID)) {
						keyStates.put(negativeAxis, value > ANALOG_DIGITAL_DEADZONE);
						keyStates.put(positiveAxis, value < -ANALOG_DIGITAL_DEADZONE);
					}
				}

			}
		}
		return keyStates;
	}

	public static void init() {
		try {
			Controllers.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void finish() {
		Controllers.destroy();
	}
}
