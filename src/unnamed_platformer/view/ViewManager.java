package unnamed_platformer.view;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.app.FileHelper;
import unnamed_platformer.app.ImageHelper;
import unnamed_platformer.app.Main;
import unnamed_platformer.app.Settings;
import unnamed_platformer.app.Settings.SettingName;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.res_mgt.types.GUI_Texture;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.hud.HUDController;

public final class ViewManager
{
	// constants
	private static final int BITS_PER_PIXEL = 4;
	public static final float SCALE = 1f;
	public static final int FPS = 60;
	public static final Dimension DEFAULT_RESOLUTION = new Dimension(900, 500);

	// GUI-related
	private static Panel guiPanel;
	private static JFrame parentFrame;
	private static Canvas renderCanvas;

	public static Dimension currentResolution = DEFAULT_RESOLUTION;

	private static boolean fullscreenValue;

	private static Rectangle viewport = new Rectangle(0, 0,
			DEFAULT_RESOLUTION.width, DEFAULT_RESOLUTION.height);

	public static class WindowEventHandler extends WindowAdapter
	{
		public void windowClosing(final WindowEvent event) {
			if (GUIManager.canExit()) {
				// system.exit(0) won't work here (causes deadlock)
				Main.doHalt();
			}
		}
	}

	public static void centerCamera(final Vector2f location,
			Rectangle levelBounds) {
		final float x = location.x;
		final float y = location.y;

		float scaledHalfWidth = currentResolution.width / SCALE / 2;
		float scaledHalfHeight = currentResolution.height / SCALE / 2;

		int left = (int) (x - scaledHalfWidth);
		int top = (int) (y - scaledHalfHeight);

		if (levelBounds != null) {
			left = (int) Math.max(left, levelBounds.getMinX());
			top = (int) Math.max(top, levelBounds.getMinY());
		}

		viewport.setBounds(left, top, scaledHalfWidth * 2, scaledHalfHeight * 2);

		int right = (int) viewport.getMaxX();
		int bottom = (int) viewport.getMaxY();

		if (Display.isActive()) {
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(left, right, bottom, top, 1, -1);
		}
	}

	public static void drawBG(final Texture bgTexture) {
		if (bgTexture == null) {
			return;
		}

		final float xPos = (int) viewport.getX();
		final float yPos = (int) viewport.getY()
				- (bgTexture.getTextureHeight() - viewport.getHeight());
		final float width = bgTexture.getTextureWidth();
		final float height = bgTexture.getTextureHeight();

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
				GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
				GL11.GL_REPEAT);

		resetColor();
		bgTexture.bind();
		GL11.glBegin(GL11.GL_QUADS);

		// Calculate how many times bigger the viewport is than the texture
		float wTileAmount = viewport.getWidth() / width;
		float hTileAmount = viewport.getHeight() / height;

		if (wTileAmount < 1) {
			wTileAmount = 1;
		}
		if (hTileAmount < 1) {
			hTileAmount = 1;
		}

