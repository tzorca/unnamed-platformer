package unnamed_platformer.res_mgt.types;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

public class BackgroundImage extends BufferedImage
{
	public BackgroundImage(ColorModel arg0, WritableRaster arg1, boolean arg2,
			Hashtable<?, ?> arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public BackgroundImage(int arg0, int arg1, int arg2, IndexColorModel arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public BackgroundImage(int arg0, int arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

}