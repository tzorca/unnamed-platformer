package unnamed_platformer.app;

import java.util.Collection;

import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.res_mgt.ResManager;

public class Todo_SQLiteDBStuff {
	public void addNewTextureNamesToSQLiteDB() {
		Collection<String> files = ResManager.list(Texture.class, true);
		
		
	}
}
 