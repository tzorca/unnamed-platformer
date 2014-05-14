package app;

import game.entities.Entity;
import game.parameters.ViewRef;
import game.parameters.ContentRef.ContentType;
import game.parameters.Ref.Flag;
import game.structures.FlColor;
import game.structures.Graphic;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import app.App.State;
import app.gui.GUIManager;

public class ViewManager {
	// TODO: Fix bug in grid transparency

	static Texture background = null;
	public static int height = ViewRef.DEFAULT_RESOLUTION.y;
	public static int width = ViewRef.DEFAULT_RESOLUTION.x;

	private static Rectangle viewport = new Rectangle();

	public static void centerCamera(Point center) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		int left = (int) (center.x - width / ViewRef.SCALE / 2);
		int right = (int) (width / ViewRef.SCALE / 2 + center.x);

		int bottom = (int) (height / ViewRef.SCALE / 2 + center.y);
		int top = (int) (center.y - height / ViewRef.SCALE / 2);

		viewport = new Rectangle(left, top, right - left, bottom - top);

		GL11.glOrtho(left, right, bottom, top, 1, -1);

	}

	public static void drawBG(Texture background) {

		float x, y, w, h;

		if (background == null) {
			return;
		}

		float tW = background.getWidth(), tH = background.getHeight();

		x = (float) viewport.x;
		y = (float) viewport.y;
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
		if (entity.checkFlag(Flag.invisible)) {
			return;
		}

		drawGraphic(entity.graphic, entity.getBox());
	}

	public static void drawGraphic(Graphic graphic, Rectangle box) {
		if (!box.intersects(viewport)) {
			return;
		}

		float x = (float) box.getX(), y = (float) box.getY(), w = (float) box
				.getWidth(), h = (float) box.getHeight();

		FlColor color = graphic.color;
		Texture t = graphic.getTexture();

		// if (!graphic.isTempHighlight()) {
		if (color != null) {
			GL11.glColor4f(color.r(), color.g(), color.b(), color.a());
		} else {
			resetColor();
		}
		// } else {
		// GL11.glColor4f(1, 0.5f, 0.5f, 0.75f);
		// }

		if (t != null) {
			float tW = t.getWidth();
			float tH = t.getHeight();
			t.bind();
			GL11.glBegin(GL11.GL_QUADS);
			drawTex(t, x, y, w, h, tW, tH);
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
			drawTex(t, p.x, p.y, w, h, tW, tH);
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
		for (int x = 0; x <= levelRect.width; x += gridSize) {
			for (int y = 0; y <= levelRect.height; y += gridSize) {
				Point p = new Point(x - 2, y - 2);
				if (viewport.contains(p)) {
					pointBuffer.add(p);
				}
			}
		}

		Texture t = (Texture) ContentManager.get(ContentType.texture, "dot");
		drawTexturesInBatch(t, pointBuffer);
		loadState();
	}

	// public static void drawEditGrid(Rectangle area, int gridSize) {
	// if (gridSize < 1) {
	// return;
	// }
	//
	// saveState();
	// ArrayList<Point> pointBuffer = new ArrayList<Point>();
	// Rectangle levelRect = LevelManager.getRect();
	// for (int x = 0; x <= levelRect.width; x += gridSize) {
	// for (int y = 0; y <= levelRect.height; y += gridSize) {
	// pointBuffer.add(new Point(x, y));
	// }
	// }
	// drawPoints(GRID_COLOR, pointBuffer);
	// loadState();
	// }

	// public static final FlColor GRID_COLOR = new FlColor(0f, 0f, 0f);
	//
	// private static void drawPoints(FlColor color, ArrayList<Point> points) {
	// GL11.glMatrixMode(GL11.GL_MODELVIEW);
	// GL11.glLoadIdentity();
	//
	// GL11.glDisable(GL11.GL_TEXTURE_2D);
	// GL11.glBegin(GL11.GL_POINTS);
	//
	// int dist = 1;
	//
	// for (Point p : points) {
	//
	// GL11.glColor4f(GRID_COLOR.r(), GRID_COLOR.g(), GRID_COLOR.b(),
	// alpha);
	//
	// GL11.glVertex2f(p.x + i, p.y + j);
	// }
	// GL11.glEnd();
	// GL11.glEnable(GL11.GL_TEXTURE_2D);
	// }

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

	public static Point translate(Point mousePos) {
		return new Point(mousePos.x + viewport.x, mousePos.y + viewport.y);
	}

	public static Point translateMouse() {
		return translate(InputManager.getMousePos());
	}

	public static void clear(FlColor c) {

		GL11.glClearColor(c.r(), c.g(), c.b(), c.a());
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public static void update() {

		if (App.state == State.play || App.state == State.edit) {

			Display.setTitle(GameManager.getGameName());

			saveState();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);

			GameManager.draw();

			loadState();
		}

		GUIManager.update();

		updateDisplay();
	}

	private static void updateDisplay() {

		Display.update();
	}

	private static Point screenRes = getCurrentResolution();

	private static Point getCurrentResolution() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		return new Point(gd.getDisplayMode().getWidth(), gd.getDisplayMode()
				.getHeight());
	}

	static boolean fullscreen = false;

	public static void toggleFullscreen() {
		fullscreen = !fullscreen;

		if (fullscreen) {
			width = screenRes.x;
			height = screenRes.y;
		} else {
			width = ViewRef.DEFAULT_RESOLUTION.x;
			height = ViewRef.DEFAULT_RESOLUTION.y;
		}

		init();
	}

	public static void resetColor() {

		GL11.glColor4f(1, 1, 1, 1);
	}

	public static Rectangle getViewRect() {
		return viewport;
	}
}
