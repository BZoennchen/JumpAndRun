package tilegame.sprites.moveing;

import tilegame.sprites.MoveableSprite;

public class RegularMoving implements Moveable
{
	private MoveableSprite mSprite;
	
	public RegularMoving(MoveableSprite mSprite)
	{
		this.mSprite = mSprite;
	}
	
	@Override
	public void updateY(long elapsedTime)
	{
		mSprite.setNewY(mSprite.getY() + mSprite.getVelocityY() * elapsedTime);
	}
	
	@Override
	public void updateX(long elapsedTime)
	{
		mSprite.setNewX(mSprite.getX() + mSprite.getVelocityX() * elapsedTime);
	}

}
