package unnamed_platformer.view;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.app.SQLiteStuff;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.view.gui.GUIManager;
import unnamed_platformer.view.gui.hud.HeadsUpDisplay;

public final class ViewManager
{
	final static private int BITS_PER_PIXEL = 4;
	public static final float SCALE = 1f;
	public static final int FPS = 60;
	public static final Dimension DEFAULT_RESOLUTION = new Dimension(900, 500);

	public static Dimension currentResolution = DEFAULT_RESOLUTION;

	private static boolean fullscreen;

	private static Panel guiPanel;
	private static JFrame parentFrame;

	private static Canvas renderCanvas;

	private static Rectangle viewport = new Rectangle(0, 0,
			ViewManager.DEFAULT_RESOLUTION.width,
			ViewManager.DEFAULT_RESOLUTION.height);

	public static class WindowEventHandler extends WindowAdapter
	{
		public void windowClosing(final WindowEvent event) {
			if (GUIManager.canExit()) {
				if (SQLiteStuff.isInitialized()) {
					SQLiteStuff.finish();
				}

				// system.exit(0) won't work here
				// there is some kind of deadlock
				Runtime.getRuntime().halt(0);
			}
		}

	}

	public static void centerCamera(final Vector2f location) {
		final float xPos = location.x;
		final float yPos = location.y;

		final int left = (int) (xPos - ViewManager.currentResolution.width
				/ ViewManager.SCALE / 2);
		final int top = (int) (yPos - ViewManager.currentResolution.height
				/ ViewManager.SCALE / 2);

		final int right = (int) (ViewManager.currentResolution.width
				/ ViewManager.SCALE / 2 + xPos);
		final int bottom = (int) (ViewManager.currentResolution.height
				/ ViewManager.SCALE / 2 + yPos);

		viewport.setBounds(left, top, right - left, bottom - top);

		if (Display.isActive()) {
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(left, right, bottom, top, 1, -1);
		}
	}

	public static void clearToColor(final Color color) {
		GL11.glClearColor(color.r, color.g, color.b, color.a);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	// TODO: Fix background tiling mechanism
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
		drawQuad(xPos, yPos, width, height, bgTexture.getWidth(),
				bgTexture.getHeight());
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
				pointBuffer.add(new int[] {
						x - 2, y - 2
				});
			}
		}

		final Texture texture = ResManager.get(Texture.class, "gui_dot");
		drawTexturesInBatch(texture, pointBuffer);
		loadState();
	}

	public static void drawGraphic(final Graphic graphic,
			final Rectangle rectangle) {
		if (!rectangle.intersects(viewport)) {
			return;
		}

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
					texture.getHeight());
			GL11.glEnd();
		}

	}

	private static void drawQuad(final float xPos, final float yPos,
			final float width, final float height, final float tWidth,
			final float tHeight) {
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(xPos, yPos);
		GL11.glTexCoord2f(tWidth, 0);
		GL11.glVertex2f(xPos + width, yPos);
		GL11.glTexCoord2f(tWidth, tHeight);
		GL11.glVertex2f(xPos + width, yPos + height);
		GL11.glTexCoord2f(0, tHeight);
		GL11.glVertex2f(xPos, yPos + height);

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

	// TODO: Implement working fullscreen/different window size functionality

	public static Dimension getScreenResolution() {
		final GraphicsDevice graphicsDevice = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		final java.awt.DisplayMode displayMode = graphicsDevice
				.getDisplayMode();

		return new Dimension(displayMode.getWidth(), displayMode.getHeight());
	}

	public static BufferedImage getScreenshot() {
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
				image.setRGB(x, height - y + 1, (0xFF << 24) | (red << 16)
						| (green << 8) | blue);
			}
		}

		return image;
	}

	public static void init() {
		parentFrame = new JFrame(Ref.APP_TITLE);
		parentFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// want to be able to handle close events as early as possible
		parentFrame.addWindowListener(new ViewManager.WindowEventHandler());

		parentFrame.setLayout(null);
		renderCanvas = new Canvas();
		renderCanvas.setSize(ViewManager.currentResolution);
		parentFrame.setLayout(new BorderLayout());
		guiPanel = new Panel();
		parentFrame.add(guiPanel);
		parentFrame.add(renderCanvas);
		parentFrame.pack();
		parentFrame.setLocationRelativeTo(null);
		parentFrame.setVisible(true);

		if (Display.isCreated()) {
			Display.destroy();
		}
		try {
			Display.setParent(renderCanvas);
			Display.setDisplayMode(new DisplayMode(
					ViewManager.currentResolution.width,
					ViewManager.currentResolution.height));
			Display.setFullscreen(fullscreen);
			Display.create();
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
				System.exit(0);
			}
		}

		setup();

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
		renderCanvas.setSize(ViewManager.currentResolution);
		renderCanvas.setLocation(0, 0);
	}

	public static void saveState() {
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
	}

	public static void setColor(final Color color) {
		GL11.glColor4f(color.r, color.g, color.b, color.a);
	}

	public static void setGUIPanel(final Panel panel) {
		parentFrame.remove(guiPanel);
		guiPanel = panel;
		parentFrame.add(guiPanel);
		parentFrame.validate();
	}

	public static void setRenderCanvasBounds(final int xPos, final int yPos,
			final int width, final int height) {
		renderCanvas.setBounds(xPos, yPos, width, height);
	}

	public static void setRenderCanvasVisibility(final boolean visibility) {
		renderCanvas.setVisible(visibility);
		if (visibility) {
			renderCanvas.requestFocus();
		}
	}

	private static void setup() {
		Display.setVSyncEnabled(true);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glViewport(0, 0, ViewManager.currentResolution.width,
				ViewManager.currentResolution.height);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, ViewManager.currentResolution.width,
				ViewManager.currentResolution.height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public static void update() {
		ViewManager.clear();

		GUIManager.update();
		World.drawBackground();
		GUIManager.drawBackground();
		World.drawForeground();
		GUIManager.drawForeground();
		HeadsUpDisplay.updateAndDraw();

		Display.sync(ViewManager.FPS);
		Display.update();
	}

	public static void clear() {
		clearToColor(Color.black);
	}

	public static Vector2f getCameraPos() {
		return viewport.getLocation();
	}

}
