package unnamed_platformer.app;

import java.lang.reflect.Field;
import java.util.EnumMap;

import org.newdawn.slick.Input;

import unnamed_platformer.app.InputManager.PlrGameKey;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class Settings
{
	public enum SettingName {
		OFFICIAL_LEVELSET_NAME, DEFAULTS_TO_FULLSCREEN, /* */
		PLR1_UP, PLR1_LEFT, PLR1_DOWN, PLR1_RIGHT, /* */
		PLR2_UP, PLR2_LEFT, PLR2_DOWN, PLR2_RIGHT, /* */
		PLR3_UP, PLR3_LEFT, PLR3_DOWN, PLR3_RIGHT, /* */
		PLR4_UP, PLR4_LEFT, PLR4_DOWN, PLR4_RIGHT, /* */
		PLR1_A, PLR1_B, PLR1_START, PLR1_EXIT, PLR1_RESTART, /* */
		PLR2_A, PLR2_B, PLR2_START, PLR2_EXIT, PLR2_RESTART, /* */
		PLR3_A, PLR3_B, PLR3_START, PLR3_EXIT, PLR3_RESTART, /* */
		PLR4_A, PLR4_B, PLR4_START, PLR4_EXIT, PLR4_RESTART, /* */
		PLR1_TOGGLE_FULLSCREEN, PLR1_SAVE_SCREENSHOT, PLR1_MULTI_SELECT, /* */
		PLR2_TOGGLE_FULLSCREEN, PLR2_SAVE_SCREENSHOT, PLR2_MULTI_SELECT, /* */
		PLR3_TOGGLE_FULLSCREEN, PLR3_SAVE_SCREENSHOT, PLR3_MULTI_SELECT, /* */
		PLR4_TOGGLE_FULLSCREEN, PLR4_SAVE_SCREENSHOT, PLR4_MULTI_SELECT, /* */
		PLR1_SECONDARY_UP, PLR1_SECONDARY_LEFT, PLR1_SECONDARY_DOWN, PLR1_SECONDARY_RIGHT, /* */
		PLR2_SECONDARY_UP, PLR2_SECONDARY_LEFT, PLR2_SECONDARY_DOWN, PLR2_SECONDARY_RIGHT, /* */
		PLR3_SECONDARY_UP, PLR3_SECONDARY_LEFT, PLR3_SECONDARY_DOWN, PLR3_SECONDARY_RIGHT, /* */
		PLR4_SECONDARY_UP, PLR4_SECONDARY_LEFT, PLR4_SECONDARY_DOWN, PLR4_SECONDARY_RIGHT, /* */
		PLR1_SCROLL_OUT, PLR1_SCROLL_IN, /* */
		PLR2_SCROLL_OUT, PLR2_SCROLL_IN, /* */
		PLR3_SCROLL_OUT, PLR3_SCROLL_IN, /* */
		PLR4_SCROLL_OUT, PLR4_SCROLL_IN, /* */
	}

	public static EnumMap<SettingName, Object> settings = new EnumMap<SettingName, Object>(
			SettingName.class);

	// Default settings
	static {
		settings.put(SettingName.OFFICIAL_LEVELSET_NAME, "Example Levels");
		settings.put(SettingName.DEFAULTS_TO_FULLSCREEN, false);
		settings.put(SettingName.PLR1_UP, "UP");
		settings.put(SettingName.PLR1_LEFT, "LEFT");
		settings.put(SettingName.PLR1_DOWN, "DOWN");
		settings.put(SettingName.PLR1_RIGHT, "RIGHT");
		settings.put(SettingName.PLR1_A, "X");
		settings.put(SettingName.PLR1_B, "Z");
		settings.put(SettingName.PLR1_START, "ENTER");
		settings.put(SettingName.PLR1_EXIT, "ESCAPE");
		settings.put(SettingName.PLR1_RESTART, "F2");
		settings.put(SettingName.PLR1_TOGGLE_FULLSCREEN, "F11");
		settings.put(SettingName.PLR1_SAVE_SCREENSHOT, "F12");
		settings.put(SettingName.PLR1_MULTI_SELECT, "LSHIFT");
		settings.put(SettingName.PLR1_SECONDARY_UP, "W");
		settings.put(SettingName.PLR1_SECONDARY_LEFT, "A");
		settings.put(SettingName.PLR1_SECONDARY_DOWN, "S");
		settings.put(SettingName.PLR1_SECONDARY_RIGHT, "D");
		settings.put(SettingName.PLR1_SCROLL_OUT, "LBRACKET");
		settings.put(SettingName.PLR1_SCROLL_IN, "RBRACKET");
	}

	/**
	 * Loads game key mappings multimap from settings map. Uses reflection to
	 * get key codes from Input class.
	 * 
	 * @return
	 */
	private static String KEYCODE_FIELD_PREFIX = "KEY_";
	public static Multimap<Integer, PlrGameKey> generateGameKeyMappings() {

		Multimap<Integer, PlrGameKey> gameKeys = HashMultimap.create();

		for (SettingName settingName : settings.keySet()) {
			if (!settingName.toString().startsWith("PLR")) {
				continue;
			}

			PlrGameKey plrGameKey = PlrGameKey.fromString(settingName
					.toString());

			String keyCodeFieldName = KEYCODE_FIELD_PREFIX + getString(settingName);

			Field keyCodeField;
			int keyCode;
			try {
				keyCodeField = Input.class.getField(keyCodeFieldName);
				keyCode = keyCodeField.getInt(null);
			} catch (Exception e) {
				System.err.println("Error finding key with name of "
						+ keyCodeFieldName);
				e.printStackTrace();
				continue;
			}

			gameKeys.put(keyCode, plrGameKey);
		}
		return gameKeys;
	}

	private static Object getObject(SettingName settingName) {
		if (!settings.containsKey(settingName)) {
			return null;
		}
		return settings.get(settingName);
	}

	public static Boolean getBoolean(SettingName settingName) {
		Object obj = getObject(settingName);
		return (Boolean) obj;
	}

	public static String getString(SettingName settingName) {
		Object obj = getObject(settingName);
		return (String) obj;
	}

}
