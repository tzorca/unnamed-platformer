package model.logic;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import model.Level;
import model.Ref;
import model.entities.Entity;
import app.ViewManager;

public class LevelGenerator {

	public static double[] randomBlockMuls = { 1, 2, 3, 4 };

	public final static String BORDER_TEXTURE = "black";
	public final static String[] RANDOM_BLOCK_TEXTURES = { "white" };

	public static Level generateLevel() {
		Rectangle levelRect = new Rectangle(0, 0, MathHelper.randRange(500,
				1500), MathHelper.randRange(500, 1500));
		int levelGridSize = Ref.DEFAULT_LEVEL_GRIDSIZE;

		int maxX = levelRect.width, maxY = levelRect.height;
		int minX = 0, minY = 0;
		int borderBoxSize = levelGridSize * 4;

		Entity playerEntity = EntityCreator.create("player", new Point(
				minX + 100, maxY - 150));

		List<Entity> newEntities = new ArrayList<Entity>();

		Entity goalEntity = EntityCreator.create("flag", new Point(maxX - 64,
				minY));

		// draw random blocks
		int maxBlocks = MathHelper.getArea(levelRect) / levelGridSize
				/ levelGridSize / levelGridSize;
		for (int i = 0; i < maxBlocks; i++) {

			Entity newBlock = null;
			do {
				newBlock = randBreakableBlock(RANDOM_BLOCK_TEXTURES, minX,
						maxX, minY, maxY, levelGridSize, randomBlockMuls);

			} while (newBlock.getBox().intersects(playerEntity.getBox())
					|| newBlock.getBox().intersects(goalEntity.getBox()));

			newEntities.add(newBlock);

			// spawn a spring every so often
			if (MathHelper.randRange(0, 2) == 0) {
				Point springPos = new Point((int) (newBlock.getX()
						+ newBlock.getBox().getWidth() / 2 - 24),
						newBlock.getY() - 22);
				newEntities.add(EntityCreator.create("spring", springPos));
			}
		}

		// draw vertical edges
		for (int i = -ViewManager.height; i <= maxY + ViewManager.height; i += borderBoxSize) {

			String tex = BORDER_TEXTURE;
			Point posT = new Point(-borderBoxSize, i);
			Point posB = new Point(maxX, i);
			for (int j = 0; j <= ViewManager.height; j += borderBoxSize) {
				Point posTadj = new Point(posT.x - j, posT.y);
				Point posBadj = new Point(posB.x + j, posB.y);

				newEntities.add(EntityCreator.create(tex, posTadj,
						borderBoxSize, false));
				newEntities.add(EntityCreator.create(tex, posBadj,
						borderBoxSize, false));
			}
		}

		// draw horizontal edges
		for (int i = -ViewManager.width; i <= maxX + ViewManager.width; i += borderBoxSize) {

			String tex = BORDER_TEXTURE;
			Point posL = new Point(i, -borderBoxSize);
			Point posR = new Point(i, maxY);

			for (int j = 0; j <= ViewManager.width; j += borderBoxSize) {
				Point posLadj = new Point(posL.x, posL.y - j);
				Point posRadj = new Point(posR.x, posR.y + j);

				newEntities.add(EntityCreator.create(tex, posLadj,
						borderBoxSize, false));
				newEntities.add(EntityCreator.create(tex, posRadj,
						borderBoxSize, false));
			}
		}

		newEntities.add(playerEntity);
		newEntities.add(goalEntity);
		return new Level(newEntities);

	}

	public static Entity randBreakableBlock(String[] textureSet,
			int minX, int maxX, int minY, int maxY, int gridSize,
			double[] blockMuls) {
		String rTex = MathHelper.randSet(textureSet);
		Point rPos = MathHelper.snapToGrid(
				new Point(MathHelper.randRange(minX, maxX), MathHelper
						.randRange(minY, maxY)), gridSize);
		int rSize = (int) (gridSize * MathHelper.randSet(blockMuls));

		return EntityCreator.create(rTex, rPos, rSize, false);
	}

}
