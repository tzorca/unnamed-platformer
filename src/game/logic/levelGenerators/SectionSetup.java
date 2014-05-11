package game.logic.levelGenerators;

import java.awt.Point;

import game.parameters.EntityRef.EntityType;

public class SectionSetup {
	public boolean allowRepeat = true;
	public EntityType entityType = EntityType.SolidBlock;
	private Point gridTranslation = new Point(1, 0);
	public String name = "untitled";

	public SectionSetup(String name, boolean allowRepeat,
			EntityType entityType, int gridXTrans, int gridYTrans) {
		this.name = name;
		this.allowRepeat = allowRepeat;
		this.entityType = entityType;
		this.gridTranslation = new Point(gridXTrans, gridYTrans);
	}

	public SectionSetup(String name, EntityType entityType, int gridXTrans,
			int gridYTrans) {
		this.name = name;
		this.entityType = entityType;
		this.gridTranslation = new Point(gridXTrans, gridYTrans);
	}

	public Point getTranslation(int gridSize) {
		return new Point(gridTranslation.x * gridSize, gridTranslation.y
				* gridSize);
	}

}
