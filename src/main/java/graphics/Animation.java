package graphics;

import java.awt.Image;
import java.util.ArrayList;

/**
 * Represents an animation cycle with different images which will displayed
 * a different amount of time.
 * @author Benedikt Zönnchen
 */
public class Animation
{
	private ArrayList<AnimFrame> frames;
	private int currentFrameIndex;
	private long animTime;
	private long totalDuration;

	/**
	 * Standard Constructor
	 */
	public Animation()
	{
		frames = new ArrayList<AnimFrame>();
		totalDuration = 0;
		start();
	}

	/**
	 * Add a new Image to the Animation loop
	 * @param image Image which will displayed a short time
	 * @param duration ms how long the frame will be displayed
	 */
	public synchronized void addFrame(Image image, long duration)
	{
		totalDuration += duration;
		frames.add(new AnimFrame(image, totalDuration));
	}

	/**
	 * reset the Animation to the start state
	 */
	public synchronized void start()
	{
		animTime = 0;
		currentFrameIndex = 0;
	}

	/**
	 * update the currentFrameIndex. Set the index to the frame which should be displayed
	 * @param elapsedTime Milliseconds that decayed
	 */
	public synchronized void update(long elapsedTime)
	{
		if(frames.size()>1)
		{
			animTime = (elapsedTime + animTime) % totalDuration;
			currentFrameIndex = 0;
			while(animTime > getFrame(currentFrameIndex).endTime)
			{
				currentFrameIndex++;
			}
		}
	}

	/**
	 * @return image Image which is on turn
	 */
	public synchronized Image getImage()
	{
		if(frames.size() > 0)
		{
			return getFrame(currentFrameIndex).image;
		}
		return null;
	}

	/**
	 * @param index framenumber
	 * @return frame
	 */
	private AnimFrame getFrame(int index)
	{
		return frames.get(index);
	}
	
	/**
	 * A combination of a single Imgage and a display end time
	 * @author Bendikt Zönnchen
	 */
	private class AnimFrame
	{
		Image image;
		long endTime;

		/**
		 * Standard Constructor
		 * @param image Image which will be dispayed in is a part of this animation
		 * @param endTime The top barrier. After this time, the frame will no longer displayed
		 */
		public AnimFrame(Image image, long endTime)
		{
			this.image = image;
			this.endTime = endTime;
		}
	}
}
