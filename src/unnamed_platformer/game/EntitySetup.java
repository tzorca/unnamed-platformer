package unnamed_platformer.game;

import java.io.Serializable;
import java.util.EnumMap;

import unnamed_platformer.game.parameters.EntityRef.EntityParam;

public class EntitySetup implements Serializable {
	private static final long serialVersionUID = 5451535085901404848L;
	
	private EnumMap<EntityParam, Object> data = new EnumMap<EntityParam, Object>(
			EntityParam.class);
	private Class<?> subclass;
	
	public void setEntityType(Class<?> type) {
		this.subclass = type;
	}
	
	public Class<?> getEntityType() {
		return subclass;
	}
	
	public void set(EntityParam param, Object value) {
		data.put(param, value);
	}
	
	public Object get(EntityParam param) {
		return data.get(param);
	}

	public boolean has(EntityParam param) {
		return data.containsKey(param);
	}


}
