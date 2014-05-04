package model.logic;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

import model.Level;
import model.entities.Entity;
import model.parameters.Ref;
import app.ViewManager;

public class LevelGenerator {

	public static double[] rndSizeMuls = { 1, 1, 2, 2, 4};

	public final static String BORDER_TEXTURE = "black";
	public final static String[] RANDOM_BLOCK_TEXTURES = { "white" };

	// testing some new generation methods...
	public static Level generateLevel() {
		int grid = Ref.DEFAULT_LEVEL_GRIDSIZE;
		Rectangle levelRect = new Rectangle(0, 0, MathHelper.randRange(
				grid * 64, grid * 128), MathHelper.randRange(grid * 16,
				grid * 64));

		int maxX = levelRect.width, maxY = levelRect.height;
		int borderBlockSize = grid * 4;

		// put player at bottom-left
		Entity playerEntity = EntityCreator.create("player", new Point(100,
				maxY - 150));

		LinkedList<Entity> newEntities = new LinkedList<Entity>();

		// put flag at top-right
		Entity goalEntity = EntityCreator.create("flag", new Point(maxX - 64,
				64));

		// draw vertical edges
		for (int i = -ViewManager.height; i <= maxY + ViewManager.height; i += borderBlockSize) {

			String tex = BORDER_TEXTURE;
			Point posT = new Point(-borderBlockSize, i);
			Point posB = new Point(maxX, i);
			for (int j = 0; j <= ViewManager.height; j += borderBlockSize) {
				Point posTadj = new Point(posT.x - j, posT.y);
				Point posBadj = new Point(posB.x + j, posB.y);

				newEntities.add(EntityCreator.create(tex, posTadj,
						borderBlockSize, false));
				newEntities.add(EntityCreator.create(tex, posBadj,
						borderBlockSize, false));
			}
		}

		// draw horizontal edges
		for (int i = -ViewManager.width; i <= maxX + ViewManager.width; i += borderBlockSize) {

			String tex = BORDER_TEXTURE;
			Point posL = new Point(i, -borderBlockSize);
			Point posR = new Point(i, maxY);

			for (int j = 0; j <= ViewManager.width; j += borderBlockSize) {
				Point posLadj = new Point(posL.x, posL.y - j);
				Point posRadj = new Point(posR.x, posR.y + j);

				newEntities.add(EntityCreator.create(tex, posLadj,
						borderBlockSize, false));
				newEntities.add(EntityCreator.create(tex, posRadj,
						borderBlockSize, false));
			}
		}

		// create random entities
		int maxBlocks = MathHelper.getArea(levelRect) * 2 / (grid * grid * grid);
		for (int i = 0; i < maxBlocks; i++) {

			Entity newBlock = null;
			do {
				newBlock = randBlock(0, maxX, 0, maxY, grid, rndSizeMuls);

				// find a spot that doesn't intersect the player or the goal
			} while (newBlock.getBox().intersects(playerEntity.getBox())
					|| newBlock.getBox().intersects(goalEntity.getBox()));

			newEntities.add(newBlock);

		}

		newEntities.add(playerEntity);
		newEntities.add(goalEntity);
		return new Level(newEntities);

	}

	public static Entity randBlock(int minX, int maxX, int minY, int maxY,
			int gridSize, double[] blockMuls) {
		String rTex;

		do {
			rTex = (String) MathHelper.randInSet(EntityCreator
					.listTextureNames());
		} while (rTex.equals("flag") || rTex.equals("player"));
		// this is just for testing and will not be hardcoded in the future

		Point rPos = MathHelper.snapToGrid(
				new Point(MathHelper.randRange(minX, maxX), MathHelper
						.randRange(minY, maxY)), gridSize);
		int rSize = (int) (gridSize * MathHelper.randSet(blockMuls));

		return EntityCreator.create(rTex, rPos, rSize, false);
	}

}
