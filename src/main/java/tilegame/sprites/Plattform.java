package tilegame.sprites;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import tilegame.sprites.moveing.CircleMoving;
import graphics.Animation;

/*
 * A moving tile
 */
public class Plattform extends Creature
{
	private List<MoveableSprite> standingSprites = new ArrayList<MoveableSprite>();
	
	public Plattform(Animation right, Animation left, float x, float y)
	{
		super(right, left, x, y);
		setMoveStrategy(new CircleMoving(this, 7, 1));
		setState(State.Flying);
	}
	
	public Plattform(Image right, Image left, float x, float y)
	{
		super(right,left, x, y);
		setMoveStrategy(new CircleMoving(this, 7, 1));
		setState(State.Flying);
	}
	
	@Override
	public void updateX(long elapsedTime)
	{
		super.updateX(elapsedTime);
		List<MoveableSprite> cache = new ArrayList<MoveableSprite>();
		for (Iterator<MoveableSprite> iterator = standingSprites.iterator(); iterator.hasNext();)
		{
			MoveableSprite sprite = iterator.next();
			if(sprite.getVelocityY() < 0 || sprite.getX()+sprite.getWidth()< getX() || sprite.getX() > getX()+ getWidth())
			{
				sprite.setTileCollisionOn(true);
				cache.add(sprite);
			}
			else
			{
				sprite.setX(sprite.getNewX()+(this.getNewX()-this.getX()));	
			}	
		}
		removeAllStandingSprites(cache);
	}
	
	@Override
	public void updateY(long elapsedTime)
	{
		super.updateY(elapsedTime);
		List<MoveableSprite> cache = new ArrayList<MoveableSprite>();
		for (Iterator<MoveableSprite> iterator = standingSprites.iterator(); iterator.hasNext();)
		{
			MoveableSprite sprite = iterator.next();
			if(sprite.getVelocityY() < 0 || sprite.getX()+sprite.getWidth()< getX() || sprite.getX() > getX()+ getWidth())
			{
				sprite.setTileCollisionOn(true);
				cache.add(sprite);
			}
			else
			{
			sprite.setVelocityY(0);
			sprite.setState(State.Normal);
			sprite.setY(getY()-sprite.getHeight());
			}
		}
		removeAllStandingSprites(cache);
	}

	@Override
	public void handlePlayerTopCollision(Player player)
	{
		player.setState(State.Normal);
		if(!standingSprites.contains(player))
		{
			addStandingSprite(player);
		}
	}
	
	@Override
	public void handlePlayerBottomCollision(Player player)
	{}
	
	@Override
	public void handlePlayerLeftCollision(Player player)
	{}
	
	@Override
	public void handlePlayerRightCollision(Player player)
	{}

	private void addStandingSprite(MoveableSprite sprite)
	{
		sprite.setTileCollisionOn(false);
		standingSprites.add(sprite);
	}
	
	private void removeAllStandingSprites(List<MoveableSprite> sprites)
	{
		standingSprites.removeAll(sprites);
	}
}
