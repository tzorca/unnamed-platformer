package unnamed_platformer.gui;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.app.App;
import unnamed_platformer.app.GameManager;
import unnamed_platformer.app.InputManager;
import unnamed_platformer.app.LevelManager;
import unnamed_platformer.app.ViewManager;
import unnamed_platformer.app.App.State;
import unnamed_platformer.game.Level;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.logic.EntityCreator;
import unnamed_platformer.game.logic.MathHelper;
import unnamed_platformer.game.parameters.Ref;
import unnamed_platformer.game.parameters.InputRef.GameKey;
import unnamed_platformer.game.parameters.Ref.Flag;
import unnamed_platformer.game.structures.FlColor;
import unnamed_platformer.game.structures.Graphic;
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
	static List<Graphic> entityPlaceholderGraphics = new ArrayList<Graphic>();
	boolean playerAdded = false;

	Vector2f cameraPos = new Vector2f(Ref.DEFAULT_LEVEL_GRIDSIZE * 4,
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

	private void loadEntityPlaceholderGraphics() {

		lstTextureNames.clear();
		for (String textureName : EntityCreator.listTextureNames()) {
			lstTextureNames.addItem(textureName);
			entityPlaceholderGraphics.add(new Graphic(textureName, transColor));
		}

		lstTextureNames.setFocusItemByIndex(0);
		lstTextureNames.selectItemByIndex(0);
	}

	@Override
	public void onStartScreen() {
		hookControls();

		loadEntityPlaceholderGraphics();

		changeLevel(0);

	}

	public static Graphic getCurrentGraphic() {
		return entityPlaceholderGraphics.get(lstTextureNames
				.getFocusItemIndex());
	}

	public void placeObject() {
		if (App.state != State.Edit) {
			return;
		}

		Vector2f loc = MathHelper.snapToGrid(ViewManager.translateMouse(),
				currentLevel.gridSize);

		if (!currentLevel.getRect().includes(loc.x, loc.y)
				&& !currentLevel.getRect().contains(loc.x, loc.y)) {
			return;
		}

		String currentTextureName = lstTextureNames.getFocusItem();

		Entity newEntity = EntityCreator.create(currentTextureName, loc);

		if (newEntity == null) {
			return;
		}

		if (newEntity.isFlagSet(Flag.player)) {
			if (playerAdded) {
				return;
			}
			playerAdded = true;
		}

		currentLevel.addEntity(newEntity);
	}

	private void processControls() {

		processCameraControls();

		processGridControls();
	}

	private void processGridControls() {

		int newGridSize = currentLevel.gridSize;
		if (InputManager.getGameKeyState(GameKey.scrollIn, 1)) {
			newGridSize /= 2;
			InputManager.resetGameKey(GameKey.scrollIn, 1);
			if (newGridSize >= 16 && newGridSize <= 128) {
				currentLevel.gridSize = newGridSize;
			}
		} else if (InputManager.getGameKeyState(GameKey.scrollOut, 1)) {
			newGridSize *= 2;
			InputManager.resetGameKey(GameKey.scrollOut, 1);
			if (newGridSize >= 16 && newGridSize <= 128) {
				currentLevel.gridSize = newGridSize;
			}
		}

	}

	private void processCameraControls() {

		Rectangle cameraBounds = currentLevel.getRect();
		cameraBounds.setX(cameraBounds.getX() - ViewManager.width / 4);
		cameraBounds.setWidth(cameraBounds.getWidth() + ViewManager.width / 2);
		cameraBounds.setY(cameraBounds.getY() - ViewManager.height / 4);
		cameraBounds.setHeight(cameraBounds.getHeight() + ViewManager.height
				/ 2);

		float oX = cameraPos.x;

		if (InputManager.getKeyState(Keyboard.KEY_RIGHT)) {
			cameraPos.x += 8;
		}

		if (InputManager.getKeyState(Keyboard.KEY_LEFT)) {
			cameraPos.x -= 8;
		}

		if (!cameraBounds.contains(cameraPos.x, cameraPos.y)) {
			cameraPos.x = oX;
		}

		float oY = cameraPos.y;

		if (InputManager.getKeyState(Keyboard.KEY_UP)) {
			cameraPos.y -= 8;
		}

		if (InputManager.getKeyState(Keyboard.KEY_DOWN)) {
			cameraPos.y += 8;
		}

		if (!cameraBounds.contains(cameraPos.x, cameraPos.y)) {
			cameraPos.y = oY;
		}

	}

	public void removeObject() {
		if (App.state != State.Edit) {
			return;
		}

		Entity atMouse = currentLevel.getTopmostEntity(ViewManager
				.translateMouse());

		if (atMouse != null) {
			currentLevel.removeEntity(atMouse);

			if (atMouse.isFlagSet(Flag.player)) {
				playerAdded = false;
			}
		}
	}

	public void update() {
		if (App.state == State.Edit) {
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

	public static void drawEntityPlaceholder() {

		if (!placeholderVisible || App.state != State.Edit) {
			return;
		}

		Graphic entityPlaceholderGraphic = getCurrentGraphic();

		if (getCurrentGraphic() == null) {
			return;
		}

		Level currentLevel = LevelManager.getCurrentLevel();

		Vector2f loc = MathHelper.snapToGrid(ViewManager.translateMouse(),
				currentLevel.gridSize);

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
			currentLevel.resetToCurrent();

			GUIManager.setStateHeld(true);
			App.state = State.Play;

			NiftyImage newImage = GUIManager
					.getImage("res/gui/img/modeEdit.png");
			pnlModeSwitch.getRenderer(ImageRenderer.class).setImage(newImage);

		} else {
			currentLevel.resetToOriginal();

			GUIManager.setStateHeld(false);
			App.state = State.Edit;

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
		currentLevel.resetToCurrent();
		GameManager.saveCurrentGame();
	}

}