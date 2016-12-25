package tilegame.sprites.collision.sprite;

import tilegame.sprites.Player;
import tilegame.sprites.Sprite;

public class PlayerSCHandler extends AbstractSCHandler
{
	private Player player;
	
	public PlayerSCHandler(Player creature, SCDetector detector)
	{
		super(creature, detector);
		this.player = creature;
	}

	@Override
	public void handleBottomCollision(Sprite sprite)
	{
		((PlayerCollisionable)sprite).handlePlayerBottomCollision(player);
	}

	@Override
	public void handleLeftCollision(Sprite sprite)
	{
		((PlayerCollisionable)sprite).handlePlayerLeftCollision(player);	
	}

	@Override
	public void handleRightCollision(Sprite sprite)
	{
		((PlayerCollisionable)sprite).handlePlayerRightCollision(player);
	}

	@Override
	public void handleTopCollision(Sprite sprite)
	{
		((PlayerCollisionable)sprite).handlePlayerTopCollision(player);		
	}
}
