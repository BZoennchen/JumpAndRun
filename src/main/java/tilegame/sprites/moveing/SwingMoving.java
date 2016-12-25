package tilegame.sprites.moveing;

import tilegame.TileMapRenderer;
import tilegame.sprites.MoveableSprite;

public class SwingMoving extends SinusMoving
{

	private MoveableSprite mSprite;
	private int xStart;
	private int period;

	public SwingMoving(MoveableSprite mSprite, int period, int amplitude)
	{
		super(mSprite, period, amplitude);
		this.period = TileMapRenderer.tileToPixel(period);
		this.mSprite = mSprite;
		xStart = mSprite.getX();
	}

	@Override
	public void updateX(long elapsedTime)
	{
		// ToDo sometimes the animation is locked
		int periodprogress = mSprite.getX() - xStart;
		// +-1 make sure that there is no lock!
		if(periodprogress > period/2.0)
		{
			//System.out.println("Wechsel: " + periodprogress);
			mSprite.setVelocityX(-mSprite.getVelocityX());
			mSprite.setNewX(xStart+(float)(period/2.0) - 1);
		}
		else if(periodprogress < 0)
		{
			//System.out.println("Wechsel: " + periodprogress);
			mSprite.setVelocityX(-mSprite.getVelocityX());
			mSprite.setNewX(xStart + 1);
		}
		else
		{
			mSprite.setNewX(mSprite.getX()+mSprite.getVelocityX()*elapsedTime);
		}
	}
}
