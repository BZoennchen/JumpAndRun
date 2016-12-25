package tilegame.sprites;

import graphics.Animation;
import java.awt.Image;
import java.awt.Rectangle;
import tilegame.sprites.collision.sprite.PlayerCollisionable;

public abstract class Sprite implements Cloneable, PlayerCollisionable
{		
	private float x;	// x-position
	private float y;	// y-position
	protected Animation animation;	// animation (a sequence of images of the same size)
	private boolean tileCollisionOn = true;
	private boolean spriteCollisionOn = false;
	private Image image;
	protected State state = State.Sleeping;
	
	public Sprite(Animation animation, float x, float y)
	{
		this.x = x;
		this.y = y;
		this.animation = animation;
	}
	
	public Sprite(Image image, float x, float y)
	{
		this.x = x;
		this.y = y;
		this.image = image;
	}

	public Rectangle getShape()
	{
		if(animation != null)
		{
			return new Rectangle(getX(), getY(), animation.getImage().getWidth(null), animation.getImage().getHeight(null));
		}
		else
		{
			return new Rectangle(getX(), getY(), image.getWidth(null), image.getHeight(null));
		}
	}

	public void update(long elapsedTime)
	{
		updateAnimations(elapsedTime);
	}
	
	protected void updateAnimations(long elapsedTime)
	{
		if(animation != null)
		{
			animation.update(elapsedTime);
		}
	}

	public void wakeUp()
	{
		state = State.Normal;
	}

	// Getter and Setter
	public boolean isSleeping()
	{
		return State.Sleeping == state;
	}

	public boolean isFlying()
	{
		return state.equals(State.Flying);
	}

	public boolean isDying()
	{
		return state.equals(State.Dying);
	}

	public abstract void setState(State state);

	public boolean isTileCollisionOn()
	{
		return tileCollisionOn;
	}
	
	public void setTileCollisionOn(boolean tileCollisionOn)
	{
		this.tileCollisionOn = tileCollisionOn;
	}
	
	public boolean isSpriteCollisionOn()
	{
		return spriteCollisionOn;
	}
	
	public void setSpriteCollisionOn(boolean spriteCollisionOn)
	{
		this.spriteCollisionOn = spriteCollisionOn;
	}
	
	public int getHeight()
	{
		return getImage().getHeight(null);
	}
	
	public int getWidth()
	{
		return getImage().getWidth(null);
	}
	
	public Image getImage()
	{
		if(animation != null)
		{
			return animation.getImage();
		}
		else
		{
			return image;
		}
	}
	
	public void setX(float x)
	{
		this.x = x;
	}

	public void setY(float y)
	{
		this.y = y;
	}
	
	public int getX()
	{
		return Math.round(x);
	}
	
	public int getY()
	{
		return Math.round(y);
	}
}

