package unnamed_platformer.gui.ext;

import unnamed_platformer.app.ContentManager;
import unnamed_platformer.game.parameters.ContentRef.ContentType;
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
