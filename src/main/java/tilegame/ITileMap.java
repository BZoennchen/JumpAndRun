/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tilegame;

import java.awt.Image;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import tilegame.sprites.IProfileHandler;
import tilegame.sprites.Player;
import tilegame.sprites.Sprite;

/**
 *
 * @author Bene
 */
public interface ITileMap extends IProfileHandler
{
	public void pack(int minWidth);

	public void pack();

	public int getWidth();

	public int getHeight();

	public Tile getTile(int x, int y);

	public Tile getLastX();

	public Tile getFirstX();

	public void setTile(int x, int y, Image image, char character);

	public Player getPlayer();

	public void setPlayer(Player player);

	public void addSprite(Sprite sprite);

	public void removeSprite(Sprite sprite);

	public void removeSprites(Collection<Sprite> deleteSprite);

	public Iterator<Sprite> getSprites(int start);

	public Iterator<Sprite> getSprites();

	public List<Sprite> getSpriteList();

	public void print();

	public void setIndex(int index);

	public int getIndex();
}
