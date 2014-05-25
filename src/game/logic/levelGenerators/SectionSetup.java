package game.logic.levelGenerators;

import game.entities.SolidBlock;

import java.awt.Point;

public class SectionSetup {
	public boolean allowRepeat = true;
	public Class<?> entityClass = SolidBlock.class;
	private Point gridTranslation = new Point(1, 0);
	public String name = "untitled";

	public SectionSetup(String name, boolean allowRepeat,
			Class<?> entityClass, int gridXTrans, int gridYTrans) {
		this.name = name;
		this.allowRepeat = allowRepeat;
		this.entityClass = entityClass;
		this.gridTranslation = new Point(gridXTrans, gridYTrans);
	}

	public SectionSetup(String name, Class<?> entityClass, int gridXTrans,
			int gridYTrans) {
		this.name = name;
		this.entityClass = entityClass;
		this.gridTranslation = new Point(gridXTrans, gridYTrans);
	}

	public Point getTranslation(int gridSize) {
		return new Point(gridTranslation.x * gridSize, gridTranslation.y
				* gridSize);
	}

}
