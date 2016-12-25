package tilegame;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import tilegame.sprites.IProfileHandler;
import tilegame.sprites.Player;
import tilegame.sprites.Sprite;

public class TileMapArray implements ITileMap, IProfileHandler
{
	private Tile[][] tiles;
	private List<Sprite> sprites;
	private Player player;
	private Profile profile;
	// No changes allowed!
	public static final int TILE_SIZE = 64;
	public static final int MAX_HEIGHT = 60;
	public static final int MAX_WIDTH = 200;
	public static int X_FIRST_PIXEL = 0;
	public static int X_LAST_PIXEL = 0;
	private Tile lastX;
	private Tile firstX;
	private int index;
	
	public TileMapArray(int x, int y, int index)
	{
		tiles = new Tile[x][y];
		sprites = new ArrayList<Sprite>();
		this.index = index;
	}

	@Override
	public void pack(int minWidth)
	{
		int width = minWidth;
		for(int x = 0; x< getWidth(); x++)
		{
			for(int y = 0; y < getHeight(); y++)
			{
				if(getTile(x, y) != null)
				{
					width = Math.max(width, x);
				}
			}
		}
		Tile[][] tmp = new Tile[width+1][getHeight()];
		System.arraycopy(tiles, 0, tmp, 0, width+1);
		Tile tfirstX = getFirstX();
		Tile tlastX = getLastX();
		tiles = tmp;
		setLastX(tlastX);
		setTile(tlastX);
		setFirstX(tfirstX);
		setTile(tfirstX);
	}

	@Override
	public void pack()
	{
		pack(0);
	}
	
	@Override
	public int getWidth(){
		return tiles.length;
	}
	
	@Override
	public int getHeight(){
		if(tiles.length > 0){
			return tiles[0].length;
		}
		return 0;
	}
	
	@Override
	public Tile getTile(int x, int y)
	{
		if(x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
		{
			return null;
		}
		else
		{
			return tiles[x][y];
		}
	}

	@Override
	public Tile getLastX()
	{
		return lastX;
	}
	
	@Override
	public Tile getFirstX()
	{
		return firstX;
	}

	@Override
	public void setTile(int x, int y, Image image, char character){
		tiles[x][y] = new Tile(x, y, image, character);

		// Set the max/min x-pixel
		if(getFirstX() == null || x < getFirstX().getX())
		{
			setFirstX(getTile(x, y));
		}
		else if(getLastX() == null || x > getLastX().getX())
		{
			setLastX(getTile(x, y));
		}
	}

	private void setTile(Tile tile)
	{
		tiles[tile.getX()][tile.getY()] = tile;
	}

	private void setLastX(Tile lastX)
	{
		this.lastX = lastX;
		TileMapArray.X_LAST_PIXEL = TileMapRenderer.tileToPixel(lastX.getX())+lastX.getWidth();

	}

	private void setFirstX(Tile firstX)
	{
		this.firstX = firstX;
		TileMapArray.X_FIRST_PIXEL = TileMapRenderer.tileToPixel(firstX.getX());
	}
	
	@Override
	public Profile getProfile()
	{
		return profile;
	}
	
	@Override
	public void setProfile(Profile profile)
	{
		this.profile = profile;
		for (Iterator<Sprite> it = sprites.iterator(); it.hasNext();)
		{
			Sprite sprite = it.next();
			if(sprite instanceof IProfileHandler)
			{
				((IProfileHandler)sprite).setProfile(profile);
			}
		}
	}
	
	@Override
	public Player getPlayer(){
		return player;
	}
	
	@Override
	public void setPlayer(Player player){
		this.player = player;
	}
	
	@Override
	public void addSprite(Sprite sprite)
	{
		sprites.add(sprite);
	}
	
	@Override
	public void removeSprite(Sprite sprite)
	{
		sprites.remove(sprite);
	}
	
	@Override
	public void removeSprites(Collection<Sprite> deleteSprite)
	{
		sprites.removeAll(deleteSprite);
	}
	
	@Override
	public Iterator<Sprite> getSprites(){
		return sprites.iterator();
	}

	@Override
	public Iterator<Sprite> getSprites(int start)
	{
		return sprites.listIterator(start);
	}
	
	@Override
	public List<Sprite> getSpriteList()
	{
		return sprites;
	}
	
	@Override
	public void print()
	{
		for (int x = 0; x < tiles.length; x++)
		{
			for (int y = 0; y < tiles[x].length; y++)
			{
				System.out.print("(" + x + "|" + y + "): " + tiles[x][y] + ",  ");
			}
			System.out.println("");
			System.out.println("");
		}
	}

	@Override
	public void setIndex(int index)
	{
		this.index = index;
	}

	@Override
	public int getIndex()
	{
		return index;
	}
}
