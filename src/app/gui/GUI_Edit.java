package app.gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import model.Level;
import model.Ref;
import model.Ref.Flag;
import model.entities.Entity;
import model.logic.EntityCreator;
import model.logic.MathHelper;
import model.structures.FlColor;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import app.App;
import app.App.State;
import app.ContentManager;
import app.ContentManager.ContentType;
import app.GameManager;
import app.InputManager;
import app.InputManager.GameKey;
import app.LevelManager;
import app.ViewManager;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;

public class GUI_Edit extends GUI_Template {

	Level currentLevel;
	int currentLevelIndex = 0;
	static List<Texture> textures = new ArrayList<Texture>();
	boolean playerAdded = false;

	Point cameraPos = new Point(Ref.DEFAULT_LEVEL_GRIDSIZE * 4,
			Ref.DEFAULT_LEVEL_GRIDSIZE * 4);

	private boolean changeLevel(int index) {
		if (!LevelManager.levelExists(index)) {
			return false;
		}

		LevelManager.changeLevel(index);
		currentLevel = LevelManager.getCurrentLevel();
		currentLevelIndex = index;
		lblCurrentLevel.setText("" + index);

		Entity player = currentLevel.findEntityByFlag(Flag.player);
		if (player != null) {
			cameraPos = player.getCenter();
			playerAdded = true;
		} else {
			playerAdded = false;
		}

		return true;
	}

	private void loadTextures() {

		lstObjects.clear();
		for (String texName : ContentManager.list(ContentType.texture, true)) {
			if (!EntityCreator.hasMapping(texName)) {
				continue;
			}
			lstObjects.addItem(texName);
			textures.add((Texture) (ContentManager.get(ContentType.texture,
					texName)));
		}

		lstObjects.setFocusItemByIndex(0);
		lstObjects.selectItemByIndex(0);
	}

	@Override
	public void onStartScreen() {
		hookControls();

		loadTextures();

		changeLevel(0);

	}

	public static Texture getCurrentTexture() {

		if (lstObjects == null) {
			return null;
		}
		return textures.get(lstObjects.getFocusItemIndex());
	}

	public void placeObject() {
		if (App.state != State.edit) {
			return;
		}
		
		Point loc = MathHelper.snapToGrid(ViewManager.translateMouse(),
				currentLevel.gridSize);

		if (!currentLevel.getRect().contains(loc)) {
			return;
		}

		String currentTextureName = lstObjects.getFocusItem();

		Entity newEntity = EntityCreator.create(currentTextureName, loc);

		if (newEntity == null) {
			return;
		}

		if (newEntity.checkFlag(Flag.player)) {
			if (playerAdded) {
				return;
			}
			playerAdded = true;
		}

		currentLevel.addEntity(newEntity, true);
	}

	private void processControls() {

		processCameraControls();

		processGridControls();
	}

	private void processGridControls() {

		int newGridSize = currentLevel.gridSize;
		if (InputManager.getGameKeyState(GameKey.scrollIn, 1)) {
			newGridSize /= 2;
			InputManager.resetGameKeyState(GameKey.scrollIn, 1);
			if (newGridSize >= 16 && newGridSize <= 128) {
				currentLevel.gridSize = newGridSize;
			}
		} else if (InputManager.getGameKeyState(GameKey.scrollOut, 1)) {
			newGridSize *= 2;
			InputManager.resetGameKeyState(GameKey.scrollOut, 1);
			if (newGridSize >= 16 && newGridSize <= 128) {
				currentLevel.gridSize = newGridSize;
			}
		}

	}

	private void processCameraControls() {
		
		Rectangle cameraBounds = currentLevel.getRect();
		cameraBounds.x -= ViewManager.width / 4;
		cameraBounds.width += ViewManager.width / 2;
		cameraBounds.y -= ViewManager.height / 4;
		cameraBounds.height += ViewManager.height / 2;

		int oX = cameraPos.x;

		if (InputManager.getKeyState(Keyboard.KEY_RIGHT)) {
			cameraPos.x += 8;
		}

		if (InputManager.getKeyState(Keyboard.KEY_LEFT)) {
			cameraPos.x -= 8;
		}

		if (!cameraBounds.contains(cameraPos)) {
			cameraPos.x = oX;
		}

		int oY = cameraPos.y;

		if (InputManager.getKeyState(Keyboard.KEY_UP)) {
			cameraPos.y -= 8;
		}

		if (InputManager.getKeyState(Keyboard.KEY_DOWN)) {
			cameraPos.y += 8;
		}

		if (!cameraBounds.contains(cameraPos)) {
			cameraPos.y = oY;
		}

	}

