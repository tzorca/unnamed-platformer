package unnamed_platformer.game.behaviours;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.app.AudioManager;
import unnamed_platformer.app.MathHelper;
import unnamed_platformer.game.entities.ActiveEntity;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameGlobals.Flag;

public class Inter_Bounce extends Interaction
{

	private float bounceStrength;
	private float controlLockTime = 0;

	public Inter_Bounce(float bounceStrength) {
		super();
		this.bounceStrength = bounceStrength;
	}

	public Inter_Bounce(float bounceStrength, float controlLockTime) {
		super();
		this.bounceStrength = bounceStrength;
		this.controlLockTime = controlLockTime;
	}

	@Override
	protected boolean performInteraction(Entity source, Entity target) {
		ActiveEntity plr = (ActiveEntity) target;

		if (!plr.hasPhysics()) {
			return false;
		}

		Vector2f sourceToTarget = MathHelper.rectACenterMinusRectBCenter(
				source.getOriginalBox(), target.getOriginalBox());
		Vector2f reactionVector = sourceToTarget.getNormal().negate()
				.scale(bounceStrength);

		plr.getPhysics().setXForce(reactionVector.x);
		plr.getPhysics().setYForce(reactionVector.y);

		plr.getPhysics().setInAir(true);

		plr.getPhysics().resetControlMechanisms();
		
		plr.getPhysics().lockFor(controlLockTime);
		
		int springSampleNo = MathHelper.randRange(0, 9);
		AudioManager.playSample("spring" + springSampleNo);

		return true;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.PLAYER);
	}

}
