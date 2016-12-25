package tilegame.sprites.collision.tile;

import java.awt.Point;

import tilegame.sprites.Creature;

public class FlyTCHandler extends EnemyTCHandler
{

	public FlyTCHandler(Creature creature, TCDetector detector)
	{
		super(creature, detector);
	}

	@Override
	public void handleCollison()
	{
		if (creature.isTileCollisionOn())
		{
			vecX = creature.getVelocityX();
			if (!handleVerticalCollision())
			{
				handleHorizontalCollision();
			}
			handleMapCollision();
		}
		else
		{
			creature.setX(creature.getNewX());
			creature.setY(creature.getNewY());
		}
	}

	@Override
	public void handleBottomCollision(Point tile)
	{
		super.handleBottomCollision(tile);
		creature.setVelocityX(-vecX);
	}

	@Override
	public void handleTopCollision(Point tile)
	{
		super.handleTopCollision(tile);
		creature.setVelocityX(-vecX);
	}
}
