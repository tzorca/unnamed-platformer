package unnamed_platformer.game.dynamics.control_mechanisms;

import org.apache.commons.lang3.SerializationUtils;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.InputManager;
import unnamed_platformer.app.LevelManager;
import unnamed_platformer.app.TimeManager;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.logic.MathHelper;
import unnamed_platformer.game.parameters.InputRef.GameKey;
import unnamed_platformer.game.parameters.Ref.Flag;

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
	public void update(float multiplier) {
		if (InputManager.getGameKeyState(GameKey.a, 1)) {
			if (TimeManager.time() - TimeManager.lastSample(hashCode()) >= fireDelay) {
				fire();
			}
		}
	}

	private void fire() {
		TimeManager.sample(hashCode());
		ActiveEntity movingProjectile = SerializationUtils.clone(projectile);

		Vector2f v = actor.getPhysics().getDirection();
		v.y -= 0.1;
		movingProjectile.getPhysics().addControlMechanism(
				new Control_PersistentVectorMovement(movingProjectile,
						projectileSpeed, MathHelper.angleFromVector(v)));

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
		this.projectile.getPhysics().clearControlMechanisms();
	}

	public void reset() {
	}

}
