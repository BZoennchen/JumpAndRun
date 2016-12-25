package graphics;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 * an panel with an background image
 * @author Benedikt Zönnchen
 */
@SuppressWarnings("serial")
public class ImagePanel extends JPanel
{
	private Image image;

	/**
	 * standard constructor
	 * @param image the background image
	 */
	public ImagePanel(Image image)
	{
		this.image = image;
		setSize(image.getWidth(null), image.getHeight(null));
		setPreferredSize(this.getSize());
		setOpaque(false);
	}

	/**
	 * override the standard method. call the super and draw an background image
	 * @param g the paint target
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(image, 0, 0, image.getWidth(null), image.getHeight(null), null);
	}
}
