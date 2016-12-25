package tilegame.sprites.collision.tile;

import java.awt.Point;

import tilegame.TileMapRenderer;
import tilegame.sprites.Creature;

public class GrubTCHandler extends EnemyTCHandler
{

	public GrubTCHandler(Creature creature, TCDetector detector)
	{
		super(creature, detector);
	}

	@Override
	public void handleBottomCollision(Point tile)
	{
		super.handleBottomCollision(tile);
		float cX = creature.getX();
		float tX = TileMapRenderer.tileToPixel(tile.x);
		
		if(creature.getVelocityX() > 0 && creature.getX() > TileMapRenderer.tileToPixel(tile.x) &&
				!detector.isTileOnPosition(tile.x+1, tile.y))
		{
			creature.setVelocityX(-creature.getVelocityX());	
		}
		else if(creature.getVelocityX() < 0 && !detector.isTileOnPosition(tile.x-1, tile.y) && cX <= tX)
		{
			creature.setVelocityX(-creature.getVelocityX());
		}	
	}
}
