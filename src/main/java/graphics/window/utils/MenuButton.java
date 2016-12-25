package graphics.window.utils;

import graphics.window.ListMenuPanel;
import input.GameAction;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * a button in the main menu which is bound on one game action
 * @author Benedikt Zönnchen
 */
@SuppressWarnings("serial")
public class MenuButton extends ListMenuButton {
	private String text;
	private GameAction action;

	/**
	 * standard constrcutor
	 * @param text the displayed name of the button
	 * @param action the bound action
	 * @param screen the accesspoint to the layeredPane and other useful display surfaces
	 */
	public MenuButton(String text, GameAction action, ListMenuPanel loadSaveMenu)
	{
		super(text,loadSaveMenu);
		this.text = text;
		this.action = action;
	}

	/**
	 * return the game action of this button
	 * @return gameAction the game action which is bound to this button
	 */
	public GameAction getGameAction()
	{
		return action;
	}

	/**
	 * return the text of this button
	 * @return text the displayed name of the button
	 */
	public String getButtonText()
	{
		return text;
	}

	/**
	 * set the new button name
	 * @param text the displayed name of the button
	 */
	public void setButtonText(String text)
	{
		this.text = text;
		setText(text);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		//action.tap();
	}

	@Override
	public void keyPressed(KeyEvent ke)
	{
		super.keyPressed(ke);
		if(ke.getKeyCode() == KeyEvent.VK_ENTER)
		{
			action.tap();
		}
	}


	/**
	 * change the display if the mouse exited the button (remove hover)
	 * @param e mouse event
	 */
	@Override
	public void mouseExited(MouseEvent e)
	{
		//screen.getScreenWindow().requestFocus();
	}

	/**
	 * activate the game action
	 * @param e mouse event
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		action.tap();
	}
}
