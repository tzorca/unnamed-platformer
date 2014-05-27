package unnamed_platformer.gui;

import org.lwjgl.opengl.GL11;

import unnamed_platformer.app.App;
import unnamed_platformer.app.App.State;
import unnamed_platformer.app.ClassLookup;
import unnamed_platformer.app.ViewManager;
import unnamed_platformer.game.parameters.GUIRef;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.nulldevice.NullSoundDevice;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.renderer.lwjgl.input.LwjglInputSystem;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.TimeProvider;

@SuppressWarnings("deprecation")
public class GUIManager {
	private static Nifty nifty;
	private static ZScreen screen;

	public static void init() {
		LwjglInputSystem inputSystem = new LwjglInputSystem();
		try {
			inputSystem.startup();
		} catch (Exception e) {
			e.printStackTrace();
		}
		nifty = new Nifty(new LwjglRenderDevice(), new NullSoundDevice(),
				inputSystem, new TimeProvider());

		nifty.setIgnoreKeyboardEvents(false);
		nifty.fromXml("res/gui/gui.xml", "Start");
		
		screenChange();
	}

	private static State lastState = State.Start;
	private static boolean stateHeld = false;

	public static boolean isStateHeld() {
		return stateHeld;
	}

	public static void setStateHeld(boolean holdState) {
		GUIManager.stateHeld = holdState;
	}

	public static void update() {
		if (App.state != lastState && !stateHeld) {
			screenChange();
			lastState = App.state;
		}

		ViewManager.saveState();

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, ViewManager.width, ViewManager.height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		// update nifty
		nifty.update();

		// render Nifty (this will change OpenGL state)
		nifty.render(false);

		ViewManager.loadState();

		((GUI_Template) getCurrentScreen().getScreenController()).update();
	}

	private static void screenChange() {
		nifty.gotoScreen(App.state.toString());

		String className = "Screen_" + App.state.toString();

		if (ClassLookup.classExists(GUIRef.PACKAGE_NAME, className)) {
			screen = (ZScreen) ClassLookup.instantiate(GUIRef.PACKAGE_NAME,
					className);
		} else {
			screen = (ZScreen) ClassLookup.instantiate(GUIRef.PACKAGE_NAME,
					"BaseScreen_Render");
		}
		ViewManager.setGUIPanel(screen.getPanel());
	}

	public static Nifty getNifty() {
		return nifty;
	}

	public static Screen getCurrentScreen() {
		return nifty.getCurrentScreen();
	}

	public static Element build(ElementBuilder elementBuilder, String parentID) {
		Screen cScr = getCurrentScreen();
		return elementBuilder.build(nifty, cScr,
				cScr.findElementByName(parentID));
	}

	public static Element findElement(String ID) {
		return getCurrentScreen().findElementByName(ID);
	}

	public static NiftyImage getImage(String filename) {
		return nifty.createImage(filename, false);
	}

	// this is REQUIRED
	public static void pushKeyEvent(int eventKey, char eventCharacter,
			boolean eventKeyState) {

		KeyboardInputEvent ie = new KeyboardInputEvent(eventKey,
				eventCharacter, eventKeyState, false, false);

		if (ie != null) {
			getCurrentScreen().keyEvent(ie);

		}
	}

}
