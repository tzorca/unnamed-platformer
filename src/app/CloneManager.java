package app;

import game.structures.QuadTree;

import org.newdawn.slick.opengl.Texture;

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
