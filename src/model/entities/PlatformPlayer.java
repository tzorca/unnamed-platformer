

package model.entities;

import java.awt.Point;

import model.Ref.Flag;
import model.behaviours.Input_HorizontalMove;
import model.behaviours.Input_Jump;
import model.behaviours.Input_Shoot;
import model.interactions.NegativeHazardReaction;

public class PlatformPlayer extends ActiveEntity {
	private static final long serialVersionUID = 8624310770832698957L;

	Input_HorizontalMove hzMoveBehaviour = new Input_HorizontalMove(3f);

	Input_Jump jumpBehaviour = new Input_Jump(5, 0.5);
	Input_Shoot shootBehaviour = new Input_Shoot(new Beam(new Point(0, 0)), 6,
			100);

	public PlatformPlayer(String imageName, Point startPos) {
		super(imageName, startPos);

		addSpProps();
	}

	public PlatformPlayer(String imageName, Point startPos, float speed,
			double jumpStrength, double jumpTime) {
		super(imageName, startPos);
		setSpeed(speed);
		setJumpStrength(jumpStrength);
		setJumpTime(jumpTime);

		addSpProps();
	}

	private void addSpProps() {
		this.addInteraction(new NegativeHazardReaction(this));
		this.addBehaviour(hzMoveBehaviour);
		this.addBehaviour(jumpBehaviour);
		this.addBehaviour(shootBehaviour);
		this.setFlag(Flag.obeysGravity, true);
		this.setFlag(Flag.tangible, true);
		this.setFlag(Flag.player, true);
	}

	public float getSpeed() {
		return hzMoveBehaviour.getSpeed();
	}

	public void setSpeed(float speed) {
		hzMoveBehaviour.setSpeed(speed);
	}

	public double getJumpStrength() {
		return jumpBehaviour.getJumpStrength();
	}

	public void setJumpStrength(double jumpStrength) {
		jumpBehaviour.setJumpStrength(jumpStrength);
	}

	public double getJumpTime() {
		return jumpBehaviour.getJumpTime();
	}

	public void setJumpTime(double jumpTime) {
		jumpBehaviour.setJumpTime(jumpTime);
	}

}
