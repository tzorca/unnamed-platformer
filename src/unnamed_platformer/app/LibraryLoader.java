package unnamed_platformer.app;

import java.io.File;

public class LibraryLoader {

	public static void init() {
		// if (!isWindows()) {
		// System.out
		// .println("Error: Operating systems other than windows are not yet supported in this application.");
		// System.exit(0);
		// }

		System.setProperty("org.lwjgl.librarypath", lwjglLibraryDirectory);

	}

	public static final String lwjglLibraryDirectory = new File("")
			.getAbsolutePath() + File.separator + "lwjgl-dll" + File.separator;

	// private static boolean isWindows() {
	// return System.getProperty("os.name").contains("Windows");
	// }
	//
	// private static Boolean is64BitJRE() {
	// //return (System.getenv("ProgramFiles(x86)") != null);
	//
	// return (System.getProperty("os.arch").indexOf("64") != -1);
	// }

}