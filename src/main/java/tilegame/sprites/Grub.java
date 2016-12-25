package tilegame.sprites;

import java.awt.Image;

import graphics.Animation;

public class Grub extends Creature
{
	public Grub(Animation right, Animation left, float x, float y)
	{
		super(right, left, x, y);
	}
	
	public Grub(Image right, Image left, float x, float y)
	{
		super(right, left, x, y);
	}
}
