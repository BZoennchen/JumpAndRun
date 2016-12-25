package tilegame.sprites.powerUp;

import java.lang.reflect.Constructor;

import tilegame.Profile;
import tilegame.sprites.Player;
import tilegame.sprites.Sprite;
import tilegame.sprites.State;
import tilegame.sprites.collision.sprite.PlayerCollisionable;
import graphics.Animation;
import tilegame.sprites.IProfileHandler;

public abstract class PowerUp extends Sprite implements PlayerCollisionable, IProfileHandler
{
	protected Profile profile;
	
	public PowerUp(Animation animation, float x, float y, Profile profile)
	{
		super(animation, x, y);
		this.profile = profile;
	}

	@Override
	public void setState(State state)
	{
		this.state = state;
	}
	
	@SuppressWarnings("unchecked")
	public Object clone()
	{
		Constructor constructor = getClass().getConstructors()[0];
		try
		{
			return constructor.newInstance(new Object[]{(Animation)animation, this.getX(), this.getY(), this.profile});
		}
		catch(Exception e)
		{
			// This should never ever happen!
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void setProfile(Profile profile)
	{
		this.profile = profile;
	}

	@Override
	public Profile getProfile()
	{
		return profile;
	}

	public abstract void handlePlayerCollision();

	@Override
	public void handlePlayerBottomCollision(Player player)
	{
		handlePlayerCollision();
	}

	@Override
	public void handlePlayerLeftCollision(Player player)
	{
		handlePlayerCollision();
	}

	@Override
	public void handlePlayerRightCollision(Player player)
	{		
		handlePlayerCollision();
	}

	@Override
	public void handlePlayerTopCollision(Player player)
	{
		handlePlayerCollision();
	}
}
