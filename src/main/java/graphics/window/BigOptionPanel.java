package graphics.window;

import graphics.GraphicFactory;
import graphics.ScreenManager;
import graphics.window.utils.InputTextField;
import graphics.window.utils.MenuButton;
import input.GameAction;
import input.InputManager;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * represents the fullscreen option menu where the user can modify his settings
 * @author Benedikt Zönnchen
 */
@SuppressWarnings("serial")
public class BigOptionPanel extends JPanel
{
	private Image backgroundImage = null;
	private List<MenuButton> buttonList;
	private ScreenManager screen;
	private InputManager inputManager;
	private List<GameAction> gameActions;
	private GridBagConstraints gbConstraints;
	private int gridX = 0;
	private int gridY = 0;
	private Color color = new Color(0xa3a3a3);
	private int textSize = 30;
	private Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

	/**
	 * 
	 * @param screen the accesspoint to the layeredPane and so on
	 * @param inputManager the manager which handles all user events
	 */
	public BigOptionPanel(ScreenManager screen, InputManager inputManager)
	{
		this.screen = screen;
		this.gameActions = new ArrayList<GameAction>();
		this.inputManager = inputManager;
		this.gbConstraints = new GridBagConstraints();
		gbConstraints.fill = GridBagConstraints.HORIZONTAL;
		setOpaque(false);
		setLayout(new GridBagLayout());
		buttonList = new ArrayList<MenuButton>();
		
		JLabel headline = new JLabel("Options");
		headline.setForeground(color);
		headline.setFont(new Font(GraphicFactory.FONT_FAMILY, Font.PLAIN, textSize));
		gbConstraints.fill = GridBagConstraints.CENTER;
		gbConstraints.ipady = 40;      //make this component tall
		gbConstraints.weightx = 0.0;
		gbConstraints.gridwidth = 2;
		gbConstraints.gridx = this.gridX;
		gbConstraints.gridy = this.gridY;
		add(headline, gbConstraints);
		gbConstraints.ipady = 0;
		gridY++;
	}

	/**
	 * remove one menu button from this option menu
	 * @param button the button which should be removed
	 */
	public void removeButton(MenuButton button)
	{
		buttonList.remove(button);
		remove(button);
	}

	/**
	 * return the specified menu button
	 * @param action the action which is bound on the button
	 * @return menuButton
	 */
	public MenuButton getButton(GameAction action)
	{
		for (Iterator<MenuButton> iterator = buttonList.iterator(); iterator.hasNext();)
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
	 * add a new enabled game action
	 * @param action
	 */
	public void addGameAcion(GameAction action)
	{
		addGameAcion(action, true);
	}

	/**
	 * add a new game action with an textfield and an label
	 * @param action the game action which is bound on textfield and label combination
	 * @param enabled true if the game action can be bound on an other keyboard key, otherwise false
	 */
	public void addGameAcion(GameAction action, boolean enabled)
	{
		gameActions.add(action);
		String labelName = action.getName();
		if(labelName.length() > 0)
		{
			String firstChar = (labelName.charAt(0)+"").toUpperCase();
			labelName = firstChar+labelName.substring(1);
		}

		JLabel label = new JLabel(labelName+":");
		label.setForeground(color);
		label.setFont(new Font(GraphicFactory.FONT_FAMILY, Font.PLAIN, textSize));
		InputTextField textfield = new InputTextField(action, inputManager, screen);
		textfield.setTextFieldEnabled(enabled);
		textfield.setFocusable(enabled);
		
		gbConstraints.insets = new Insets(40,60,0,60);  //top padding
		gbConstraints.weightx = 0.5;
		gbConstraints.gridwidth = 1;
		gbConstraints.fill = GridBagConstraints.NONE;
		gbConstraints.gridx = gridX;
		gbConstraints.gridy = gridY;
		add(label, gbConstraints);

		gridX++;
		
		gbConstraints.fill = GridBagConstraints.NONE;
		gbConstraints.weightx = 0.5;
		gbConstraints.gridx = gridX;
		gbConstraints.gridy = gridY;
		
		gridY++;
		gridX--;
		add(textfield, gbConstraints);
	}

	/**
	 * add a new menu button and calculate the display
	 * @param gameAction the game action which is bound on textfield and label combination
	 */
	public void addMenuGameAction(GameAction gameAction, ListMenuPanel loadSaveMenu)
	{
		MenuButton button = new MenuButton(gameAction.getName(), gameAction, loadSaveMenu);
		//button.revalidate(); 
		button.repaint();
		button.setCursor(cursor);
		gbConstraints.fill = GridBagConstraints.CENTER;
		gbConstraints.ipady = 40;      //make this component tall
		gbConstraints.weightx = 0.0;
		gbConstraints.gridwidth = 2;
		gbConstraints.gridx = this.gridX;
		gbConstraints.gridy = this.gridY;
		add(button, gbConstraints);
		gbConstraints.ipady = 0;
		gridY++;
	}

	/**
	 * Overrides the standard paintComponent. Draw the background image
	 * @param g the paint target
	 */
	@Override
	public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, backgroundImage.getWidth(null), backgroundImage.getHeight(null), null);
    } 

	/**
	 * return the background image
	 * @return backgroundImage the background image
	 */
	public Image getBackgroundImage()
	{
		return backgroundImage;
	}

	/**
	 * set the background image
	 * @param backgroundImage the background image
	 */
	public void setBackgroundImage(Image backgroundImage)
	{
		this.backgroundImage = backgroundImage;
	}
}
