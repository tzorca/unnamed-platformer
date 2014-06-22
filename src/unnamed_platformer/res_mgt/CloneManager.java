package unnamed_platformer.res_mgt;

import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.structures.QuadTree;

import com.rits.cloning.Cloner;

public class CloneManager {

	private static Cloner cloner = new Cloner();
	static {
		cloner.dontClone(QuadTree.class);
		cloner.dontClone(Texture.class);
	}

	public static <T> T deepClone(T o) {
		return cloner.deepClone(o);
	}
}
