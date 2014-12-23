package unnamed_platformer.view.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class Style
{
	private Font font;
	private Color backcolor, forecolor, selectionForecolor, selectionBackcolor;
	private int paddingTop = 0, paddingBottom = 0;
	private int paddingLeft = 0, paddingRight = 0;
	private Border border, clickedBorder;
	private Integer verticalTextPosition, horizontalTextPosition;
	private Integer horizontalAlignment;
	private Boolean opaque;
	private Integer visibleRowCount, layoutOrientation; 

	public Style(Style style) {
		this.font = style.font;
		this.backcolor = style.backcolor;
		this.forecolor = style.forecolor;
		this.selectionBackcolor = style.selectionForecolor;
		this.selectionBackcolor = style.selectionBackcolor;
		this.paddingTop = style.paddingTop;
		this.paddingLeft = style.paddingLeft;
		this.paddingRight = style.paddingRight;
		this.paddingBottom = style.paddingBottom;
		this.border = style.border;
		this.clickedBorder = style.clickedBorder;
		this.verticalTextPosition = style.verticalTextPosition;
		this.horizontalTextPosition = style.horizontalTextPosition;
		this.horizontalAlignment = style.horizontalAlignment;
		this.opaque = style.opaque;
		this.visibleRowCount = style.visibleRowCount;
		this.layoutOrientation = style.layoutOrientation;
	}

	public Style() {
	}

	public void apply(JComponent c) {
		if (font != null) {
			c.setFont(font);
		}
		if (backcolor != null) {
			c.setBackground(backcolor);
		}
		if (forecolor != null) {
			c.setForeground(forecolor);
		}

		if (opaque != null) {
			c.setOpaque(false);
		}

		EmptyBorder paddingBorder = new EmptyBorder(paddingTop, paddingLeft,
				paddingBottom, paddingRight);
		final CompoundBorder normalCompoundBorder = new CompoundBorder(border,
				paddingBorder);
		final CompoundBorder clickedCompoundBorder = new CompoundBorder(
				clickedBorder, paddingBorder);
		c.setBorder(normalCompoundBorder);

		if (clickedBorder != null) {

			c.addMouseListener(new MouseAdapter() {

				public void mousePressed(MouseEvent e) {
					JComponent component = (JComponent) e.getSource();
					component.setBorder(clickedCompoundBorder);
				}

				public void mouseReleased(MouseEvent e) {
					JComponent component = (JComponent) e.getSource();
					component.setBorder(normalCompoundBorder);
				}
			});
		}


		if (c instanceof AbstractButton) {
			final AbstractButton ab = (AbstractButton) c;
			if (verticalTextPosition != null) {
				ab.setVerticalTextPosition(verticalTextPosition);
			}
			if (horizontalAlignment != null) {
				ab.setHorizontalAlignment(horizontalAlignment);
			}

			if (selectionBackcolor == null) {
				selectionBackcolor = backcolor;
			}
			if (selectionForecolor == null) {
				selectionForecolor = forecolor;
			}

			if (horizontalTextPosition != null) {
				ab.setHorizontalTextPosition(horizontalTextPosition);
			}
			ab.addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent e) {
					ab.setBackground(selectionBackcolor);
					ab.setForeground(selectionForecolor);
				}

				public void focusLost(FocusEvent e) {
					ab.setBackground(backcolor);
					ab.setForeground(forecolor);
				}
			});
		}

		if (c instanceof JList) {
			JList<?> jl = (JList<?>) c;
			jl.setSelectionBackground(selectionBackcolor);
			jl.setSelectionForeground(selectionForecolor);
			
			if (visibleRowCount != null) {
				jl.setVisibleRowCount(visibleRowCount);
			}
			
			if (layoutOrientation != null) {
				jl.setLayoutOrientation(layoutOrientation);
			}
		}

		if (c instanceof JScrollPane) {
			if (backcolor != null) {
				JScrollPane jsp = (JScrollPane) c;
				jsp.setBackground(backcolor);
			}
		}
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public void setBackcolor(Color backcolor) {
		this.backcolor = backcolor;
	}

	public void setForecolor(Color forecolor) {
		this.forecolor = forecolor;
	}

	public void setSelectionForecolor(Color selectionForecolor) {
		this.selectionForecolor = selectionForecolor;
	}

	public void setSelectionBackcolor(Color selectionBackcolor) {
		this.selectionBackcolor = selectionBackcolor;
	}

	public void setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
	}

	public void setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
	}

	public void setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	public void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}

	public void setBorder(Border border) {
		this.border = border;
	}

	public void setVerticalTextPosition(int verticalTextPosition) {
		this.verticalTextPosition = verticalTextPosition;
	}

	public void setHorizontalAlignment(int horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	public void setPadding(int top, int left, int bottom, int right) {
		paddingTop = top;
		paddingLeft = left;
		paddingBottom = bottom;
		paddingRight = right;
	}

	public void setClickedBorder(Border clickedBorder) {
		this.clickedBorder = clickedBorder;
	}

	public void setOpaque(Boolean opaque) {
		this.opaque = opaque;
	}

	public void setLayoutOrientation(Integer orientation) {
		this.layoutOrientation = orientation;
	}

	public void setVisibleRowCount(Integer count) {
		this.visibleRowCount = count;
	}

}
