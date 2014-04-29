package app.gui.ext;

import app.ContentManager;
import app.ContentManager.ContentType;
import de.lessvoid.nifty.render.NiftyImage;

public class ImageEntryModel {
	private String label;
	private NiftyImage icon;

	public ImageEntryModel(final String text, final NiftyImage image) {
		this.label = text;
		this.icon = image;
	}

	public ImageEntryModel(final String text, final String imageName) {
		this.label = text;
		this.icon = (NiftyImage) ContentManager.get(ContentType.niftyImage,
				imageName);
	}

	public String getLabel() {
		return label;
	}

	public NiftyImage getIcon() {
		return icon;
	}
}
