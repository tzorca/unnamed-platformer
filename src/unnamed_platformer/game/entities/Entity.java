package unnamed_platformer.game.entities;

import java.io.Serializable;
import java.util.EnumSet;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;

import unnamed_platformer.game.EntitySetup;
import unnamed_platformer.game.parameters.EntityRef.EntityParam;
import unnamed_platformer.game.parameters.Ref.Flag;
import unnamed_platformer.game.parameters.Ref.SizeMethod;
import unnamed_platformer.game.structures.Graphic;

public abstract class Entity implements Serializable {
	private static final long serialVersionUID = 2898448772127546782L;

	private EntitySetup originalSetup;
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

	public Rectangle getBox() {
		return box;
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

		if (!setup.has(EntityParam.width)) {
			setupEntity(graphic, new Rectangle(pos.getX(), pos.getY(), 0, 0),
					SizeMethod.TEXTURE, flags);
		} else {
			float width = (float) setup.get(EntityParam.width);
			setupEntity(graphic, new Rectangle(pos.getX(), pos.getY(), width,
					width), SizeMethod.TEXTURE_SCALE, flags);
		}
	}

	private void setupEntity(Graphic graphic, Rectangle r,
			SizeMethod sizeMethod, EnumSet<Flag> flags) {
		this.graphic = graphic;
		this.box.setLocation(r.getLocation());

		Texture texture = graphic.getTexture();

		switch (sizeMethod) {
		case ABSOLUTE:
			box.setSize(r.getWidth(), r.getHeight());
			break;
		case TEXTURE:
			this.box.setSize(texture.getImageWidth(), texture.getImageHeight());
			break;
		case TEXTURE_SCALE:
			this.box.setWidth(r.getWidth());
			this.box.setHeight((int) (texture.getImageHeight() * (r.getWidth() / (texture
					.getImageWidth() + 0.0f))));
			break;
		default:
			break;
		}

		this.flags = flags;

		this.startPos = getPos();
	}

	public void update() {
	}

	public boolean collidesWith(Entity otherEntity) {
		return getCroppedBox().intersects(otherEntity.getCroppedBox());

	}

	private Rectangle getCroppedBox() {
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
		box.setLocation(newPoint);
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

	public void setStartPos(Vector2f pos) {
		this.startPos = pos;
	}

}
