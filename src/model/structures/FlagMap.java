package model.structures;

import java.io.Serializable;
import java.util.HashMap;

import model.Ref.Flag;

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
	
	public FlagMap clone() {
		FlagMap fm = new FlagMap();
		
		fm.putAll(this);
		return fm;
	}
}
