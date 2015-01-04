package unnamed_platformer.input;

import unnamed_platformer.app.TimeManager;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.input.InputManager.PlrGameKey;

public class KeyState
{
	boolean state = false;
	boolean prevState = false;
	boolean pressed = false;

	public void reset() {
		state = false;
		prevState = false;
		pressed = false;
	}

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

	// Pressed is a consumable boolean
	public boolean pressed() {
		if (pressed) {
			pressed = false;
			return true;
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

		if (TimeManager.secondsSince(TimeManager.lastSample(pgk)) > Ref.INPUT_DELAY_TIME) {
			// Check if repeating
			if (TimeManager.periodElapsed(pgk, REPETITION,
					Ref.INPUT_REPEAT_TIME)) {
				return current();
			}
		}

		return false;
	}
}
