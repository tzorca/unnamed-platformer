package unnamed_platformer.game.behaviours;

import unnamed_platformer.app.AudioManager;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.GameGlobals.Flag;

public class Inter_Breakable extends Interaction
{
	String sfxBreak = null;

	public Inter_Breakable() {
		super();
	}

	public Inter_Breakable(String sfxBreak) {
		super();
		this.sfxBreak = sfxBreak;
	}

	@Override
	protected boolean isValidTarget(Entity target) {
		return target.isFlagSet(Flag.HURTS_OTHERS);
	}

	@Override
	protected boolean performInteraction(Entity source, Entity target) {
		source.setFlag(Flag.INACTIVE_UNTIL_PLAYER_DEATH, true);
		source.setFlag(Flag.INVISIBLE, true);
		if (sfxBreak != null) {
			AudioManager.playSample(sfxBreak);
		}
		return true;
	}

}
