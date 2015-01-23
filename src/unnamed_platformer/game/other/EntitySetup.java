package unnamed_platformer.game.other;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

import unnamed_platformer.globals.EntityRef.EntityParam;

public class EntitySetup implements Serializable
{
	private static final long serialVersionUID = 5451535085901404848L;

	private Map<EntityParam, Object> data = new EnumMap<EntityParam, Object>(
			EntityParam.class);
	private String className;

	public void setEntityClassName(String name) {
		className = name;
	}

	public String getEntityClassName() {
		return className;
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
