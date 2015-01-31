package unnamed_platformer.game.entities;

import java.awt.image.BufferedImage;
import java.util.EnumSet;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import unnamed_platformer.game.other.CollisionData;
import unnamed_platformer.game.other.EntitySetup;
import unnamed_platformer.game.other.SizeStrategy;
import unnamed_platformer.game.other.SizeStrategy.Strategy;
import unnamed_platformer.game.other.TextureSetup.CollisionShapeOption;
import unnamed_platformer.globals.EntityRef.EntityParam;
import unnamed_platformer.globals.GameRef.Flag;
import unnamed_platformer.res_mgt.ResManager;
import unnamed_platformer.view.Graphic;
import unnamed_platformer.view.ViewManager;

public abstract class Entity
{
	protected EntitySetup originalSetup;
	protected EnumSet<Flag> flags = EnumSet.noneOf(Flag.class);
	private Rectangle box = new Rectangle(0, 0, 0, 0);
	public Graphic graphic;

	public int zIndex = 0; // higher indices are brought to front

	protected Vector2f startPos;

	public EntitySetup getOriginalSetup() {
		return originalSetup;
	}

	public Entity(EntitySetup setup) {
		Graphic graphic = (Graphic) setup.get(EntityParam.GRAPHIC);
		Vector2f pos = (Vector2f) setup.get(EntityParam.LOCATION);
		setup.setEntityClassName(this.getClass().getSimpleName());
		originalSetup = setup;

		SizeStrategy sizeStrategy = (SizeStrategy) setup
				.get(EntityParam.SIZE_STRATEGY);

		setupEntity(graphic, pos, sizeStrategy, flags);
	}

	private void setupEntity(Graphic graphic, Vector2f pos,
			SizeStrategy sizeStrategy, EnumSet<Flag> flags) {
		this.graphic = graphic;
		setLocation(pos);

		BufferedImage image = graphic.getObjectImage();

		if (sizeStrategy == null) {
			sizeStrategy = new SizeStrategy(Strategy.texture, 1);
		} else if (sizeStrategy.getStrategy() == Strategy.absoluteSize
				&& sizeStrategy.getSize() == null) {
			System.out
					.println("Warning: Invalid size strategy specified. Defaulting to texture size.");
			sizeStrategy = new SizeStrategy(Strategy.texture, 1);
		}
		try {
			switch (sizeStrategy.getStrategy()) {
			case absoluteSize:
				box.setSize(sizeStrategy.getSize().getX(), sizeStrategy
						.getSize().getY());
				break;
			case texture:
				box.setSize(image.getWidth(), image.getHeight());
				break;
			case textureScale:
				box.setWidth(image.getWidth() * sizeStrategy.getSizeScale());
				box.setHeight((int) (image.getHeight() * (this.box.getWidth() / (image
						.getWidth() + 0.0f))));
				break;
			case absoluteWidth:
				box.setWidth(sizeStrategy.getSizeScale());
				box.setHeight((int) (image.getHeight() * (this.box.getWidth() / (image
						.getWidth() + 0.0f))));
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

	public Vector2f getPos() {
		return new Vector2f(box.getX(), box.getY());
	}

	public void setPos(Vector2f newPoint) {
		if (newPoint == box.getLocation()) {
			return;
		}
		collisionCache_rectDirty = true;
		collisionCache_shapeDirty = true;

		box.setLocation(new Vector2f(newPoint));
	}

	public Vector2f getCenter() {
		Rectangle rect = getCollisionRect();

		return new Vector2f(rect.getCenterX(), rect.getCenterY());
	}

	public void setCenter(Vector2f p) {
		collisionCache_rectDirty = true;
		collisionCache_shapeDirty = true;

		box.setCenterX(p.getX());
		box.setCenterY(p.getY());
	}

	public boolean isFlagSet(Flag f) {
		return flags.contains(f);
	}

	public void setFlag(Flag flag, boolean truthValue) {
		if (truthValue) {
			flags.add(flag);
		} else {
			flags.remove(flag);
		}
	}

	public void setY(float y) {
		collisionCache_rectDirty = true;
		collisionCache_shapeDirty = true;

		box.setY(y);
	}

	public void setX(float x) {
		collisionCache_rectDirty = true;
		collisionCache_shapeDirty = true;

		box.setX(x);
	}

	public boolean hasNoFlags() {
		return flags.isEmpty();
	}

	public Rectangle getOriginalBox() {
		return box;
	}

	public boolean isActive() {
		return false;
	}

	public void draw() {
		if (isFlagSet(Flag.INVISIBLE)) {
			return;
		}

		ViewManager.drawGraphic(graphic, getOriginalBox());
	}

	boolean collisionCache_rectDirty = true;
	boolean collisionCache_shapeDirty = true;
	Rectangle collisionCache_Rect;
	Shape collisionCache_Shape;

	private void setLocation(Vector2f pos) {
		collisionCache_rectDirty = true;
		collisionCache_shapeDirty = true;

		box.setLocation(pos);
	}

	private CollisionData getCollisionData() {
		return ResManager.get(CollisionData.class, graphic.getTextureName());
	}

	public Rectangle getCollisionRect() {
		if (collisionCache_rectDirty) {
			collisionCache_Rect = getCollisionRect(box);
			collisionCache_rectDirty = false;
		}
		return collisionCache_Rect;
	}

	public Shape getCollisionShape() {
		if (collisionCache_shapeDirty) {
			collisionCache_Shape = getCollisionShape(box);
			collisionCache_shapeDirty = false;
		}

		return collisionCache_Shape;
	}

	private Rectangle getCollisionRect(Rectangle entityBox) {
		return (Rectangle) getCollisionData().getScaledShape(entityBox,
				CollisionShapeOption.rectangle);
	}

	private Shape getCollisionShape(Rectangle entityBox) {
		return getCollisionData().getScaledShape(entityBox,
				graphic.getTextureSetup().getCollisionShape());
	}

}
