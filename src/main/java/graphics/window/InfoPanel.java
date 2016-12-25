package graphics.window;

import graphics.ImagePanel;
import java.awt.FlowLayout;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;
import tilegame.Profile;
import tilegame.ResourceManager;
import tilegame.sprites.IProfileHandler;


/**
 * represents the info panel in the right upper corner in the game
 * @author Benedikt Zönnchen
 */
@SuppressWarnings("serial")
public final class InfoPanel extends JPanel implements IProfileHandler
{
	private Profile profile;
	private JLabel pointLabel;
	private JLabel levelLabel;
	private JLabel lifeLabel;

	/**
	 * standard constrcutor
	 */
	public InfoPanel()
	{
		this.profile = Profile.getInstance();
		this.init();
	}

	/**
	 * 
	 * @param profile the user profile with level, life and points information
	 */
	@Override
	public void setProfile(Profile profile)
	{
		this.profile = profile;
	}

	/**
	 * build the whole info panel content
	 */
	public void init()
	{
		setOpaque(true);
		setLayout(new FlowLayout(FlowLayout.RIGHT));
		int width = 210;
		int height = 60;
		ImagePanel pointPanel = new ImagePanel(ResourceManager.loadImage(ResourceManager.IMAGE_FOLDER + "BigCoin.png"));
		add(pointPanel);
		pointLabel = new JLabel();
		add(pointLabel);
		width += pointPanel.getWidth() + pointLabel.getWidth() + 5;
		
		JPanel spacePanel = new JPanel();
		spacePanel.setSize(20, 60);
		spacePanel.setOpaque(false);
		spacePanel.setPreferredSize(spacePanel.getSize());
		add(spacePanel);
		width += spacePanel.getWidth();
		
		ImagePanel starPanel = new ImagePanel(ResourceManager.loadImage(ResourceManager.IMAGE_FOLDER + "star.png"));
		add(starPanel);
		levelLabel = new JLabel();
		add(levelLabel);
		width += starPanel.getWidth() + levelLabel.getWidth() + 5;
		
		JPanel spacePanel2 = new JPanel();
		spacePanel2.setSize(20, 60);
		spacePanel2.setOpaque(false);
		spacePanel2.setPreferredSize(spacePanel.getSize());
		add(spacePanel2);
		width += spacePanel2.getWidth();
		
		ImagePanel heartPanel = new ImagePanel(ResourceManager.loadImage(ResourceManager.IMAGE_FOLDER + "heart.png"));
		add(heartPanel);
		lifeLabel = new JLabel();
		add(lifeLabel);
		width += starPanel.getWidth() + lifeLabel.getWidth() + 5;
		
		this.setSize(width, height);
		this.setPreferredSize(this.getSize());
	}

	/**
	 * draw the info panel with the level, points and lifes of the current profile
	 * @param g paint target
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		pointLabel.setText("<html><font size=\"12\" color=\"#FF0080\">"+profile.getPoints()+"</font></html>");
		levelLabel.setText("<html><font size=\"12\" color=\"#FF0080\">"+profile.getLevel()+"</font></html>");
		lifeLabel.setText("<html><font size=\"12\" color=\"#FF0080\">"+profile.getLifes()+"</font></html>");
	}

	@Override
	public Profile getProfile()
	{
		return profile;
	}
}
