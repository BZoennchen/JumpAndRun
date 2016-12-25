package core;

import graphics.GraphicFactory;
import graphics.window.BigOptionPanel;
import graphics.window.InfoPanel;
import graphics.window.MenuPanel;
import graphics.window.OptionPanel;
import graphics.window.utils.MenuButton;
import input.GameAction;
import input.InputManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import tilegame.Profile;
import tilegame.ResourceManager;
import tilegame.ITileMap;
import tilegame.TileMapRenderer;
import tilegame.sprites.IProfileHandler;
import tilegame.sprites.Player;
import tilegame.sprites.Sprite;
import tilegame.sprites.powerUp.PowerUp;

/**
 * The GameManager extends the GameCore and manages the whole game progress.
 * It controls the game and sysem state, use the inputManager, screenManager
 * and resourceManager. The GameManager is the central point (where all Objects
 * interaction comes together) and the most significant class in the Gameapplication.
 *
 * At the current state of Development the GameManager has to much responsibility.
 * So we need more classes for this problem.
 *
 * Definitions:
 * game state: all interaction in the running game (jump, move and so on...)
 * system state: all interaction in the running Application but not in the
 * running game (pause, go to options, quit and so on...)
 *
 * @author Benedikt Zönnchen
 */
public class GameManager extends GameCore implements ActionListener
{
	public static final float GRAVITY = Player.GRAVITY;
	private boolean gameStarted = false;
	private Window window;
	protected InputManager inputManager;
	protected Player player;
	private boolean paused;
	private int mapCounter;
	private JButton playButton;
	private JButton configButton;
	private JButton quitButton;
	private JButton pauseButton;
	private JPanel playButtonSpace;
	private OptionPanel optionWindow;
	private BigOptionPanel bigOptionPanel;
	private ITileMap tileMap;
	private TileMapRenderer tileMapRenderer;
	protected GameAction jump;
	protected GameAction exit;
	protected GameAction moveLeft;
	protected GameAction moveRight;
	protected GameAction pause;
	protected GameAction option;
	protected GameAction startGame;
	protected GameAction menu;
	protected GameAction saveProfile, loadProfile;
	protected ResourceManager resourceManager;
	private java.util.List<Sprite> deletedSprites = new ArrayList<Sprite>();
	protected MenuPanel menuWindow;
	protected InfoPanel infoPanel;
	protected Profile profile;

	private int maxHeight, minHeight, maxWidth, minWidth;
	/**
	 * The start point
	 * @param input empty array
	 */
	public static void main(String input[])
	{
		new GameManager().run();
	}

	/**
	 * the initiation method. This will be called only once.
	 */
	@Override
	public void init()
	{
		super.init();
		resourceManager = ResourceManager.getInstance();
		window = this.screen.getScreenWindow();
		inputManager = new InputManager(window);
		profile = Profile.getInstance();
		mapCounter = profile.getLevel();
		createGameActions();
		// initialization methods
		initComponents();
	}

