package graphics.window;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import graphics.ScreenManager;
import graphics.window.utils.InputTextField;
import input.GameAction;
import input.InputManager;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * the small centered option menu.
 * NOT IN USE!
 * @author Benedikt Zönnchen
 */
@SuppressWarnings("serial")
public final class OptionPanel extends JPanel
{
	private JPanel optionPanel;
	private InputManager inputManager;
	private ScreenManager screen;
	private List<GameAction> gameActions;
	
	public OptionPanel(InputManager inputManager)
	{
		this.inputManager = inputManager;
		this.gameActions = new ArrayList<GameAction>();
		setOpaque(false);
		init();
	}
	
	@Override
	public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Color ppColor = new Color(2, 70, 110, 70); //r,g,b,alpha
        g.setColor(ppColor);
        g.fillRect(0,0,getWidth(),getHeight()); //x,y,width,height
    }    

	
	public void init(){
		optionPanel = new JPanel();
		optionPanel.setOpaque(false);
		optionPanel.setLayout(new GridLayout(5,2));
		optionPanel.setPreferredSize(optionPanel.getSize());
		add(optionPanel);
		setVisible(false);
	}
	
	public boolean isOpened()
	{
		return this.isVisible();
	}
	
	public void addGameAcion(GameAction action)
	{
		addGameAcion(action, true);
	}
	
	public void addGameAcion(GameAction action, boolean enabled)
	{
		gameActions.add(action);
		String labelName = action.getName();
		if(labelName.length() > 0)
		{
			String firstChar = (labelName.charAt(0)+"").toUpperCase();
			labelName = firstChar+labelName.substring(1);
		}

		JLabel label = new JLabel("<HTML><FONT COLOR=RED>"+labelName+":</FONT></HTML>");
		InputTextField textfield = new InputTextField(action, inputManager, screen);
		textfield.setTextFieldEnabled(enabled);
		optionPanel.setLayout(new GridLayout(gameActions.size()*2, 2));
		optionPanel.add(label);
		optionPanel.add(textfield);
		
		optionPanel.add(new JLabel(""));
		optionPanel.add(new JLabel(""));
		
		optionPanel.setSize(300, 35*gameActions.size());
		optionPanel.setPreferredSize(optionPanel.getSize());
		setSize(optionPanel.getPreferredSize());
		setPreferredSize(this.getSize());
	}
}
