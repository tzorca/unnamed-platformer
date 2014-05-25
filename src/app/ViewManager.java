package app;

import game.entities.Entity;
import game.parameters.ContentRef.ContentType;
import game.parameters.Ref.Flag;
import game.parameters.ViewRef;
import game.structures.FlColor;
import game.structures.Graphic;
import gui.GUIManager;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;

import app.App.State;

public class ViewManager {

	private static JFrame parentFrame;
	private static Canvas lwjglCanvas;
	static {
		parentFrame = new JFrame("TODO: Title");
		parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		lwjglCanvas = new Canvas();
		lwjglCanvas.setSize(ViewRef.DEFAULT_RESOLUTION);
		parentFrame.add(lwjglCanvas);
		parentFrame.pack();
		parentFrame.setLocationRelativeTo(null);
		parentFrame.setVisible(true);
		try {
			Display.setParent(lwjglCanvas);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	static Texture background = null;
	public static int height = ViewRef.DEFAULT_RESOLUTION.height;
	public static int width = ViewRef.DEFAULT_RESOLUTION.width;

	private static Rectangle viewport = new Rectangle(0, 0,
			ViewRef.DEFAULT_RESOLUTION.width, ViewRef.DEFAULT_RESOLUTION.height);

	public static void centerCamera(Vector2f vector2f) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		float x = vector2f.x;
		float y = vector2f.y;

		int left = (int) (x - width / ViewRef.SCALE / 2);
		int right = (int) (width / ViewRef.SCALE / 2 + x);

		int bottom = (int) (height / ViewRef.SCALE / 2 + y);
		int top = (int) (y - height / ViewRef.SCALE / 2);

		viewport = new Rectangle(left, top, right - left, bottom - top);

		GL11.glOrtho(left, right, bottom, top, 1, -1);
	}

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

	public static void drawEntity(Entity entity) {
		if (entity.isFlagSet(Flag.invisible)) {
			return;
		}

		drawGraphic(entity.graphic, entity.getBox());
	}

	public static void drawGraphic(Graphic graphic,
			org.newdawn.slick.geom.Rectangle rectangle) {
		if (!rectangle.intersects(viewport)) {
			return;
		}

		float x = (float) (int) rectangle.getX();
		float y = (float) (int) rectangle.getY();
		float w = (float) (int) rectangle.getWidth();
		float h = (float) (int) rectangle.getHeight();

		FlColor color = graphic.color;
		Texture t = graphic.getTexture();

		if (color != null) {
			GL11.glColor4f(color.r(), color.g(), color.b(), color.a());
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

	public static void setColor(FlColor color) {

		GL11.glColor4f(color.r(), color.g(), color.b(), color.a());
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

	public static void drawTexturesInBatch(Texture t, List<Point> points) {
		t.bind();

		int w = t.getImageWidth();
		int h = t.getImageHeight();

		float tW = t.getWidth();
		float tH = t.getHeight();

		GL11.glBegin(GL11.GL_QUADS);
		for (Point p : points) {
			drawTex(t, p.getX(), p.getY(), w, h, tW, tH);
		}
		GL11.glEnd();
	}

	public static void drawEditGrid(int gridSize) {
		if (gridSize < 1) {
			return;
		}

		saveState();
		ArrayList<Point> pointBuffer = new ArrayList<Point>();
		Rectangle levelRect = LevelManager.getRect();
		for (int x = 0; x <= levelRect.getWidth(); x += gridSize) {
			for (int y = 0; y <= levelRect.getHeight(); y += gridSize) {
				Point p = new Point(x - 2, y - 2);
				if (viewport.contains(p.getX(), p.getY())) {
					pointBuffer.add(p);
				}
			}
		}

		Texture t = (Texture) ContentManager.get(ContentType.texture, "dot");
		drawTexturesInBatch(t, pointBuffer);
		loadState();
	}

	public static void init() {
		if (Display.isCreated()) {
			Display.destroy();
		}
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setFullscreen(fullscreen);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		setup();
	}

	private static void setup() {
		Display.setVSyncEnabled(true);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glViewport(0, 0, width, height);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public static void loadState() {
		GL11.glPopAttrib();
	}

	public static void saveState() {
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
	}

	public static Vector2f translate(java.awt.Point mousePos) {
		return new Vector2f(mousePos.x + viewport.getX(), mousePos.y
				+ viewport.getY());
	}

	public static Vector2f translateMouse() {
		return translate(InputManager.getMousePos());
	}

	public static void clear(FlColor c) {
		GL11.glClearColor(c.r(), c.g(), c.b(), c.a());
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public static void update() {
		if (App.state == State.play || App.state == State.edit) {

			Display.setTitle(GameManager.getGameName());

			GameManager.draw();
		}

		GUIManager.update();

		Display.update();
		Display.sync(ViewRef.FPS);
	}

	static boolean fullscreen = false;

	public static void toggleFullscreen() {
		fullscreen = !fullscreen;

		if (fullscreen) {
			Dimension screenRes = ViewRef.getScreenResolution();
			width = (int) screenRes.getWidth();
			height = (int) screenRes.getHeight();
		} else {
			width = ViewRef.DEFAULT_RESOLUTION.width;
			height = ViewRef.DEFAULT_RESOLUTION.height;
		}

		init();
	}

	public static void resetColor() {

		GL11.glColor4f(1, 1, 1, 1);
	}

	public static Rectangle getViewRect() {
		return viewport;
	}

	public static void drawGraphic(Graphic graphic, Rectangle2D rect2D) {
		drawGraphic(graphic, new Rectangle((float) rect2D.getX(),
				(float) rect2D.getY(), (float) rect2D.getWidth(),
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
}
