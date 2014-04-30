// Tim Zorca
// CPSC 3520
package model.behaviours;

import model.Ref.Flag;
import model.entities.ActiveEntity;
import model.logic.MathHelper;

import org.apache.commons.lang3.SerializationUtils;
import org.lwjgl.util.vector.Vector2f;

import app.InputManager;
import app.InputManager.GameKey;
import app.LevelManager;
import app.TimeManager;

public class Input_Shoot extends Behaviour {

	private static final long serialVersionUID = 2964710767437924198L;
	private ActiveEntity projectile = null;
	private double projectileSpeed = 0;
	private float fireDelay = 0;

	public Input_Shoot(ActiveEntity projectile, double speed, float fireDelay) {
		this.setProjectile(projectile);
		this.projectileSpeed = speed;
		this.fireDelay = fireDelay;
	}

	@Override
	public void run(ActiveEntity targetObj, float delta) {
		if (InputManager.getGameKeyState(GameKey.a, 1)) {
			if (TimeManager.time() - TimeManager.lastSample(hashCode()) >= fireDelay) {
				fire(targetObj);
			}
		}
	}

	private void fire(ActiveEntity originObj) {
		TimeManager.sample(hashCode());
		ActiveEntity movingProjectile = SerializationUtils.clone(projectile);

		Vector2f v = originObj.getDirection();
		v.y -= 0.1;
		movingProjectile.addBehaviour(new PersistentVectorMovement(
				projectileSpeed, MathHelper.angleFromVector(v)));

		movingProjectile.setCenter(originObj.getCenter());

		LevelManager.addEntity(movingProjectile, false);
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
		this.projectile.clearBehaviours();
	}

}
