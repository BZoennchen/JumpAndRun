package tilegame.sprites.collision.sprite;

import tilegame.sprites.Creature;
import tilegame.sprites.Sprite;

public interface SCDetector
{
	public Sprite getSpriteCollision(Creature creature, int x, int y);
}
