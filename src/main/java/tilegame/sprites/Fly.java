package tilegame.sprites;

import java.awt.Image;
import graphics.Animation;

public class Fly extends Creature
{	
	public Fly(Animation right, Animation left, float x, float y)
	{
		super(right, left, x, y);
		setState(State.Flying);
	}
	
	public Fly(Image right, Image left, float x, float y)
	{
		super(right, left, x, y);
		setState(State.Flying);
	}
}
