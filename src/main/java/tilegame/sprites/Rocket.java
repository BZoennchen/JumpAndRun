package tilegame.sprites;

import java.awt.Image;

import graphics.Animation;

public class Rocket extends Creature
{

	public Rocket(Animation right, Animation left, float x, float y)
	{
		super(right, left, x, y);
		setState(State.Flying);
	}
	
	public Rocket(Image right, Image left, float x, float y)
	{
		super(right, left, x, y);
		setState(State.Flying);
	}
	
	@Override
	public void handlePlayerBottomCollision(Player player)
	{
		super.handlePlayerBottomCollision(player);
	}
	
	@Override
	public void handlePlayerTopCollision(Player player)
	{
		handlePlayerBottomCollision(player);
	}
	
	@Override
	public void handlePlayerRightCollision(Player player)
	{
		handlePlayerBottomCollision(player);
	}
	
	@Override
	public void handlePlayerLeftCollision(Player player)
	{
		handlePlayerBottomCollision(player);
	}	

}
