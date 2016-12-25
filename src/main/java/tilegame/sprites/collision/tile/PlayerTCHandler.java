package tilegame.sprites.collision.tile;

import java.awt.Point;
import tilegame.Tile;

import tilegame.sprites.Creature;

public class PlayerTCHandler extends AbstractTCHandler
{

	public PlayerTCHandler(Creature creature, TCDetector detector)
	{
		super(creature, detector);
	}
	
	@Override
	public void handleTopCollision(Point tile)
	{
		super.handleTopCollision(tile);
		creature.setVelocityY(-creature.getVelocityY());
	}

	@Override
	public void handleCollison()
	{		
		super.handleCollison();

		/*
		 * ! Now all sprite handle map collision !
		 * Collision detection and handling of the mapend and mapbegin
		 */
		/*Tile tile = detector.getMapLeftCollision(creature, creature.getNewX());

		if(tile != null)
		{
			handleLeftCollision(new Point(tile.getX()-1, tile.getY()));
		}

		tile = detector.getMapRightCollision(creature, creature.getNewX());

		if(tile != null)
		{
			handleRightCollision(new Point(tile.getX()+1, tile.getY()));
		}*/
	}

}
