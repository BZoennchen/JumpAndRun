package tilegame;

import java.io.Serializable;

public class Profile implements Serializable
{
	public static int START_LIFES = 4;
	public static int START_LEVEL = 1;
	public static int START_POINTS = 0;
	private int points = START_POINTS;
	private int lifes = START_LIFES;
	private int level= START_LEVEL;
	private static Profile instance;
	private static int MAX_POINTS = 99;
	
	public static Profile getInstance()
	{
		if(instance == null)
		{
			instance = new Profile();
		}
		return instance;
	}
	
	private Profile()
	{}
	
	public void save()
	{
		
	}
	
	public void load()
	{
		
	}
		
	public void addPoint()
	{
		points++;
		if(points > MAX_POINTS)
		{
			points = 0;
			addLife();
		}
	}

	public void addLife()
	{
		lifes++;
	}

	public void nextLevel()
	{
		level++;
	}
	
	public int getPoints()
	{
		return points;
	}

	public void setPoints(int points)
	{
		this.points = points;
	}

	public void removeLife()
	{
		this.lifes--;
	}
	
	public int getLifes()
	{
		return lifes;
	}

	public void setLifes(int lifes)
	{
		this.lifes = lifes;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}
	
	public void reset()
	{
		points = START_POINTS;
		lifes = START_LIFES;
		level= START_LEVEL;
	}
}
