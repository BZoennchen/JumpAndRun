package tilegame.sprites.moveing;

import tilegame.TileMapRenderer;
import tilegame.sprites.MoveableSprite;

public class CircleMoving extends RegularMoving
{
	private MoveableSprite mSprite;
	private float periodchanger;
	private int amplitude;
	private int period;
	private int yStart;
	private int xStart;
	
	public CircleMoving(MoveableSprite mSprite, int period, int amplitude)
	{
		super(mSprite);
		this.mSprite = mSprite;
		this.yStart = mSprite.getY();
		this.xStart = mSprite.getX();
		this.amplitude = TileMapRenderer.tileToPixel(amplitude);
		this.period = TileMapRenderer.tileToPixel(period*2);
		this.periodchanger = new Float(2*Math.PI/this.period);
	}

	@Override
	public void updateY(long elapsedTime)
	{
		float sinus = new Float(amplitude*Math.sin(periodchanger*Math.abs(mSprite.getNewX()-xStart)));
		//mSprite.setNewY(yStart + sinus);
		if(mSprite.getVelocityX() > 0)
		{
			mSprite.setNewY(yStart + sinus);
		}
		else
		{
			mSprite.setNewY(yStart - sinus);
		}
	}
	
	@Override
	public void updateX(long elapsedTime)
	{
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
