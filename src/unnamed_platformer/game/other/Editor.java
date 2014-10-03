package unnamed_platformer.game.other;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.view.ViewManager;
import unnamed_platformer.view.gui.objects.ImageListEntry;

import com.google.common.collect.Lists;

public class Editor
{
	public int gridSize = 48;
	private boolean playerAdded;

	private boolean unsavedChanges;

	private Multiselect multiselect;

	private Vector2f cameraPos = new Vector2f(Ref.DEFAULT_LEVEL_GRIDSIZE * 4,
			Ref.DEFAULT_LEVEL_GRIDSIZE * 4);

	public Editor(final int levelIndex) {
		changeLevel(levelIndex);
		World.getCurrentLevel().setSize(Ref.DEFAULT_LEVEL_RECTANGLE);
	}

	public boolean changeLevel(final int index) {
		if (!World.hasLevelIndex(index)) {
			return false;
		}

		World.setLevelByIndex(index);

		final Entity player = World.getCurrentLevel().findEntityByFlag(
				Flag.PLAYER);

		if (player == null) {
			playerAdded = false;
		} else {
			cameraPos = player.getCenter();
			playerAdded = true;
		}

		return true;
	}

	public void placeObject(final Vector2f location,
			final ImageListEntry imageListEntry) {
		if (World.playing()) {
			return;
		}

		if (multiselect == null) {
			final Vector2f snappedLocation = MathHelper.snapToGrid(location,
					gridSize);
			placeObjectAfterChecks(snappedLocation, imageListEntry);
		} else {
			for (final Vector2f multiselectLocation : multiselect
					.getLastLocations()) {
				placeObjectAfterChecks(multiselectLocation, imageListEntry);
			}
		}

	}

	private void placeObjectAfterChecks(final Vector2f location,
			final ImageListEntry imageListEntry) {

		if (!World.getCurrentLevel().getRect().includes(location.x, location.y)
				&& !World.getCurrentLevel().getRect()
						.contains(location.x, location.y)) {
			return;
		}

		if (imageListEntry == null) {
			return;
		}

		final String textureName = imageListEntry.getInternalName();
		final Entity newEntity = EntityCreator.create(textureName, location);

		if (newEntity == null) {
			return;
		}

		if (newEntity.isFlagSet(Flag.PLAYER)) {
			if (playerAdded) {
				return;
			}
			playerAdded = true;
		}

		World.getCurrentLevel().addEntity(newEntity);
		unsavedChanges = true;
	}

	public void removeObject(final Vector2f location) {
		if (World.playing()) {
			return;
		}

		final Entity entityAtCursor = World.getCurrentLevel().getTopmostEntity(
				location);

		if (entityAtCursor != null) {
			World.getCurrentLevel().removeEntity(entityAtCursor);
			unsavedChanges = true;

			if (entityAtCursor.isFlagSet(Flag.PLAYER)) {
				playerAdded = false;
			}
		}
	}

	public void switchToEditMode() {
		World.getCurrentLevel().resetToOriginal();
		World.setPlaying(false);
	}

	public void switchToPlayMode() {
		World.getCurrentLevel().resetToCurrent();
		World.setPlaying(true);
	}

	public void resetToEditPlacement() {
		World.getCurrentLevel().resetToCurrent();

	}

	public void moveCamera(final Vector2f cameraDelta) {

		final Rectangle cameraBounds = World.getCurrentLevel().getRect();
		cameraBounds.setX(cameraBounds.getX() - Display.getWidth() / 4f);
		cameraBounds
				.setWidth(cameraBounds.getWidth() + Display.getWidth() / 2f);
		cameraBounds.setY(cameraBounds.getY() - Display.getHeight() / 4f);
		cameraBounds.setHeight(cameraBounds.getHeight() + Display.getHeight()
				/ 2f);

		final float origX = cameraPos.x;
		cameraPos.x += cameraDelta.x;
		if (!cameraBounds.contains(cameraPos.x, cameraPos.y)) {
			cameraPos.x = origX;
		}

		final float origY = cameraPos.y;
		cameraPos.y += cameraDelta.y;
		if (!cameraBounds.contains(cameraPos.x, cameraPos.y)) {
			cameraPos.y = origY;
		}
	}

	public void update() {
		ViewManager.centerCamera(cameraPos);

	}

	public boolean levelInc(final int increment) {
		return changeLevel(World.getCurrentLevelIndex() + increment);
	}

	public boolean removeLevel() {
		final int indexToRemove = World.getCurrentLevelIndex();
		final int prevIndex = indexToRemove - 1;
		if (changeLevel(prevIndex)) {
			World.removeLevelByIndex(indexToRemove);
			return true;
		} else {
			final int nextLevelIndex = World.getCurrentLevelIndex() + 1;
			if (changeLevel(nextLevelIndex)) {
				World.removeLevelByIndex(indexToRemove);
				return true;
			}
		}
		return false;
	}

	public void startMultiselect(final Vector2f origin) {
		multiselect = new Multiselect(origin);
	}

	protected class Multiselect
	{
		final private Vector2f origin;

		public Multiselect(final Vector2f origin) {
			this.origin = origin;
		}

		public Vector2f getOrigin() {
			return origin;
		}

		public List<Vector2f> getLastLocations() {
			return lastLocations;
		}

		private List<Vector2f> lastLocations = new ArrayList<Vector2f>();

		public List<Vector2f> getLocations(final Vector2f dest,
				final int xSeparation, final int ySeparation) {
			final Vector2f snapDest = MathHelper.snapToGrid(dest, gridSize);
			final Vector2f snapOrigin = MathHelper.snapToGrid(origin, gridSize);

			final int startX = (int) snapOrigin.x;
			final int startY = (int) snapOrigin.y;
			final int endX = (int) snapDest.x;
			final int endY = (int) snapDest.y;

			boolean xCountUp = false;
			if (startX < endX) {
				xCountUp = true;
			}

			boolean yCountUp = false;
			if (startY < endY) {
				yCountUp = true;
			}

			final int xIterator = (int) xSeparation * (xCountUp ? 1 : -1);
			final int yIterator = (int) ySeparation * (yCountUp ? 1 : -1);

			List<Vector2f> locations = Lists.newArrayList();
			int xPos = startX;
			while (xCountUp && xPos <= endX || !xCountUp && xPos >= endX) {

				int yPos = startY;
				while (yCountUp && yPos <= endY || !yCountUp && yPos >= endY) {
					locations.add(new Vector2f(xPos, yPos));
					yPos += yIterator;
				}
				xPos += xIterator;
			}

			lastLocations = locations;
			return locations;
		}
	}

	public List<Vector2f> getPotentialPaintLocations(final Vector2f startPos,
			final int xSeparation, final int ySeparation) {

		return multiselect == null ? Lists.newArrayList(startPos) : multiselect
				.getLocations(startPos, xSeparation, ySeparation);
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

	public void setCameraPos(final Vector2f pos) {
		cameraPos = pos;
	}

	public void save() {
		resetToEditPlacement();
		if (World.saveCurrentGame()) {
			unsavedChanges = false;
		}
	}

	public Vector2f getCameraPos() {
		return new Vector2f(cameraPos);
	}
}
