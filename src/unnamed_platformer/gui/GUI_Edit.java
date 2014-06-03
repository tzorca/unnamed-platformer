package unnamed_platformer.gui;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.app.App;
import unnamed_platformer.app.App.State;
import unnamed_platformer.app.GameManager;
import unnamed_platformer.app.InputManager;
import unnamed_platformer.app.ViewManager;
import unnamed_platformer.game.Level;
import unnamed_platformer.game.logic.Editor;
import unnamed_platformer.game.logic.EntityCreator;
import unnamed_platformer.game.logic.MathHelper;
import unnamed_platformer.game.parameters.InputRef.GameKey;
import unnamed_platformer.game.structures.FlColor;
import unnamed_platformer.game.structures.Graphic;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;

// TODO: Implement background texture selection in editor
public class GUI_Edit extends GUI_Template {

	static List<Graphic> entityPlaceholderGraphics = new ArrayList<Graphic>();

	public static Graphic getCurrentGraphic() {
		return entityPlaceholderGraphics.get(lstTextureNames
				.getFocusItemIndex());
	}

	private void loadEntityPlaceholderGraphics() {

		lstTextureNames.clear();
		for (String textureName : EntityCreator.listTextureNames()) {
			lstTextureNames.addItem(textureName);
			entityPlaceholderGraphics.add(new Graphic(textureName, transColor));
		}

		lstTextureNames.setFocusItemByIndex(0);
		lstTextureNames.selectItemByIndex(0);
	}

	Editor editor;

	@Override
	public void onStartScreen() {
		hookControls();

		loadEntityPlaceholderGraphics();
		editor = new Editor(0);
	}

	@Override
	public void onEndScreen() {
		// TODO: Prompt to save level
	}

	public void update() {
		if (App.state == State.Edit) {
			editor.update();

			processControls();

			for (Element e : editModeElements) {
				if (!e.isVisible()) {
					e.show();
				}
			}
		} else {

			for (Element e : editModeElements) {
				if (e.isVisible()) {
					e.hide();
				}
			}
		}
	}

	private void processControls() {

		processCameraControls();

		processGridControls();
	}

	private void processGridControls() {

		int newGridSize = Editor.gridSize;
		if (InputManager.getGameKeyState(GameKey.scrollIn, 1)) {
			newGridSize /= 2;
			InputManager.resetGameKey(GameKey.scrollIn, 1);
			if (newGridSize >= 8 && newGridSize <= 128) {
				Editor.gridSize = newGridSize;
			}
		} else if (InputManager.getGameKeyState(GameKey.scrollOut, 1)) {
			newGridSize *= 2;
			InputManager.resetGameKey(GameKey.scrollOut, 1);
			if (newGridSize >= 8 && newGridSize <= 128) {
				Editor.gridSize = newGridSize;
			}
		}

	}

	private void processCameraControls() {
		Vector2f cameraDelta = new Vector2f(0, 0);
		if (InputManager.getKeyState(Keyboard.KEY_RIGHT)) {
			cameraDelta.x += 8;
		}

		if (InputManager.getKeyState(Keyboard.KEY_LEFT)) {
			cameraDelta.x -= 8;
		}

		if (InputManager.getKeyState(Keyboard.KEY_UP)) {
			cameraDelta.y -= 8;
		}

		if (InputManager.getKeyState(Keyboard.KEY_DOWN)) {
			cameraDelta.y += 8;
		}

		editor.tryMoveCamera(cameraDelta);

	}

	public void main_MouseOver() {
		placeholderVisible = true;
	}

	public void toolbar_MouseOver() {
		placeholderVisible = false;
	}

	static boolean placeholderVisible = false;

	static FlColor transColor = new FlColor(1, 1, 1, 0.75f);

