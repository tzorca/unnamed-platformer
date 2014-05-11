package game.structures;

import game.entities.ActiveEntity;

import java.awt.Point;

import org.lwjgl.util.vector.Vector2f;

public class MoveAttempt {

	public Vector2f velocity;
	public ActiveEntity actor;

	public MoveAttempt(ActiveEntity actor, Vector2f velocity) {
		this.actor = actor;
		this.velocity = velocity;
	}

}
