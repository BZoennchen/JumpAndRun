package tilegame.sprites;

import java.awt.Image;
import tilegame.sprites.collision.sprite.SCHandler;
import tilegame.sprites.collision.tile.TCHandler;
import graphics.Animation;
/*
 * Creature can move and can handle collisions with other sprite
 */
public abstract class Creature extends MoveableSprite
{
	// States
	public static final float GRAVITY = .002f;
	private static final float TOP_COLLISION_VLOCITY_Y = -0.2f;
	private TCHandler tcHandler;
	private SCHandler scHandler;
	
	public Creature(Animation right, Animation left, float x, float y)
	{
		super(right,left, x, y);
	}
	
	public Creature(Image right, Image left, float x, float y)
	{
		super(right,left, x, y);
	}
		
	
	// Sprite Collision Handling
	public void handleHorizontalSpriteCollison(Sprite sprite){}
	
	public void handleVerticalSpriteCollison(Sprite sprite){}

	@Override
	public void handlePlayerTopCollision(Player player)
	{
		player.setVelocityY(TOP_COLLISION_VLOCITY_Y);
		player.setState(State.Normal);
		die();
	}

	@Override
	public void handlePlayerBottomCollision(Player player)
	{
		player.die();
	}

	@Override
	public void handlePlayerLeftCollision(Player player)
	{
		player.die();
	}
	
	@Override
	public void handlePlayerRightCollision(Player player)
	{
		handlePlayerLeftCollision(player);
	}
	
	@Override
	public void update(long elapsedTime)
	{
		// apply gravity
		if (!isFlying()) {
            setVelocityY(getVelocityY() +
                GRAVITY * elapsedTime);
        }
		
		updateX(elapsedTime);
        updateY(elapsedTime);
        
		if(tcHandler != null)
		{
			tcHandler.handleCollison();
		}
		else
		{
			setX(getNewX());
			setY(getNewY());
		}
		
		if(scHandler != null)
		{
			scHandler.handleCollison();
		}
		
		super.update(elapsedTime);
	}
	
	// Getter and Setter		
	public void setTileCollisionHandler(TCHandler tcHandler)
	{
		this.tcHandler = tcHandler;
	}
	
	public void setSpriteCollisionHandler(SCHandler scHandler)
	{
		this.scHandler = scHandler;
	}

	public void die()
	{
		setState(State.Dying);
	}
}
