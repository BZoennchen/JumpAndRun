
package tilegame.sprites;

import graphics.Animation;
import java.awt.Image;
import tilegame.ResourceManager;
import tilegame.TileMapRenderer;
import tilegame.sprites.collision.tile.GrubTCHandler;
import tilegame.sprites.collision.tile.SimpleTCDetector;
import tilegame.sprites.collision.tile.TCDetector;

/**
 * This Factory creates all Sprites
 * ToDo: A lot
 * @author Benedikt Zönnchen
 */
public class SpriteFactory
{
	
	public Sprite getSprite(char c, int x, int y)
	{
		switch(c)
		{
			case '1':

		}

		return null;
	}
/*
	private Sprite getGrub(int x, int y)
	{
		Animation animation = new Animation();
		Image image1 = ResourceManager.loadImage("images/grub1.png");
		Image image2 = ResourceManager.loadImage("images/grub2.png");

		Animation right = new Animation();
		Animation left = new Animation();
		left.addFrame(image1, 200);
		left.addFrame(image2, 200);
		right.addFrame(ResourceManager.getMirrorImage(image1), 200);
		right.addFrame(ResourceManager.getMirrorImage(image2), 200);
		MoveableSprite grub = new Grub(right, left, TileMapRenderer.tileToPixel(x), TileMapRenderer.tileToPixel(y));
		grub.setVelocityX(.1f);
		//grub.setState(Creature.STATE_FLYING);
		grub.update(0);
		// tile collision detection installation
		TCDetector detector = new SimpleTCDetector(tileMap);
		grub.setTileCollisionHandler(new GrubTCHandler(grub, detector));
		return grub;
	}*/

}
