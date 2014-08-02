package unnamed_platformer.game;

import java.util.List;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.google.common.collect.Lists;

import unnamed_platformer.app.GameManager;
import unnamed_platformer.app.InputManager;
import unnamed_platformer.app.Main;
import unnamed_platformer.app.Main.State;
import unnamed_platformer.app.MathHelper;
import unnamed_platformer.app.ViewManager;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.gui.GUIManager;
import unnamed_platformer.gui.objects.ImageListEntry;

public class Editor {

	// TODO: Fix random generation broken bug
	// TODO: Fix collision bug with complicated object size/shape

	public static int gridSize = 32;
	Level currentLevel;
	int currentLevelIndex = 0;
	boolean playerAdded = false;

	Vector2f cameraPos = new Vector2f(Ref.DEFAULT_LEVEL_GRIDSIZE * 4, Ref.DEFAULT_LEVEL_GRIDSIZE * 4);

	public Editor(int levelIndex) {
		changeLevel(levelIndex);
		currentLevel.setSize(Ref.DEFAULT_LEVEL_RECTANGLE);
	}

	// returns the level index after the change
	public boolean changeLevel(int index) {
		if (!GameManager.levelExists(index)) {
			return false;
		}

		GameManager.changeLevelToIndex(index);
		currentLevel = GameManager.getCurrentLevel();
		currentLevelIndex = index;

		Entity player = currentLevel.findEntityByFlag(Flag.player);
		if (player != null) {
			cameraPos = player.getCenter();
			playerAdded = true;
		} else {
			playerAdded = false;
		}

		return true;
	}

	public void placeObject(Vector2f location, ImageListEntry imageListEntry) {
		if (Main.state != State.Edit) {
			return;
		}

		if (multiselect == null) {
			Vector2f v = MathHelper.snapToGrid(location, gridSize);
			_placeObject(v, imageListEntry);
		} else {
			for (Vector2f v : multiselect.getLastLocations()) {
				_placeObject(v, imageListEntry);
			}
		}

	}

	public void _placeObject(Vector2f v, ImageListEntry imageListEntry) {

		if (!currentLevel.getRect().includes(v.x, v.y) && !currentLevel.getRect().contains(v.x, v.y)) {
			return;
		}

		String currentTextureName = imageListEntry.getInternalName();

		Entity newEntity = EntityCreator.create(currentTextureName, v);

		if (newEntity == null) {
			return;
		}

		if (newEntity.isFlagSet(Flag.player)) {
			// TODO: Allow changing player spawn position by
			// simply removing the old one first
			if (playerAdded) {
				return;
			}
			playerAdded = true;
		}

		currentLevel.addEntity(newEntity);
	}

	public void removeObject(Vector2f location) {
		if (Main.state != State.Edit) {
			return;
		}

		Entity atMouse = currentLevel.getTopmostEntity(location);

		if (atMouse != null) {
			currentLevel.removeEntity(atMouse);

			if (atMouse.isFlagSet(Flag.player)) {
				playerAdded = false;
			}
		}
	}

	public void switchToEditMode() {
		currentLevel.resetToOriginal();

		GUIManager.setStateHeld(false);
		Main.state = State.Edit;
	}

	public void switchToPlayMode() {
		currentLevel.resetToCurrent();

		GUIManager.setStateHeld(true);
		Main.state = State.Play;

	}

	public void resetToEditPlacement() {
		currentLevel.resetToCurrent();

	}

	public void tryMoveCamera(Vector2f cameraDelta) {

		Rectangle cameraBounds = currentLevel.getRect();
		cameraBounds.setX(cameraBounds.getX() - Display.getWidth() / 4f);
		cameraBounds.setWidth(cameraBounds.getWidth() + Display.getWidth() / 2f);
		cameraBounds.setY(cameraBounds.getY() - Display.getHeight() / 4f);
		cameraBounds.setHeight(cameraBounds.getHeight() + Display.getHeight() / 2f);

		float origX = cameraPos.x;
		cameraPos.x += cameraDelta.x;
		if (!cameraBounds.contains(cameraPos.x, cameraPos.y)) {
			cameraPos.x = origX;
		}

		float origY = cameraPos.y;
		cameraPos.y += cameraDelta.y;
		if (!cameraBounds.contains(cameraPos.x, cameraPos.y)) {
			cameraPos.y = origY;
		}
	}

	public void update() {
		ViewManager.centerCamera(cameraPos);
	}

	public boolean levelInc(int i) {
		return changeLevel(currentLevelIndex + i);
	}

	public boolean removeLevel() {
		int levelIndexToRemove = currentLevelIndex;
		int prevLevelIndex = currentLevelIndex - 1;
		if (changeLevel(prevLevelIndex)) {
			GameManager.removeLevelByIndex(levelIndexToRemove);
			return true;
		}
		return false;
	}

	private Multiselect multiselect = null;

	public void startMultiselect(Vector2f origin) {
		multiselect = new Multiselect(origin);
	}

	protected class Multiselect {
		private Vector2f origin;

		public Multiselect(Vector2f origin) {
			this.origin = origin;
		}

		public Vector2f getOrigin() {
			return origin;
		}

		public List<Vector2f> getLastLocations() {
			return lastLocations;
		}

		private List<Vector2f> lastLocations;

		public List<Vector2f> getLocations(Vector2f dest, int xSeparation, int ySeparation) {
			Rectangle rect = MathHelper.getEnclosingRect(MathHelper.snapToGrid(origin, gridSize),
					MathHelper.snapToGrid(dest, gridSize));

			int minX = (int) rect.getMinX();
			int maxX = (int) rect.getMaxX();
			int minY = (int) rect.getMinY();
			int maxY = (int) rect.getMaxY();

			List<Vector2f> locations = Lists.newArrayList();
			for (int x = minX; x <= maxX; x += xSeparation) {
				for (int y = minY; y <= maxY; y += ySeparation) {
					locations.add(new Vector2f(x, y));
				}
			}

			lastLocations = locations;
			return locations;
		}
	}

	public List<Vector2f> getPaintDrawLocations(int xSeparation, int ySeparation) {
		Vector2f gridMousePos = MathHelper.snapToGrid(InputManager.getGameMousePos(), gridSize);

		return multiselect == null ? Lists.newArrayList(gridMousePos) : multiselect.getLocations(gridMousePos,
				xSeparation, ySeparation);

	}

	public void exitMultiselect() {
		multiselect = null;
	}
}
