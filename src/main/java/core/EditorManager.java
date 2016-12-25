package core;

import graphics.GraphicFactory;
import graphics.window.ListMenuPanel;
import graphics.window.utils.MenuButton;
import input.GameAction;
import input.InputManager;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import tilegame.ITileMap;
import tilegame.ResourceManager;
import tilegame.Tile;
import tilegame.TileMapArray;
import tilegame.TileMapRenderer;

/**
 *
 * @author Benedikt Zönnchen
 */
public class EditorManager extends GameCore implements ActionListener
{

	private JButton jbSave, jbLoad, jbQuit, jbDelete;
	private ITileMap tileMap;
	private Tile currentTile;
	private Image currentImage;
	private Window window;
	private InputManager inputManager;
	private TileMapRenderer tileMapRenderer;
	private ResourceManager ressourceManager;
	private Selection selector;
	private ListMenuPanel loadSaveMenu;
	private GameAction bot, top, right, left;
	private LinkedList<GameAction> loadMapActions;
	private GameAction set, scrollForward, scrollBackward, switchSelection;
	private GameAction loadMap;
	private String currentFileName = "";
	private char[] spriteChars;
	private char[] tileChars;
	private int index = -1;

	@Override
	public void init()
	{
		super.init();
		ressourceManager = ResourceManager.getInstance();
		window = this.screen.getScreenWindow();
		inputManager = new InputManager(window);
		initComponents();

		//TODO: exception handling
		try {
			tileMap = ressourceManager.getEmptyTileMap(TileMapArray.MAX_HEIGHT, TileMapArray.MAX_WIDTH);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		currentTile = tileMap.getTile(0, tileMap.getHeight() - 1);
		tileMapRenderer = new TileMapRenderer(tileMap, screen);
		tileMapRenderer.setBackground(ResourceManager.loadImage("images/background.jpg"));
		selector = Selection.Tiles;
		currentFileName = ResourceManager.MAP_NAMES + (tileMap.getIndex()) + ResourceManager.MAP_DATATYPE;
		spriteChars = ressourceManager.getSpriteCharList();
		tileChars = ressourceManager.getTileCharList();
		currentImage = null;
	}

	public static void main(String[] args)
	{
		new EditorManager().run();
	}

	@Override
	public void update(long elapsedTime)
	{
		if (!loadSaveMenu.isVisible())
		{
			checkSystemInput();
		}
		else
		{
			//TODO: exception handling
			try {
				checkLoadMapInput();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Check the system input. The user can move the rectangle in the tile map
	 */
	public void checkSystemInput()
	{
		if (bot.isPressed())
		{
			Tile tmp = tileMap.getTile(currentTile.getX(), currentTile.getY() + 1);
			if (tmp != null)
			{
				currentTile = tmp;
			}
		}
		else if (top.isPressed())
		{
			Tile tmp = tileMap.getTile(currentTile.getX(), currentTile.getY() - 1);
			if (tmp != null)
			{
				currentTile = tmp;
			}
		}
		else if (right.isPressed())
		{
			Tile tmp = tileMap.getTile(currentTile.getX() + 1, currentTile.getY());
			if (tmp != null)
			{
				currentTile = tmp;
			}
		}
		else if (left.isPressed())
		{
			Tile tmp = tileMap.getTile(currentTile.getX() - 1, currentTile.getY());
			if (tmp != null)
			{
				currentTile = tmp;
			}
		}
		else if (set.isPressed())
		{
			char c;
			if(index < 0 || (selector==Selection.Tiles && tileChars.length <= index) || (selector==Selection.Sprites && spriteChars.length <= index))
			{
				c = ' ';
			}
			else
			{
				c = selector==Selection.Tiles?tileChars[index]:spriteChars[index];
			}
			System.out.println(c);
			tileMap.setTile(currentTile.getX(), currentTile.getY(), currentImage, c);
		}
		else if (scrollForward.isPressed())
		{
			Image image;
			if (selector.equals(Selection.Tiles))
			{
				if((index+1) < tileChars.length)
				{
					image =  ressourceManager.getTileImage(tileChars[index+1]);
				}
				else
				{
					image = null;
				}
			}
			else
			{
				if((index+1) < spriteChars.length)
				{
					image = ressourceManager.getSpriteImage(spriteChars[index + 1]);
				}
				else
				{
					image = null;
				}
			}

			if (image != null || currentImage != null)
			{
				currentImage = image;
				index++;
			}
		}
		else if (scrollBackward.isPressed())
		{
			Image image;
			if (selector.equals(Selection.Tiles))
			{
				if(index > 0)
				{
					image =  ressourceManager.getTileImage(tileChars[index-1]);
				}
				else
				{
					image = null;
				}
			}
			else
			{
				if(index > 0)
				{
					image = ressourceManager.getSpriteImage(spriteChars[index-1]);
				}
				else
				{
					image = null;
				}
			}
			if (image != null || currentImage != null)
			{
				currentImage = image;
				index--;
			}
		}
		else if (switchSelection.isPressed())
		{
			if (selector.equals(Selection.Tiles))
			{
				if(index >= spriteChars.length)
				{
					index = spriteChars.length-1;
				}
				else if(index < 0)
				{
					index = 0;
				}
				selector = Selection.Sprites;
				currentImage = ressourceManager.getSpriteImage(spriteChars[index]);
			}
			else
			{
				if(index >= tileChars.length)
				{
					index = tileChars.length-1;
				}
				else if(index < 0)
				{
					index = 0;
				}
				selector = Selection.Tiles;
				currentImage = ressourceManager.getTileImage(tileChars[index]);
			}
		}
	}

	/**
	 * Check all input if the user is in the load map dialog
	 */
	private void checkLoadMapInput() throws URISyntaxException {
		int counter = 0;
		GameAction selectedAction = null;
		for (GameAction gameAction : loadMapActions)
		{
			if (gameAction.isPressed())
			{
				selectedAction = gameAction;
			}
			counter++;
		}
		if (selectedAction != null)
		{
			try
			{
				if (selectedAction.getName().equals("EMPTY"))
				{
					currentFileName = ResourceManager.MAP_NAMES + counter + ".txt";
				}
				else
				{
					currentFileName = selectedAction.getName();
				}

				if (loadMap.isPressed())
				{
					if (selectedAction.getName().equals("EMPTY"))
					{
						tileMap = ressourceManager.getEmptyTileMap(TileMapArray.MAX_HEIGHT, TileMapArray.MAX_WIDTH);
					}
					else
					{
						tileMap = ressourceManager.loadMap(currentFileName, true);
					}

					currentTile = tileMap.getTile(0, tileMap.getHeight() - 1);
					tileMapRenderer.setTileMap(tileMap);
				}
				setButtonsEnable(true);
				loadSaveMenu.setVisible(false);
				screen.getScreenWindow().requestFocus();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	private void deleteMap()
	{
		try
		{
			ressourceManager.deleteMap(currentFileName);

			// generate empty Map
			tileMap = ressourceManager.getEmptyTileMap(TileMapArray.MAX_HEIGHT, TileMapArray.MAX_WIDTH);
			currentTile = tileMap.getTile(0, tileMap.getHeight() - 1);
			tileMapRenderer.setTileMap(tileMap);

			//TODO: very inefficient cause the whole menu will be recreated!
			createLoadSaveMenu();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * save the current tileMap
	 */
	private void saveMap()
	{
		try
		{
			if(!ressourceManager.mapExists(currentFileName))
			{
				addToLoadSaveMenu(currentFileName);
			}
			ressourceManager.saveMap(tileMap, currentFileName);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}


	/**
	 * draws all components and images to the screen. The automaticall draw
	 * mechanism is deactivated so the programmer has the full control.
	 * @param g Graphics from the main screen
	 */
	@Override
	public void draw(Graphics2D g)
	{
		int offsetY = tileMapRenderer.getOffsetY(currentTile);
		int offsetX =tileMapRenderer.getOffsetX(currentTile);
		int xPixel = 0;
		int yPixel = 0;

		g.setColor(Color.CYAN);
		tileMapRenderer.drawTileMap(g, offsetX, offsetY);
		if (currentImage != null)
		{
			// center image
			xPixel =  TileMapRenderer.tileToPixel(currentTile.getX()) + offsetX + (TileMapArray.TILE_SIZE / 2 - currentImage.getWidth(null) / 2);
			yPixel =  TileMapRenderer.tileToPixel(currentTile.getY()) + offsetY + (TileMapArray.TILE_SIZE / 2 - currentImage.getHeight(null) / 2);
			g.drawImage(currentImage, xPixel, yPixel, null);
		}
		g.drawRect(TileMapRenderer.tileToPixel(currentTile.getX()) + 1 + offsetX, TileMapRenderer.tileToPixel(currentTile.getY()) + 1 + offsetY, TileMapArray.TILE_SIZE - 2, TileMapArray.TILE_SIZE - 2);
		g.drawRect(TileMapRenderer.tileToPixel(currentTile.getX()) + offsetX, TileMapRenderer.tileToPixel(currentTile.getY()) + offsetY, TileMapArray.TILE_SIZE, TileMapArray.TILE_SIZE);
		screen.getScreenWindow().getLayeredPane().paintComponents(g);
	}

	/**
	 * Adds a new entry in the load/save-dialog. This happens if the user
	 * save a new map
	 * @param filename filename of the map
	 */
	private void addToLoadSaveMenu(String filename)
	{
		loadSaveMenu.removeButton(loadMapActions.size() - 1);
		loadMapActions.removeLast();

		GameAction action = new GameAction(filename, GameAction.DETECT_INITAL_PRESS_ONLY);
		loadMapActions.add(action);
		MenuButton button = new MenuButton(action.getName(), action, loadSaveMenu);
		loadSaveMenu.addButton(button);

		action = new GameAction("EMPTY", GameAction.DETECT_INITAL_PRESS_ONLY);
		loadMapActions.add(action);
		button = new MenuButton(action.getName(), action, loadSaveMenu);
		loadSaveMenu.addButton(button);
	}

	/**
	 * init the load/save dialog
	 * this will be call at the init and if the user delete a Map!
	 */
	private void createLoadSaveMenu() throws URISyntaxException {
		loadSaveMenu = GraphicFactory.getListMenuPanel();
		Container layeredPane = screen.getScreenWindow().getLayeredPane();
		File[] mapFiles = ressourceManager.getMapFileList();
		loadMapActions = new LinkedList<GameAction>();
		for (int i = 0; i < mapFiles.length; i++)
		{
			GameAction action = new GameAction(mapFiles[i].getName(), GameAction.DETECT_INITAL_PRESS_ONLY);
			loadMapActions.add(action);
			MenuButton button = new MenuButton(action.getName(), action, loadSaveMenu);
			loadSaveMenu.addButton(button);
		}
		GameAction action = new GameAction("EMPTY", GameAction.DETECT_INITAL_PRESS_ONLY);
		loadMapActions.add(action);
		MenuButton button = new MenuButton(action.getName(), action, loadSaveMenu);
		loadSaveMenu.addButton(button);
		layeredPane.add(loadSaveMenu);
		loadSaveMenu.setVisible(false);
	}

	/**
	 * create the small option menu in the left top corner of the screen
	 */
	private void createOptionMenu()
	{
		// create buttons
		jbQuit = GraphicFactory.getButton("quit", "Quit", this);
		jbLoad = GraphicFactory.getButton("quit", "Load Map", this);
		//jbSaveAs = GraphicFactory.getButton("quit", "Save Map As", this);
		jbSave = GraphicFactory.getButton("quit", "Save Map", this);
		jbDelete = GraphicFactory.getButton("quit", "Delete Map", this);
		// create the space where the play/pause buttons go.

		Container contentPane = screen.getScreenWindow().getContentPane();

		loadMap = new GameAction("Load Map", GameAction.DETECT_INITAL_PRESS_ONLY);
		// add components to the screen's content pane
		contentPane.add(jbSave);
		//contentPane.add(jbSaveAs);
		contentPane.add(jbLoad);
		contentPane.add(jbQuit);
		contentPane.add(jbDelete);
	}

	/**
	 * init all system input actions and the inputmanager
	 */
	private void createEditorActions()
	{
		top = new GameAction("top", GameAction.DETECT_INITAL_PRESS_ONLY);
		left = new GameAction("left", GameAction.DETECT_INITAL_PRESS_ONLY);
		bot = new GameAction("bot", GameAction.DETECT_INITAL_PRESS_ONLY);
		right = new GameAction("right", GameAction.DETECT_INITAL_PRESS_ONLY);

		set = new GameAction("set", GameAction.DETECT_INITAL_PRESS_ONLY);
		scrollForward = new GameAction("scroll forward", GameAction.DETECT_INITAL_PRESS_ONLY);
		scrollBackward = new GameAction("scroll backward", GameAction.DETECT_INITAL_PRESS_ONLY);
		switchSelection = new GameAction("switch selection", GameAction.DETECT_INITAL_PRESS_ONLY);

		inputManager.mapToKey(top, KeyEvent.VK_W);
		inputManager.mapToKey(bot, KeyEvent.VK_S);
		inputManager.mapToKey(left, KeyEvent.VK_A);
		inputManager.mapToKey(right, KeyEvent.VK_D);
		inputManager.mapToKey(set, KeyEvent.VK_ENTER);
		inputManager.mapToKey(scrollForward, KeyEvent.VK_DOWN);
		inputManager.mapToKey(scrollBackward, KeyEvent.VK_UP);
		inputManager.mapToKey(switchSelection, KeyEvent.VK_LEFT);
		inputManager.mapToKey(switchSelection, KeyEvent.VK_RIGHT);
	}

	/**
	 * these listener is for the transparent buttons in the upper corner
	 * @param e actionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();
		if (src == jbQuit)
		{
			stop();
		}
		else if (src == jbLoad)
		{
			loadMap.tap();
			loadSaveMenu.setVisible(true);
			loadSaveMenu.requestFocus();
			setButtonsEnable(false);
		}
		else if (src == jbSave)
		{
			saveMap();
		}
		else if(src == jbDelete)
		{
			deleteMap();
		}
	}

	/**
	 * deactivate the transparent buttons in the top corner
	 * @param enable
	 */
	private void setButtonsEnable(boolean enable)
	{
		jbQuit.setEnabled(enable);
		jbQuit.setVisible(enable);
		jbLoad.setEnabled(enable);
		jbLoad.setVisible(enable);
		jbSave.setEnabled(enable);
		jbSave.setVisible(enable);
	}

	/**
	 * initiate the main frame
	 */
	@Override
	protected void initComponents()
	{
		try
		{
			java.awt.EventQueue.invokeAndWait(new Runnable()
			{
				@Override
				public void run()
				{
					JFrame frame = screen.getScreenWindow();
					frame.setLayout(new FlowLayout());
					Container contentPane = frame.getContentPane();
					if (contentPane instanceof JComponent)
					{
						((JComponent) contentPane).setOpaque(false);
					}
					contentPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
					frame.validate();
					createOptionMenu();

					//TODO: exception handling
					try {
						createLoadSaveMenu();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
					createEditorActions();
				}
			});
		}
		catch (InterruptedException ex)
		{
			ex.printStackTrace();
		}
		catch (InvocationTargetException ex)
		{
			Logger.getLogger(EditorManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
