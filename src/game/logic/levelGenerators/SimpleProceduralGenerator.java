package game.logic.levelGenerators;

import game.entities.Hazard;
import game.entities.SolidBlock;
import game.entities.Spikes;
import game.logic.EntityCreator;
import game.logic.MathHelper;
import game.parameters.EntityRef;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import app.ViewManager;

public class SimpleProceduralGenerator extends LevelGenerator {

	private static ArrayList<SectionSetup> sectionSetups = new ArrayList<SectionSetup>();
	static {
		// just an alias
		ArrayList<SectionSetup> ss = sectionSetups;

		ss.add(new SectionSetup("horizontal", SolidBlock.class, 1, 0));
		ss.add(new SectionSetup("hazard", false, Hazard.class, 1, 0));
		ss.add(new SectionSetup("spikes", false, Spikes.class, 1, 0));
		ss.add(new SectionSetup("slopeDown", SolidBlock.class, 1, 1));
		ss.add(new SectionSetup("slopeUp", SolidBlock.class, 1, -1));
		ss.add(new SectionSetup("verticalUp", false, SolidBlock.class, 0, -1));

		ss.add(new SectionSetup("verticalDown", false, SolidBlock.class, 0, 1));
	}

	static int areaCount = 15;
	static int minSections = 3, maxSections = 9;
	static int maxPieces = 3;

	@Override
	protected void internalBuild() {
		grid = 56;
		int maxY = grid * 64;

		// put player at bottom-left
		addDistinct("player", grid * 4, maxY - grid * 4);

		// create a cursor to the left and below the
		// player's initial position
		Point cursor = new Point(0, maxY - grid);

		SectionSetup sectionSetup = sectionSetups.get(0);
		SectionSetup lastSectionSetup = (SectionSetup) MathHelper
				.randInList(sectionSetups);
		int sectionCount;
		int pieceCount = 5;
		for (int areaIterator = 0; areaIterator < areaCount; areaIterator++) {

			// Each area chooses a texture for each Entity subclass
			// These are picked randomly at the start of an area iteration
			HashMap<Class<?>, String> areaTextureMap = new HashMap<Class<?>, String>();
			for (Class<?> entityClass : EntityRef.getEntitySubclasses()) {
				areaTextureMap.put(entityClass,
						EntityCreator.chooseTextureFromType(entityClass));
			}

			sectionCount = MathHelper.randRange(minSections, maxSections);
			for (int sectionIterator = 0; sectionIterator < sectionCount; sectionIterator++) {

				String textureName = areaTextureMap
						.get(sectionSetup.entityClass);

				String solidBlockTextureName = areaTextureMap
						.get(SolidBlock.class);

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
