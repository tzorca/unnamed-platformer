package unnamed_platformer.view;

import java.awt.Color;
import java.awt.color.ColorSpace;

public class FluidColor extends Color
{
	private static final long serialVersionUID = 3994986255009786644L;

	public FluidColor(ColorSpace cspace, float[] components, float alpha) {
		super(cspace, components, alpha);
	}

	public FluidColor(float r, float g, float b, float a) {
		super(r, g, b, a);
	}

	public FluidColor(float r, float g, float b) {
		super(r, g, b);
	}

	public FluidColor(int rgba, boolean hasalpha) {
		super(rgba, hasalpha);
	}

	public FluidColor(int r, int g, int b, int a) {
		super(r, g, b, a);
	}

	public FluidColor(int r, int g, int b) {
		super(r, g, b);
	}

	public FluidColor(int rgb) {
		super(rgb);
	}

	public FluidColor highlight() {
		float greyAvg = (getRed() + getGreen() + getBlue()) / 3 / 255f;

		float[] newColors = new float[3];
		newColors[0] = getRed() / 255f * 2.5f - greyAvg;
		newColors[1] = getGreen() / 255f * 2.5f - greyAvg;
		newColors[2] = getBlue() / 255f * 2.5f - greyAvg;

		// limit to valid range
		for (int i = 0; i < 3; i++) {
			if (newColors[i] > 1) {
				newColors[i] = 1;
			} else if (newColors[i] < 0) {
				newColors[i] = 0;
			}
		}

		return new FluidColor(newColors[0], newColors[1], newColors[2]);
	}

	private final static int BRIGHTNESS_INC = 0x25;


	public FluidColor darker() {
		return brighter(-1f);
	}
	public FluidColor darker(float mul) {
		return brighter(-mul);
	}

	public FluidColor brighter() {
		return brighter(1f);
	}
	
	public FluidColor incrementHue(float inc) {
		float[] hsb = new float[3];
		Color.RGBtoHSB(getRed(), getGreen(), getBlue(), hsb);
		hsb[0] += inc;
		while (hsb[0] > 1) {
			hsb[0] -= 1;
		}
		while (hsb[0] < 0) {
			hsb[0] += 1;
		}
		return new FluidColor(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
	}

	public FluidColor brighter(float mul) {
		int nr = (int) (getRed() + BRIGHTNESS_INC * mul);
		int ng = (int) (getGreen() + BRIGHTNESS_INC * mul);
		int nb = (int) (getBlue() + BRIGHTNESS_INC * mul);

		nr = enforceBounds(nr);
		ng = enforceBounds(ng);
		nb = enforceBounds(nb);

		return new FluidColor(nr, ng, nb);
	}
	
	private static int enforceBounds(int colorVal) {
		if (colorVal > 255) {
			colorVal = 255;
		} else if (colorVal < 0) {
			colorVal = 0;
		}

		return colorVal;
	}


}
