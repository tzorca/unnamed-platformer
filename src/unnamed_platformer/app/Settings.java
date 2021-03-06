package unnamed_platformer.app;

import java.io.IOException;
import java.util.EnumMap;

import org.ini4j.Wini;

import unnamed_platformer.globals.FileGlobals;
import unnamed_platformer.input.InputManager.PlrGameKey;
import unnamed_platformer.input.RawKey;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class Settings
{
	public enum SettingName {
		OFFICIAL_LEVELSET_NAME, DEFAULTS_TO_FULLSCREEN, AUTO_SAVE_ON_EXIT, HIDE_CURSOR, /* */
		PLR1_UP, PLR1_LEFT, PLR1_DOWN, PLR1_RIGHT, /* */
		PLR2_UP, PLR2_LEFT, PLR2_DOWN, PLR2_RIGHT, /* */
		PLR3_UP, PLR3_LEFT, PLR3_DOWN, PLR3_RIGHT, /* */
		PLR4_UP, PLR4_LEFT, PLR4_DOWN, PLR4_RIGHT, /* */
		PLR1_A, PLR1_B, PLR1_START, PLR1_EXIT, PLR1_RESTART, /* */
		PLR2_A, PLR2_B, PLR2_START, PLR2_EXIT, PLR2_RESTART, /* */
		PLR3_A, PLR3_B, PLR3_START, PLR3_EXIT, PLR3_RESTART, /* */
		PLR4_A, PLR4_B, PLR4_START, PLR4_EXIT, PLR4_RESTART, /* */
		PLR1_SAVE_SCREENSHOT, PLR1_MULTI_SELECT, /* */
		PLR2_SAVE_SCREENSHOT, PLR2_MULTI_SELECT, /* */
		PLR3_SAVE_SCREENSHOT, PLR3_MULTI_SELECT, /* */
		PLR4_SAVE_SCREENSHOT, PLR4_MULTI_SELECT, /* */
		PLR1_SECONDARY_UP, PLR1_SECONDARY_LEFT, PLR1_SECONDARY_DOWN, PLR1_SECONDARY_RIGHT, /* */
		PLR2_SECONDARY_UP, PLR2_SECONDARY_LEFT, PLR2_SECONDARY_DOWN, PLR2_SECONDARY_RIGHT, /* */
		PLR3_SECONDARY_UP, PLR3_SECONDARY_LEFT, PLR3_SECONDARY_DOWN, PLR3_SECONDARY_RIGHT, /* */
		PLR4_SECONDARY_UP, PLR4_SECONDARY_LEFT, PLR4_SECONDARY_DOWN, PLR4_SECONDARY_RIGHT, /* */
		PLR1_SCROLL_OUT, PLR1_SCROLL_IN, /* */
		PLR2_SCROLL_OUT, PLR2_SCROLL_IN, /* */
		PLR3_SCROLL_OUT, PLR3_SCROLL_IN, /* */
		PLR4_SCROLL_OUT, PLR4_SCROLL_IN, /* */
		PLR1_TESTING, PLR2_TESTING, /* */
		PLR3_TESTING, PLR4_TESTING /* */
	}

	public static EnumMap<SettingName, Object> settings = new EnumMap<SettingName, Object>(
			SettingName.class);

	private static void loadDefaultSettings() {
		settings.put(SettingName.OFFICIAL_LEVELSET_NAME, "Example Levels");
		settings.put(SettingName.DEFAULTS_TO_FULLSCREEN, false);
		settings.put(SettingName.AUTO_SAVE_ON_EXIT, false);
		settings.put(SettingName.HIDE_CURSOR, false);
		settings.put(SettingName.PLR1_UP, "UP");
		settings.put(SettingName.PLR1_LEFT, "LEFT");
		settings.put(SettingName.PLR1_DOWN, "DOWN");
		settings.put(SettingName.PLR1_RIGHT, "RIGHT");
		settings.put(SettingName.PLR1_A, "X");
		settings.put(SettingName.PLR1_B, "Z");
		settings.put(SettingName.PLR1_START, "RETURN");
		settings.put(SettingName.PLR1_EXIT, "ESCAPE");
		settings.put(SettingName.PLR1_RESTART, "F2");
		settings.put(SettingName.PLR1_SAVE_SCREENSHOT, "F12");
		settings.put(SettingName.PLR1_MULTI_SELECT, "LSHIFT");
		settings.put(SettingName.PLR1_SECONDARY_UP, "W");
		settings.put(SettingName.PLR1_SECONDARY_LEFT, "A");
		settings.put(SettingName.PLR1_SECONDARY_DOWN, "S");
		settings.put(SettingName.PLR1_SECONDARY_RIGHT, "D");
		settings.put(SettingName.PLR1_SCROLL_OUT, "LBRACKET");
		settings.put(SettingName.PLR1_SCROLL_IN, "RBRACKET");
		settings.put(SettingName.PLR1_TESTING, "F6");
	}

	public static Multimap<RawKey, PlrGameKey> generateGameKeyMappings() {

		Multimap<RawKey, PlrGameKey> gameKeys = HashMultimap.create();

		for (SettingName settingName : settings.keySet()) {
			if (!settingName.toString().startsWith("PLR")) {
				// Not a key mappping
				continue;
			}

			PlrGameKey plrGameKey = PlrGameKey.fromString(settingName
					.toString());

			String rawKeyName = getString(settingName);

			RawKey key;
			try {
				key = RawKey.fromString(rawKeyName);
				gameKeys.put(key, plrGameKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	private static Wini ini;

	public static void init() {
		loadDefaultSettings();

		boolean noSettingsFile = !(FileGlobals.SETTINGS_FILE.exists());

		if (noSettingsFile) {
			createNewINI();
			connectINI();
			saveINI();
			return;
		}

		connectINI();
		loadINI();
	}

	private static void loadINI() {
		try {
			ini.load();
		} catch (Exception e) {
			System.err.println("Error loading settings file.");
			e.printStackTrace();
		}

		for (SettingName settingName : SettingName.values()) {
			String strValue = ini.get(SECTION_NAME, settingName.toString());

			if (strValue == null) {
				continue;
			}

			settings.put(settingName,
					StringUtilities.inferStringAsType(strValue));
		}
	}

	private static void connectINI() {
		try {
			ini = new Wini(FileGlobals.SETTINGS_FILE);
		} catch (Exception e) {
			System.err.println("Error connecting to settings file.");
			e.printStackTrace();
			return;
		}
	}

	private static void createNewINI() {
		try {
			FileGlobals.SETTINGS_FILE.createNewFile();
		} catch (IOException e) {
			System.err.println("Error creating new settings file.");
			e.printStackTrace();
		}
	}

	private static final String SECTION_NAME = "Settings";

	public static void saveINI() {
		if (ini == null) {
			connectINI();
			if (ini == null) {
				System.err
						.println("Could not save settings to file. (INI connection failed)");
			}
			return;
		}

		for (SettingName settingName : settings.keySet()) {
			ini.put(SECTION_NAME, settingName.toString(),
					settings.get(settingName));
		}

		try {
			ini.store();
		} catch (IOException e) {
			System.err.println("Error saving settings file.");
			e.printStackTrace();
		}
	}

	public static void setString(String key, String value) {
		SettingName settingName = SettingName.valueOf(key);

		if (settingName == null) {
			System.err.println("Invalid setting name " + key);
			return;
		}

		settings.put(settingName, value);
		saveINI();

	}

}
