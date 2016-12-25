package graphics.window;

import graphics.window.utils.BaseMenuButton;
import graphics.window.utils.ListMenuButton;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A Fullscreen Menu for different amount of listed items.
 * It is possible to scroll throw the items.
 * This list needs the ListMenuButtons!!!
 * The deletion of buttons doesnt work ATM!
 * @author Benedikt Zönnchen
 */
public class ListMenuPanel extends JPanel
{

	protected List<ListMenuButton> buttons;
	private List<Component> glues;
	private ListMenuButton selectedButton;
	private ListMenuButton currentButton;		// last button in the list
	public final int MAX_VISIBLE_ELEMENTS;
	private Image backgroundImage = null;
	private int buttonCount;
	private int firstVisibleIndex = 0;

	/**
	 * Standard constructor
	 */
	public ListMenuPanel()
	{
		this(5);
	}

	/**
	 * Constructor
	 * @param maxVisibleElements maximal amount of element which are displayed
	 */
	public ListMenuPanel(int maxVisibleElements)
	{
		this.MAX_VISIBLE_ELEMENTS = maxVisibleElements;
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		buttons = new ArrayList<ListMenuButton>();
		glues = new ArrayList<Component>();

		add(Box.createVerticalGlue());


		addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusGained(FocusEvent fe)
			{
				selectedButton.requestFocusInWindow();
			}
		});
	}

	/**
	 * paint all component and the background
	 * @param g standard Graphics object, generall from the main frame
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (backgroundImage != null)
		{
			g.drawImage(backgroundImage, 0, 0, backgroundImage.getWidth(null), backgroundImage.getHeight(null), null);
		}
	}

	/**
	 * change the view if the user press down and if he is at the end of the visible list
	 * @param button button which will be selected by the user
	 */
	public void appendEnd(ListMenuButton button)
	{
		if (buttons.contains(button) && buttons.indexOf(button) >= (firstVisibleIndex + MAX_VISIBLE_ELEMENTS))
		{
			super.remove(buttons.get(firstVisibleIndex));
			super.remove(glues.get(firstVisibleIndex));
			add(button);
			firstVisibleIndex++;
		}
	}

	/**
	 * changes the view if the user press up and if he is at the begin of the visible list
	 * @param button button which will be selected by the user
	 */
	public void appendStart(ListMenuButton button)
	{
		if(buttons.contains(button) && buttons.indexOf(button) < (firstVisibleIndex))
		{
			firstVisibleIndex--;
			removeAll();
			glues.clear();
			add(Box.createVerticalGlue());
			for (int i = firstVisibleIndex; i < (firstVisibleIndex + MAX_VISIBLE_ELEMENTS); i++)
			{
				add(buttons.get(i));
			}

		}
	}

	/**
	 * return a specified button if he is in this List
	 * @param button the specified button
	 * @return button or null
	 */
	public ListMenuButton getButton(BaseMenuButton button)
	{
		for (Iterator<ListMenuButton> iterator = buttons.iterator(); iterator.hasNext();)
		{
			ListMenuButton tbutton = (ListMenuButton) iterator.next();
			if (tbutton.equals(button))
			{
				return tbutton;
			}
		}
		return null;
	}

	/**
	 * get the button by the index
	 * @param index
	 * @return buttpn
	 */
	public ListMenuButton getButton(int index)
	{
		return buttons.get(index);
	}

	/**
	 * removes the button from the list
	 * @param index
	 */
	public void removeButton(int index)
	{
		removeButton(getButton(index));
	}

	/**
	 * adds a new button to the end of the list.
	 * If the max visible element counter is reach
	 * the button will only add in the list and not to the panel itself
	 * @param button button
	 */
	public void addButton(ListMenuButton button)
	{
		button.setPreviews(currentButton);

		if (currentButton != null)
		{
			currentButton.setNext(button);
		}

		currentButton = button;

		if (buttonCount < MAX_VISIBLE_ELEMENTS)
		{
			add(button);
		}

		buttonCount++;
		buttons.add(button);

		if (selectedButton == null)
		{
			selectedButton = button;
		}
	}

	/**
	 * add the button to the panel. It centered the button and adds a new glue
	 * for better look
	 * @param button button
	 */
	protected void add(JButton button)
	{
		button.repaint();
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		Component comp = Box.createVerticalGlue();
		glues.add(comp);
		super.add(button);
		super.add(comp);
	}

	/**
	 * remove the button from the panel. The glue will also deleted if
	 * the button was visible.
	 * @param button button
	 */
	protected void remove(JButton button)
	{
		int index = contains(button);
		if(index != -1)
		{
			Component comp = getComponent(index+1);
			super.remove(comp);
			super.remove(getComponent(index));
			glues.remove(comp);
		}
	}

	/** ToDo: Rename the buttons!
	 * remove one button in the list
	 * @param button
	 */
	public void removeButton(ListMenuButton button)
	{
		if(buttons.contains(button))
		{
			buttonCount--;
			int index = buttons.indexOf(button);
			ListMenuButton previews = null;
			ListMenuButton next = null;

			if(index+1 < buttons.size())
			{
				next = getButton(index+1);
			}

			if(index-1 >= 0)
			{
				previews = getButton(index-1);
			}

			if(previews != null)
			{
				previews.setNext(next);
			}

			if(next != null)
			{
				next.setPreviews(next);
			}

			buttons.remove(button);
			remove(button);
			currentButton = buttons.get(buttons.size()-1);
			if (button == selectedButton)
			{
				selectedButton = buttons.get(0);
			}
		}	
	}

	/**
	 * return the background image
	 * @return backgroundImage the image
	 */
	public Image getBackgroundImage()
	{
		return backgroundImage;
	}

	/**
	 * set the background image
	 * @param backgroundImage the image
	 */
	public void setBackgroundImage(Image backgroundImage)
	{
		this.backgroundImage = backgroundImage;
	}

	/**
	 * returns the amount of button in this list
	 * @return buttonCount
	 */
	public int getButtonCount()
	{
		return buttonCount;
	}

	/**
	 * checks if the button is on the panel (not in the list!)
	 * if it is so it returns the index of the component otherwise it returns -1
	 * @param button
	 * @return index or -1
	 */
	public int contains(JButton button)
	{
		Component[] cmps = getComponents();
		for(int i = 0; i < cmps.length; i++)
		{
			if(cmps[i].equals(button))
			{
				return i;
			}
		}
		return -1;
	}
}