	/**
	 * Starts and restart the game. If the user is finally dead the profile
	 * will be resetted and the right level will be load.
	 */
	public void startGame()
	{
		// load tileMap
		if (profile.getLifes() <= 0)
		{
			profile.reset();
		}
		try
		{
			tileMap = resourceManager.loadMap(ResourceManager.MAP_NAMES + profile.getLevel() + ResourceManager.MAP_DATATYPE);
			tileMap.setProfile(profile);
			maxHeight = Math.max(TileMapRenderer.tileToPixel(tileMap.getHeight() + 10), screen.getHeight());
			maxWidth = Math.max(TileMapRenderer.tileToPixel(tileMap.getWidth()), screen.getWidth());
			minHeight = -TileMapRenderer.TILE_SIZE;
			minWidth = -TileMapRenderer.TILE_SIZE;
			// TODO:
			//resourceManager.saveMap(tileMap, "maps/states/testSave.txt");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		tileMapRenderer = new TileMapRenderer(tileMap, screen);
		tileMapRenderer.setBackground(ResourceManager.loadImage(ResourceManager.IMAGE_FOLDER + "/background.jpg"));
		player = tileMap.getPlayer();
	}

	/**
	 * Updates the game state. This is the place where everything happens.
	 * First it checks the System input. Secondly the game input will be checked
	 * and than all old game objects will be deleted.
	 * @param elapsedTime the elapsed time
	 */
	@Override
	public void update(long elapsedTime)
	{

		checkSystemInput();
		if (!isPaused() && isStarted())
		{
			if (mapCounter != profile.getLevel())
			{
				mapCounter = profile.getLevel();
				startGame();
			}

			checkGameInput();
			java.util.List<Sprite> copy = new ArrayList<Sprite>(tileMap.getSpriteList());

			for (Iterator<Sprite> it = copy.iterator(); it.hasNext();)
			{
				Sprite sprite = it.next();

				if (!sprite.isSleeping())
				{
					sprite.update(elapsedTime);
				}
				/*
				 * delete all sprites which are out of the map area or not longer active
				 */
				if (
						sprite.getY() > maxHeight || sprite.getY() < minHeight ||
						sprite.getX() > maxWidth || sprite.getX() < minWidth||
						sprite instanceof PowerUp && sprite.isDying())
				{
					deletedSprites.add(sprite);
					if(sprite.equals(player) && !player.isDying())
					{
						player.die();
					}
				}
			}

			tileMap.getSpriteList().removeAll(deletedSprites);
			deletedSprites.clear();
		}
	}

	/**
	 * Checks the User input for changing the System state.
	 * The user can switch to the option menu or can quit the game
	 */
	public void checkSystemInput()
	{
		if (pause.isPressed() && !bigOptionPanel.isVisible())
		{
			setPaused(!paused);
		}
		if (startGame.isPressed())
		{
			setGameStarted(true);
			menuWindow.setVisible(false);
			setButtonsEnable(true);
			setPaused(false);
			window.requestFocus();
			if (player == null || player.isDying())
			{
				startGame();
			}
		}
		else if(saveProfile.isPressed())
		{
			try
			{
				resourceManager.saveProfile(profile, "lastGame.ser");
			}
			catch (IOException ex)
			{
				Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		else if(loadProfile.isPressed())
		{
			try
			{
				profile = resourceManager.loadProfile("lastGame.ser");
			}
			catch (Exception ex)
			{
				System.out.println("FEHLER!");
				profile = Profile.getInstance();
			}
			infoPanel.setProfile(profile);
			// To Do bad solution! 
			player = null;
			startGame.tap();
		}
		else if(menu.isPressed())
		{
			setPaused(true);
			MenuButton button = menuWindow.getButton(startGame);
			if (isStarted() && !player.isDying())
			{
				button.setButtonText("Resume Game");
			}
			else
			{
				button.setButtonText("Start Game");
			}
			menuWindow.setVisible(true);
			bigOptionPanel.setVisible(false);
			setButtonsEnable(false);
			//setCursorVisible(true);
		}
		else if(exit.isPressed())
		{
			stop();
		}
		else if(option.isPressed())
		{
			if ((bigOptionPanel.isVisible() && isPaused()) || (!bigOptionPanel.isVisible() && !isPaused()))
			{
				setPaused(!isPaused());
			}

			if(bigOptionPanel.isVisible())
			{
				//setCursorVisible(true);
				if (isStarted())
				{
					menuWindow.setVisible(false);
					setButtonsEnable(true);
				}
				else
				{
					menuWindow.setVisible(true);
					setButtonsEnable(false);
				}
				bigOptionPanel.setVisible(false);
			}
			else
			{
				menuWindow.setVisible(false);
				bigOptionPanel.setVisible(true);
				setButtonsEnable(false);
			}
		}
	}

	/**
	 * Checks the user input for changing the game state.
	 * The user can jump, move and so on...
	 */
	public void checkGameInput()
	{
		if (!player.isDying())
		{
			if (moveLeft.isPressed())
			{
				player.setVelocityX(-(Player.SPEED));
			}
			else if (moveRight.isPressed())
			{
				player.setVelocityX(Player.SPEED);
			}
			else
			{
				player.setVelocityX(0);
			}
			if (jump.isPressed() && !player.isJumping())
			{
				player.jump();
			}
		}
	}

	/**
	 * Draw the whole game or system state on the screen (fullscreen) or
	 * to a specified frame (window mode)
	 * @param g
	 */
	@Override
	public void draw(Graphics2D g)
	{
		JFrame frame = screen.getScreenWindow();

		if (gameStarted)
		{
			tileMapRenderer.draw(g);
		}

		// the layered pane contains things like popups (tooltips,
		// popup menus) and the content pane.
		frame.getLayeredPane().paintComponents(g);

		// todo
		//menuWindow.paintComponent(g);
	}

	/**
	 * Set the game state to pause
	 * @param paused true = pause, false = unpause
	 */
	public void setPaused(boolean paused)
	{
		if (paused != this.paused)
		{
			this.paused = paused;
			inputManager.resetAllGameActions();
		}
		playButtonSpace.removeAll();
		if (isPaused())
		{
			playButtonSpace.add(playButton);
		}
		else
		{
			playButtonSpace.add(pauseButton);
		}
	}

	/**
	 * Checks the user input to the small and transparent ingame option menu
	 * and change the system state (pause, go to config, unpause)
	 * @param e the incomming action event
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();
		if (src == quitButton)
		{
			// fire the "exit" gameAction
			menu.tap();
		}
		else if (src == configButton)
		{
			// doesn't do anything (for now)
			option.tap();
		}
		else if (src == playButton || src == pauseButton)
		{
			// fire the "pause" gameAction
			pause.tap();
		}
	}

	/**
	 * Return true if the game is pause, otherwise it return false
	 * @return paused boolean
	 */
	public boolean isPaused()
	{
		return paused;
	}

	/**
	 * Return true if the game has started, otherwise it return false
	 * @return gameStarted boolean
	 */
	public boolean isStarted()
	{
		return gameStarted;
	}

	/**
	 * Change the system state to game startet or to game not started
	 * @param started boolean
	 */
	public void setGameStarted(boolean started)
	{
		this.gameStarted = started;
	}

	/**
	 * helper method to create all game actions
	 */
	private void createGameActions()
	{
		jump = new GameAction("jump", GameAction.NORMAL);
		moveLeft = new GameAction("moveLeft", GameAction.NORMAL);
		moveRight = new GameAction("moveRight", GameAction.NORMAL);
		pause = new GameAction("pause", GameAction.NORMAL);

		exit = new GameAction("exit", GameAction.NORMAL);
		option = new GameAction("options", GameAction.DETECT_INITAL_PRESS_ONLY);
		menu = new GameAction("Menu", GameAction.DETECT_INITAL_PRESS_ONLY);
		startGame = new GameAction("Start Game", GameAction.DETECT_INITAL_PRESS_ONLY);
		loadProfile = new GameAction("Load Game", GameAction.DETECT_INITAL_PRESS_ONLY);
		saveProfile = new GameAction("Save Game", GameAction.DETECT_INITAL_PRESS_ONLY);

		inputManager.mapToKey(jump, KeyEvent.VK_SPACE);
		inputManager.mapToKey(moveLeft, KeyEvent.VK_A);
		inputManager.mapToKey(moveRight, KeyEvent.VK_D);
		inputManager.mapToKey(jump, KeyEvent.VK_W);
		inputManager.mapToKey(pause, KeyEvent.VK_P);
		//inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
		inputManager.mapToKey(option, KeyEvent.VK_O);
		inputManager.mapToKey(menu, KeyEvent.VK_ESCAPE);
	}

	/**
	 * create the small option menu in the left top corner of the screen
	 */
	private void createOptionMenu()
	{
		// create buttons
		quitButton = GraphicFactory.getButton("quit", "Quit", this);
		playButton = GraphicFactory.getButton("play", "Continue", this);
		pauseButton = GraphicFactory.getButton("pause", "Pause", this);
		configButton = GraphicFactory.getButton("config", "Change Settings", this);

		// create the space where the play/pause buttons go.
		playButtonSpace = new JPanel();
		playButtonSpace.setOpaque(false);

		Container contentPane = screen.getScreenWindow().getContentPane();

		// add components to the screen's content pane
		contentPane.add(playButtonSpace);
		contentPane.add(configButton);
		contentPane.add(quitButton);
	}

	/**
	 * creates the big fullscreen option menu
	 */
	private void createBigOptionMenu()
	{
		Container layeredPane = screen.getScreenWindow().getLayeredPane();
		bigOptionPanel = GraphicFactory.getBigOptionPanel(inputManager);
		bigOptionPanel.addGameAcion(jump);
		bigOptionPanel.addGameAcion(moveRight);
		bigOptionPanel.addGameAcion(moveLeft);
		bigOptionPanel.addGameAcion(option, false);
		bigOptionPanel.addGameAcion(pause, false);
		bigOptionPanel.addMenuGameAction(menu, menuWindow);
		layeredPane.add(bigOptionPanel);
		bigOptionPanel.setVisible(false);
	}

	/**
	 * create the fullscreen main menu
	 */
	private void createMenu()
	{
		Container layeredPane = screen.getScreenWindow().getLayeredPane();
		//MenuWindow
		menuWindow = GraphicFactory.getMenuPanel();
		menuWindow.addButton("Start Game", startGame);
		menuWindow.addButton("Option", option);
		menuWindow.addButton("Load Last Game", loadProfile);
		menuWindow.addButton("Save Game", saveProfile);
		menuWindow.addButton("Exit", exit);
		layeredPane.add(menuWindow);
		menuWindow.setVisible(true);
	}

	/**
	 * create the info panel in the right top corner
	 */
	private void createInfoPanel()
	{
		JFrame frame = screen.getScreenWindow();
		infoPanel = GraphicFactory.getInfoPanel();
		//infoPanel.setSize(screen.getWidth() + 64 - optionWindow.getWidth(), infoPanel.getHeight());
		infoPanel.setPreferredSize(infoPanel.getSize());
		Container contentPane = frame.getContentPane();
		contentPane.add(infoPanel);
		infoPanel.setVisible(true);
	}

	private void setButtonsEnable(boolean enable)
	{
		quitButton.setEnabled(enable);
		quitButton.setVisible(enable);
		playButton.setEnabled(enable);
		playButton.setVisible(enable);
		pauseButton.setEnabled(enable);
		pauseButton.setVisible(enable);
		configButton.setVisible(enable);
		configButton.setEnabled(enable);
	}

	/**
	 * initiate the main frame
	 */
	@Override
	protected void initComponents()
	{
		EventQueue.invokeLater(new Runnable() {
			@Override
            public void run() {
                JFrame frame = screen.getScreenWindow();
				frame.setLayout(new FlowLayout());
				Container contentPane = frame.getContentPane();
				if (contentPane instanceof JComponent)
				{
					((JComponent) contentPane).setOpaque(false);
				}
				contentPane.setLayout(new FlowLayout(FlowLayout.LEFT));
				frame.validate();

				createOptionMenu();
				createMenu();
				createInfoPanel();
				createBigOptionMenu();
            }
        });
	}
}
