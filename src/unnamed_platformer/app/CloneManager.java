package unnamed_platformer.app;

import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.game.structures.QuadTree;

import com.rits.cloning.Cloner;

public class CloneManager {

	private static Cloner cloner = new Cloner();
	static {
		cloner.dontClone(Texture.class);
		cloner.dontClone(QuadTree.class);
	}

	public static <T> T deepClone(T o) {
		return cloner.deepClone(o);
	}
}
