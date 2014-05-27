package unnamed_platformer.game.parameters;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class ViewRef {

	public static final Dimension DEFAULT_RESOLUTION = new Dimension(950, 500);

	public static final float SCALE = 1f;

	public static final int FPS = 60;

	public static Dimension getScreenResolution() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		return new Dimension(gd.getDisplayMode().getWidth(), gd
				.getDisplayMode().getHeight());
	}

}