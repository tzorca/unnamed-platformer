package unnamed_platformer.globals;

import java.awt.Font;

import javax.swing.UIManager;

public class GUIRef {
	public static final String PACKAGE_NAME = Ref.BASE_PACKAGE_NAME + ".gui";

	public static final Font HEADING_FONT = new Font("Tahoma", Font.PLAIN, 48);
	
	public static final Font SUB_HEADING_FONT = new Font("Tahoma", Font.PLAIN,
			18);

	public static final String CENTER_LAYOUT = "pushx, alignx center, wrap";

	static {
		// enable native look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// enable anti-aliased text:
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("swing.aatext", "true");
	}
}
