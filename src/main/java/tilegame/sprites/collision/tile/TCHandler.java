package tilegame.sprites.collision.tile;

import java.awt.Point;

public interface TCHandler
{
	public abstract void handleCollison();
	public abstract void handleTopCollision(Point tile);
	public abstract void handleBottomCollision(Point tile);
	public abstract void handleRightCollision(Point tile);
	public abstract void handleLeftCollision(Point tile);
	public void setTileCollisionDetector(TCDetector detector);
}
