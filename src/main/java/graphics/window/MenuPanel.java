package graphics.window;

import graphics.ScreenManager;
import graphics.window.utils.ListMenuButton;
import graphics.window.utils.MenuButton;
import input.GameAction;
import java.util.Iterator;
import javax.swing.Box;
import javax.swing.BoxLayout;

/**
 * represent the main menu panel in the game
 * @author Benedikt Zönnchen
 */
@SuppressWarnings("serial")
public class MenuPanel extends ListMenuPanel
{
	/**
	 * standard constructor
	 * @param screen the accesspoint the the layeredPane and so on
	 */
	public MenuPanel(ScreenManager screen)
	{
		super();
		setOpaque(false);
		setLayout(new BoxLayout(this , BoxLayout.Y_AXIS));
		
		//add(Box.createVerticalStrut(500));
		add(Box.createVerticalGlue());
	}

	/**
	 * return the MenuButton
	 * @param action the action which is bound on this button
	 * @return button a button combined with an game action
	 */
	public MenuButton getButton(GameAction action)
	{
		for (Iterator<ListMenuButton> iterator = buttons.iterator(); iterator.hasNext();)
		{
			MenuButton button = (MenuButton) iterator.next();
			if(button.getGameAction().equals(action))
			{
				return button;
			}
		}
		return null;
	}
	
	/**
	 * Adds an button at the bottom combined with an GameAction
	 * @param name displayed name of the Button
	 * @param action the game action which will activate if the user hit the button
	 */
	public void addButton(String name, GameAction action)
	{
		MenuButton button = new MenuButton(name, action, this);
		super.addButton(button);
	}
}
