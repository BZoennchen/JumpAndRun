package tilegame.sprites;

import java.awt.Image;
import tilegame.TileMapArray;
import tilegame.sprites.moveing.RegularMoving;

/*
 * Produce creatures and add them to the game (tile map).
 * For example a canon which produce rockets 
 */
public class CreatureFactory extends Creature
{

	private MoveableSprite mSprite;
	private long rate;
	private long rateBuffer = 0;
	private TileMapArray map;
	private static int MAX_OVERLAP = 30;

	public CreatureFactory(Image rightImage, Image leftImage, float x, float y, MoveableSprite mSprite, long rate, TileMapArray map)
	{
		super(rightImage, leftImage, x, y);
		this.mSprite = mSprite;
		this.rate = rate;
		this.map = map;
		setVelocityX(0);
		setVelocityY(0);
		setMoveStrategy(new RegularMoving(this));
		setSpriteCollisionOn(true);
		setState(State.Flying);
	}

	@Override
	public void update(long elapsedTime)
	{
		super.update(elapsedTime);
		rateBuffer = rateBuffer + elapsedTime;
		if (rateBuffer >= rate)
		{
			rateBuffer = 0;
			try
			{
				MoveableSprite cloneSprite = (MoveableSprite) this.mSprite.clone();
				cloneSprite.setX(this.getX());
				cloneSprite.setNewX(this.getX());
				if (cloneSprite.getVelocityY() < 0)
				{
					cloneSprite.setNewY(this.getY() - this.getHeight());
					cloneSprite.setY(this.getY() - this.getHeight());
				}
				else
				{
					cloneSprite.setNewY(this.getY() + this.getHeight());
					cloneSprite.setY(this.getY() + this.getHeight());
				}

				// No copy of the relation to other objects!
				cloneSprite.setMoveStrategy(new RegularMoving(cloneSprite));
				map.addSprite(cloneSprite);
			}
			catch (CloneNotSupportedException e)
			{
				System.out.println("Can not clone this mSprite");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void handlePlayerBottomCollision(Player player)
	{
		// only handle bottom collision if the player come form the buttom direction
		if ((getY() + getHeight()) - player.getY() < MAX_OVERLAP)
		{
			player.setY(getY() + getHeight());
			player.setVelocityY(-player.getVelocityY());
		}
		//player.setState(State.Normal);
	}

	@Override
	public void handlePlayerTopCollision(Player player)
	{
		player.setY(getY() - player.getHeight());
		player.setVelocityY(0);
		player.setState(State.Normal);
	}

	@Override
	public void handlePlayerLeftCollision(Player player)
	{
		player.setX(getX() + getWidth());
	}

	@Override
	public void handlePlayerRightCollision(Player player)
	{
		player.setX(getX() - player.getWidth());
	}
}
