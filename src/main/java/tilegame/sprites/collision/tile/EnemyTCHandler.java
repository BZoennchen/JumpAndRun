package tilegame.sprites.collision.tile;

import java.awt.Point;

import tilegame.sprites.Creature;

public class EnemyTCHandler extends AbstractTCHandler
{
	protected float vecX = 0;
	public EnemyTCHandler(Creature creature, TCDetector detector)
	{
		super(creature, detector);
	}

	@Override
	public void handleCollison()
	{
		vecX = creature.getVelocityX();
		super.handleCollison();
	}
	
	@Override
	public void handleRightCollision(Point tile)
	{
		super.handleRightCollision(tile);
		creature.setVelocityX(-vecX);
	}
	
	@Override
	public void handleLeftCollision(Point tile)
	{
		super.handleLeftCollision(tile);
		creature.setVelocityX(-vecX);
	}
}
