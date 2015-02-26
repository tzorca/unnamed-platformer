package unnamed_platformer.game.serialization;

import java.lang.reflect.Type;
import java.util.Map.Entry;
import java.util.Set;

import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.editor.EntitySetup;
import unnamed_platformer.game.editor.SizeStrategy;
import unnamed_platformer.game.physics.DirectionalEnums.Orientation;
import unnamed_platformer.globals.GameGlobals.EntityParam;
import unnamed_platformer.view.Graphic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class EntitySetupDeserializer implements JsonDeserializer<EntitySetup>
{
	@Override
	public EntitySetup deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		EntitySetup entitySetup = new EntitySetup();

		JsonObject jsonObj = json.getAsJsonObject();

		JsonObject paramData = jsonObj.get("data").getAsJsonObject();
		Set<Entry<String, JsonElement>> params = paramData.entrySet();

		for (Entry<String, JsonElement> param : params) {
			JsonElement value = param.getValue();
			EntityParam paramType = EntityParam.valueOf(param.getKey());
			switch (paramType) {
			case GRAPHIC:
				entitySetup.set(paramType,
						context.deserialize(value, Graphic.class));
				break;
			case LOCATION:
				entitySetup.set(paramType,
						context.deserialize(value, Vector2f.class));
				break;
			case ORIENTATION:
				entitySetup.set(paramType,
						context.deserialize(value, Orientation.class));
				break;
			case SIZE_STRATEGY:
				entitySetup.set(paramType,
						context.deserialize(value, SizeStrategy.class));
				break;
			case UNUSED_A:
				break;
			case UNUSED_B:
				break;
			default:
				break;

			}
		}

		entitySetup.setEntityClassName(jsonObj.get("className").getAsString());

		return entitySetup;
	}

}
