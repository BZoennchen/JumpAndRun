package tilegame.sprites.moveing;

import tilegame.sprites.MoveableSprite;

public class CircleMoving_temp implements Moveable
{
	public static float RADIUS = 100;
	private float xStart;
	private float yStart;
	
	private MoveableSprite mSprite;
	
	public CircleMoving_temp(MoveableSprite mSprite)
	{
		this.mSprite = mSprite;
		xStart = mSprite.getX();
		yStart = mSprite.getY();
	}
	
	@Override
	public void updateX(long elapsedTime)
	{
		float newX = mSprite.getX() + mSprite.getVelocityX() * elapsedTime;
		if(Math.abs(newX-xStart) > RADIUS)
		{
			mSprite.setVelocityX(-mSprite.getVelocityX());
			mSprite.setNewX(mSprite.getX() + mSprite.getVelocityX() * elapsedTime);
		}
		else
		{
			mSprite.setNewX(newX);	
		}	
	}

	@Override
	public void updateY(long elapsedTime)
	{
		double powRadius = Math.pow(RADIUS, 2);
		double powX = Math.pow((mSprite.getNewX() - xStart), 2);
		double y = Math.pow((powRadius+powX), 0.5);
		
		if(mSprite.getVelocityX() < 0)
		{	
			mSprite.setNewY(new Float(y+yStart));
		}
		else
		{
			mSprite.setNewY(new Float(-y+yStart));
		}
	}

}
