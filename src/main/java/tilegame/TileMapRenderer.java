package tilegame;

import graphics.ScreenManager;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.Iterator;
import tilegame.sprites.MoveableSprite;
import tilegame.sprites.Player;
import tilegame.sprites.Sprite;
/*
 * Draw the tile map and converts between tile and pixel
 */
public class TileMapRenderer
{
	public static final int TILE_SIZE = TileMapArray.TILE_SIZE;
	public static final int TILE_BIT_SIZE = 6;
	private Image background;
	private int screenHeight;
	private int screenWidth;
	private ITileMap map;
	
	public TileMapRenderer(ITileMap map, ScreenManager screen)
	{
		this.screenHeight = screen.getHeight();
		this.screenWidth = screen.getWidth();
		this.map = map;
	}
	
	public static int pixelToTile(float pixel)
	{
		return TileMapRenderer.pixelToTile(Math.round(pixel));
	}
	
	public static int pixelToTile(int pixel)
	{
		return pixel >> TILE_BIT_SIZE;
	}
	
	public static int tileToPixel(int tileNum)
	{
		return tileNum << TILE_BIT_SIZE;
	}

	/**
	 * draw only the map in editor mode
	 * @param g
	 * @param offsetX
	 * @param offsetY
	 */
	public void drawTileMap(Graphics2D g, int offsetX, int offsetY)
	{
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int mapWidth = tileToPixel(map.getWidth());

        // draw black background, if needed
        if (background == null ||
            screenHeight > background.getHeight(null))
        {
            g.setColor(Color.black);
            g.fillRect(0, 0, screenWidth, screenHeight);
        }

        // draw parallax background image
        if (background != null)
		{
            int x = offsetX * (screenWidth - background.getWidth(null)) / (screenWidth - mapWidth);
            int y = screenHeight - background.getHeight(null);
            g.drawImage(background, x, y, null);
        }

        // draw the visible tiles
        int firstTileX = pixelToTile(-offsetX);
        int lastTileX = firstTileX + pixelToTile(screenWidth) + 1;
        int firstTileY = 0;
        int lastTileY = map.getHeight();
        //offsetY = getOffsetY(map);
		//offsetY = 0;
		int xPixel;
		int yPixel;

        for (int y=firstTileY; y<lastTileY; y++) {
            for (int x=firstTileX; x <= lastTileX; x++) {
                Tile tile = map.getTile(x, y);
                if (tile != null) {
					if(tile.getImage() != null)
					{
						// center image
						xPixel =  tileToPixel(x) + offsetX + (TileMapArray.TILE_SIZE / 2 - tile.getImage().getWidth(null) / 2);
						yPixel =  tileToPixel(y) + offsetY + (TileMapArray.TILE_SIZE / 2 - tile.getImage().getHeight(null) / 2);
					}
					else
					{
						xPixel =  tileToPixel(x) + offsetX;
						yPixel =  tileToPixel(y) + offsetY;
					}
                g.drawImage(tile.getImage(), xPixel, yPixel, null);
                }
            }
        }
	}

