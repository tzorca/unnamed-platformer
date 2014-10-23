package unnamed_platformer.view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import unnamed_platformer.app.InputManager;
import unnamed_platformer.app.InputManager.PlrGameKey;
import unnamed_platformer.app.Main;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.view.ViewManager;

public final class GUIHelper
{

	public interface ParamRunnable
	{
		public void run(Object param);
	}

	public static boolean confirmDangerousWithCallback(final String msg,
			final ParamRunnable paramRunnable) {
		Runnable dialogRunnable = new Runnable() {
			public void run() {
				boolean returnValue = confirmDangerous(msg);
				paramRunnable.run(returnValue);
			}
		};

		SwingUtilities.invokeLater(dialogRunnable);
		return false;
	}

	public static boolean confirmDangerous(final String msg) {
		Object[] options = { "Yes", "No" };
		boolean returnValue = JOptionPane.showOptionDialog(
				ViewManager.getFrame(), msg, Ref.APP_TITLE,
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,
				options, options[1]) == 0;
		return returnValue;
	}

	// TODO: Change box's title from "Input" to something that makes sense
	public static String getInput(String msg, String defaultVal) {
		String result = JOptionPane.showInputDialog(null, msg, defaultVal);
		return result != null ? result : "";
	}

	public static void removeButtonPadding(JButton button) {
		button.setBorder(null);
		button.setBorderPainted(false);
		button.setMargin(new Insets(0, 0, 0, 0));
	}

	public static void removeButtonPadding(List<JButton> buttons) {
		for (JButton button : buttons) {
			removeButtonPadding(button);
		}
	}

	public static void styleButton(final JButton button, final int paddingSize,
			Color backgroundColor) {
		styleComponentBorder(button, paddingSize, true);
		styleComponentColors(button, backgroundColor);
		button.setContentAreaFilled(false);
		button.setVerticalTextPosition(SwingConstants.CENTER);
		button.setOpaque(true);
	}

	// COLOR_LIGHT_GREY = new Color(0xee, 0xee, 0xee),
	// COLOR_DARK_BLUE_1 = new Color(0x10, 0x10, 0x20),
	// COLOR_DARK_BLUE_2 = new Color(0x20, 0x20, 0x30),
	// COLOR_DARK_BLUE_3 = new Color(0x30, 0x30, 0x40),
	// COLOR_DARK_BLUE_4 = new Color(0x40, 0x40, 0x50),
	// COLOR_HIGHLIGHT_BLUE = new Color(0x30, 0x30, 0x70);

	public static void styleComponentColors(final JComponent component,
			final Color backgroundColor) {

		boolean isDarkBG = (backgroundColor.getRed()
				+ backgroundColor.getBlue() + backgroundColor.getGreen()) / 3 < 127;

		component.setForeground(isDarkBG ? Color.WHITE : Color.BLACK);
		component.setBackground(backgroundColor);

		if (component instanceof JButton) {

			component.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					component.setBackground(highlight(backgroundColor
							.brighter()));
				}

				@Override
				public void focusLost(FocusEvent e) {
					component.setBackground(backgroundColor);
				}

			});
		}
	}

	public static void styleComponentBorder(JComponent component,
			int paddingSize, boolean clickable) {
		Border raisedBevelBorder = BorderFactory
				.createSoftBevelBorder(BevelBorder.RAISED);
		Border loweredBevelBorder = BorderFactory
				.createSoftBevelBorder(BevelBorder.LOWERED);

		Border paddingBorder = new EmptyBorder(paddingSize,
				(int) (paddingSize * 2.5), paddingSize,
				(int) (paddingSize * 2.5));

		final Border compoundLoweredBorder = BorderFactory
				.createCompoundBorder(loweredBevelBorder, paddingBorder);

		final Border compoundRaisedBorder = BorderFactory.createCompoundBorder(
				raisedBevelBorder, paddingBorder);

		if (clickable) {
			component.addMouseListener(new MouseAdapter() {

				public void mousePressed(MouseEvent e) {
					JComponent component = (JComponent) e.getSource();
					component.setBorder(compoundLoweredBorder);
				}

				public void mouseReleased(MouseEvent e) {
					JComponent component = (JComponent) e.getSource();
					component.setBorder(compoundRaisedBorder);
				}
			});
		}

		component.setBorder(clickable ? compoundRaisedBorder
				: compoundLoweredBorder);
	}

	public static void styleButtons(final List<JButton> buttons,
			final int paddingSize, final Color backgroundColor) {
		for (JButton button : buttons) {
			styleButton(button, paddingSize, backgroundColor);
		}
	}

	public static Color highlight(Color original) {
		float greyAvg = (original.getRed() + original.getGreen() + original
				.getBlue()) / 3 / 255f;

		float[] newColors = new float[3];
		newColors[0] = original.getRed() / 255f * 2.5f - greyAvg;
		newColors[1] = original.getGreen() / 255f * 2.5f - greyAvg;
		newColors[2] = original.getBlue() / 255f * 2.5f - greyAvg;

		// limit to valid range
		for (int i = 0; i < 3; i++) {
			if (newColors[i] > 1) {
				newColors[i] = 1;
			} else if (newColors[i] < 0) {
				newColors[i] = 0;
			}
		}

		return new Color(newColors[0], newColors[1], newColors[2]);
	}

	public static void toWidest(List<? extends JComponent> components) {
		int maxWidth = 0;

		for (JComponent c : components) {
			if (c.getPreferredSize().getWidth() > maxWidth) {
				maxWidth = (int) c.getPreferredSize().getWidth();
			}
		}

		for (JComponent c : components) {
			Dimension newSize = new Dimension(maxWidth, (int) c
					.getPreferredSize().getHeight());
			c.setPreferredSize(newSize);
			c.setSize(newSize);
			c.setMinimumSize(newSize);
		}
	}

	public static void addDirectionalNavigation(final List<JButton> buttons,
			final Runnable exitRunnable) {
		addDirectionalNavigation(buttons, exitRunnable, false);
	}

	public static void addDirectionalNavigation(final List<JButton> buttons,
			final Runnable exitRunnable, final boolean vertical) {
		for (int i = 0; i < buttons.size(); i++) {

			// Get current button
			final JButton currentButton = buttons.get(i);

			// Get previous and next buttons
			int prevIndex = i - 1;
			if (prevIndex < 0) {
				prevIndex = buttons.size() - 1;
			}
			final JButton prevButton = buttons.get(prevIndex);
			int nextIndex = i + 1;
			if (nextIndex >= buttons.size()) {
				nextIndex = 0;
			}
			final JButton nextButton = buttons.get(nextIndex);

			// Add directional navigation
			currentButton.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {

					Collection<PlrGameKey> gameKeys = InputManager
							.getGameKeysMatchingKeyEvent(e);

					// No matching keys
					if (gameKeys.size() == 0) {
						return;
					}

					for (PlrGameKey gk : gameKeys) {
						switch (gk.getGameKey()) {
						case A:
							currentButton.doClick();
							break;
						case B:
							if (exitRunnable != null) {
								exitRunnable.run();
							}
							break;
						case LEFT:
							if (!vertical) {
								prevButton.requestFocus();
							}
							break;
						case RIGHT:
							if (!vertical) {
								nextButton.requestFocus();
							}
							break;
						case UP:
							if (vertical) {
								prevButton.requestFocus();
							}
							break;
						case DOWN:
							if (vertical) {
								nextButton.requestFocus();
							}
							break;
						case EXIT:
							Main.doHalt();
						default:
							break;
						}
					}
				}
			});
		}
	}

}
