package unnamed_platformer.game.other;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.LinkedList;

import org.newdawn.slick.geom.Rectangle;

import unnamed_platformer.globals.Ref.BlueprintField;
import unnamed_platformer.view.Graphic;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LevelSerializer implements JsonSerializer<Level>,
		JsonDeserializer<Level>
{

	@Override
	public JsonElement serialize(Level src, Type typeOfSrc,
			JsonSerializationContext context) {
		EnumMap<BlueprintField, Object> map = Maps
				.newEnumMap(BlueprintField.class);
		map.put(BlueprintField.LEVEL_BG, src.getBackgroundGraphic());
		map.put(BlueprintField.LEVEL_RECT, src.getRect());
		map.put(BlueprintField.LEVEL_ENTITIES, src.getEntitySetups());
		return context.serialize(map);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Level deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		EnumMap<BlueprintField, Object> map = context
				.deserialize(json, typeOfT);

		LinkedList<EntitySetup> entitySetups = (LinkedList<EntitySetup>) map
				.get(BlueprintField.LEVEL_ENTITIES);

		Level newLevel = new Level(
				EntityCreator.buildFromSetupCollection(entitySetups));
		newLevel.setBackgroundGraphic((Graphic) map.get(BlueprintField.LEVEL_BG));
		newLevel.setRect((Rectangle) map.get(BlueprintField.LEVEL_RECT));
		
		return newLevel;
	}

}
