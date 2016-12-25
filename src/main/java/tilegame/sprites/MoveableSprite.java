package tilegame.sprites;

import java.awt.Image;

import tilegame.sprites.moveing.Moveable;
import tilegame.sprites.moveing.RegularMoving;
import graphics.Animation;

public abstract class MoveableSprite extends Sprite implements Moveable
{	
	private float newX; // a little buffer check before set the real x
	private float newY; // a little buffer check before set the real y
	private float dx;	// x-pixel per second
	private float dy;	// y-pixel per second
	private Moveable moveStrategy;  /* separate the moving from the creature, 
									its possible to change the move behavior*/
	private Animation rightAnimation;
	private Animation leftAnimation;
	private Image rightImage;
	private Image leftImage;
	private boolean animated;
	protected Image image;
	
	public MoveableSprite(Animation right, Animation left, float x, float y)
	{
		super(right, x, y);
		this.moveStrategy = new RegularMoving(this);
		this.rightAnimation = right;
		this.leftAnimation = left;
		this.animation = right;
		this.animated = true;
		this.newX = x;
		this.newY = y;
	}
	
	public MoveableSprite(Image rightImage, Image leftImage, float x, float y)
	{
		super(rightImage, x, y);
		this.moveStrategy = new RegularMoving(this);
		this.rightImage = rightImage;
		this.leftImage = leftImage;
		this.animated = false;
		this.newX = x;
		this.newY = y;
	}
		
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	@Override
	protected void updateAnimations(long elapsedTime)
	{
		if(isAnimated())
		{
			if(getVelocityX() > 0)
			{
				animation = rightAnimation;
			}
			else if (getVelocityX() < 0)
			{
				animation = leftAnimation;
			}	
			animation.update(elapsedTime);
		}
		else
		{
			if(getVelocityX() > 0)
			{
				image = rightImage;
			}
			else if (getVelocityX() < 0)
			{
				image = leftImage;
			}	
		}
	}
	
	@Override
	public void updateY(long elapsedTime)
	{
		moveStrategy.updateY(elapsedTime);
	}
	
	@Override
	public void updateX(long elapsedTime)
	{
		moveStrategy.updateX(elapsedTime);
	}
	
	// Getter and Setter
	@Override
	public void setState(State state)
	{
		if(state.equals(State.Dying))
		{
			setMoveStrategy(new RegularMoving(this));
			setTileCollisionOn(false);
			setSpriteCollisionOn(false);
		}
		this.state = state;
	}
	
	public void setMoveStrategy(Moveable moveStrategy)
	{
		this.moveStrategy = moveStrategy;
	}
	
	public boolean isAnimated()
	{
		return animated;
	}
	
	public void setNewX(float newX)
	{
		this.newX = newX;
	}
	
	public void setNewY(float newY)
	{
		this.newY = newY;
	}
	
	public void setVelocityX(float dx)
	{
		this.dx = dx;
	}

	public void setVelocityY(float dy)
	{
		this.dy = dy;
	}

	public float getVelocityX()
	{
		return dx;
	}

	public float getVelocityY()
	{
		return dy;
	}
	
	public int getNewX()
	{
		return Math.round(newX);
	}

	public int getNewY()
	{
		return Math.round(newY);
	}

}

