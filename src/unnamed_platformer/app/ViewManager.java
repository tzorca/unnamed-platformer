package unnamed_platformer.app;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Panel;
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

import unnamed_platformer.app.Main.State;
import unnamed_platformer.game.entities.Entity;
import unnamed_platformer.globals.Ref;
import unnamed_platformer.globals.Ref.Flag;
import unnamed_platformer.globals.ViewRef;
import unnamed_platformer.gui.GUIManager;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.structures.Graphic;

public class ViewManager {

	private static JFrame parentFrame;
	private static Canvas renderCanvas;
	private static Panel guiPanel;

	public static void init() {
		parentFrame = new JFrame(Ref.APP_TITLE);
		parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		parentFrame.setLayout(null);
		renderCanvas = new Canvas();
		renderCanvas.setSize(ViewRef.DEFAULT_RESOLUTION);
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
			Display.setDisplayMode(new DisplayMode(ViewRef.DEFAULT_RESOLUTION.width, ViewRef.DEFAULT_RESOLUTION.height));
			Display.setFullscreen(fullscreen);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		setup();
	}

	public static void update() {
		if (Main.state == State.Play || Main.state == State.Edit) {
			Display.setTitle(GameManager.getGameName());

			GameManager.draw();
		}
		GUIManager.update();

		Display.sync(ViewRef.FPS);
		Display.update();
	}

	static Texture background = null;

	private static Rectangle viewport = new Rectangle(0, 0, ViewRef.DEFAULT_RESOLUTION.width,
			ViewRef.DEFAULT_RESOLUTION.height);

	public static void centerCamera(Vector2f vector2f) {

		float x = vector2f.x;
		float y = vector2f.y;

		int left = (int) (x - ViewRef.DEFAULT_RESOLUTION.width / ViewRef.SCALE / 2);
		int top = (int) (y - ViewRef.DEFAULT_RESOLUTION.height / ViewRef.SCALE / 2);

		int right = (int) (ViewRef.DEFAULT_RESOLUTION.width / ViewRef.SCALE / 2 + x);
		int bottom = (int) (ViewRef.DEFAULT_RESOLUTION.height / ViewRef.SCALE / 2 + y);

		viewport.setBounds(left, top, right - left, bottom - top);

		if (Display.isActive()) {
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(left, right, bottom, top, 1, -1);
		}
	}

	// TODO: Fix background tiling mechanism
	public static void drawBG(Texture background) {
		float x, y, w, h;

		if (background == null) {
			return;
		}

		float tW = background.getWidth(), tH = background.getHeight();

		x = (float) (int) viewport.getX();
		y = (float) (int) viewport.getY();
		w = (float) background.getTextureWidth();
		h = (float) background.getTextureHeight();

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

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

	public static void drawEntity(Entity entity) {
		if (entity.isFlagSet(Flag.invisible)) {
			return;
		}

		drawGraphic(entity.graphic, entity.getBox());
	}

	@SuppressWarnings("unused")
	private static void printRect(Rectangle rect) {
		System.out.println(rect.getX() + "," + rect.getY() + "," + rect.getWidth() + "," + rect.getHeight());
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

	public static void setColor(Color color) {
		GL11.glColor4f(color.r, color.g, color.b, color.a);
	}

	private static void drawTex(Texture t, float x, float y, float w, float h, float tW, float tH) {
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(x, y);
		GL11.glTexCoord2f(tW, 0);
		GL11.glVertex2f(x + w, y);
		GL11.glTexCoord2f(tW, tH);
		GL11.glVertex2f(x + w, y + h);
		GL11.glTexCoord2f(0, tH);
		GL11.glVertex2f(x, y + h);

	}

	public static void drawTexturesInBatch(Texture t, ArrayList<int[]> pointBuffer) {
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

	// TODO: Fix bug in grid transparency when no objects displayed
	public static void drawEditorGrid(int gridSize) {
		if (gridSize < 1) {
			return;
		}

		saveState();
		ArrayList<int[]> pointBuffer = new ArrayList<int[]>();
		Rectangle levelRect = GameManager.getRect();

		int minX = (int) viewport.getMinX(), maxX = (int) viewport.getMaxX(), minY = (int) viewport.getMinY(), maxY = (int) viewport
				.getMaxY();
		
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

	private static void setup() {
		Display.setVSyncEnabled(true);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glViewport(0, 0, ViewRef.DEFAULT_RESOLUTION.width, ViewRef.DEFAULT_RESOLUTION.height);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, ViewRef.DEFAULT_RESOLUTION.width, ViewRef.DEFAULT_RESOLUTION.height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public static void loadState() {
		GL11.glPopAttrib();
	}

	public static void saveState() {
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
	}

	public static void clear(Color c) {
		GL11.glClearColor(c.r, c.g, c.b, c.a);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	// TODO: Remove references to ViewRef.DEFAULT_RESOLUTION...
	// Keep in mind that the internal render resolution should stay the same
	// But it will not be equivalent to the renderCanvas resolution

	static boolean fullscreen = false;

	// TODO: Implement working fullscreen/different window size functionality

	public static void resetColor() {

		GL11.glColor4f(1, 1, 1, 1);
	}

	public static void drawGraphic(Graphic graphic, Rectangle2D rect2D) {
		drawGraphic(graphic, new Rectangle((float) rect2D.getX(), (float) rect2D.getY(), (float) rect2D.getWidth(),
				(float) rect2D.getHeight()));

	}

	private static TreeMap<Integer, List<Entity>> zIndexBuckets = new TreeMap<Integer, List<Entity>>();

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
				drawEntity(e);
			}
		}
	}

	public static void setRenderCanvasVisibility(boolean b) {
		renderCanvas.setVisible(b);
		if (b) {
			renderCanvas.requestFocus();
		}
	}

	public static void setGUIPanel(Panel panel) {
		parentFrame.remove(guiPanel);
		guiPanel = panel;
		parentFrame.add(guiPanel);
		parentFrame.validate();
	}

	public static Panel getGUIPanel() {
		return guiPanel;
	}

	public static JFrame getFrame() {
		return parentFrame;
	}

	public static BufferedImage getScreenshot() {
		GL11.glReadBuffer(GL11.GL_FRONT);
		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();
		int bpp = 4;
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int i = (x + (width * y)) * bpp;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
			}
		}

		return image;
	}

	public static void resetRenderCanvasBounds() {
		renderCanvas.setSize(ViewRef.DEFAULT_RESOLUTION);
		renderCanvas.setLocation(0, 0);
	}

	public static void setRenderCanvasBounds(int x, int y, int width, int height) {
		renderCanvas.setBounds(x, y, width, height);
	}

	public static boolean rectInView(Rectangle r) {
		return viewport.intersects(r) || viewport.contains(r);
	}

	public static float getViewportX() {
		return viewport.getX();
	}

	public static float getViewportY() {
		return viewport.getY();
	}

	public static int getRenderCanvasY() {
		return renderCanvas.getY();
	}

	public static float getRenderCanvasX() {
		return renderCanvas.getX();
	}

	public static void focusRenderCanvas() {
		if (!renderCanvas.hasFocus()) {
			renderCanvas.requestFocusInWindow();
		}
	}

	public static Object getRenderCanvas() {
		return renderCanvas;
	}

}
