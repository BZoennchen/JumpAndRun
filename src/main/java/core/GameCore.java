package core;

import graphics.NullRepaintManager;
import graphics.ScreenManager;
import java.awt.*;
import javax.swing.UIManager;
import tilegame.ResourceManager;

/**
    Simple abstract class used for testing. Subclasses should
    implement the draw() method.
*/
public abstract class GameCore {

    protected static final int FONT_SIZE = 24;
    
    private static final DisplayMode POSSIBLE_MODES[] = {
		new DisplayMode(1600, 1200, 32, 0),
        new DisplayMode(1600, 1200, 24, 0),
        new DisplayMode(1600, 1200, 16, 0),
    	new DisplayMode(1280, 1024, 32, 0),
        new DisplayMode(1280, 1024, 24, 0),
        new DisplayMode(1280, 1024, 16, 0),
        new DisplayMode(1280, 800, 32, 0),
        new DisplayMode(1280, 800, 24, 0),
        new DisplayMode(1280, 800, 16, 0),
    	new DisplayMode(1024, 768, 32, 0),
        new DisplayMode(1024, 768, 24, 0),
        new DisplayMode(1024, 768, 16, 0),
        new DisplayMode(800, 600, 32, 0),
        new DisplayMode(800, 600, 24, 0),
        new DisplayMode(800, 600, 16, 0),	
        new DisplayMode(640, 480, 32, 0),
        new DisplayMode(640, 480, 24, 0),
        new DisplayMode(640, 480, 16, 0)
    };

    private boolean isRunning;
    protected ScreenManager screen;


    /**
        Signals the game loop that it's time to quit
    */
    public void stop() {
        isRunning = false;
    }


    /**
        Calls init() and gameLoop()
    */
    public void run() {
        try {
            init();
            gameLoop();
        }
        finally {
            screen.restoreScreen();
        }
    }


    /**
        Sets full screen mode and initiates and objects.
    */
    public void init() {
		try
		{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		screen = ScreenManager.getInstance();
		DisplayMode displayMode = screen.findFirstCompatibleMode(POSSIBLE_MODES);
		boolean fullscreen = true;
		if(ResourceManager.getInstance().getConfig().get("fullscreen").equals("false"))
		{
			fullscreen = false;
		}
		screen.setFullScreen(displayMode, fullscreen);
		isRunning = true;
		NullRepaintManager.install();
    }


    /**
        Runs through the game loop until stop() is called.
        To Do! Sleep and elapsedTime better calculation!
    */
    public void gameLoop() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;

        while (isRunning) {
            long elapsedTime =
                System.currentTimeMillis() - currTime;
            currTime += elapsedTime;

            // update
            update(elapsedTime);

            // draw the screen
            Graphics2D g = screen.getGraphics();
            draw(g);
            g.dispose();
            screen.update();

            // take a nap
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException ex) { }
        }
    }


    /**
        Updates the state of the game/animation based on the
        amount of elapsed time that has passed.
    */
    public void update(long elapsedTime) {
        // do nothing
    }


    /**
        Draws to the screen. Subclasses must override this
        method.
    */
    public abstract void draw(Graphics2D g);

	
	/**
	 * initial the swing components. Subclasses must override this
	 * method.
	 */
	protected abstract void initComponents();


}
