package tilegame.sprites.powerUp;

import tilegame.Profile;
import graphics.Animation;
import tilegame.sprites.State;

public class Coin extends PowerUp
{
	public Coin(Animation animation, float x, float y, Profile profile)
	{
		super(animation, x, y, profile);
	}

	@Override
	public void handlePlayerCollision()
	{
		setSpriteCollisionOn(false);
		setState(State.Dying);
		profile.addPoint();
	}
}
