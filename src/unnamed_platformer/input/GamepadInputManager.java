package unnamed_platformer.input;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

public class GamepadInputManager
{
	static List<Controller> gamepads = new ArrayList<Controller>();

	public static void update() {
	}

	public static void init() {
		try {
			Controllers.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < Controllers.getControllerCount(); i++) {
			gamepads.add(Controllers.getController(i));
		}

		// for (Controller gamepad : gamepads) {
		// System.out.println(gamepad.getName());
		// }
	}

}
