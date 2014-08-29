package unnamed_platformer.app;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

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

import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.game.other.World;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.gui.GUIManager;
import unnamed_platformer.gui.GUIManager.ScreenType;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.structures.Graphic;

public class ViewManager {

	public static class WindowEventHandler extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
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

	static Texture background = null;

	public static final Dimension DEFAULT_RESOLUTION = new Dimension(960, 600);

	public static final int FPS = 60;
	
	static boolean fullscreen = false;

	private static Panel guiPanel;
	private static JFrame parentFrame;

	private static Canvas renderCanvas;

	public static final float SCALE = 1f;

	private static Rectangle viewport = new Rectangle(0, 0,
			ViewManager.DEFAULT_RESOLUTION.width, ViewManager.DEFAULT_RESOLUTION.height);


	private static TreeMap<Integer, List<Entity>> zIndexBuckets = new TreeMap<Integer, List<Entity>>();

	
	public static void centerCamera(Vector2f vector2f) {

		float x = vector2f.x;
		float y = vector2f.y;

		int left = (int) (x - ViewManager.DEFAULT_RESOLUTION.width / ViewManager.SCALE
				/ 2);
		int top = (int) (y - ViewManager.DEFAULT_RESOLUTION.height / ViewManager.SCALE
				/ 2);

		int right = (int) (ViewManager.DEFAULT_RESOLUTION.width / ViewManager.SCALE / 2 + x);
		int bottom = (int) (ViewManager.DEFAULT_RESOLUTION.height / ViewManager.SCALE
				/ 2 + y);

		viewport.setBounds(left, top, right - left, bottom - top);

		if (Display.isActive()) {
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(left, right, bottom, top, 1, -1);
		}
	}

	public static void clear(Color c) {
		GL11.glClearColor(c.r, c.g, c.b, c.a);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	// TODO: Fix background tiling mechanism
	public static void drawBG(Texture background) {
		float x, y, w, h;

		if (background == null) {
			return;
		}

		float tW = background.getWidth(), tH = background.getHeight();

		x = (float) (int) viewport.getX();
		y = (float) (int) viewport.getY()- (background.getTextureHeight()-viewport.getHeight());
		w = (float) background.getTextureWidth();
		h = (float) background.getTextureHeight();

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
				GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
				GL11.GL_REPEAT);

		background.bind();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(x, y);
		GL11.glTexCoord2f(tW, 0);
		GL11.glVertex2f(x + w, y);
		GL11.glTexCoord2f(tW, tH);
		GL11.glVertex2f(x + w, y + h);
		GL11.glTexCoord2f(0, tH);
		GL11.glVertex2f(x, y + h);
		GL11.glEnd();
	}

	// TODO: Fix bug in grid transparency when no objects displayed
	public static void drawEditorGrid(int gridSize) {
		if (gridSize < 1) {
			return;
		}

		saveState();
		ArrayList<int[]> pointBuffer = new ArrayList<int[]>();
		Rectangle levelRect = World.getLevelRect();

		int minX = (int) viewport.getMinX(), maxX = (int) viewport.getMaxX(), minY = (int) viewport
				.getMinY(), maxY = (int) viewport.getMaxY();

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

		Texture t = ResManager.get(Texture.class, "gui_dot");
		drawTexturesInBatch(t, pointBuffer);
		loadState();
	}

	public static void drawEntities(LinkedList<Entity> entities) {
		zIndexBuckets.clear();

		for (Entity e : entities) {
			if (!zIndexBuckets.containsKey(e.zIndex)) {
				zIndexBuckets.put(e.zIndex, new LinkedList<Entity>());
			}
			zIndexBuckets.get(e.zIndex).add(e);
		}

		for (int zIndex : zIndexBuckets.keySet()) {
			for (Entity e : zIndexBuckets.get(zIndex)) {
				if (e.isFlagSet(Flag.INVISIBLE)) {
					return;
				}

				drawGraphic(e.graphic, e.getOriginalBox());
			}
		}
	}

	public static void drawGraphic(Graphic graphic, Rectangle rectangle) {
		if (!rectangle.intersects(viewport)) {
			return;
		}

		float x = (float) (int) rectangle.getX();
		float y = (float) (int) rectangle.getY();
		float w = (float) (int) rectangle.getWidth();
		float h = (float) (int) rectangle.getHeight();

		Color color = graphic.color;
		Texture t = graphic.getTexture();

		if (color != null) {
			GL11.glColor4f(color.r, color.g, color.b, color.a);
		} else {
			resetColor();
		}

		if (t != null) {
			t.bind();

			GL11.glBegin(GL11.GL_QUADS);
			drawTex(t, x, y, w, h, t.getWidth(), t.getHeight());
			GL11.glEnd();
		} else {
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(x, y);
			GL11.glVertex2f(x + w, y);
			GL11.glVertex2f(x + w, y + h);
			GL11.glVertex2f(x, y + h);
			GL11.glEnd();
		}

	}

