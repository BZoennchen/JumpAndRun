/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tilegame;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import tilegame.sprites.Player;
import tilegame.sprites.Sprite;

/**
 * Not working jet ToDo !!!
 * @author Benedikt Zönnchen
 */
public class TileMapList implements ITileMap
{
	private List<List<Tile>> tiles;
	private List<Sprite> sprites;
	private Player player;
	private Profile profile;
	// No changes allowed!
	public static final int TILE_SIZE = 64;
	public static final int MAX_HEIGHT = 30;
	public static final int MAX_WIDTH = 100;
	public static int X_FIRST_PIXEL = 0;
	public static int X_LAST_PIXEL = 0;
	private Tile lastX;
	private Tile firstX;
	private int index;

	public TileMapList(int x, int y, int index)
	{
		tiles = new ArrayList<List<Tile>>();
		sprites = new ArrayList<Sprite>();
		this.index = index;
	}

	@Override
	public void pack()
	{
		int width = 0;
		for(int x = 0; x< getWidth(); x++)
		{
			for(int y = 0; y < getHeight(); y++)
			{
				if(getTile(x, y) != null && width < y)
				{
					width = y;
				}
			}
		}
	}

	@Override
	public int getWidth()
	{
		return tiles.size();
	}

	@Override
	public int getHeight()
	{
		if(tiles.size() > 0 && tiles.get(0) != null)
		{
			return tiles.get(0).size();
		}
		return 0;
	}

	@Override
	public Tile getTile(int x, int y)
	{
		List<Tile> col = tiles.get(x);
		if(col == null || x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
		{
			return null;
		}
		else
		{
			return col.get(y);
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
		
		List<Tile> col = tiles.get(x);
		if(col == null)
		{
			col = new ArrayList<Tile>();
			tiles.add(col);
		}

		col.add(y, new Tile(x, y, image, character));

		// Set the max/min x-pixel
		if(getFirstX() == null || x < getFirstX().getX())
		{
			firstX = getTile(x, y);
			TileMapArray.X_FIRST_PIXEL = TileMapRenderer.tileToPixel(firstX.getX());
		}
		else if(getLastX() == null || x > getLastX().getX())
		{
			lastX = getTile(x, y);
			TileMapArray.X_LAST_PIXEL = TileMapRenderer.tileToPixel(lastX.getX())+lastX.getWidth();
		}
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
	public List<Sprite> getSpriteList()
	{
		return sprites;
	}

	@Override
	public void print()
	{
		for (Iterator<List<Tile>> it = tiles.iterator(); it.hasNext();)
		{
			List<Tile> col = it.next();
			for (Iterator<Tile> it1 = col.iterator(); it1.hasNext();)
			{
				Tile tile = it1.next();
			}
		}

		/*for (int x = 0; x < tiles.size(); x++)
		{
			for (int y = 0; y < tiles[x].length; y++)
			{
				System.out.print("(" + x + "|" + y + "): " + tiles[x][y] + ",  ");
			}
			System.out.println("");
			System.out.println("");
		}*/
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

	@Override
	public void pack(int minWidth)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Iterator<Sprite> getSprites(int start)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
