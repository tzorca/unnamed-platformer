package app.gui;

import java.util.Properties;
import java.util.Stack;

import model.structures.Callback;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

public class GUI_Popups implements Controller {

	private static Stack<Callback> callbackStack = new Stack<Callback>();
	private static Stack<Element> popupStack = new Stack<Element>();

	public void popInput_btnOkay_Clicked() {

		runCallback(GUIManager.getCurrentScreen().getRootElement()
				.findElementByName("popInput_txtInput")
				.getNiftyControl(TextField.class).getRealText());

		closePopup();
	}

	public void popInput_btnCancel_Clicked() {
		clearCallback();
		closePopup();
	}



	public static void showInputBox(String message, Callback callback) {
		Nifty nifty = GUIManager.getNifty();

		Element popInput = nifty.createPopup("popInput");

		Element lblMessage = popInput.findElementByName("popInput_lblMessage");
		Element txtInput = popInput.findElementByName("popInput_txtInput");

		lblMessage.getRenderer(TextRenderer.class).setText(message);

		callbackStack.push(callback);
		popupStack.push(popInput);

		nifty.showPopup(nifty.getCurrentScreen(), popInput.getId(), txtInput);
	}

	
	private void runCallback(Object param) {
		if (!callbackStack.isEmpty()) {
			callbackStack.pop().execute(param);
		}
	}

	private void clearCallback() {
		callbackStack.pop();
	}

	private void closePopup() {
		if (!popupStack.isEmpty()) {
			GUIManager.getNifty().closePopup(popupStack.pop().getId());
		}
	}
	
	@Override
	public void bind(Nifty nifty, Screen screen, Element element,
			Properties parameter, Attributes controlDefinitionAttributes) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(Properties parameter,
			Attributes controlDefinitionAttributes) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFocus(boolean getFocus) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean inputEvent(NiftyInputEvent inputEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub

	}

}
