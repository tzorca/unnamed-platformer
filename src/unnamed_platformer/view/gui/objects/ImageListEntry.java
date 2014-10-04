package unnamed_platformer.view.gui.objects;

import javax.swing.Icon;

public class ImageListEntry implements Comparable<ImageListEntry>
{

	private Icon image;
	private String displayName;
	private String internalName;

	public void setImage(Icon image) {
		this.image = image;
	}

	public void setDisplayName(String text) {
		this.displayName = text;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setInternalName(String text) {
		this.internalName = text;
	}

	public String getInternalName() {
		return internalName;
	}

	public Icon getImage() {
		return image;
	}

	
	public ImageListEntry(Icon image, String displayName) {
		this.image = image;
		this.displayName = displayName;
		this.internalName = displayName;
	}

	public ImageListEntry(Icon image, String displayName, String internalName) {
		this.image = image;
		this.displayName = displayName;
		this.internalName = internalName;
	}

	@Override
	public int compareTo(ImageListEntry o) {
		if (!(o instanceof ImageListEntry)) {
			return -1;
		}
		ImageListEntry otherEntry = (ImageListEntry) o;

		return this.getDisplayName().compareTo(otherEntry.getDisplayName());
	}
}