	public static void drawGraphic(Graphic graphic, Rectangle2D rect2D) {
		drawGraphic(graphic, new Rectangle((float) rect2D.getX(),
				(float) rect2D.getY(), (float) rect2D.getWidth(),
				(float) rect2D.getHeight()));

	}

	private static void drawTex(Texture t, float x, float y, float w, float h,
			float tW, float tH) {
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(x, y);
		GL11.glTexCoord2f(tW, 0);
		GL11.glVertex2f(x + w, y);
		GL11.glTexCoord2f(tW, tH);
		GL11.glVertex2f(x + w, y + h);
		GL11.glTexCoord2f(0, tH);
		GL11.glVertex2f(x, y + h);

	}

	public static void drawTexturesInBatch(Texture t,
			ArrayList<int[]> pointBuffer) {
		t.bind();

		int w = t.getImageWidth();
		int h = t.getImageHeight();

		float tW = t.getWidth();
		float tH = t.getHeight();

		GL11.glBegin(GL11.GL_QUADS);
		for (int[] point : pointBuffer) {
			drawTex(t, point[0], point[1], w, h, tW, tH);
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

	public static Panel getGUIPanel() {
		return guiPanel;
	}

	public static Object getRenderCanvas() {
		return renderCanvas;
	}

	// TODO: Remove references to ViewManager.DEFAULT_RESOLUTION...
	// Keep in mind that the internal render resolution should stay the same
	// But it will not be equivalent to the renderCanvas resolution

	public static float getRenderCanvasX() {
		return renderCanvas.getX();
	}

	// TODO: Implement working fullscreen/different window size functionality

	public static int getRenderCanvasY() {
		return renderCanvas.getY();
	}

	public static Dimension getScreenResolution() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		return new Dimension(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight());
	}

	public static BufferedImage getScreenshot() {
		GL11.glReadBuffer(GL11.GL_FRONT);
		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();
		int bpp = 4;
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, buffer);

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int i = (x + (width * y)) * bpp;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16)
						| (g << 8) | b);
			}
		}

		return image;
	}

	public static float getViewportX() {
		return viewport.getX();
	}

	public static float getViewportY() {
		return viewport.getY();
	}

	public static void init() {
		parentFrame = new JFrame(Ref.APP_TITLE);
		parentFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		// want to be able to handle close events as early as possible
		parentFrame.addWindowListener(new ViewManager.WindowEventHandler());
		
		parentFrame.setLayout(null);
		renderCanvas = new Canvas();
		renderCanvas.setSize(ViewManager.DEFAULT_RESOLUTION);
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
					ViewManager.DEFAULT_RESOLUTION.width,
					ViewManager.DEFAULT_RESOLUTION.height));
			Display.setFullscreen(fullscreen);
			Display.create();
		} catch (LWJGLException e) {
			// This exception typically occurs because pixel acceleration
			// is not supported. Software mode works as a (slow) workaround.
			System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL",
					"true");
			try {
				System.out.println("Warning: Defaulting to software mode. Performance may be suboptimal.");
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

	@SuppressWarnings("unused")
	private static void printRect(Rectangle rect) {
		System.out.println(rect.getX() + "," + rect.getY() + ","
				+ rect.getWidth() + "," + rect.getHeight());
	}

	public static boolean rectInView(Rectangle r) {
		return r.intersects(viewport);
	}

	public static void resetColor() {

		GL11.glColor4f(1, 1, 1, 1);
	}

	public static void resetRenderCanvasBounds() {
		renderCanvas.setSize(ViewManager.DEFAULT_RESOLUTION);
		renderCanvas.setLocation(0, 0);
	}

	public static void saveState() {
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
	}

	public static void setColor(Color color) {
		GL11.glColor4f(color.r, color.g, color.b, color.a);
	}

	public static void setGUIPanel(Panel panel) {
		parentFrame.remove(guiPanel);
		guiPanel = panel;
		parentFrame.add(guiPanel);
		parentFrame.validate();
	}

	public static void setRenderCanvasBounds(int x, int y, int width, int height) {
		renderCanvas.setBounds(x, y, width, height);
	}

	public static void setRenderCanvasVisibility(boolean b) {
		renderCanvas.setVisible(b);
		if (b) {
			renderCanvas.requestFocus();
		}
	}

	private static void setup() {
		Display.setVSyncEnabled(true);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glViewport(0, 0, ViewManager.DEFAULT_RESOLUTION.width,
				ViewManager.DEFAULT_RESOLUTION.height);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, ViewManager.DEFAULT_RESOLUTION.width,
				ViewManager.DEFAULT_RESOLUTION.height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public static void update() {
		// TODO: Improve state logic here
		if (GUIManager.atScreen(ScreenType.Play)
				|| GUIManager.atScreen(ScreenType.Edit)) {
			Display.setTitle(World.getName());

			World.draw();
		}
		GUIManager.update();

		Display.sync(ViewManager.FPS);
		Display.update();
	}

}
