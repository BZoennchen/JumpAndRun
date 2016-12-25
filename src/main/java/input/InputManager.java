package input;


import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/**
 * The InputManager manages all input of key and mouse events. Events are mapped to
 * GameActions.
 */
public class InputManager implements KeyListener, MouseListener,
		MouseMotionListener, MouseWheelListener
{
	/**
	 * An invisible cursor.
	 */
	public static final Cursor	INVISIBLE_CURSOR	= Toolkit.getDefaultToolkit()
															.createCustomCursor(Toolkit.getDefaultToolkit()
															.getImage(""),new Point(0,0),"invisible");

	// mouse codes
	public static final int		MOUSE_MOVE_LEFT		= 0;
	public static final int		MOUSE_MOVE_RIGHT	= 1;
	public static final int		MOUSE_MOVE_UP		= 2;
	public static final int		MOUSE_MOVE_DOWN		= 3;
	public static final int		MOUSE_WHEEL_UP		= 4;
	public static final int		MOUSE_WHEEL_DOWN	= 5;
	public static final int		MOUSE_BUTTON_1		= 6;
	public static final int		MOUSE_BUTTON_2		= 7;
	public static final int		MOUSE_BUTTON_3		= 8;

	private static final int	NUM_MOUSE_CODES		= 9;

	// key codes are defined in java.awt.KeyEvent.
	// most of the codes (except for some rare ones like
	// "alt graph") are less than 600.
	private static final int	NUM_KEY_CODES		= 600;

	private GameAction[]		keyActions			= new GameAction[NUM_KEY_CODES];
	private GameAction[]		mouseActions		= new GameAction[NUM_MOUSE_CODES];

	private Point				mouseLocation;
	private Point				centerLocation;
	private Component			comp;
	private Robot				robot;
	private boolean				isRecentering;
	private List<Integer>		disabledKeys		= new ArrayList<Integer>();

	/**
	 * Creates a new InputManager that listens to input from the specified
	 * component.
	 * @param comp the component which will be observed by the input manager
	 */
	public InputManager(Component comp)
	{
		this.comp = comp;
		mouseLocation = new Point();
		centerLocation = new Point();

		// register key and mouse listeners
		comp.addKeyListener(this);
		comp.addMouseListener(this);
		comp.addMouseMotionListener(this);
		comp.addMouseWheelListener(this);

		// allow input of the TAB key and other keys normally
		// used for focus traversal
		comp.setFocusTraversalKeysEnabled(false);
	}

	/**
	 * Sets the cursor on this InputManager's input component.
	 */
	public void setCursor(Cursor cursor)
	{
		comp.setCursor(cursor);
	}

	/**
	 * Sets whether realtive mouse mode is on or not. For relative mouse mode,
	 * the mouse is "locked" in the center of the screen, and only the changed
	 * in mouse movement is measured. In normal mode, the mouse is free to move
	 * about the screen.
	 * @param mode true=recenter mouse mode, false=standard mode
	 */
	public void setRelativeMouseMode(boolean mode)
	{
		if (mode == isRelativeMouseMode()) { return; }

		if (mode)
		{
			try
			{
				robot = new Robot();
				recenterMouse();
			}
			catch (AWTException ex)
			{
				// couldn't create robot!
				robot = null;
			}
		}
		else
		{
			robot = null;
		}
	}

	/**
	 * Returns whether or not relative mouse mode is on.
	 */
	public boolean isRelativeMouseMode()
	{
		return (robot != null);
	}

	/**
	 * Maps a GameAction to a specific key. The key codes are defined in
	 * java.awt.KeyEvent. If the key already has a GameAction mapped to it, the
	 * new GameAction overwrites it.
	 * @param gameAction game action which will map to the mouse code
	 * @param keyCode key code
	 */
	public void mapToKey(GameAction gameAction, int keyCode)
	{
		keyActions[keyCode] = gameAction;
	}

	/**
	 * Maps a GameAction to a specific mouse action. The mouse codes are defined
	 * herer in InputManager (MOUSE_MOVE_LEFT, MOUSE_BUTTON_1, etc). If the
	 * mouse action already has a GameAction mapped to it, the new GameAction
	 * overwrites it.
	 * @param gameAction game action which will map to the mouse code
	 * @param mouseCode mouse code 
	 */
	public void mapToMouse(GameAction gameAction, int mouseCode)
	{
		mouseActions[mouseCode] = gameAction;
	}

	/**
	 * Clears all mapped keys and mouse actions to this GameAction.
	 */
	public void clearMap(GameAction gameAction)
	{
		for (int i = 0; i < keyActions.length; i++)
		{
			if (keyActions[i] == gameAction)
			{
				keyActions[i] = null;
			}
		}

		for (int i = 0; i < mouseActions.length; i++)
		{
			if (mouseActions[i] == gameAction)
			{
				mouseActions[i] = null;
			}
		}

		gameAction.reset();
	}

	/**
	 * Gets a List of names of the keys and mouse actions mapped to this
	 * GameAction. Each entry in the List is a String.
	 * @param gameCode game action
	 * @return map map with all mouse and key names which are bound on the game action
	 */
	public List<String> getMaps(GameAction gameCode)
	{
		ArrayList<String> list = new ArrayList<String>();

		for (int i = 0; i < keyActions.length; i++)
		{
			if (keyActions[i] == gameCode)
			{
				list.add(getKeyName(i));
			}
		}

		for (int i = 0; i < mouseActions.length; i++)
		{
			if (mouseActions[i] == gameCode)
			{
				list.add(getMouseName(i));
			}
		}
		return list;
	}

	/**
	 * Gets a List of codes of the keys and mouse actions mapped to this
	 * GameAction. Each entry in the List is a code.
	 * @param gameCode game action
	 * @return map map with all mouse and key codes which are bound on the game action
	 */
	public List<Integer> getKeyCodeMaps(GameAction gameCode)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();

		for (int i = 0; i < keyActions.length; i++)
		{
			if (keyActions[i] == gameCode)
			{
				list.add(i);
			}
		}

		for (int i = 0; i < mouseActions.length; i++)
		{
			if (mouseActions[i] == gameCode)
			{
				list.add(i);
			}
		}
		return list;
	}

	/**
	 * Resets all GameActions so they appear like they haven't been pressed.
	 */
	public void resetAllGameActions()
	{
		for (int i = 0; i < keyActions.length; i++)
		{
			if (keyActions[i] != null)
			{
				keyActions[i].reset();
			}
		}

		for (int i = 0; i < mouseActions.length; i++)
		{
			if (mouseActions[i] != null)
			{
				mouseActions[i].reset();
			}
		}
	}

	/**
	 * Gets the name of a key code.
	 * @param keyCode key code
	 */
	public static String getKeyName(int keyCode)
	{
		return KeyEvent.getKeyText(keyCode);
	}
	
	public boolean isKeyBindable(int keyCode)
	{
		if(disabledKeys.contains(keyCode))
		{
			return false;
		}
		
		if(keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_RIGHT 
				|| keyCode == KeyEvent.VK_TAB || keyCode == KeyEvent.VK_UP 
				|| keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_LEFT)
		{
			return true;
		}
		else if((48 <= keyCode && keyCode <= 57) ||
				 (65 <= keyCode && keyCode <= 90))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Gets the name of a mouse code.
	 * @param mouseCode mouse code
	 */
	public static String getMouseName(int mouseCode)
	{
		switch (mouseCode)
		{
			case MOUSE_MOVE_LEFT:
				return "Mouse Left";
			case MOUSE_MOVE_RIGHT:
				return "Mouse Right";
			case MOUSE_MOVE_UP:
				return "Mouse Up";
			case MOUSE_MOVE_DOWN:
				return "Mouse Down";
			case MOUSE_WHEEL_UP:
				return "Mouse Wheel Up";
			case MOUSE_WHEEL_DOWN:
				return "Mouse Wheel Down";
			case MOUSE_BUTTON_1:
				return "Mouse Button 1";
			case MOUSE_BUTTON_2:
				return "Mouse Button 2";
			case MOUSE_BUTTON_3:
				return "Mouse Button 3";
			default:
				return "Unknown mouse code " + mouseCode;
		}
	}

	/**
	 * Gets the x position of the mouse.
	 * @return x
	 */
	public int getMouseX()
	{
		return mouseLocation.x;
	}

	/**
	 * Gets the y position of the mouse.
	 * @return y
	 */
	public int getMouseY()
	{
		return mouseLocation.y;
	}

	/**
	 * Uses the Robot class to try to postion the mouse in the center of the
	 * screen.
	 * <p>
	 * Note that use of the Robot class may not be available on all platforms.
	 */
	private synchronized void recenterMouse()
	{
		if (robot != null && comp.isShowing())
		{
			centerLocation.x = comp.getWidth() / 2;
			centerLocation.y = comp.getHeight() / 2;
			SwingUtilities.convertPointToScreen(centerLocation, comp);
			isRecentering = true;
			robot.mouseMove(centerLocation.x, centerLocation.y);
		}
	}

	/**
	 * return the game action which is bound on the key event
	 * @param e key event
	 * @return gameAction
	 */
	private GameAction getKeyAction(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		if (keyCode < keyActions.length)
		{
			return keyActions[keyCode];
		}
		else
		{
			return null;
		}
	}

	/**
	 * Gets the mouse code for the button specified in this MouseEvent.
	 * @param e mouse event
	 * @return mouseCode mouse code
	 */
	public static int getMouseButtonCode(MouseEvent e)
	{
		switch (e.getButton())
		{
			case MouseEvent.BUTTON1:
				return MOUSE_BUTTON_1;
			case MouseEvent.BUTTON2:
				return MOUSE_BUTTON_2;
			case MouseEvent.BUTTON3:
				return MOUSE_BUTTON_3;
			default:
				return -1;
		}
	}

	/**
	 * Gets the game action for the mouse event 
	 * @param e mouse event
	 * @return gameAction
	 */
	private GameAction getMouseButtonAction(MouseEvent e)
	{
		int mouseCode = getMouseButtonCode(e);
		if (mouseCode != -1)
		{
			return mouseActions[mouseCode];
		}
		else
		{
			return null;
		}
	}

	/**
	 * activate the game action press action which is bound on the key event
	 * @param e key event
	 */
	@Override
	public void keyPressed(KeyEvent e)
	{
		GameAction gameAction = getKeyAction(e);
		if (gameAction != null)
		{
			gameAction.press();
		}
		// make sure the key isn't processed for anything else
		e.consume();
	}

	/**
	 * activate the realese action in the specified game action for the key event
	 * @param e key event
	 */
	@Override
	public void keyReleased(KeyEvent e)
	{
		GameAction gameAction = getKeyAction(e);
		if (gameAction != null)
		{
			gameAction.release();
		}
		// make sure the key isn't processed for anything else
		e.consume();
	}

	/**
	 * make sure the key isn't processed for anything else
	 * @param e key event
	 */
	@Override
	public void keyTyped(KeyEvent e)
	{
		e.consume();
	}

	/**
	 * from the MouseListener interface
	 * @param e mouse event
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		GameAction gameAction = getMouseButtonAction(e);
		if (gameAction != null)
		{
			gameAction.press();
		}
	}

	 
	/**
	 * from the MouseListener interface
	 * @param e mouse event
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		GameAction gameAction = getMouseButtonAction(e);
		if (gameAction != null)
		{
			gameAction.release();
		}
	}

	
	/**
	 * from the MouseListener interface
	 * @param e mouse event
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
	// do nothing
	}

	
	/**
	 * from the MouseListener interface
	 * @param e mouse event
	 */
	@Override
	public void mouseEntered(MouseEvent e)
	{
		mouseMoved(e);
	}

	
	/**
	 * from the MouseListener interface
	 * @param e mouse event
	 */
	@Override
	public void mouseExited(MouseEvent e)
	{
		mouseMoved(e);
	}

	
	/**
	 * from the MouseMotionListener interface
	 * @param e mouse event
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseMoved(e);
	}

	
	/**
	 * from the MouseMotionListener interface
	 * @param e mouse event
	 */
	@Override
	public synchronized void mouseMoved(MouseEvent e)
	{
		// this event is from re-centering the mouse - ignore it
		if (isRecentering && centerLocation.x == e.getX()
				&& centerLocation.y == e.getY())
		{
			isRecentering = false;
		}
		else
		{
			int dx = e.getX() - mouseLocation.x;
			int dy = e.getY() - mouseLocation.y;
			mouseHelper(MOUSE_MOVE_LEFT, MOUSE_MOVE_RIGHT, dx);
			mouseHelper(MOUSE_MOVE_UP, MOUSE_MOVE_DOWN, dy);

			if (isRelativeMouseMode())
			{
				recenterMouse();
			}
		}

		mouseLocation.x = e.getX();
		mouseLocation.y = e.getY();

	}

	
	/**
	 * from the MouseWheelListener interface
	 * @param e mouse wheel event
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		mouseHelper(MOUSE_WHEEL_UP, MOUSE_WHEEL_DOWN, e.getWheelRotation());
	}

	/**
	 * to do
	 * @param codeNeg
	 * @param codePos
	 * @param amount
	 */
	private void mouseHelper(int codeNeg, int codePos, int amount)
	{
		GameAction gameAction;
		if (amount < 0)
		{
			gameAction = mouseActions[codeNeg];
		}
		else
		{
			gameAction = mouseActions[codePos];
		}
		if (gameAction != null)
		{
			gameAction.press(Math.abs(amount));
			gameAction.release();
		}
	}

	/**
	 * disable a key
	 * @param keyCode key code
	 */
	public void disabledKey(Integer keyCode)
	{
		disabledKeys.remove(keyCode);
		disabledKeys.add(keyCode);
	}

	/**
	 * enable a key
	 * @param keyCode key code
	 */
	public void enableKey(Integer keyCode)
	{
		disabledKeys.remove(keyCode);
	}
}
