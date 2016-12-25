package tilegame.sprites.moveing;

import tilegame.TileMapRenderer;
import tilegame.sprites.MoveableSprite;

public class SinusMoving extends RegularMoving
{
	private MoveableSprite mSprite;
	private float periodchanger = 0.015f;
	private float amplitude;
	private float period;
	private float yStart;
	private float xStart;
	
	
	public SinusMoving(MoveableSprite mSprite, int period, int amplitude)
	{
		super(mSprite);
		this.mSprite = mSprite;
		this.yStart = mSprite.getY();
		this.xStart = mSprite.getX();
		this.amplitude = TileMapRenderer.tileToPixel(amplitude);
		this.period = TileMapRenderer.tileToPixel(period);
		this.periodchanger = new Float(2*Math.PI/this.period);
	}

	@Override
	public void updateY(long elapsedTime)
	{
		float sinus = new Float(amplitude*Math.sin(periodchanger*(mSprite.getNewX()-xStart)));
		mSprite.setNewY(yStart + sinus);
	}
}
