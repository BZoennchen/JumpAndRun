package tilegame.sprites.collision.tile;

import java.awt.Point;

import tilegame.Tile;
import tilegame.TileMapRenderer;
import tilegame.sprites.Creature;
import tilegame.sprites.State;

public abstract class AbstractTCHandler implements TCHandler
{

	protected Creature creature;
	protected TCDetector detector;
	protected static int MAX_OVERLAP = 10;

	public AbstractTCHandler(Creature creature, TCDetector detector)
	{
		this.creature = creature;
		this.detector = detector;
	}

	@Override
	public void handleCollison()
	{
		if (creature.isTileCollisionOn())
		{
			handleVerticalCollision();
			handleHorizontalCollision();
			handleMapCollision();
		}
		else
		{
			creature.setX(creature.getNewX());
			creature.setY(creature.getNewY());
		}
	}

	protected void handleMapCollision()
	{
		if (creature.isTileCollisionOn())
		{
			Tile tile = null;
			/*
			 * Collision detection and handling of the mapend and mapbegin
			 */
			tile = detector.getMapLeftCollision(creature, creature.getNewX());

			if (tile != null)
			{
				handleLeftCollision(new Point(tile.getX() - 1, tile.getY()));
			}

			tile = detector.getMapRightCollision(creature, creature.getNewX());

			if (tile != null)
			{
				handleRightCollision(new Point(tile.getX() + 1, tile.getY()));
			}
		}
	}

	protected boolean handleVerticalCollision()
	{
		Point point = null;
		Tile tile = null;
		// x direction
		tile = detector.getTileCollision(creature, creature.getNewX(), creature.getY());
		if (tile == null)
		{
			creature.setX(creature.getNewX());
			return false;
		}
		else
		{
			point = new Point(tile.getX(), tile.getY());
			// line up with the tile boundary
			if (creature.getVelocityX() >= 0)
			{
				handleRightCollision(point);
			}
			else if (creature.getVelocityX() < 0)
			{
				handleLeftCollision(point);
			}
		}
		return true;
	}

	protected boolean handleHorizontalCollision()
	{
		Point point = null;
		Tile tile = null;
		// y direction
		tile = detector.getTileCollision(creature, creature.getX(), creature.getNewY());

		if (tile == null)
		{
			creature.setY(creature.getNewY());
			return false;
		}
		else
		{
			point = new Point(tile.getX(), tile.getY());
			// line up with the tile boundary
			if (creature.getVelocityY() >= 0)
			{
				handleBottomCollision(point);
			}
			else if (creature.getVelocityY() < 0)
			{
				handleTopCollision(point);
			}
		}
		return true;
	}

	@Override
	public void handleBottomCollision(Point tile)
	{
		float y = TileMapRenderer.tileToPixel(tile.y) - creature.getHeight();

		if ((y + MAX_OVERLAP) < (creature.getY()))
		{
			creature.die();
		}
		else
		{
			creature.setY(y);
			creature.setVelocityY(0);
			creature.setState(State.Normal);
		}
	}

	@Override
	public void handleTopCollision(Point tile)
	{
		float y = TileMapRenderer.tileToPixel(tile.y + 1);
		if ((y - MAX_OVERLAP) > creature.getY())
		{
			creature.die();
		}
		else
		{
			creature.setY(y);
		}
	}

	@Override
	public void handleLeftCollision(Point tile)
	{
		float x = TileMapRenderer.tileToPixel(tile.x + 1);
		if ((x - MAX_OVERLAP) > creature.getX())
		{
			creature.die();
		}
		else
		{
			creature.setX(x);
		}
	}

	@Override
	public void handleRightCollision(Point tile)
	{
		float x = TileMapRenderer.tileToPixel(tile.x) - creature.getWidth();
		if ((x + MAX_OVERLAP) < creature.getX())
		{
			creature.die();
		}
		else
		{
			creature.setX(x);
		}
	}

	@Override
	public void setTileCollisionDetector(TCDetector detector)
	{
		this.detector = detector;
	}
}