		drawQuad(xPos, yPos, width * wTileAmount, height * hTileAmount,
				bgTexture.getWidth() * wTileAmount, bgTexture.getHeight()
						* hTileAmount);
		GL11.glEnd();
	}

	public static void drawGrid(final int gridSize, final Color color) {
		if (gridSize < 1) {
			return;
		}
		saveState();

		if (color == null) {
			resetColor();
		} else {
			GL11.glColor4f(color.r, color.g, color.b, color.a);
		}
		final ArrayList<int[]> pointBuffer = new ArrayList<int[]>();
		final Rectangle levelRect = World.getLevelRect();

		final int minX = (int) viewport.getMinX();
		final int maxX = (int) viewport.getMaxX();
		final int minY = (int) viewport.getMinY();
		final int maxY = (int) viewport.getMaxY();

		for (int x = 0; x <= levelRect.getWidth(); x += gridSize) {
			if (x < minX || x > maxX) {
				continue;
			}
			for (int y = 0; y <= levelRect.getHeight(); y += gridSize) {
				if (y < minY || y > maxY) {
					continue;
				}
				pointBuffer.add(new int[] { x - 2, y - 2 });
			}
		}

		final Texture texture = ResManager.get(GUI_Texture.class, "gui_dot");
		drawTexturesInBatch(texture, pointBuffer);
		loadState();
	}

	public static boolean isFullscreen() {
		return fullscreenValue;
	}

	public static void drawGraphic(final Graphic graphic,
			final Rectangle rectangle) {
		drawGraphic(graphic, rectangle, false);
	}

	public static void drawGraphic(final Graphic graphic,
			final Rectangle rectangle, final boolean horzFlip) {
		if (!rectangle.intersects(viewport)) {
			return;
		}

		// quantize floats before drawing
		final float xPos = (float) (int) rectangle.getX();
		final float yPos = (float) (int) rectangle.getY();
		final float width = (float) (int) rectangle.getWidth();
		final float height = (float) (int) rectangle.getHeight();

		final Color color = graphic.color;
		final Texture texture = graphic.getTexture();

		if (color == null) {
			resetColor();
		} else {
			GL11.glColor4f(color.r, color.g, color.b, color.a);
		}

		if (texture == null) {
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(xPos, yPos);
			GL11.glVertex2f(xPos + width, yPos);
			GL11.glVertex2f(xPos + width, yPos + height);
			GL11.glVertex2f(xPos, yPos + height);
			GL11.glEnd();
		} else {
			texture.bind();
			GL11.glBegin(GL11.GL_QUADS);
			drawQuad(xPos, yPos, width, height, texture.getWidth(),
					texture.getHeight(), horzFlip);
			GL11.glEnd();
		}
	}

	private static void drawQuad(final float xPos, final float yPos,
			final float width, final float height, final float tWidth,
			final float tHeight) {
		drawQuad(xPos, yPos, width, height, tWidth, tHeight, false);
	}

	private static void drawQuad(final float xPos, final float yPos,
			final float width, final float height, final float tWidth,
			final float tHeight, boolean horzFlip) {

		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(horzFlip ? xPos + width : xPos, yPos);
		GL11.glTexCoord2f(tWidth, 0);
		GL11.glVertex2f(horzFlip ? xPos : xPos + width, yPos);
		GL11.glTexCoord2f(tWidth, tHeight);
		GL11.glVertex2f(horzFlip ? xPos : xPos + width, yPos + height);
		GL11.glTexCoord2f(0, tHeight);
		GL11.glVertex2f(horzFlip ? xPos + width : xPos, yPos + height);
	}

	public static void drawTexturesInBatch(final Texture texture,
			final List<int[]> pointBuffer) {
		texture.bind();

		final int imageWidth = texture.getImageWidth();
		final int imageHeight = texture.getImageHeight();

		final float tWidth = texture.getWidth();
		final float tHeight = texture.getHeight();

		GL11.glBegin(GL11.GL_QUADS);
		for (final int[] point : pointBuffer) {
			drawQuad(point[0], point[1], imageWidth, imageHeight, tWidth,
					tHeight);
		}
		GL11.glEnd();
	}

	public static void focusRenderCanvas() {
		if (!renderCanvas.hasFocus()) {
			renderCanvas.requestFocusInWindow();
		}
	}

	public static JFrame getFrame() {
		return parentFrame;
	}

	// Keep in mind that the internal render resolution should stay the same
	// But it will not be equivalent to the renderCanvas resolution

	public static Dimension getScreenResolution() {
		final GraphicsDevice graphicsDevice = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		final java.awt.DisplayMode displayMode = graphicsDevice
				.getDisplayMode();

		return new Dimension(displayMode.getWidth(), displayMode.getHeight());
	}

	private static final Pattern PATTERN_SCREENSHOT_FILENAME = Pattern
			.compile("scr\\d{4}\\.png");

	private static final int PREVIEW_IMAGE_WIDTH = 256;

	public static void saveScreenshot() {
		String newFilename = FileHelper
				.getScreenshotFilename(PATTERN_SCREENSHOT_FILENAME);
		if (newFilename == null) {
			return;
		}

		BufferedImage image = ViewManager.getScreenshot();

		File screenshotFile = new File(newFilename);
		try {
			ImageIO.write(image, "PNG", screenshotFile);
		} catch (IOException e) {
			System.out.println("Screenshot failed: " + e.getMessage());
			e.printStackTrace();
			return;
		}

		System.out.println("Saved screenshot to " + screenshotFile.getName());

	}

	public static void savePreviewImage() {
		// Not currently playing
		if (!World.playing()) {
			return;
		}

		final File previewImageFile = new File(ResManager.getFilename(
				ImageIcon.class, World.getName()));

		// Preview image already exists
		if (previewImageFile.exists()) {
			return;
		}

		doWhenRendering(new Runnable() {
			public void run() {
				try {

					final BufferedImage previewImage = ImageHelper
							.resizeToWidth(ViewManager.getScreenshot(),
									PREVIEW_IMAGE_WIDTH);

					ImageIO.write(previewImage, "PNG", previewImageFile);
					System.out.println("Saved preview image to "
							+ previewImageFile.getName());
				} catch (IOException e) {
					System.out.println("Saving preview image failed: "
							+ e.getMessage());
					e.printStackTrace();
				}
			}
		});

	}

	private static Queue<Runnable> whileRenderingRunnables = new LinkedList<Runnable>();
	private static Queue<Runnable> whileActiveRunnables = new LinkedList<Runnable>();

	/**
	 * Schedules a method to be executed while display is rendering
	 * 
	 * @param runnable
	 */
	public static void doWhenRendering(Runnable runnable) {
		whileRenderingRunnables.add(runnable);
	}
	
	/**
	 * Schedules a method to be executed while display thread is active
	 * 
	 * @param runnable
	 */
	public static void doWhenActive(Runnable runnable) {
		whileActiveRunnables.add(runnable);

	}

	public static BufferedImage getScreenshot() {
		if (fullscreenValue) {
			return getScreenshotViaRobotScreenCapture();
		} else {
			return getScreenshotViaGLRead();
		}
	}

	private static BufferedImage getScreenshotViaRobotScreenCapture() {
		java.awt.Rectangle displayRect = new java.awt.Rectangle(Display.getX(),
				Display.getY(), Display.getWidth(), Display.getHeight());

		BufferedImage screenshot = null;
		try {
			screenshot = new Robot().createScreenCapture(displayRect);
		} catch (AWTException e) {
			System.err.println("Could not get screenshot.");
			e.printStackTrace();
		}
		return screenshot;
	}

	@SuppressWarnings("unused")
	private static BufferedImage getScreenshotViaPrintscreenButton() {
		try {
			Robot robot;

			robot = new Robot();

			robot.keyPress(KeyEvent.VK_PRINTSCREEN);
			robot.delay(40);
			robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
			robot.delay(40);

			Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard()
					.getContents(null);

			if (t != null && t.isDataFlavorSupported(DataFlavor.imageFlavor)) {
				Image image = (Image) t.getTransferData(DataFlavor.imageFlavor);
				return ImageHelper.toBufferedImage(image);
			}

		} catch (Exception e) {
			System.err.println("Could not get screenshot.");
			e.printStackTrace();
		}
		return null;
	}

	private static BufferedImage getScreenshotViaGLRead() {
		GL11.glReadBuffer(GL11.GL_FRONT);
		final int width = Display.getDisplayMode().getWidth();
		final int height = Display.getDisplayMode().getHeight();
		final ByteBuffer buffer = BufferUtils.createByteBuffer(width * height
				* BITS_PER_PIXEL);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, buffer);

		final BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				final int index = (x + width * y) * BITS_PER_PIXEL;
				final int red = buffer.get(index) & 0xFF;
				final int green = buffer.get(index + 1) & 0xFF;
				final int blue = buffer.get(index + 2) & 0xFF;
				image.setRGB(x, height - (y + 1), (0xFF << 24) | (red << 16)
						| (green << 8) | blue);
			}
		}

		return image;
	}

	public static void loadState() {
		GL11.glPopAttrib();
	}

	public static boolean rectInView(final Rectangle rect) {
		return rect.intersects(viewport);
	}

	public static void resetColor() {
		GL11.glColor4f(1, 1, 1, 1);
	}

	public static void resetRenderCanvasBounds() {
		renderCanvas.setSize(currentResolution);
		renderCanvas.setLocation(0, 0);
	}

	public static void saveState() {
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
	}

	public static void setColor(final Color color) {
		GL11.glColor4f(color.r, color.g, color.b, color.a);
	}

	public static void changeGUIPanel(final Panel panel) {
		parentFrame.remove(guiPanel);
		guiPanel = panel;
		parentFrame.add(guiPanel);
		parentFrame.validate();
		guiPanel.validate();
	}

	public static void setRenderCanvasBounds(final int xPos, final int yPos,
			final int width, final int height) {
		renderCanvas.setBounds(xPos, yPos, width, height);
	}

	public static void setRenderCanvasVisibility(final boolean visibility) {
		renderCanvas.setVisible(visibility);
		if (visibility) {
			renderCanvas.requestFocusInWindow();
		} else {
			// Workaround to prevent opengl from stealing view when not supposed
			// to
			renderCanvas.setBounds(0, 0, 0, 0);
		}
	}

	public static void init() {
		setFullscreen(Settings.getBoolean(SettingName.DEFAULTS_TO_FULLSCREEN));

		HUDController.init();

		if (Settings.getBoolean(SettingName.HIDE_CURSOR)) {
			hideCursor();
		}
	}

	private static void hideCursor() {
		// LWJGL
		Cursor blankLWJGLCursor;
		try {
			blankLWJGLCursor = new Cursor(1, 1, 0, 0, 1,
					BufferUtils.createIntBuffer(1), null);
			Mouse.setNativeCursor(blankLWJGLCursor);
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Swing
		BufferedImage blankCursorImage = new BufferedImage(16, 16,
				BufferedImage.TYPE_INT_ARGB);
		java.awt.Cursor blankSwingCursor = Toolkit.getDefaultToolkit()
				.createCustomCursor(blankCursorImage, new java.awt.Point(0, 0),
						"blank cursor");
		parentFrame.getContentPane().setCursor(blankSwingCursor);
	}

	private static void reinitFrame() {
		if (parentFrame != null) {
			parentFrame.setVisible(false);
			parentFrame.removeAll();
			parentFrame.dispose();
		}

		parentFrame = new JFrame(Ref.APP_TITLE);
		parentFrame.setBackground(java.awt.Color.black);
		parentFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// should be able to handle close events as early as possible
		parentFrame.addWindowListener(new WindowEventHandler());

		if (fullscreenValue) {
			parentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			parentFrame.setUndecorated(true);
		} else {
			parentFrame.setExtendedState(JFrame.NORMAL);
			parentFrame.setUndecorated(false);
		}
	}

	public static void setFullscreen(boolean newFullscreenValue) {
		fullscreenValue = newFullscreenValue;
		reinitFrame();
		changeResolution(fullscreenValue ? getScreenResolution()
				: DEFAULT_RESOLUTION);
		parentFrame.setLocationRelativeTo(null);

		parentFrame.toFront();
		parentFrame.requestFocusInWindow();

		// Workaround to prevent opengl from stealing view when not supposed to
		setRenderCanvasVisibility(renderCanvas.isVisible());
	}

	// Note: If fullscreen is set, newResolution will be overriden
	public static void changeResolution(Dimension newResolution) {
		currentResolution = newResolution;
		setupView();
	}

	private static void setupView() {
		parentFrame.setLayout(null);
		if (guiPanel == null) {
			guiPanel = new Panel();
			renderCanvas = new Canvas();
		}
		parentFrame.setLayout(new BorderLayout());
		parentFrame.add(guiPanel);
		parentFrame.add(renderCanvas);

		renderCanvas.setBackground(java.awt.Color.black);
		renderCanvas.setSize(currentResolution);

		parentFrame.pack();
		parentFrame.setLocationRelativeTo(null);
		parentFrame.setVisible(true);

		try {
			Display.setParent(renderCanvas);
			Display.setFullscreen(fullscreenValue);
			Display.setDisplayMode(new DisplayMode(currentResolution.width,
					currentResolution.height));

			if (!Display.isCreated()) {
				Display.create();
			}
		} catch (LWJGLException e) {
			// This exception typically occurs because pixel acceleration
			// is not supported. Software mode works as a (slow) workaround.
			System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL",
					"true");
			try {
				System.out
						.println("Warning: Defaulting to software mode. Performance may be suboptimal.");
				Display.create();
			} catch (LWJGLException e2) {
				e2.printStackTrace();
				Main.doExit();
			}
		}

		Display.setVSyncEnabled(true);

		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glViewport(0, 0, currentResolution.width, currentResolution.height);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, currentResolution.width, currentResolution.height, 0,
				1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public static void update() {
		ViewManager.clear();

		GUIManager.update();
		World.drawBackground();
		GUIManager.drawBackground();
		World.drawForeground();
		GUIManager.drawForeground();
		HUDController.updateAndDraw();

		Display.sync(FPS);
		Display.update();

		if (Display.isActive()) {
			while (!whileActiveRunnables.isEmpty()) {
				whileActiveRunnables.poll().run();
			}
			if (Display.isVisible() && Display.getWidth() > 0
					&& Display.getHeight() > 0) {
				while (!whileRenderingRunnables.isEmpty()) {
					whileRenderingRunnables.poll().run();
				}
			}
		}
	}

	public static void clear() {
		GL11.glClearColor(0f, 0f, 0f, 0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public static Vector2f getCameraPos() {
		return viewport.getLocation();
	}

	public static void toggleFullscreen() {
		setFullscreen(!isFullscreen());
	}

	public static Rectangle getViewport() {
		return new Rectangle(viewport.getX(), viewport.getY(),
				viewport.getWidth(), viewport.getHeight());
	}

	public static void validate() {
		parentFrame.validate();
		parentFrame.repaint();
	}

	public static boolean renderCanvasVisible() {
		return renderCanvas.hasFocus();
	}

}
