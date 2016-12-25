package tilegame.sprites.powerUp;

import tilegame.Profile;
import graphics.Animation;
import tilegame.sprites.State;

public class Star extends PowerUp
{
	public Star(Animation animation, float x, float y, Profile profile)
	{
		super(animation, x, y, profile);
	}

	@Override
	public void handlePlayerCollision()
	{
		setState(State.Dying);
		setSpriteCollisionOn(false);
		profile.nextLevel();
	}
}
