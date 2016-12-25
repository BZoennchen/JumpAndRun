package tilegame;

import core.DirFileFilter;
import core.ImageFileFilter;
import graphics.Animation;
import graphics.ScreenManager;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import core.MapFileFilter;
import graphics.Adjustment;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import tilegame.sprites.CreatureFactory;
import tilegame.sprites.Fly;
import tilegame.sprites.Grub;
import tilegame.sprites.Plattform;
import tilegame.sprites.Player;
import tilegame.sprites.Rocket;
import tilegame.sprites.collision.sprite.PlayerSCHandler;
import tilegame.sprites.collision.sprite.SCDetector;
import tilegame.sprites.collision.sprite.SimpleSCDetector;
import tilegame.sprites.collision.tile.EnemyTCHandler;
import tilegame.sprites.collision.tile.FlyTCHandler;
import tilegame.sprites.collision.tile.GrubTCHandler;
import tilegame.sprites.collision.tile.PlayerTCHandler;
import tilegame.sprites.collision.tile.SimpleTCDetector;
import tilegame.sprites.collision.tile.TCDetector;
import tilegame.sprites.moveing.RegularMoving;
import tilegame.sprites.moveing.SinusMoving;
import tilegame.sprites.moveing.SwingMoving;
import tilegame.sprites.powerUp.Coin;
import tilegame.sprites.powerUp.Star;
import utils.PropertyHandler;

/**
 * Singletone Pattern
 * @author Benedikt Zönnchen
 */
public class ResourceManager
{
	public static String MAP_NAMES;
	public static String MAP_DATATYPE;
	public static String MAP_FOLDER;
	public static String IMAGE_FOLDER = "/images/";
	public static String SPRITE_FOLDER = "/images/sprites/";
	public static String TILE_FOLDER = "/images/tiles/";
	public static String MENU_IMAGE_FOLDER = "/images/menu/";
	public static String PROFILE_FOLDER;
	private Map<Character, Image> tiles = new HashMap<Character, Image>();			// contains all tile images
	private Map<Character, List<Image>> sprites = new HashMap<Character,  List<Image>>();	// contains all sprite image 
	private TileMapArray tileMap;
	private Properties config;
	private static ResourceManager manager;
	private static OS os;

	private ResourceManager()
	{
		loadConfig();
		loadTiles();
		loadSpritesImages();
	}

	public static ResourceManager getInstance()
	{
		if(manager == null)
		{
			manager = new ResourceManager();
		}
		return manager;
	}

	public Properties getConfig()
	{
		return config;
	}

