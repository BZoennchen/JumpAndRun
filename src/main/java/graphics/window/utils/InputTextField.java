package graphics.window.utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.List;

import graphics.GraphicFactory;
import graphics.ScreenManager;
import input.GameAction;
import input.InputManager;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

/**
 * a textfield where the user can bind a key to an game action like jump and so on
 * @author Benedikt Zönnchen
 */
@SuppressWarnings("serial")
public class InputTextField extends JTextField implements KeyListener, FocusListener
{
	private GameAction gameAction;
	private InputManager inputManager;
	private ScreenManager screen;
	private boolean enabled;
	private final int TEXT_SIZE_HOVER = 40;
	private final int TEXT_SIZE_NORMAL = 30;
	private final Font normalFont;
	private final Font hoverFont;

	/**
	 * standard constructor
	 * @param gameAction the current bound game action
	 * @param inputManager the manager for all user input events
	 * @param screen the manager for putting the textfield on the right place
	 */
	public InputTextField(GameAction gameAction, InputManager inputManager, ScreenManager screen)
	{
		enableEvents(KeyEvent.KEY_EVENT_MASK | MouseEvent.MOUSE_EVENT_MASK |
				MouseEvent.MOUSE_MOTION_EVENT_MASK | MouseEvent.MOUSE_WHEEL_EVENT_MASK);
		setEditable(false);
		this.gameAction = gameAction;
		this.inputManager = inputManager;
		this.screen = screen;
		normalFont = new Font(GraphicFactory.FONT_FAMILY, Font.PLAIN, TEXT_SIZE_NORMAL);
		hoverFont = new Font(GraphicFactory.FONT_FAMILY, Font.PLAIN, TEXT_SIZE_HOVER);
		this.setFont(normalFont);
		this.setForeground(GraphicFactory.COLOR_NORMAL);
		addKeyListener(this);
		addFocusListener(this);
		setHorizontalAlignment(JTextField.CENTER);
		List<String> keys = inputManager.getMaps(gameAction);
		setMinimumSize(new Dimension(400, 40));
		setPreferredSize(getMinimumSize());
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setOpaque(false);
		setBorder(null);
		
		if(!keys.isEmpty()){
			setText(keys.get(0));
		}
	}

	/**
	 * change the textfield mode
	 * @param enabled if true the textfield the game action can be bind on an other key
	 */
	public void setTextFieldEnabled(boolean enabled)
	{
		int keyCode = 0;
		List<Integer> keyCodeMap = inputManager.getKeyCodeMaps(gameAction);
		this.enabled = enabled;
		
		if(keyCodeMap.size() > 0)
		{
			keyCode = keyCodeMap.get(0);
		}
		
		if(enabled)
		{
			inputManager.enableKey(keyCode);
		}
		else
		{
			inputManager.disabledKey(keyCode);
		}
		setEnabled(enabled);
	}

	/**
	 * map a new key to an game action if this textfield is boundable(enabled)
	 * @param event keyEvent
	 */
	@Override
	public void keyPressed(KeyEvent event)
	{
		if(enabled)
		{
			if(inputManager.isKeyBindable(event.getKeyCode()))
			{
				setText(InputManager.getKeyName(event.getKeyCode()));
				inputManager.clearMap(gameAction);
				inputManager.mapToKey(gameAction, event.getKeyCode());
			}

			event.consume();
		}	
		screen.getScreenWindow().requestFocus();
	}

	@Override
	public void keyReleased(KeyEvent arg0){}

	@Override
	public void keyTyped(KeyEvent arg0){}

	/**
	 * change style if this textfield gain focus by the user
	 * @param arg0 not used
	 */
	@Override
	public void focusGained(FocusEvent arg0)
	{
		this.setFont(hoverFont);
		this.setForeground(GraphicFactory.COLOR_HOVER);
		setBorder(BorderFactory.createLineBorder(GraphicFactory.COLOR_HOVER));
	}

	/**
	 * change style if this textfield lose focus
	 * @param arg0 not used
	 */
	@Override
	public void focusLost(FocusEvent arg0)
	{
		this.setFont(normalFont);
		this.setForeground(GraphicFactory.COLOR_NORMAL);
		setBorder(null);
	}
}
