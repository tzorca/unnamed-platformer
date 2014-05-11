package game.logic.levelGenerators;

import game.entities.Entity;
import game.logic.EntityCreator;
import game.logic.MathHelper;

import java.awt.Point;
import java.util.Set;

public class CompletelyRandomGenerator extends LevelGenerator {

	private Set<String> textureNames = EntityCreator.listTextureNames();
	
	@Override
	protected void internalBuild() {
		 textureNames = EntityCreator.listTextureNames();

		int maxX = levelRect.width, maxY = levelRect.height;

		// put player at bottom-left
		addDistinct("player", 100, maxY - 150);

		// put flag at top-right
		addDistinct("flag", maxX - 64, 64);

		// add border to level
		addLevelEdges("black");

		int maxBlocks = MathHelper.getArea(levelRect) * 2
				/ (grid * grid * grid);

		// add random entities
		for (int i = 0; i < maxBlocks; i++) {

			Entity newEntity = null;

			// Add random entity in a spot that doesn't
			// intersect the player or the goal
			do {
				newEntity = createRandomEntity(0, maxX, 0, maxY, grid,
						rndSizeMuls);
			} while (entityIntersectsDistinct(newEntity));

			add(newEntity);
		}
	}

	private Entity createRandomEntity(int minX, int maxX, int minY,
			int maxY, int gridSize, double[] blockMuls) {
		String rTex;

		do {
			rTex = (String) MathHelper.randInSet(textureNames);
		} while (rTex.equals("flag") || rTex.equals("player"));
		// this is just for testing and will not be hardcoded in the future

		Point rPos = MathHelper.snapToGrid(
				new Point(MathHelper.randRange(minX, maxX), MathHelper
						.randRange(minY, maxY)), gridSize);
		int rSize = (int) (gridSize * MathHelper.randSet(blockMuls));

		return EntityCreator.create(rTex, rPos, rSize, false);
	}

}
