package unnamed_platformer.game.behaviours;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.InputManager;
import unnamed_platformer.app.MathHelper;
import unnamed_platformer.app.TimeManager;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.other.EntityCreator;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.InputRef.GameKey;

public class Ctrl_Shoot extends ControlMechanism {

	private ActiveEntity projectile = null;
	private double projectileSpeed = 0;
	private float fireDelay = 0;

	public Ctrl_Shoot(ActiveEntity actor, ActiveEntity projectile,
			double speed, float fireDelay) {
		super(actor);
		this.setProjectile(projectile);
		this.projectileSpeed = speed;
		this.fireDelay = fireDelay;
	}

	@Override
	public void doUpdate(float multiplier) {
		if (InputManager.getGameKeyState(GameKey.a, 1)) {
			if (TimeManager.time() - TimeManager.lastSample(hashCode()) >= fireDelay) {
				fire();
			}
		}
	}

	private void fire() {
		TimeManager.sample(hashCode());
		ActiveEntity movingProjectile = (ActiveEntity) EntityCreator
				.buildFromSetup(projectile.getOriginalSetup());

		Vector2f v = actor.getPhysics().getDirection();
		v.y -= 0.1;
		movingProjectile.getPhysics().addControlMechanism(
				new Ctrl_PersistentVectorMovement(movingProjectile,
						projectileSpeed, MathHelper.angleFromVector(v)));

		movingProjectile.setCenter(actor.getCenter());

		World.addEntity(movingProjectile);
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
		this.projectile.setFlag(Flag.SOLID, false);
		this.projectile.getPhysics().clearControlMechanisms();
	}

	public void reset() {
	}

}
