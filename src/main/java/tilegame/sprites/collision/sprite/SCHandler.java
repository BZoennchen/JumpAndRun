package tilegame.sprites.collision.sprite;

import tilegame.sprites.Sprite;

public interface SCHandler
{
	public abstract void handleCollison();
	public abstract void handleTopCollision(Sprite sprite);
	public abstract void handleBottomCollision(Sprite sprite);
	public abstract void handleRightCollision(Sprite sprite);
	public abstract void handleLeftCollision(Sprite sprite);
	public void setSpriteCollisionDetector(SCDetector detector);
}
