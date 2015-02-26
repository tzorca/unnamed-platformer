package unnamed_platformer.game.editor;

import java.io.Serializable;

import org.newdawn.slick.geom.Vector2f;

public class SizeStrategy implements Serializable {
	private static final long serialVersionUID = -5770101634692295458L;
	
	private float sizeScale;
	private Vector2f sizeValue;
	private Strategy strategy;

	public SizeStrategy(Strategy strategy, Vector2f sizeValue) {
		this.strategy = strategy;
		this.sizeValue = sizeValue;
	}

	public SizeStrategy(Strategy strategy, float sizeScale) {
		this.strategy = strategy;
		this.sizeScale = sizeScale;
	}

	public enum Strategy {
		absoluteSize, texture, textureScale, absoluteWidth
	}

	public Strategy getStrategy() {
		return this.strategy;
	}

	public float getSizeScale() {
		return sizeScale;
	}

	public Vector2f getSize() {
		return sizeValue;
	}

}
