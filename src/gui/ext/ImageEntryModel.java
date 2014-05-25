package gui.ext;

import game.parameters.ContentRef.ContentType;
import app.ContentManager;
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