	public static void drawEntityPlaceholder() {

		if (!placeholderVisible || App.state != State.Edit) {
			return;
		}

		Graphic entityPlaceholderGraphic = getCurrentGraphic();

		if (getCurrentGraphic() == null) {
			return;
		}

		Level currentLevel = GameManager.getCurrentLevel();

		Vector2f loc = MathHelper.snapToGrid(ViewManager.translateMouse(),
				Editor.gridSize);

		Texture t = entityPlaceholderGraphic.getTexture();

		Rectangle2D entityPlaceholderRect = new Rectangle2D.Float(loc.x, loc.y,
				t.getImageWidth(), t.getImageHeight());

		Rectangle2D levelRect = new Rectangle2D.Float(currentLevel.getRect()
				.getX(), currentLevel.getRect().getY(), currentLevel.getRect()
				.getWidth(), currentLevel.getRect().getHeight());

		if (levelRect.contains(entityPlaceholderRect)) {
			ViewManager.drawGraphic(entityPlaceholderGraphic,
					entityPlaceholderRect);
		} else {
			ViewManager.setColor(transColor);
		}
	}

	// hooks into nifty gui event
	public void placeObject() {
		editor.placeObject(ViewManager.translateMouse(),
				lstTextureNames.getFocusItem());
	}

	// hooks into nifty gui event
	public void removeObject() {
		editor.removeObject(ViewManager.translateMouse());
	}

	Label lblCurrentLevel;
	Element pnlModeSwitch, pnlPrevLevl, pnlAddLevel, pnlNextLevel,
			pnlRemoveLevel;
	List<Element> editModeElements = new ArrayList<Element>();
	static ListBox<String> lstTextureNames;

	@SuppressWarnings("unchecked")
	private void hookControls() {
		lstTextureNames = GUIManager.findElement("lstObjects").getNiftyControl(
				ListBox.class);

		lblCurrentLevel = GUIManager.findElement("lblCurrentLevel")
				.getNiftyControl(Label.class);

		pnlModeSwitch = GUIManager.findElement("pnlModeSwitch");

		editModeElements.add(GUIManager.findElement("lstObjects"));
		editModeElements.add(GUIManager.findElement("pnlAddLevel"));
		editModeElements.add(GUIManager.findElement("pnlPrevLevel"));
		editModeElements.add(GUIManager.findElement("pnlNextLevel"));
		editModeElements.add(GUIManager.findElement("pnlRemoveLevel"));
		editModeElements.add(GUIManager.findElement("pnlSave"));
	}

	public void pnlModeSwitch_Clicked() {
		if (App.state == State.Edit) {
			editor.switchToPlayMode();

			NiftyImage newImage = GUIManager
					.getImage("res/gui/img/modeEdit.png");
			pnlModeSwitch.getRenderer(ImageRenderer.class).setImage(newImage);

		} else {
			editor.switchToEditMode();

			NiftyImage newImage = GUIManager
					.getImage("res/gui/img/modePlay.png");
			pnlModeSwitch.getRenderer(ImageRenderer.class).setImage(newImage);
		}

	}

	@NiftyEventSubscriber(id = "lstObjects")
	public void onListBoxSelectionChanged(final String id,
			final ListBoxSelectionChangedEvent<String> event) {

		// prevent edit camera controls from interfering with selection
		lstTextureNames.setFocusable(false);
	}

	public void pnlAddLevel_Clicked() {
		GameManager.addBlankLevel();
		editor.changeLevel(GameManager.getLevelCount() - 1);
		updateCurrentLevelLabel();
	}

	private void updateCurrentLevelLabel() {
		lblCurrentLevel.setText(GameManager.getCurrentLevelNumber() + "");
	}

	public void pnlNextLevel_Clicked() {
		editor.levelInc(1);
		updateCurrentLevelLabel();
	}

	public void pnlPrevLevel_Clicked() {
		editor.levelInc(-1);
		updateCurrentLevelLabel();
	}

	public void pnlRemoveLevel_Clicked() {
		editor.removeLevel();
		updateCurrentLevelLabel();
	}

	public void pnlSave_Clicked() {
		editor.resetToEditPlacement();
		GameManager.saveCurrentGame();
	}

}
