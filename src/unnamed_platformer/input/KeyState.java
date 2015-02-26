package unnamed_platformer.input;

import unnamed_platformer.app.TimeManager;
import unnamed_platformer.globals.AppGlobals;
import unnamed_platformer.input.InputManager.PlrGameKey;

public class KeyState
{
	private boolean state = false;
	private boolean prevState = false;
	private boolean pressed = false;
	private boolean disableNextPress = false;

	public void update(boolean state) {
		this.state = state;
		if (state && !prevState) {
			pressed = true;
		}
		prevState = state;
	}

	public boolean current() {
		return state;
	}

	public boolean previous() {
		return prevState;
	}

	public void reset() {
		pressed = false;
	}

	public void disableNextPress() {
		disableNextPress = true;
	}

	// Pressed is a consumable boolean
	public boolean pressed() {
		if (pressed) {
			pressed = false;
			if (disableNextPress) {
				disableNextPress = false;
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private final static String REPETITION = "REPETITION";

	public boolean occurredOrRepeating(PlrGameKey pgk) {
		// Check if occurred
		if (pressed()) {
			TimeManager.sample(pgk);
			return true;
		}

		if (TimeManager.secondsSince(TimeManager.lastSample(pgk)) > AppGlobals.INPUT_DELAY_SECONDS) {
			// Check if repeating
			if (TimeManager.periodElapsed(pgk, REPETITION,
					AppGlobals.INPUT_REPEAT_SECONDS)) {
				return current();
			}
		}

		return false;
	}
}
