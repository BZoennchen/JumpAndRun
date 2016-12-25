package graphics.window.utils;

import graphics.window.ListMenuPanel;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Blinking button if selected
 * @author Benedikt Zönnchen
 */
public class ListMenuButton extends BaseMenuButton implements KeyListener, MouseListener
{

	private ListMenuButton previews, next;
	private ListMenuPanel listPanel;

	public ListMenuButton(String text, ListMenuPanel listPanel)
	{
		super(text);
		this.listPanel = listPanel;
		addKeyListener(this);
		addMouseListener(this);
	}

	public void setPreviews(ListMenuButton previews)
	{
		this.previews = previews;
	}

	public void setNext(ListMenuButton next)
	{
		this.next = next;
	}

	@Override
	public void keyTyped(KeyEvent ke)
	{
		// ignore
	}

	@Override
	public void keyPressed(final KeyEvent ke)
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				if (ke.getKeyCode() == KeyEvent.VK_DOWN && next != null)
				{
					listPanel.appendEnd(next);
					next.requestFocus();
				}
				else if (ke.getKeyCode() == KeyEvent.VK_UP && previews != null)
				{
					listPanel.appendStart(previews);
					previews.requestFocus();
				}
			}
		});
	}

	@Override
	public void keyReleased(KeyEvent ke)
	{
		// ignore
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		//action.tap();
	}

	/**
	 * change the display if the mouse entered the button (hover)
	 * @param e mouse event
	 */
	@Override
	public void mouseEntered(MouseEvent e)
	{
		requestFocus();
	}

	/**
	 * change the display if the mouse exited the button (remove hover)
	 * @param e mouse event
	 */
	@Override
	public void mouseExited(MouseEvent e)
	{
		//listPanel.requestFocus();
	}

	/**
	 * activate the game action
	 * @param e mouse event
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		// ignore
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}
}
