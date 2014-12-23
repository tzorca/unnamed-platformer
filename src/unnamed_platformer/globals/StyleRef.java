package unnamed_platformer.globals;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.border.MatteBorder;

import unnamed_platformer.view.FluidColor;
import unnamed_platformer.view.gui.Style;

public class StyleRef
{
	public static final FluidColor
	/* */COLOR_LIGHT_GREY = new FluidColor(0xee, 0xee, 0xee),
	/* */COLOR_ORANGE = new FluidColor(0xbb, 0x60, 0x40),
	/* */COLOR_MAIN = new FluidColor(0x20, 0x20, 0x40).incrementHue(-0.1f),
	/* */COLOR_MAIN_MINUS, COLOR_MAIN_PLUS, COLOR_MAIN_PLUS_PLUS,
			COLOR_MAIN_HIGHLIGHT;

	// Dynamically generate other colors from main color
	static {
		COLOR_MAIN_MINUS = COLOR_MAIN.darker();

		COLOR_MAIN_PLUS = COLOR_MAIN.brighter();

		COLOR_MAIN_PLUS_PLUS = COLOR_MAIN.brighter(2);
		COLOR_MAIN_HIGHLIGHT = COLOR_MAIN_PLUS.highlight();
	}

	public static final String CENTER_LAYOUT = "pushx, alignx center, wrap";

	private static final int BASE_FONT_SIZE = 20,
			LARGER_FONT_SIZE = (int) (BASE_FONT_SIZE * 1.5),
			LARGEST_FONT_SIZE = (int) (BASE_FONT_SIZE * 2.0),
			SMALLER_FONT_SIZE = (int) (BASE_FONT_SIZE * 0.7);

	private static final String NORMAL_FONT_NAME = "Trebuchet MS";

	public static final Font
	/* */FONT_HEADING = new Font(NORMAL_FONT_NAME, Font.PLAIN,
			LARGEST_FONT_SIZE),
	/* */FONT_SUB_HEADING = new Font(NORMAL_FONT_NAME, Font.PLAIN,
			LARGER_FONT_SIZE),
	/* */FONT_NORMAL = new Font(NORMAL_FONT_NAME, Font.PLAIN, BASE_FONT_SIZE),
			/* */FONT_SMALL = new Font(NORMAL_FONT_NAME, Font.PLAIN,
					SMALLER_FONT_SIZE),
			/* */FONT_HUD = new Font("Lucida Console", Font.PLAIN,
					BASE_FONT_SIZE);

	public static final Style STYLE_HEADING, STYLE_SUB_HEADING,
			STYLE_ABSTRACT_BUTTON, STYLE_NORMAL_BUTTON, STYLE_TYPICAL_LIST,
			STYLE_TYPICAL_SCROLLPANE, STYLE_ENTITY_LIST, STYLE_CHOICE_LIST,
			STYLE_MESSAGE, STYLE_PADDED_MESSAGE, STYLE_PRESS_KEY_MESSAGE,
			STYLE_WORLD_LIST;

	static {
		STYLE_HEADING = new Style();
		STYLE_HEADING.setFont(StyleRef.FONT_HEADING);
		STYLE_HEADING.setForecolor(StyleRef.COLOR_LIGHT_GREY);

		STYLE_SUB_HEADING = new Style(STYLE_HEADING);
		STYLE_SUB_HEADING.setFont(StyleRef.FONT_SUB_HEADING);

		STYLE_MESSAGE = new Style(STYLE_HEADING);
		STYLE_MESSAGE.setFont(StyleRef.FONT_NORMAL);

		STYLE_PADDED_MESSAGE = new Style(STYLE_MESSAGE);
		STYLE_PADDED_MESSAGE.setPadding(8, 8, 8, 8);

		STYLE_PRESS_KEY_MESSAGE = new Style(STYLE_MESSAGE);
		STYLE_PRESS_KEY_MESSAGE.setForecolor(Color.WHITE);
		STYLE_PRESS_KEY_MESSAGE.setPadding(24, 32, 24, 32);

		STYLE_ABSTRACT_BUTTON = new Style();
		STYLE_ABSTRACT_BUTTON.setForecolor(Color.WHITE);
		STYLE_ABSTRACT_BUTTON.setSelectionBackcolor(COLOR_MAIN_HIGHLIGHT);
		STYLE_ABSTRACT_BUTTON.setBackcolor(COLOR_MAIN);
		STYLE_ABSTRACT_BUTTON
				.setBorder(BorderFactory.createRaisedBevelBorder());
		STYLE_ABSTRACT_BUTTON.setClickedBorder(BorderFactory
				.createLoweredBevelBorder());

		STYLE_NORMAL_BUTTON = new Style(STYLE_ABSTRACT_BUTTON);
		STYLE_NORMAL_BUTTON.setPadding(10, 20, 10, 20);
		STYLE_NORMAL_BUTTON.setFont(StyleRef.FONT_NORMAL);

		STYLE_TYPICAL_LIST = new Style();
		STYLE_TYPICAL_LIST.setFont(StyleRef.FONT_NORMAL);
		STYLE_TYPICAL_LIST.setBackcolor(StyleRef.COLOR_MAIN);
		STYLE_TYPICAL_LIST.setForecolor(Color.WHITE);
		STYLE_TYPICAL_LIST.setSelectionBackcolor(StyleRef.COLOR_MAIN_HIGHLIGHT);
		STYLE_TYPICAL_LIST.setSelectionForecolor(Color.WHITE);

		STYLE_WORLD_LIST = new Style(STYLE_TYPICAL_LIST); 
		STYLE_WORLD_LIST.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		STYLE_WORLD_LIST.setVisibleRowCount(-1);

		STYLE_TYPICAL_SCROLLPANE = new Style();
		STYLE_TYPICAL_SCROLLPANE.setFont(StyleRef.FONT_NORMAL);
		STYLE_TYPICAL_SCROLLPANE.setBackcolor(StyleRef.COLOR_MAIN_MINUS);
		STYLE_TYPICAL_SCROLLPANE.setBorder(BorderFactory
				.createLoweredBevelBorder());

		STYLE_ENTITY_LIST = new Style();
		STYLE_ENTITY_LIST.setSelectionBackcolor(StyleRef.COLOR_MAIN_HIGHLIGHT);
		STYLE_ENTITY_LIST.setBackcolor(StyleRef.COLOR_MAIN_PLUS);
		STYLE_ENTITY_LIST.setFont(StyleRef.FONT_SMALL);
		STYLE_ENTITY_LIST.setForecolor(java.awt.Color.white);

		STYLE_CHOICE_LIST = new Style();
		STYLE_CHOICE_LIST.setSelectionBackcolor(StyleRef.COLOR_MAIN_HIGHLIGHT);
		STYLE_CHOICE_LIST.setForecolor(java.awt.Color.white);
		STYLE_CHOICE_LIST.setBackcolor(StyleRef.COLOR_MAIN);
		STYLE_CHOICE_LIST.setFont(StyleRef.FONT_SMALL);
		STYLE_CHOICE_LIST.setBorder(new MatteBorder(1, 0, 0, 0,
				Color.LIGHT_GRAY));
	}
}
