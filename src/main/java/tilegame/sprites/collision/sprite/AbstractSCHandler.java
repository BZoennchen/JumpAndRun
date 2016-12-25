package tilegame.sprites.collision.sprite;

import tilegame.sprites.Creature;
import tilegame.sprites.Sprite;

public abstract class AbstractSCHandler implements SCHandler
{

	protected Creature creature;
	protected SCDetector detector;

	public AbstractSCHandler(Creature creature, SCDetector detector)
	{
		this.creature = creature;
		this.detector = detector;
	}

	@Override
	public void handleCollison()
	{
		Sprite sprite = null;
		// x and y sprite-collision
		if (creature.isSpriteCollisionOn())
		{
			// handle only one collision!
			if(!handleVertical(sprite))
			{
				handleHorizontal(sprite);
			}
		}
	}

	private boolean handleHorizontal(Sprite sprite)
	{
		// x
		sprite = detector.getSpriteCollision(creature, creature.getNewX(), creature.getY());
		if (sprite != null && sprite.isSpriteCollisionOn())
		{
			if (creature.getVelocityX() >= 0)
			{
				handleRightCollision(sprite);
			}
			else
			{
				handleLeftCollision(sprite);
			}
			return true;
		}
		return false;
	}

	private boolean handleVertical(Sprite sprite)
	{
		// y
		sprite = detector.getSpriteCollision(creature, creature.getX(), creature.getNewY());
		if (sprite != null && sprite.isSpriteCollisionOn())
		{
			if (creature.getVelocityY() > 0)
			{
				handleTopCollision(sprite);
				return true;
			}
			else if (creature.getVelocityY() < 0)
			{
				handleBottomCollision(sprite);
				return true;
			}
		}
		return false;
	}

	@Override
	public void setSpriteCollisionDetector(SCDetector detector)
	{
		this.detector = detector;
	}
}
