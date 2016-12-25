package tilegame;

import java.awt.Image;
import java.io.Serializable;

public class Tile implements Serializable
{
	private int x;
	private int y;
	private int width = TileMapArray.TILE_SIZE;
	private int height = TileMapArray.TILE_SIZE;
	private Image image;
	private char character;
	private boolean collisionRight = true;
	private boolean collisionLeft = true;
	private boolean collisionTop = true;
	private boolean collisionBottom = true;
	
	public Tile(int x, int y, Image image, char character)
	{
		this.character = character;
		this.image = image;
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return x;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public int getY()
	{
		return y;
	}
	public void setY(int y)
	{
		this.y = y;
	}
	public int getWidth()
	{
		return width;
	}
	public void setWidth(int width)
	{
		this.width = width;
	}
	public int getHeight()
	{
		return height;
	}
	public void setHeight(int height)
	{
		this.height = height;
	}
	public Image getImage()
	{
		return image;
	}
	
	public void installCollision(boolean right, boolean left, boolean top, boolean bottom)
	{
		this.collisionRight = right;
		this.collisionLeft = left;
		this.collisionBottom = bottom;
		this.collisionTop = top;
	}

	public void setChar(char character)
	{
		this.character = character;
	}
	
	public void setImage(Image image)
	{
		this.image = image;
	}
	
	public boolean isCollisionBottom()
	{
		return collisionBottom;
	}
	
	public boolean isCollisionLeft()
	{
		return collisionLeft;
	}
	
	public boolean isCollisionRight()
	{
		return collisionRight;
	}
	
	public boolean isCollisionTop()
	{
		return collisionTop;
	}

	public char getChar()
	{
		return character;
	}
}
