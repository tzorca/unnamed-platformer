package app.gui.ext;

import de.lessvoid.nifty.controls.ListBox.ListBoxViewConverter;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;

public class ImageListBoxViewConverter implements
		ListBoxViewConverter<ImageEntryModel> {
	private static final String LINE_ICON = "#line-icon";
	private static final String LINE_TEXT = "#line-text";

	/**
	 * Default constructor.
	 */
	public ImageListBoxViewConverter() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void display(final Element listBoxItem,
			final ImageEntryModel item) {
		final Element text = listBoxItem.findElementByName(LINE_TEXT);
		final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
		final Element icon = listBoxItem.findElementByName(LINE_ICON);
		final ImageRenderer iconRenderer = icon
				.getRenderer(ImageRenderer.class);
		if (item != null) {
			textRenderer.setText(item.getLabel());
			iconRenderer.setImage(item.getIcon());
		} else {
			textRenderer.setText("");
			iconRenderer.setImage(null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unused")
	@Override
	public final int getWidth(final Element listBoxItem,
			final ImageEntryModel item) {
		final Element text = listBoxItem.findElementByName(LINE_TEXT);
		final TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
		final Element icon = listBoxItem.findElementByName(LINE_ICON);
		final ImageRenderer iconRenderer = icon
				.getRenderer(ImageRenderer.class);
		return ((textRenderer.getFont() == null) ? 0 : textRenderer.getFont()
				.getWidth(item.getLabel()))
				+ ((item.getIcon() == null) ? 0 : item.getIcon().getWidth());
	}
}