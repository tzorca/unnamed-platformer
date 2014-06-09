package unnamed_platformer.gui.objects;

import javax.swing.Icon;

public class ImageListEntry {

	private Icon image;

	public void setImage(Icon image) {
		this.image = image;
	}

	public void setText(String text) {
		this.text = text;
	}

	private String text;

	public String getText() {
		return text;
	}

	public Icon getImage() {
		return image;
	}

	public ImageListEntry(Icon image, String text) {
		this.image = image;
		this.text = text;
	}

}
