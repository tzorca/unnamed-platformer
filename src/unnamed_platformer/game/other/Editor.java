package unnamed_platformer.game.other;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.InputManager;
import unnamed_platformer.app.MathHelper;
import unnamed_platformer.app.ViewManager;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.gui.objects.ImageListEntry;

import com.google.common.collect.Lists;

public class Editor {
	public int gridSize = 32;
	Level currentLevel;
	int currentLevelIndex = 0;
	boolean playerAdded = false;

	boolean unsavedChanges = false;

	Vector2f cameraPos = new Vector2f(Ref.DEFAULT_LEVEL_GRIDSIZE * 4,
			Ref.DEFAULT_LEVEL_GRIDSIZE * 4);

	public Editor(int levelIndex) {
		changeLevel(levelIndex);
		currentLevel.setSize(Ref.DEFAULT_LEVEL_RECTANGLE);
	}

	public boolean changeLevel(int index) {
		if (!World.hasLevelIndex(index)) {
			return false;
		}

		World.setLevelByIndex(index);
		currentLevel = World.getCurrentLevel();
		currentLevelIndex = index;

		Entity player = currentLevel.findEntityByFlag(Flag.PLAYER);
		if (player != null) {
			cameraPos = player.getCenter();
			playerAdded = true;
		} else {
			playerAdded = false;
		}

		return true;
	}

	public void placeObject(Vector2f location, ImageListEntry imageListEntry) {
		if (World.playing()) {
			return;
		}

		if (multiselect == null) {
			Vector2f v = MathHelper.snapToGrid(location, gridSize);
			placeObjectAfterChecks(v, imageListEntry);
		} else {
			for (Vector2f v : multiselect.getLastLocations()) {
				placeObjectAfterChecks(v, imageListEntry);
			}
		}

	}

	private void placeObjectAfterChecks(Vector2f v, ImageListEntry imageListEntry) {

		if (!currentLevel.getRect().includes(v.x, v.y)
				&& !currentLevel.getRect().contains(v.x, v.y)) {
			return;
		}
		
		if (imageListEntry == null) {
			return;
		}

		String currentTextureName = imageListEntry.getInternalName();

		Entity newEntity = EntityCreator.create(currentTextureName, v);

		if (newEntity == null) {
			return;
		}

		if (newEntity.isFlagSet(Flag.PLAYER)) {
			if (playerAdded) {
				return;
			}
			playerAdded = true;
		}

		currentLevel.addEntity(newEntity);
		unsavedChanges = true;
	}

	public void removeObject(Vector2f location) {
		if (World.playing()) {
			return;
		}

		Entity atMouse = currentLevel.getTopmostEntity(location);

		if (atMouse != null) {
			currentLevel.removeEntity(atMouse);
			unsavedChanges = true;

			if (atMouse.isFlagSet(Flag.PLAYER)) {
				playerAdded = false;
			}
		}
	}

	public void switchToEditMode() {
		currentLevel.resetToOriginal();
		World.setPlaying(false);
	}

	public void switchToPlayMode() {
		currentLevel.resetToCurrent();
		World.setPlaying(true);
	}

	public void resetToEditPlacement() {
		currentLevel.resetToCurrent();

	}

	public void tryMoveCamera(Vector2f cameraDelta) {

		Rectangle cameraBounds = currentLevel.getRect();
		cameraBounds.setX(cameraBounds.getX() - Display.getWidth() / 4f);
		cameraBounds
				.setWidth(cameraBounds.getWidth() + Display.getWidth() / 2f);
		cameraBounds.setY(cameraBounds.getY() - Display.getHeight() / 4f);
		cameraBounds.setHeight(cameraBounds.getHeight() + Display.getHeight()
				/ 2f);

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
			World.removeLevelByIndex(levelIndexToRemove);
			return true;
		} else {
			int nextLevelIndex = currentLevelIndex + 1;
			if (changeLevel(nextLevelIndex)) {
				World.removeLevelByIndex(levelIndexToRemove);
				return true;
			}
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

		private List<Vector2f> lastLocations = new ArrayList<Vector2f>();

		public List<Vector2f> getLocations(Vector2f dest, int xSeparation,
				int ySeparation) {
			Vector2f snapDest = MathHelper.snapToGrid(dest, gridSize);
			Vector2f snapOrigin = MathHelper.snapToGrid(origin, gridSize);

			int startX = (int) snapOrigin.x;
			int startY = (int) snapOrigin.y;
			int endX = (int) snapDest.x;
			int endY = (int) snapDest.y;

			boolean xCountUp = startX < endX ? true : false;
			boolean yCountUp = startY < endY ? true : false;

			int xIterator = (int) xSeparation * (xCountUp ? 1 : -1);
			int yIterator = (int) ySeparation * (yCountUp ? 1 : -1);

			List<Vector2f> locations = Lists.newArrayList();
			int x = startX;
			while ((xCountUp && x <= endX) || (!xCountUp && x >= endX)) {

				int y = startY;
				while ((yCountUp && y <= endY) || (!yCountUp && y >= endY)) {
					locations.add(new Vector2f(x, y));
					y += yIterator;
				}
				x += xIterator;
			}

			lastLocations = locations;
			return locations;
		}
	}

	public List<Vector2f> getPaintDrawLocations(int xSeparation, int ySeparation) {
		Vector2f gridMousePos = MathHelper.snapToGrid(
				InputManager.getGameMousePos(), gridSize);

		return multiselect == null ? Lists.newArrayList(gridMousePos)
				: multiselect.getLocations(gridMousePos, xSeparation,
						ySeparation);

	}

	public void exitMultiselect() {
		multiselect = null;
	}

	public boolean unsavedChangesExist() {
		return unsavedChanges;
	}

	public void overrideSavedChanges() {
		unsavedChanges = false;
	}

	public void setCameraPos(Vector2f pos) {
		cameraPos = pos;
	}

	public void save() {
		resetToEditPlacement();
		if (World.saveCurrentGame()) {
			unsavedChanges = false;
		}
	}
}
