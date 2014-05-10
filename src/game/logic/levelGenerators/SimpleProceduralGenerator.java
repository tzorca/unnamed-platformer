package game.logic.levelGenerators;

import game.logic.MathHelper;
import game.parameters.EntityRef.EntityType;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;

public class SimpleProceduralGenerator extends LevelGenerator {

	private enum SectionType {
		horizontal, verticalUp, slopeUp, slopeDown, gap
	}

	private static HashMap<SectionType, Integer> maxLength = new HashMap<SectionType, Integer>();
	private static HashMap<SectionType, Boolean> allowRepeat = new HashMap<SectionType, Boolean>();
	static {
		// maxLength.put(SectionType.horizontal, 64 * 10);

	}

	@Override
	protected void internalBuild() {
		grid = 56;
		int maxY = grid * 64;

		// put player at bottom-left
		addDistinct("player", grid, maxY - grid * 2);

		// add border to level
		// addLevelEdges("black");

		Point cursor = new Point(grid, maxY - grid);

		// put at least one platform underneath the player...
		add(EntityType.SolidBlock, cursor.x, cursor.y, grid);

		SectionType sectionType = SectionType.horizontal;
		SectionType lastSectionType = SectionType.gap;
		for (int sectionIterator = 0; sectionIterator < 200; sectionIterator++) {
			do {
				sectionType = (SectionType) MathHelper.randInArray(SectionType
						.values());
			} while ((sectionType == lastSectionType)
					&& (sectionType == SectionType.gap || sectionType == SectionType.verticalUp));

			int sectionLength = MathHelper.randRange(1, 3);
			for (int pieceIterator = 0; pieceIterator < sectionLength; pieceIterator++) {
				switch (sectionType) {
				case horizontal:
					cursor.x += grid;
					add(EntityType.SolidBlock, cursor.x, cursor.y, grid);
					break;
				case gap:
					cursor.x += grid;
					break;
				case slopeDown:
					cursor.x += grid;
					cursor.y += grid;
					add(EntityType.SolidBlock, cursor.x, cursor.y, grid);
					break;
				case slopeUp:
					cursor.y -= grid;
					cursor.x += grid;
					add(EntityType.SolidBlock, cursor.x, cursor.y, grid);
					break;
				case verticalUp:
					cursor.y -= grid;
					add(EntityType.SolidBlock, cursor.x, cursor.y, grid);
					break;
				default:
					break;
				}
			}
			lastSectionType = sectionType;
		}

		// put flag at end
		cursor.x += grid;
		addDistinct("flag", cursor.x, cursor.y);

		setLevelRect(new Rectangle(0, 0, cursor.x + grid, maxY));

	}

}
