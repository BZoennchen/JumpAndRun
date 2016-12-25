package tilegame.sprites;

import tilegame.Profile;
import graphics.Animation;

public class Player extends Creature implements IProfileHandler
{
	public static final float SPEED = .25f;
	private static final float JUMP_VELOCITY_Y = -1f;
	private Animation stand;
	private Profile profile;
	
	public Player(Animation right, Animation left,
			float x, float y, Animation stand, Profile profile)
	{
		super(right, left, x, y);
		this.stand = stand;
		this.profile = profile;
	}
	
	public void jump(){
		setVelocityY(JUMP_VELOCITY_Y);
		setState(State.Jumping);
	}
	
	@Override
	public void updateAnimations(long elapsedTime)
	{
		if(getVelocityX() == 0 && stand != null)
		{
			animation = stand;
		}
		
		super.updateAnimations(elapsedTime);
		//System.out.println(this.getState());
	}

	@Override
	public void updateX(long elapsedTime)
	{
		super.updateX(elapsedTime);
	}
	
	@Override
	public void updateY(long elapsedTime)
	{
		super.updateY(elapsedTime);
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

	public boolean isJumping()
	{
		return state.equals(State.Jumping);
	}

	@Override
	public void die()
	{
		if(!isDying())
		{
			setState(State.Dying);
			setVelocityX(0);
			getProfile().removeLife();
		}
	}
}
