package unnamed_platformer.app;

import java.io.File;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.globals.EntityRef;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.globals.TextureRef;
import unnamed_platformer.res_mgt.ClassLookup;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.structures.TextureSetup;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class SQLiteStuff {

	private static final class Names {
		public static final class Tbl {
			static final String textureMappings = "textureMappings";
		}

		public static final class Col {
			static final String textureName = "textureName";
			static final String entityName = "entityName";
			static final String collisionShape = "collisionShape";
		}
	}

	private static SQLiteConnection db;

	private static boolean initialized = false;

	public static void init() {
		connect(new File(Ref.RESOURCE_DIR + "data.sqlite"));
		initialized = true;
		initTables();
		insertNewTextureNames();
		addTextureMappings();
	}

	public static boolean isInitialized() {
		return initialized;
	}

	private static void addTextureMappings() {
		try {
			SQLiteStatement st = db.prepare("select * FROM " + Names.Tbl.textureMappings + ";");

			while (st.step()) {
				String entityClassName = st.columnString(1);
				String textureName = st.columnString(0);
				if (!entityClassName.equals("none")) {
					Class<?> entityClass = ClassLookup.getClass(EntityRef.PACKAGE_NAME, entityClassName);
					EntityRef.addTextureNameToEntityClassMapping(textureName, entityClass);
				}
				String collisionShape = st.columnString(2);
				TextureRef.addSetup(textureName, new TextureSetup(collisionShape));
			}

			st.dispose();
		} catch (SQLiteException e) {
			System.out.println("Could not add texture mappings: " + e.toString());
			e.printStackTrace();
			System.exit(0);
		}

	}

	private static void insertNewTextureNames() {
		Collection<String> textureNames = ResManager.list(Texture.class, true);
		if (textureNames.size() == 0) {
			return;
		}

		try {
			SQLiteStatement st = db
					.prepare("insert or ignore into " + Names.Tbl.textureMappings + " values (?, ?, ?);");

			for (String textureName : textureNames) {
				st.bind(1, textureName);
				st.bind(2, "entity");
				st.bind(3, "rectangle");
				exec(st);
				st.reset();
			}

			st.dispose();
		} catch (Exception e) {
			System.out.println("Could not insert new texture names: " + e.toString());
			e.printStackTrace();
			System.exit(0);
		}
	}

	private static void initTables() {
		try {
			String creationSQL = "";
			creationSQL += "create table if not exists " + Names.Tbl.textureMappings;
			creationSQL += " (";
			creationSQL += Names.Col.textureName + " text, ";
			creationSQL += Names.Col.entityName + " text, ";
			creationSQL += Names.Col.collisionShape + " text, ";
			creationSQL += "PRIMARY KEY(" + Names.Col.textureName + ")";
			creationSQL += ")";

			db.exec(creationSQL);

		} catch (SQLiteException e) {
			System.out.println("Could not create tables: " + e.toString());
			System.exit(0);
		}
	}

	private static void connect(File file) {
		db = new SQLiteConnection(file);
		try {
			db.open(true);
		} catch (SQLiteException e) {
			System.out.println("Could not connect to datatbase: " + e.toString());
			System.exit(0);
		}
	}

	public static void turnOffLogging() {
		Logger.getLogger("com.almworks.sqlite4java").setLevel(Level.OFF);
	}

	public static void finish() {
		db.dispose();
	}

	private static void exec(SQLiteStatement st) throws SQLiteException {
		while (st.step()) {
		}
	}

}