	/**
	 * draw the whole map, player, sprites in game mode
	 * @param g
	 */
	public void draw(Graphics2D g)
	{
		int wakeUpBuffer = 100;
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int mapWidth = tileToPixel(map.getWidth());
                
        // get the scrolling position of the map
        // based on player's position
        int offsetX = this.getOffsetX();

        // get the y offset to draw all sprites and tiles
        int offsetY = getOffsetY(map);
        
        // draw black background, if needed
        if (background == null || screenHeight > background.getHeight(null))
        {
            g.setColor(Color.black);
            g.fillRect(0, 0, screenWidth, screenHeight);
        }

        // draw parallax background image
        if (background != null) {
            int x = offsetX * (screenWidth - background.getWidth(null)) / (screenWidth - mapWidth);
            int y = screenHeight - background.getHeight(null);
			g.drawImage(background, x, y, null);
        }

        // draw the visible tiles
        int firstTileX = pixelToTile(-offsetX);
        int lastTileX = firstTileX + pixelToTile(screenWidth) + 1;
        int firstTileY = 0;
        int lastTileY = map.getHeight();
        offsetY = getOffsetY(map);
        
        
        for (int y=firstTileY; y<lastTileY; y++) {
            for (int x=firstTileX; x <= lastTileX; x++) {
                Tile tile = map.getTile(x, y);
                if (tile != null) {
                    g.drawImage(tile.getImage(), tileToPixel(x) + offsetX,
                        tileToPixel(y) + offsetY, null);
                }
            }
        }

        // draw sprites
        Iterator<Sprite> i = map.getSprites();
        while (i.hasNext()) {
            Sprite sprite = (Sprite)i.next();
            int x = Math.round(sprite.getX()) + offsetX;
            int y = Math.round(sprite.getY() + offsetY);
            
            if(sprite instanceof Player)
            {
            	 y = Math.round(sprite.getY()) + offsetY;
            }
            
            g.drawImage(sprite.getImage(), x, y, null);
            g.setBackground(Color.WHITE);
            g.setColor(Color.WHITE);
            //g.drawString(sprite.getX() + "/" + sprite.getY(), x, y-10);
			
            // wake up the creature when it's on screen
			// ToDo collison off if player is away!
            if (x >= -wakeUpBuffer && x < screenWidth+wakeUpBuffer && y >= -wakeUpBuffer && y < screenHeight+wakeUpBuffer)
            {
				if(!sprite.isDying())
				{
					sprite.setSpriteCollisionOn(true);
				}
		
				if(sprite.isSleeping())
				{
					sprite.wakeUp();
				}
            }
        }
    }
	
	// Setter and Getter
	public int getOffsetX()
	{
		MoveableSprite player = map.getPlayer();
		int mapWidth = tileToPixel(map.getWidth()); 
        int offsetX = this.screenWidth / 2 - Math.round(player.getX()) - TILE_SIZE;
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, screenWidth - mapWidth);
		return offsetX;
	}
	
	public int getOffsetY(ITileMap map)
	{
		MoveableSprite player = map.getPlayer();
		int mapHeight = tileToPixel(map.getHeight());
		int offsetY = 0;

		if(player == null || Math.round(player.getY()) > mapHeight - screenHeight/2)
		{
			offsetY = screenHeight - mapHeight;
		}
		else
		{
			//offsetY = Math.round(player.getY()) + player.getHeight()/2 - screenHeight/2;
			offsetY = screenHeight/2 - Math.round(player.getY());		
		}
		return offsetY;
	}

	/**
	 * return the offset without the player object, oriented by a tile location
	 * @return int offsetX
	 */
	public int getOffsetX(Tile currentTile)
	{
		int mapWidth = TileMapRenderer.tileToPixel(map.getWidth());
		int offsetX = screenWidth / 2 - Math.round(TileMapRenderer.tileToPixel(currentTile.getX())) - TileMapArray.TILE_SIZE;
		offsetX = Math.min(offsetX, 0);
		offsetX = Math.max(offsetX, screenWidth - mapWidth);
		return offsetX;
	}

	/**
	 * return the offset without the player object, oriented by a tile location
	 * @return int offsetX
	 */
	public int getOffsetY(Tile currentTile)
	{
		int y = TileMapRenderer.tileToPixel(currentTile.getY());
		int mapHeight = TileMapRenderer.tileToPixel(map.getHeight());
		if (mapHeight - Math.round(y) > screenHeight / 2)
		{
			return (screenHeight / 2 - Math.round(y));
		}
		return screenHeight - mapHeight;
	}
		
	public void setTileMap(ITileMap map)
	{
		this.map = map;
	}
	
	public void setBackground(Image background)
	{
		this.background = background;
	}
}
