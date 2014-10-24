package unnamed_platformer.view.gui;

import java.awt.Color;

public class ColorHelper
{

	public static Color highlight(Color original) {
		float greyAvg = (original.getRed() + original.getGreen() + original
				.getBlue()) / 3 / 255f;

		float[] newColors = new float[3];
		newColors[0] = original.getRed() / 255f * 2.2f - greyAvg;
		newColors[1] = original.getGreen() / 255f * 2.2f - greyAvg;
		newColors[2] = original.getBlue() / 255f * 2.2f - greyAvg;

		// limit to valid range
		for (int i = 0; i < 3; i++) {
			if (newColors[i] > 1) {
				newColors[i] = 1;
			} else if (newColors[i] < 0) {
				newColors[i] = 0;
			}
		}

		return new Color(newColors[0], newColors[1], newColors[2]);
	}

	private final static int BRIGHTNESS_INC = 0x25;

	public static Color darken(Color color) {
		return brighten(color, -1f);
	}

	public static Color brighten(Color color) {
		return brighten(color, 1f);
	}

	public static Color brighten(Color color, float mul) {
		int nr = (int) (color.getRed() + BRIGHTNESS_INC * mul);
		int ng = (int) (color.getGreen() + BRIGHTNESS_INC * mul);
		int nb = (int) (color.getBlue() + BRIGHTNESS_INC * mul);

		return getColorInBounds(nr, ng, nb);
	}

	private static Color getColorInBounds(int r, int g, int b) {
		if (r > 255) {
			r = 255;
		} else if (r < 0) {
			r = 0;
		}

		if (g > 255) {
			g = 255;
		} else if (g < 0) {
			g = 0;
		}

		if (b > 255) {
			b = 255;
		} else if (b < 0) {
			b = 0;
		}

		return new Color(r, g, b);
	}
}
