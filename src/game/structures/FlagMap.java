package game.structures;

import game.parameters.Ref.Flag;

import java.io.Serializable;
import java.util.HashMap;

public class FlagMap extends HashMap<Flag, Boolean> implements Serializable {
	private static final long serialVersionUID = -2064808301406882426L;

	public FlagMap() {
		super();
	}
	
	public FlagMap(Flag[] flags) {
		super();
		for (Flag f : flags) {
			this.put(f, true);
		}
	}
}