	public void removeObject() {
		if (App.state != State.edit) {
			return;
		}
		
		Entity atMouse = currentLevel.getTopmostEntity(ViewManager
				.translateMouse());

		if (atMouse != null) {
			currentLevel.removeEntity(atMouse);

			if (atMouse.checkFlag(Flag.player)) {
				playerAdded = false;
			}
		}
	}

	public void update() {
		if (App.state == State.edit) {
			ViewManager.centerCamera(cameraPos);

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

	public void main_MouseOver() {
		placeholderVisible = true;
	}

	public void toolbar_MouseOver() {
		placeholderVisible = false;
	}

	static boolean placeholderVisible = false;

	static FlColor transColor = new FlColor(1, 1, 1, 0.75f);

	public static void drawPlaceholderElement() {

		if (!placeholderVisible || App.state != State.edit) {
			return;
		}
		Level currentLevel = LevelManager.getCurrentLevel();

		Point loc = MathHelper.snapToGrid(ViewManager.translateMouse(),
				currentLevel.gridSize);

		Texture t = getCurrentTexture();
		if (t == null) {
			return;
		}
		t.bind();
		Rectangle texRect = new Rectangle(loc.x, loc.y, t.getImageWidth(),
				t.getImageHeight());

		if (currentLevel.getRect().contains(texRect)) {
			ViewManager.drawTexture(t, texRect, transColor);
		} else {
			ViewManager.setColor(transColor);
		}
	}

	// private void drawCamera() {
	// Rectangle texRect = new Rectangle(cameraPos.x, cameraPos.y, 32, 32);
	// ViewManager.drawTexture(cameraTexture, texRect, noTransColor);
	// }

	Label lblCurrentLevel;
	Element pnlModeSwitch, pnlPrevLevl, pnlAddLevel, pnlNextLevel,
			pnlRemoveLevel;
	List<Element> editModeElements = new ArrayList<Element>();
	static ListBox<String> lstObjects;

	@SuppressWarnings("unchecked")
	private void hookControls() {
		lstObjects = GUIManager.findElement("lstObjects").getNiftyControl(
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

		if (App.state == State.edit) {
			currentLevel.overwriteOriginalWithCurrent();
			
			GUIManager.setStateHeld(true);
			App.state = State.play;

			NiftyImage newImage = GUIManager
					.getImage("res/gui/img/modeEdit.png");
			pnlModeSwitch.getRenderer(ImageRenderer.class).setImage(newImage);

		} else {
			currentLevel.reset();

			GUIManager.setStateHeld(false);
			App.state = State.edit;

			NiftyImage newImage = GUIManager
					.getImage("res/gui/img/modePlay.png");
			pnlModeSwitch.getRenderer(ImageRenderer.class).setImage(newImage);
		}

	}

	@NiftyEventSubscriber(id = "lstObjects")
	public void onListBoxSelectionChanged(final String id,
			final ListBoxSelectionChangedEvent<String> event) {

		// prevent edit camera controls from interfering with selection
		lstObjects.setFocusable(false);
	}

	public void pnlAddLevel_Clicked() {
		GameManager.addBlankLevel();
		changeLevel(LevelManager.getLevelCount() - 1);
	}

	public void pnlNextLevel_Clicked() {
		changeLevel(currentLevelIndex + 1);
	}

	public void pnlPrevLevel_Clicked() {
		changeLevel(currentLevelIndex - 1);
	}

	public void pnlRemoveLevel_Clicked() {
		int levelIndexToRemove = currentLevelIndex;
		if (changeLevel(currentLevelIndex - 1)) {
			LevelManager.removeLevel(levelIndexToRemove);
		}
	}

	public void pnlSave_Clicked() {
		GameManager.saveCurrentGame();
	}

}
