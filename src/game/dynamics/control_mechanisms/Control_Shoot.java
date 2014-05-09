package game.dynamics.control_mechanisms;

import game.entities.ActiveEntity;
import game.logic.MathHelper;
import game.parameters.InputRef.GameKey;
import game.parameters.Ref.Flag;

import org.apache.commons.lang3.SerializationUtils;
import org.lwjgl.util.vector.Vector2f;

import app.InputManager;
import app.LevelManager;
import app.TimeManager;

public class Control_Shoot extends ControlMechanism {
	private static final long serialVersionUID = 2964710767437924198L;

	private ActiveEntity projectile = null;
	private double projectileSpeed = 0;
	private float fireDelay = 0;

	public Control_Shoot(ActiveEntity actor, ActiveEntity projectile,
			double speed, float fireDelay) {
		super(actor);
		this.setProjectile(projectile);
		this.projectileSpeed = speed;
		this.fireDelay = fireDelay;
	}

	@Override
	public void update(long delta) {
		if (InputManager.getGameKeyState(GameKey.a, 1)) {
			if (TimeManager.time() - TimeManager.lastSample(hashCode()) >= fireDelay) {
				fire();
			}
		}
	}

	private void fire() {
		TimeManager.sample(hashCode());
		ActiveEntity movingProjectile = SerializationUtils.clone(projectile);

		Vector2f v = actor.physics.getDirection();
		v.y -= 0.1;
		movingProjectile.physics
				.addControlMechanism(new Control_PersistentVectorMovement(
						movingProjectile, projectileSpeed, MathHelper
								.angleFromVector(v)));

		movingProjectile.setCenter(actor.getCenter());

		LevelManager.addEntity(movingProjectile);
	}

	public double getSpeed() {
		return projectileSpeed;
	}

	public void setSpeed(double speed) {
		this.projectileSpeed = speed;
	}

	public ActiveEntity getProjectile() {
		return projectile;
	}

	public void setProjectile(ActiveEntity projectile) {
		this.projectile = projectile;
		this.projectile.setFlag(Flag.solid, false);
		this.projectile.physics.clearControlMechanisms();
	}

}
