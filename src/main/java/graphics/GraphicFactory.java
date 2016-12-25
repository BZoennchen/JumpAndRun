package graphics;

import graphics.window.BigOptionPanel;
import graphics.window.InfoPanel;
import graphics.window.ListMenuPanel;
import graphics.window.MenuPanel;
import graphics.window.OptionPanel;
import input.InputManager;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import tilegame.OS;
import tilegame.ResourceManager;

/**
 * A Factory which return same useful modified Swing componets
 * @author Benedikt Zönnchen
 */
public class GraphicFactory
{
	public static String FONT_FAMILY = getFont();		// the font family of the whole application
	public static final Color COLOR_HOVER = new Color(0x00cccc);
	public static final Color COLOR_NORMAL = new Color(0xa3a3a3);
	/**
	 * Modify a standard JButton
	 * @param name name of the image which will be represent the button
	 * @param toolTip tooltip of the button
	 * @param listener actionlistener of the button
	 * @return modified (imageIcon, transparent, bigger, other behavoir) JButton
	 */
	public static JButton getButton(String name, String toolTip, ActionListener listener) 
	{
        // load the image
		ScreenManager screen = ScreenManager.getInstance();
		ImageIcon iconRollover = new ImageIcon(ResourceManager.loadImage(ResourceManager.MENU_IMAGE_FOLDER + name + ".png"));
		int w = iconRollover.getIconWidth();
		int h = iconRollover.getIconHeight();

        // get the cursor for this button
        Cursor cursor =
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

        // make translucent default image
        Image image = screen.createCompatibleImage(w, h,
            Transparency.TRANSLUCENT);
        Graphics2D g = (Graphics2D)image.getGraphics();
        Composite alpha = AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER, .5f);
        g.setComposite(alpha);
        g.drawImage(iconRollover.getImage(), 0, 0, null);
        g.dispose();
        ImageIcon iconDefault = new ImageIcon(image);

        // make a pressed image
        image = screen.createCompatibleImage(w, h,
            Transparency.TRANSLUCENT);
        g = (Graphics2D)image.getGraphics();
        g.drawImage(iconRollover.getImage(), 2, 2, null);
        g.dispose();
        ImageIcon iconPressed = new ImageIcon(image);

        // create the button
        JButton button = new JButton();
        button.addActionListener(listener);
        button.setIgnoreRepaint(true);
        button.setFocusable(false);
        button.setToolTipText(toolTip);
        button.setBorder(null);
        button.setContentAreaFilled(false);
		button.setFocusPainted(false);
        button.setCursor(cursor);
        button.setIcon(iconDefault);
        button.setRolloverIcon(iconRollover);
        button.setPressedIcon(iconPressed);

        return button;
    }

	/**
	 * create and return the option menu (BigOptionPanel)
	 * @param inputManager manager for all user input action like mouse and key events
	 * @return optionPanel the option menu of the application
	 */
	public static BigOptionPanel getBigOptionPanel(InputManager inputManager)
	{
		ScreenManager screen = ScreenManager.getInstance();
		//MenuWindow
		BigOptionPanel optionPanel = new BigOptionPanel(ScreenManager.getInstance(), inputManager);
		optionPanel.setBackgroundImage(ResourceManager.loadImage(ResourceManager.IMAGE_FOLDER + "menu_background.jpg"));
		optionPanel.setSize(screen.getWidth(), screen.getHeight());
		optionPanel.setLocation((screen.getWidth()-optionPanel.getWidth())/2,
        		(screen.getHeight()-optionPanel.getHeight())/2);
        return optionPanel;
	}

	/**
	 * create and return the main menu
	 * @return menuWindow main menu panel
	 */
	public static MenuPanel getMenuPanel()
	{
		ScreenManager screen = ScreenManager.getInstance();
		//MenuWindow
        MenuPanel menuWindow = new MenuPanel(screen);
		menuWindow.setBackgroundImage(ResourceManager.loadImage(ResourceManager.IMAGE_FOLDER + "menu_background.jpg"));
        menuWindow.setSize(screen.getWidth(), screen.getHeight());
        menuWindow.setLocation((screen.getWidth()-menuWindow.getWidth())/2,
        		(screen.getHeight()-menuWindow.getHeight())/2);
        menuWindow.setVisible(true);        
        return menuWindow;
	}

	/**
	 * create and return the main menu
	 * @return menuWindow main menu panel
	 */
	public static ListMenuPanel getListMenuPanel()
	{
		ScreenManager screen = ScreenManager.getInstance();
		//MenuWindow
        ListMenuPanel menuWindow = new ListMenuPanel();
		menuWindow.setBackgroundImage(ResourceManager.loadImage("images/menu_background.jpg"));
        menuWindow.setSize(screen.getWidth(), screen.getHeight());
        menuWindow.setLocation((screen.getWidth()-menuWindow.getWidth())/2,
        		(screen.getHeight()-menuWindow.getHeight())/2);
        menuWindow.setVisible(true);
        return menuWindow;
	}


	/**
	 * create and return the in game info panel
	 * @return infoPanel panel which displays the users life, points, level and so on
	 */
	public static InfoPanel getInfoPanel()
	{
		InfoPanel infoPanel = new InfoPanel();
		infoPanel.setOpaque(false);
		return infoPanel;
	}

	/**
	 * create and return the big option menu panel
	 * @param inputManger manager for all user input action like mouse and key events
	 * @return optionPanel the option menu of the application
	 */
	public static OptionPanel getOptionPanel(InputManager inputManger)
	{
		return new OptionPanel(inputManger);
	}

	private static String getFont()
	{
		if(ResourceManager.getOS() == OS.Win)
		{
			return "Comic Sans MS";
		}
		else if(ResourceManager.getOS() == OS.Mac)
		{
			return "BlairMdITC TT";
		}
		return "SansSerif";
	}
}
