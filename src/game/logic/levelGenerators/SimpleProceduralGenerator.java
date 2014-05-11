package game.logic.levelGenerators;

import game.logic.EntityCreator;
import game.logic.MathHelper;
import game.parameters.EntityRef.EntityType;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import app.ViewManager;

public class SimpleProceduralGenerator extends LevelGenerator {

	private static ArrayList<SectionSetup> sectionSetups = new ArrayList<SectionSetup>();
	static {
		// just an alias
		ArrayList<SectionSetup> ss = sectionSetups;

		ss.add(new SectionSetup("horizontal", EntityType.SolidBlock, 1, 0));
		ss.add(new SectionSetup("hazard", false, EntityType.Hazard, 1, 0));
		ss.add(new SectionSetup("slopeDown", EntityType.SolidBlock, 1, 1));
		ss.add(new SectionSetup("slopeUp", EntityType.SolidBlock, 1, -1));
		ss.add(new SectionSetup("verticalUp", false, EntityType.SolidBlock, 0,
				-1));
	}

	static int areaCount = 10;
	static int minSections = 3, maxSections = 15;
	static int maxPieces = 3;

	@Override
	protected void internalBuild() {
		grid = 56;
		int maxY = grid * 64;

		// put player at bottom-left
		addDistinct("player", grid, maxY - grid * 2);

		// create a cursor under the player's initial position
		Point cursor = new Point(grid, maxY - grid);

		// add a starting platform underneath the player
		add(EntityType.SolidBlock, cursor.x, cursor.y, grid);
		cursor.x += grid;
		add(EntityType.SolidBlock, cursor.x, cursor.y, grid);

		SectionSetup sectionSetup;
		SectionSetup lastSectionSetup = (SectionSetup) MathHelper
				.randInList(sectionSetups);
		for (int areaIterator = 0; areaIterator < areaCount; areaIterator++) {

			// Each area has a single type of texture for each type of entity
			// These are picked randomly at the start of an area iteration
			HashMap<EntityType, String> areaTextureMap = new HashMap<EntityType, String>();
			for (EntityType type : EntityType.values()) {
				areaTextureMap.put(type,
						EntityCreator.chooseTextureFromType(type));
			}

			int sectionCount = MathHelper.randRange(minSections, maxSections);
			for (int sectionIterator = 0; sectionIterator < sectionCount; sectionIterator++) {

				do {
					sectionSetup = (SectionSetup) MathHelper
							.randInList(sectionSetups);
				} while (!sectionSetup.allowRepeat
						&& sectionSetup.name.equals(lastSectionSetup.name));

				if (!sectionSetup.allowRepeat) {
					if (sectionSetup.name.equals(lastSectionSetup.name)) {
						System.out.println(sectionSetup.name);
					}
				}

				String textureName = areaTextureMap
						.get(sectionSetup.entityType);

				String solidBlockTextureName = areaTextureMap
						.get(EntityType.SolidBlock);

				Point translation = sectionSetup.getTranslation(grid);

				int pieceCount = MathHelper.randRange(1, maxPieces);
				for (int pieceIterator = 0; pieceIterator < pieceCount; pieceIterator++) {
					cursor.translate(translation.x, translation.y);
					add(textureName, cursor.x, cursor.y, grid);
					for (int undergroundIterator = grid; undergroundIterator < ViewManager.height * 2; undergroundIterator += grid) {

						add(solidBlockTextureName, cursor.x, cursor.y
								+ undergroundIterator, grid);
					}
				}
			}
		}

		// put goal at end
		cursor.x += grid;
		addDistinct("flag", cursor.x, cursor.y);

		setLevelRect(new Rectangle(0, 0, cursor.x + grid, maxY));

	}

}
