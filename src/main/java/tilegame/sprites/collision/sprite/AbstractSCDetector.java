package tilegame.sprites.collision.sprite;

import java.util.Iterator;

import tilegame.ITileMap;
import tilegame.sprites.Creature;
import tilegame.sprites.Sprite;

public abstract class AbstractSCDetector implements SCDetector
{
	protected ITileMap tileMap;
	
	public AbstractSCDetector(ITileMap tileMap)
	{
		this.tileMap = tileMap;	
	}
	
	@Override
	public Sprite getSpriteCollision(Creature creature, int x, int y)
	{
		int start = tileMap.getSpriteList().indexOf(creature);
		for (Iterator<Sprite> iterator = tileMap.getSprites(start); iterator.hasNext();)
		{
			Sprite eSprite = iterator.next();
			if(!eSprite.equals(creature) && creature.getShape().intersects(eSprite.getShape()))
			{
				return eSprite;
			}
		}
		return null;
	}
}