	public void saveMap(ITileMap tileMap, String filename) throws IOException
	{
		int width = tileMap.getWidth();
		int height = tileMap.getHeight();

		BufferedWriter writer = new BufferedWriter(new FileWriter(MAP_FOLDER + filename));
		Character c = null;
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				Tile tile = tileMap.getTile(x, y);
				if(tile != null)
				{
					c = tileMap.getTile(x, y).getChar();
					if (c != null)
					{
						writer.write(c);
					}
					else
					{
						//writer.write(' ');
					}
				}
				else
				{
					//writer.write(' ');
				}
			}
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}

	public void deleteMap(String filename) throws IOException, URISyntaxException {
		int number = getMapIndex(filename);

		File tmp = null;
		int i = 1;
		boolean fileExists = false;

		if(this.getMapCount() == number)
		{
			// delete last file
			File file = new File(ResourceManager.pathToAbsolutePath(MAP_FOLDER+filename));
			file.delete();
		}
		else
		{
			// rename files
			do
			{
				tmp = new File(ResourceManager.pathToAbsolutePath(MAP_FOLDER+MAP_NAMES+i+MAP_DATATYPE));
				fileExists = tmp.exists();
				if(i > number)
				{
					tmp.renameTo(new File(ResourceManager.pathToAbsolutePath(MAP_FOLDER+MAP_NAMES+(i-1)+MAP_DATATYPE)));
				}
				i++;
			}while(fileExists);
		}
	}

	public TileMapArray loadMap(String filename) throws IOException, URISyntaxException {
		return loadMap(filename, false);
	}


	public TileMapArray loadMap(String filename, boolean editable) throws IOException, URISyntaxException {
		BufferedReader reader = new BufferedReader(new FileReader(ResourceManager.pathToAbsolutePath(MAP_FOLDER+filename).getPath()));
		List<String> lines = new ArrayList<String>();
		int width = 0;
		int height = 0;

		while (true)
		{
			String line = reader.readLine();
			if (line == null)
			{
				break;
			}
			if (!line.startsWith("#"))
			{
				lines.add(line);
				width = Math.max(width, line.length());
				height++;
			}
		}
		reader.close();
		
		if(editable)
		{
			tileMap = new TileMapArray(TileMapArray.MAX_WIDTH, TileMapArray.MAX_HEIGHT, getMapIndex(filename));
			int yOffset = TileMapArray.MAX_HEIGHT-height;
			for (int j = 0; j < TileMapArray.MAX_HEIGHT; j++)
			{
				for (int i = 0; i < TileMapArray.MAX_WIDTH; i++)
				{
					char character = ' ';
					if(j >= yOffset && lines.get(j-yOffset).length() > i)
					{
						character = lines.get(j-yOffset).charAt(i);
					}

					if (character >= 'A' && character <= 'N')
					{
						tileMap.setTile(i, j, tiles.get(character), character);
					}
					else if(character == ' ')
					{
						tileMap.setTile(i, j, null, character);
					}
					else if(editable)
					{
						Image image1 = getSpriteImage(character);
						tileMap.setTile(i, j, image1, character);
					}
				}
			}
			return tileMap;
		}
		else
		{
			return loadMap(filename, lines, height, width);
		}
	}

	private TileMapArray loadMap(final String filename, final List<String> lines, final int height, final int width) throws IOException
	{
		tileMap = new TileMapArray(width, height, getMapIndex(filename));
		Profile profile = Profile.getInstance();
		Player player = createPlayer(profile);
		
		tileMap.setProfile(profile);
		tileMap.addSprite(player);
		tileMap.setPlayer(player);

		for (int j = 0; j < height; j++)
		{
			for (int i = 0; i < lines.get(j).length(); i++)
			{
				char character = lines.get(j).charAt(i);

				if (character >= 'A' && character <= 'N')
				{
					tileMap.setTile(i, j, tiles.get(character), character);
				}
				else
				{
					if (character == 'P')
					{
						player.setX(TileMapRenderer.tileToPixel(i));
						player.setY(TileMapRenderer.tileToPixel(j));
					}
					else if (character == 'o')
					{
						Coin coin = new Coin(createAnimation(character, 50), 0, 0, profile);
						coin.setX(TileMapRenderer.tileToPixel(i) + (TileMapArray.TILE_SIZE / 2 - coin.getWidth() / 2));
						coin.setY(TileMapRenderer.tileToPixel(j) + (TileMapArray.TILE_SIZE / 2 - coin.getHeight() / 2));
						tileMap.addSprite(coin);
					}
					else if (character == '!')
					{
					}
					else if (character == '*')
					{
						Star star = new Star(createAnimation(character, 500), 0, 0, profile);
						tileMap.addSprite(star);

						// Center the coins
						star.setX(TileMapRenderer.tileToPixel(i) + (TileMapArray.TILE_SIZE / 2 - star.getWidth() / 2));
						star.setY(TileMapRenderer.tileToPixel(j) + (TileMapArray.TILE_SIZE / 2 - star.getHeight() / 2));
					}
					else if (character == '1')
					{					
						Grub grub = new Grub(
								createAnimation(character, Adjustment.Right, 200),
								createAnimation(character, Adjustment.Left, 200),
								TileMapRenderer.tileToPixel(i), TileMapRenderer.tileToPixel(j));
						tileMap.addSprite(grub);
						grub.setVelocityX(.1f);
						//grub.setState(Creature.STATE_FLYING);
						grub.update(0);
						// tile collision detection installation
						TCDetector detector = new SimpleTCDetector(tileMap);
						grub.setTileCollisionHandler(new GrubTCHandler(grub, detector));
					}
					else if (character == '2')
					{
						Fly fly = new Fly(
								createAnimation(character, Adjustment.Right, 100),
								createAnimation(character, Adjustment.Left, 100),
								TileMapRenderer.tileToPixel(i), TileMapRenderer.tileToPixel(j));
						fly.setMoveStrategy(new SinusMoving(fly, 4, 1));
						tileMap.addSprite(fly);
						fly.setVelocityX(.2f);
						fly.setVelocityY(0);

						// tile collision detection installation
						TCDetector detector = new SimpleTCDetector(tileMap);
						fly.setTileCollisionHandler(new FlyTCHandler(fly, detector));
					}
					else if (character == '3')
					{
						Fly fly = new Fly(
								createAnimation(character, Adjustment.Right, 100),
								createAnimation(character, Adjustment.Left, 100),
								TileMapRenderer.tileToPixel(i), TileMapRenderer.tileToPixel(j));
						fly.setMoveStrategy(new SwingMoving(fly, 4, 1));
						tileMap.addSprite(fly);
						fly.setVelocityX(-.2f);
						fly.setVelocityY(0);

						// tile collision detection installation
						TCDetector detector = new SimpleTCDetector(tileMap);
						fly.setTileCollisionHandler(new FlyTCHandler(fly, detector));
					}
					else if (character == '4')
					{
						Rocket rocket = new Rocket(
								getSpriteImage(character, Adjustment.Right), getSpriteImage(character, Adjustment.Left),
								TileMapRenderer.tileToPixel(i), TileMapRenderer.tileToPixel(j));
						tileMap.addSprite(rocket);
						rocket.setVelocityX(.1f);
						rocket.setVelocityY(0);

						// tile collision detection installation
						TCDetector detector = new SimpleTCDetector(tileMap);
						rocket.setTileCollisionHandler(new EnemyTCHandler(rocket, detector));
					}
					else if (character == '5')
					{
						Image plattformImage = getSpriteImage(character);
						Plattform plattform = new Plattform(plattformImage, plattformImage,
								TileMapRenderer.tileToPixel(i), TileMapRenderer.tileToPixel(j));
						plattform.setVelocityX(-.2f);
						plattform.setVelocityY(0);
						plattform.update(0);
						tileMap.addSprite(plattform);
						//player.addPlayerObserver(plattform);
						plattform.setTileCollisionOn(false);
					}
					else if (character == '6')
					{
						Image rocketImage = getSpriteImage(character, 1);
						Image pipeImage =  getSpriteImage(character, 0);
						Rocket rocket = new Rocket(rocketImage, rocketImage, TileMapRenderer.tileToPixel(i), TileMapRenderer.tileToPixel(j));
						rocket.setVelocityX(0);
						rocket.setVelocityY(-0.5f);
						rocket.setTileCollisionOn(false);
						rocket.setMoveStrategy(new RegularMoving(rocket));

						CreatureFactory factory = new CreatureFactory(pipeImage, pipeImage, 0, 0, rocket, 3000, tileMap);
						factory.setX(TileMapRenderer.tileToPixel(i) + (TileMapArray.TILE_SIZE / 2 - factory.getWidth() / 2));
						factory.setY(TileMapRenderer.tileToPixel(j));
						tileMap.addSprite(factory);
						factory.update(0);
					}
					else if (character == '7')
					{
						Image rocketImage = getSpriteImage(character, 1);
						Image pipeImage =  getSpriteImage(character, 0);
						Rocket rocket = new Rocket(rocketImage, rocketImage, TileMapRenderer.tileToPixel(i), TileMapRenderer.tileToPixel(j));
						rocket.setTileCollisionOn(false);
						rocket.setVelocityX(0);
						rocket.setVelocityY(0.5f);
						rocket.setMoveStrategy(new RegularMoving(rocket));

						//grub.setState(Creature.STATE_FLYING);
						CreatureFactory factory = new CreatureFactory(pipeImage, pipeImage, 0, 0, rocket, 3000, tileMap);
						factory.setX(TileMapRenderer.tileToPixel(i) + (TileMapArray.TILE_SIZE / 2 - factory.getWidth() / 2));
						factory.setY(TileMapRenderer.tileToPixel(j));
						tileMap.addSprite(factory);
						factory.update(0);
					}
				}
			}
		}
		tileMap.pack(TileMapRenderer.pixelToTile(ScreenManager.getInstance().getWidth()));
		return tileMap;
	}

	private Animation createAnimation(char c, int rate)
	{
		return createAnimation(c, Adjustment.Right, rate);
	}

	private Animation createAnimation(char c, Adjustment ad, int rate)
	{
		Animation animation = new Animation();
		List<Image> images = getSpriteImages(c);
		for (int i = 0; i<images.size(); i++)
		{
			animation.addFrame(getSpriteImage(c, i, ad), rate);
		}
		return animation;
	}

	private void loadTiles()
	{
		loadTile('A');
		loadTile('B');
		loadTile('C');
		loadTile('D');
		loadTile('E');
		loadTile('F');
		loadTile('G');
		loadTile('H');
		loadTile('I');
		loadTile('J');
		loadTile('K');
		loadTile('L');
		loadTile('M');
		loadTile('N');
	}

	/**
	 * Load all sprite images
	 */
	private void loadSpritesImages() {
		FileFilter dirFilter = new DirFileFilter();
		FileFilter imageFilter = new ImageFileFilter();
		File root = null;
		try {
			root = new File(ResourceManager.pathToAbsolutePath(SPRITE_FOLDER));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		File[] dirs = root.listFiles(dirFilter);
		File[] images = null;
		char c =' ';

		// for each dir
		for(int i = 0; i < dirs.length; i++)
		{
			c = config.getProperty(dirs[i].getName()).charAt(0);
			images = dirs[i].listFiles(imageFilter);

			// for each image in dir
			for(int j = 0; j < images.length; j++)
			{
				List<Image> list = sprites.get(c);
                if (list == null)
				{
					 sprites.put(c, list=new ArrayList<Image>());
				}
				list.add(ResourceManager.loadImage(getRelativePath(SPRITE_FOLDER, images[j].getPath())));
			}
		}
	}

	private static String getRelativePath(String base, String path) {
		return path.substring(path.indexOf(base));
	}

	/**
	 * Load images and save the reference in a map so that we minimize image duplications
	 * This method should be used instead of call directly loadImage!
	 * @param c
	 * @return Image
	 */
	private List<Image> getSpriteImages(char c)
	{
		return sprites.get(c);
	}

	private void loadTile(Character c)
	{
		tiles.put(c, loadImage(TILE_FOLDER+"tile_" + c + ".png"));
	}

	public Image getTileImage(Character c)
	{
		return tiles.get(c);
	}

	/**
	 * returns only the first image of the images of one sprite
	 * @param c
	 * @return image
	 */
	public Image getSpriteImage(char c)
	{
		return getSpriteImage(c, 0, Adjustment.Left);
	}

	private Image getSpriteImage(char c, int index)
	{
		return getSpriteImage(c, index, Adjustment.Left);
	}

	private Image getSpriteImage(char c, Adjustment ad)
	{
		return getSpriteImage(c, 0, ad);
	}

	private Image getSpriteImage(char c, int index, Adjustment ad)
	{
		Image image = null;
		List<Image> images = sprites.get(c);
		if(images != null)
		{
			image = images.get(index);
			switch(ad)
			{
				case Left: break; // normal
				case Right: image = ResourceManager.getMirrorImage(image);break;
				case Down: image = ResourceManager.getUpsideToggeldImage(image); break;
				case Up: image = ResourceManager.getToggeledImage(image); break;
			}
		}
		return image;
	}

	private Player createPlayer(Profile profile)
	{
		Player player = new Player(createAnimation('P', Adjustment.Left, 120), createAnimation('P', Adjustment.Right, 120), 0, 0, null, profile);
		TCDetector tcDetector = new SimpleTCDetector(tileMap);
		player.setTileCollisionHandler(new PlayerTCHandler(player, tcDetector));
		SCDetector scDetector = new SimpleSCDetector(tileMap);
		player.setSpriteCollisionHandler(new PlayerSCHandler(player, scDetector));
		return player;
	}

	public static Image rotateImage(Image image, double degree)
	{
		BufferedImage bufferedImage = ScreenManager.getInstance().createCompatibleImage(image.getWidth(null), image.getHeight(null), Transparency.BITMASK);

		int w = image.getWidth(null);
		int h = image.getHeight(null);

		Graphics2D g = bufferedImage.createGraphics();
		g.rotate(Math.toRadians(degree), w / 2, h / 2);
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return bufferedImage;
	}

	public char[] getTileCharList()
	{
		Set list = tiles.keySet();
		char[] array = new char[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext();)
		{
			array[i] = (Character)it.next();
			i++;
		}
		Arrays.sort(array);
		return array;
	}

	public char[] getSpriteCharList()
	{
		Set list = sprites.keySet();
		char[] array = new char[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext();)
		{
			array[i] = (Character)it.next();
			i++;
		}
		Arrays.sort(array);
		return array;
	}

	public File[] getMapFileList() throws URISyntaxException {
		File dir = new File(ResourceManager.pathToAbsolutePath(MAP_FOLDER));
		return dir.listFiles(new MapFileFilter());
	}

	private static URI pathToAbsolutePath(final String path) throws URISyntaxException {
		return ResourceManager.class.getResource(path).toURI();
	}

	public String[] getMapFileNameList()
	{
		File dir = new File(MAP_FOLDER);
		return dir.list();
	}

	public boolean mapExists(String filename)
	{
		String[] nameList = getMapFileNameList();
		for (int i = 0; i < nameList.length; i++)
		{
			if(nameList[i].equals(filename))
			{
				return true;
			}
		}
		return false;
	}

	public int getMapCount() throws URISyntaxException {
		return getMapFileList().length;
	}

	public void saveProfile(Profile profile, String filename) throws IOException
	{
		ObjectOutputStream objOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(PROFILE_FOLDER+filename)));
		objOut.writeObject(profile);
		objOut.close();
	}

	public Profile loadProfile(String filename) throws IOException, ClassNotFoundException
	{
		ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream(PROFILE_FOLDER+filename)));
		Profile tmp = (Profile) objIn.readObject();
		objIn.close();
		return tmp;
	}

	public static Image getFlippedImage(Image image)
	{
		return rotateImage(image, 180.0);
	}

	public static Image getToggeledImage(Image image)
	{
		return rotateImage(image, 90.0);
	}

	public static Image getUpsideToggeldImage(Image image)
	{
		return rotateImage(image, 270.0);
	}

	public static Image getMirrorImage(Image image)
	{
		BufferedImage bufferedImage = ScreenManager.getInstance().createCompatibleImage(image.getWidth(null), image.getHeight(null), Transparency.BITMASK);
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		int x = -1;
		int y = 1;

		Graphics2D g = bufferedImage.createGraphics();
		AffineTransform transform = new AffineTransform();
		transform.scale(x, y);
		transform.translate((x - 1) * w / 2, (y - 1) * h / 2);
		g.drawImage(image, transform, null);
		g.dispose();
		return bufferedImage;
	}

	private void loadConfig()
	{
		config =  null;
		try
		{
			config = PropertyHandler.loadProperties();
			MAP_NAMES = config.getProperty("map_names");
			MAP_DATATYPE = config.getProperty("map_datatype");
			MAP_FOLDER = config.getProperty("map_folder");
			PROFILE_FOLDER = config.getProperty("game_state_folder");
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	private int getMapIndex(String filename)
	{
		String mapNumber = filename.substring(MAP_NAMES.length(), filename.length()-MAP_DATATYPE.length());
		return Integer.parseInt(mapNumber);
	}

	public static Image loadImage(String fileName)
	{
		return new ImageIcon(ResourceManager.class.getResource(fileName).getFile()).getImage();
	}

	public static OS getOS()
	{
		if(os == null)
		{
			String os = System.getProperty("os.name").toLowerCase();
			if(os.contains("win"))
			{
				ResourceManager.os = OS.Win;
			}
			else if (os.contains("mac"))
			{
				ResourceManager.os = OS.Mac;
			}
			else if(os.contains("nix") || os.contains("nux"))
			{
				ResourceManager.os = OS.Mac;
			}
		}
		return os;
	}

	/**
	 * init the empty tile map so that the map is full of tiles
	 * @param width
	 * @param height
	 * @return tileMap
	 */
	public ITileMap getEmptyTileMap(int width, int height) throws URISyntaxException {
		ITileMap tm = new TileMapArray(width, width, this.getMapCount()+1);
		for (int row = 0; row < tm.getWidth(); row++)
		{
			for (int col = 0; col < tm.getHeight(); col++)
			{
				tm.setTile(row, col, null,' ');
			}
		}
		return tm;
	}
}
