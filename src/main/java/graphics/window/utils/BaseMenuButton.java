/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package graphics.window.utils;

import graphics.GraphicFactory;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JButton;


/**
 * 
 * @author Bene
 */
public abstract class BaseMenuButton extends JButton implements FocusListener
{
	public static final int TEXT_SIZE_HOVER = 40;
	public static final int TEXT_SIZE_NORMAL = 30;
	private String text;
	private final Font normalFont;
	private final Font hoverFont;


	public BaseMenuButton(String text)
	{
		this.text = text;
		setText(text);
		setForeground(GraphicFactory.COLOR_NORMAL);
		setOpaque(false);
		normalFont = new Font(GraphicFactory.FONT_FAMILY, Font.PLAIN, TEXT_SIZE_NORMAL);
		hoverFont = new Font(GraphicFactory.FONT_FAMILY, Font.PLAIN, TEXT_SIZE_HOVER);
		setFont(normalFont);
		addFocusListener(this);
		setBorder(null);
		setContentAreaFilled(false);
		setMinimumSize(new Dimension(400, 40));
		setPreferredSize(getMinimumSize());
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	/**
	 * change the display if the button gain focus (hover)
	 * @param e focus event
	 */
	@Override
	public void focusGained(FocusEvent fe)
	{
		setForeground(GraphicFactory.COLOR_HOVER);
		setFont(hoverFont);
	}

	/**
	 * change the display if the button lose focus (remove hover)
	 * @param e
	 */
	@Override
	public void focusLost(FocusEvent fe)
	{
		setForeground(GraphicFactory.COLOR_NORMAL);
		setFont(normalFont);
	}
}
