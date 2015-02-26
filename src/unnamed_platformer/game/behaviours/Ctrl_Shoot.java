package unnamed_platformer.game.behaviours;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.MathHelper;
import unnamed_platformer.app.AudioManager;
import unnamed_platformer.app.TimeManager;
import unnamed_platformer.game.editor.EntityCreator;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.zones.World;
import unnamed_platformer.globals.GameGlobals.Flag;
import unnamed_platformer.input.GameKey;
import unnamed_platformer.input.InputManager;

public class Ctrl_Shoot extends ControlMechanism
{

	private ActiveEntity projectile = null;
	private double projectileSpeed = 0;
	private float fireDelay = 0;
	private float variability = 0;

	public Ctrl_Shoot(ActiveEntity actor, ActiveEntity projectile,
			double speed, float fireDelay, float variability) {
		super(actor);
		this.setProjectile(projectile);
		this.projectileSpeed = speed;
		this.fireDelay = fireDelay;
		this.variability = variability;
	}

	@Override
	public void doUpdate(float multiplier) {
		if (InputManager.keyIsPressed(GameKey.B, 1)) {
			if (TimeManager.time() - TimeManager.lastSample(hashCode()) >= fireDelay) {
				fire();
			}
		}
	}

	private void fire() {
		AudioManager.playSample("laser");
		TimeManager.sample(hashCode());
		ActiveEntity movingProjectile = (ActiveEntity) EntityCreator
				.buildFromSetup(projectile.getOriginalSetup());

		Vector2f v = actor.getPhysics().getDirection();

		if (variability != 0) {
			v.y = MathHelper.randRange((int) (-variability * 1000),
					(int) (variability * 1000)) / 1000f;
		}

		movingProjectile.getPhysics().addControlMechanism(
				new Ctrl_PersistentVectorMovement(movingProjectile,
						projectileSpeed, v));

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
