package unnamed_platformer.input;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.util.EnumMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.view.ViewManager;

public class MouseInputManager
{
	private static Rectangle mouseBox = new Rectangle();

	private static EnumMap<MouseButton, Boolean> nowMouseButtonStates = new EnumMap<MouseButton, Boolean>(
			MouseButton.class);
	private static EnumMap<MouseButton, Boolean> prevMouseButtonStates = new EnumMap<MouseButton, Boolean>(
			MouseButton.class);
	private static Point lastMousePos = new Point(0, 0);

	private static EnumMap<MouseEventType, Runnable> eventHandlers = new EnumMap<MouseEventType, Runnable>(
			MouseEventType.class);

	private static PointerInfo pointerInfo;

	public static void init() {
		// get pointer info
		pointerInfo = MouseInfo.getPointerInfo();
	}
	
	public static void setEventHandler(MouseEventType inputEventType,
			Runnable runnable) {
		eventHandlers.put(inputEventType, runnable);
	}

	private static Point getMousePosInWindow() {
		Point mousePos = pointerInfo.getLocation();
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

	private static void detectMouseButtons() {
		nowMouseButtonStates.put(MouseButton.left, Mouse.isButtonDown(0));
		nowMouseButtonStates.put(MouseButton.right, Mouse.isButtonDown(1));

		if (!prevMouseButtonStates.isEmpty()) {
			for (MouseButton button : MouseButton.values()) {
				boolean nowState = nowMouseButtonStates.get(button);
				boolean prevState = prevMouseButtonStates.get(button);

				if (nowState) {
					if (button == MouseButton.left) {
						eventHandlers.get(MouseEventType.leftMouseDown).run();
					} else if (button == MouseButton.right) {
						eventHandlers.get(MouseEventType.rightMouseDown).run();
					}
				}

				if (!nowState && prevState) {
					if (button == MouseButton.left) {
						eventHandlers.get(MouseEventType.leftClick).run();
					} else if (button == MouseButton.right) {
						eventHandlers.get(MouseEventType.rightClick).run();
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
			eventHandlers.get(MouseEventType.mouseMotion).run();
		}

		lastMousePos = mousePos;

		mouseBox = new Rectangle(mousePos.x - 8, mousePos.y - 8, 16, 16);
	}

	public enum MouseEventType {
		leftClick, rightClick, leftMouseDown, rightMouseDown, mouseMotion
	}

	public static void resetEvents() {
		for (MouseEventType iet : MouseEventType.values()) {
			eventHandlers.put(iet, new Runnable() {
				public void run() {
				}
			});
		}
	}

	public static boolean mouseIntersects(Rectangle box) {
		return mouseBox.intersects(box);
	}

	public static void update() {
		detectMouseButtons();
		detectMousePos();
	}

}
