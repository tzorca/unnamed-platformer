package unnamed_platformer.app;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.globals.EntityRef;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.res_mgt.ClassLookup;
import unnamed_platformer.res_mgt.ResManager;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

public class SQLiteStuff {

	private static class Names {
		public static class tbl {
			static String textureMappings = "textureMappings";
		}

		public static class col {
			static String textureName = "textureName";
			static String entityName = "entityName";
		}
	}

	private static SQLiteConnection db;

	public static void init() {
		turnOffLogging();
		connect(new File(Ref.RESOURCE_DIR + "data.sqlite"));
		initTables();
		insertNewTextureNames();
		addTextureMappings();
	}

	private static void addTextureMappings() {
		SQLiteStatement st = null;
		try {
			st = db.prepare("select * FROM " + Names.tbl.textureMappings + ";");

			while (st.step()) {
				String entityClassName = st.columnString(1);
				if (entityClassName.equals("none")) {
					continue;
				}
				String textureName = st.columnString(0);
				Class<?> entityClass = ClassLookup.getClass(EntityRef.PACKAGE_NAME, entityClassName);
				EntityRef.addTextureNameToEntityClassMapping(textureName, entityClass);
			}
		} catch (SQLiteException e) {
			System.out.println("Could not add texture mappings: " + e.toString());
			e.printStackTrace();
			System.exit(0);
		} finally {
			st.dispose();
		}

	}

	private static void insertNewTextureNames() {
		SQLiteStatement st = null;
		try {
			st = db.prepare("insert or ignore into " + Names.tbl.textureMappings + " values (?, ?);");

			for (String textureName : ResManager.list(Texture.class, true)) {
				st.bind(1, textureName);
				st.bind(2, "entity");
				exec(st);
				st.reset();
			}
		} catch (Exception e) {
			System.out.println("Could not insert new texture names: " + e.toString());
			e.printStackTrace();
			System.exit(0);
		} finally {
			st.dispose();
		}
	}

	private static void initTables() {
		try {
			db.exec("create table if not exists " + Names.tbl.textureMappings + " (" + Names.col.textureName
					+ " text, " + Names.col.entityName + " text, PRIMARY KEY(" + Names.col.textureName + "));");

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

	private static void turnOffLogging() {
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
