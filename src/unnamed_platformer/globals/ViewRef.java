package unnamed_platformer.globals;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class ViewRef {

	public static final Dimension DEFAULT_RESOLUTION = new Dimension(960, 600);

	public static final float SCALE = 1f;

	public static final int FPS = 60;

	public static final Color GUI_BG_COLOR = new Color(0x16, 0x17, 0x26);
	
	public static final Color GUI_FG_COLOR = Color.WHITE;

	public static Dimension getScreenResolution() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		return new Dimension(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight());
	}

}