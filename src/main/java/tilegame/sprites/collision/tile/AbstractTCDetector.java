package tilegame.sprites.collision.tile;

import tilegame.Tile;
import tilegame.TileMapArray;
import tilegame.TileMapRenderer;
import tilegame.sprites.Sprite;

public abstract class AbstractTCDetector implements TCDetector
{
	protected TileMapArray tileMap;
	
	public AbstractTCDetector(TileMapArray tileMap)
	{
		this.tileMap = tileMap;
	}
	
	@Override
	public Tile getTileCollision(Sprite sprite, float x, float y)
	{
		float fromX = Math.min(sprite.getX(), x);
        float fromY = Math.min(sprite.getY(), y);
        float toX = Math.max(sprite.getX(), x);
        float toY = Math.max(sprite.getY(), y);


        // get the tile locations
        int fromTileX = TileMapRenderer.pixelToTile(fromX);
        int fromTileY = TileMapRenderer.pixelToTile(fromY);
        int toTileX = TileMapRenderer.pixelToTile(
            toX + sprite.getWidth() - 1);
        int toTileY = TileMapRenderer.pixelToTile(
            toY + sprite.getHeight() - 1 );
        
        // check each tile for a collision
        for (int tX=fromTileX; tX<=toTileX; tX++) {
            for (int tY=fromTileY; tY<=toTileY; tY++) {
                if (/*tX < 0 || tX >= tileMap.getWidth() || ToDo */
                		tileMap.getTile(tX, tY) != null)
                {
                	// found collision 
                	return tileMap.getTile(tX, tY);
                }
            } 
        }
        return null;
	}

	@Override
	public Tile getMapLeftCollision(Sprite sprite, float x)
	{
		float fromX = Math.min(sprite.getX(), x);
		if(TileMapArray.X_FIRST_PIXEL > fromX)
		{
			return tileMap.getFirstX();
		}
		return null;
	}

	@Override
	public Tile getMapRightCollision(Sprite sprite, float x)
	{
        float toX = Math.max(sprite.getX(), x);
		if(TileMapArray.X_LAST_PIXEL < (toX+sprite.getWidth()))
		{
			return tileMap.getLastX();
		}
		return null;
	}

	@Override
	public boolean isTileOnPosition(int x, int y)
	{
		if(tileMap.getTile(x, y) != null)
		{
			return true;
		}
		return false;
	}
}
