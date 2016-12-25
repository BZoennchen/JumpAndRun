package tilegame.sprites.collision.sprite;

import tilegame.sprites.Player;

public interface PlayerCollisionable
{
	public void handlePlayerTopCollision(Player player);

	public void handlePlayerBottomCollision(Player player);

	public void handlePlayerLeftCollision(Player player);

	public void handlePlayerRightCollision(Player player);
}
