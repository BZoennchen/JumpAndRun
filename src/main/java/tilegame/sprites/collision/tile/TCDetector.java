package tilegame.sprites.collision.tile;

import tilegame.Tile;
import tilegame.sprites.Sprite;

public interface TCDetector
{
	public abstract Tile getTileCollision(Sprite sprite, float x, float y);
	public boolean isTileOnPosition(int x, int y);
	public Tile getMapRightCollision(Sprite sprite, float x);
	public Tile getMapLeftCollision(Sprite sprite, float x);
}
