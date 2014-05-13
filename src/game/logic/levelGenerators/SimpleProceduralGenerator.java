package game.logic.levelGenerators;

import game.logic.EntityCreator;
import game.logic.MathHelper;
import game.parameters.EntityRef.EntityType;

import java.awt.Point;
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
		ss.add(new SectionSetup("spikes", false, EntityType.Spikes, 1, 0));
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
		addDistinct("player", grid*4, maxY - grid * 4);

		// create a cursor to the left and below the
		// player's initial position
		Point cursor = new Point(0, maxY - grid);

		SectionSetup sectionSetup = sectionSetups.get(0); 
		SectionSetup lastSectionSetup = (SectionSetup) MathHelper
				.randInList(sectionSetups);
		int sectionCount;
		int pieceCount = 5;
		for (int areaIterator = 0; areaIterator < areaCount; areaIterator++) {

			// Each area has a single type of texture for each type of entity
			// These are picked randomly at the start of an area iteration
			HashMap<EntityType, String> areaTextureMap = new HashMap<EntityType, String>();
			for (EntityType type : EntityType.values()) {
				areaTextureMap.put(type,
						EntityCreator.chooseTextureFromType(type));
			}

			sectionCount = MathHelper.randRange(minSections, maxSections);
			for (int sectionIterator = 0; sectionIterator < sectionCount; sectionIterator++) {
				
				String textureName = areaTextureMap
						.get(sectionSetup.entityType);

				String solidBlockTextureName = areaTextureMap
						.get(EntityType.SolidBlock);

				Point translation = sectionSetup.getTranslation(grid);

				for (int pieceIterator = 0; pieceIterator < pieceCount; pieceIterator++) {
					cursor.translate(translation.x, translation.y);
					add(textureName, cursor.x, cursor.y, grid);
					for (int undergroundIterator = grid; undergroundIterator < ViewManager.height * 2; undergroundIterator += grid) {

						add(solidBlockTextureName, cursor.x, cursor.y
								+ undergroundIterator, grid);
					}
				}
				pieceCount = MathHelper.randRange(1, maxPieces);
				
				lastSectionSetup = sectionSetup;
				do {
					sectionSetup = (SectionSetup) MathHelper
							.randInList(sectionSetups);
				} while (!lastSectionSetup.allowRepeat
						&& sectionSetup.equals(lastSectionSetup));
			}
			

		}

		// put goal at end
		cursor.y -= grid;
		addDistinct("flag", cursor.x, cursor.y);

	}

}
