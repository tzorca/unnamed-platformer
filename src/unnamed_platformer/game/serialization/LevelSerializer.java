package unnamed_platformer.game.serialization;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.LinkedList;

import org.newdawn.slick.geom.Rectangle;

import unnamed_platformer.game.editor.EntityCreator;
import unnamed_platformer.game.editor.EntitySetup;
import unnamed_platformer.game.zones.Level;
import unnamed_platformer.view.Graphic;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LevelSerializer implements JsonSerializer<Level>,
		JsonDeserializer<Level>
{
	private static final Type ENTITY_SETUP_LIST_TYPE = new TypeToken<LinkedList<EntitySetup>>() {
		private static final long serialVersionUID = -8307301960581307792L;
	}.getType();

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
	public Level deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		// TimeManager.sample(json);
		JsonObject jsonObject = json.getAsJsonObject();

		JsonElement jsonEntitySetups = jsonObject
				.get(BlueprintField.LEVEL_ENTITIES.toString());

		LinkedList<EntitySetup> entitySetups = context.deserialize(
				jsonEntitySetups, ENTITY_SETUP_LIST_TYPE);

		Level newLevel = new Level(
				EntityCreator.buildFromSetupCollection(entitySetups));

		newLevel.setBackgroundGraphic((Graphic) context.deserialize(
				(jsonObject.get(BlueprintField.LEVEL_BG.toString())),
				Graphic.class));
		newLevel.setRect((Rectangle) context.deserialize(
				(jsonObject.get(BlueprintField.LEVEL_RECT.toString())),
				Rectangle.class));

		// System.out.println(TimeManager.secondsSince(TimeManager
		// .lastSample(json)));
		return newLevel;
	}
}
