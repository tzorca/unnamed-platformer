package unnamed_platformer.app;

import java.io.File;

public class LibraryLoader {

	public static void init() {
		System.setProperty("org.lwjgl.librarypath", lwjglLibraryDirectory);
	}

	public static final String lwjglLibraryDirectory = new File("")
			.getAbsolutePath() + File.separator + "lwjgl-dll" + File.separator;
}