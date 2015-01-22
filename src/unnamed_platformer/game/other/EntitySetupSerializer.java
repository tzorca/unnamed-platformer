package unnamed_platformer.game.other;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class EntitySetupSerializer implements
		JsonSerializer<EntitySetup>
{
	public JsonElement serialize(EntitySetup src, Type typeOfSrc,
			JsonSerializationContext context) {
		return new Gson().toJsonTree(EntitySetupSerializable.get(src));

	}

}
