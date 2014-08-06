package unnamed_platformer.game.entities;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.EnumSet;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.globals.EntityRef.EntityParam;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.structures.Graphic;
import unnamed_platformer.structures.SizeStrategy;
import unnamed_platformer.structures.SizeStrategy.Strategy;

public abstract class Entity implements Serializable {
	private static final long serialVersionUID = 2898448772127546782L;

	protected EntitySetup originalSetup;
	protected EnumSet<Flag> flags = EnumSet.noneOf(Flag.class);
	protected Rectangle box = new Rectangle(0, 0, 0, 0);
	public Graphic graphic;

	public int zIndex = 0; // higher indices are brought to front

	protected Vector2f startPos;

	public float getX() {
		return box.getX();
	}

	public float getY() {
		return box.getY();
	}
	
	public void setRect(Rectangle box) {
		this.box = box;
	}

	public EntitySetup getOriginalSetup() {
		return originalSetup;
	}

	public Entity(EntitySetup setup) {
		Graphic graphic = (Graphic) setup.get(EntityParam.graphic);
		Vector2f pos = (Vector2f) setup.get(EntityParam.location);
		setup.setEntityClassName(this.getClass().getSimpleName());
		originalSetup = setup;

		SizeStrategy sizeStrategy = (SizeStrategy) setup.get(EntityParam.sizeStrategy);

		setupEntity(graphic, pos, sizeStrategy, flags);
	}

	private void setupEntity(Graphic graphic, Vector2f pos, SizeStrategy sizeStrategy, EnumSet<Flag> flags) {
		this.graphic = graphic;
		this.box.setLocation(pos);

		BufferedImage image = graphic.getTextureImage();

		if (sizeStrategy == null) {
			sizeStrategy = new SizeStrategy(Strategy.texture, 1);
		} else if (sizeStrategy.getStrategy() == Strategy.absoluteSize && sizeStrategy.getSize() == null) {
			System.out.println("Warning: Invalid size strategy specified. Defaulting to texture size.");
			sizeStrategy = new SizeStrategy(Strategy.texture, 1);
		}
		try {
			switch (sizeStrategy.getStrategy()) {
			case absoluteSize:
				box.setSize(sizeStrategy.getSize().getX(), sizeStrategy.getSize().getY());
				break;
			case texture:
				this.box.setSize(image.getWidth(), image.getHeight());
				break;
			case textureScale:
				this.box.setWidth(image.getWidth() * sizeStrategy.getSizeScale());
				this.box.setHeight((int) (image.getHeight() * (this.box.getWidth() / (image.getWidth() + 0.0f))));
				break;
			case absoluteWidth:
				this.box.setWidth(sizeStrategy.getSizeScale());
				this.box.setHeight((int) (image.getHeight() * (this.box.getWidth() / (image.getWidth() + 0.0f))));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			System.out.println("Error setting entity size: " + e.toString());

		}

		this.flags = flags;

		this.startPos = pos;
	}

	public void update() {
	}

	public Shape getCollisionShape() {
		Rectangle cropRect = graphic.getCroppedRectangle(box);

		return cropRect;
	}
	

	public Rectangle getCollisionRect() {
		Rectangle cropRect = graphic.getCroppedRectangle(box);

		return cropRect;
	}

	public boolean pointInside(Point p) {
		return box.contains(p);
	}

	public Vector2f getPos() {
		return new Vector2f(box.getX(), box.getY());
	}

	public void setPos(Vector2f newPoint) {
		box.setLocation(new Vector2f(newPoint));
	}

	public Vector2f getCenter() {
		return new Vector2f(box.getCenterX(), box.getCenterY());
	}

	public void setCenter(Vector2f p) {
		box.setCenterX(p.getX());
		box.setCenterY(p.getY());
	}

	public boolean isFlagSet(Flag f) {
		return flags.contains(f);
	}

	public void setFlag(Flag flag, boolean truthValue) {
		if (!truthValue) {
			flags.remove(flag);
		} else {
			flags.add(flag);
		}
	}

	public void toggleFlag(Flag flag) {
		if (flags.contains(flag)) {
			flags.remove(flag);
		} else {
			flags.add(flag);
		}
	}

	public void setY(float y) {
		box.setY(y);
	}

	public void setX(float x) {
		box.setX(x);
	}

	public boolean hasNoFlags() {
		return flags.isEmpty();
	}

	public Rectangle getOriginalBox() {
		return box;
	}

}
