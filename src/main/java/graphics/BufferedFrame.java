package graphics;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JFrame;

/**
 * A manuell double buffered JFrame
 * @author Benedikt Zönnchen
 */
public class BufferedFrame extends JFrame{
	
	Image bufImage;
	Graphics bufG;

	/**
	 * double buffered update method
	 * @param g the paint target
	 */
	@Override
	public void update(Graphics g) {
		int w = this.getSize().width;
        int h = this.getSize().height;
 
        if(bufImage == null){
              bufImage = this.createImage(w,h);
              bufG = bufImage.getGraphics();
        }
        
        bufG.setColor(this.getBackground());
        bufG.fillRect(0,0,w,h);
        
        bufG.setColor(this.getForeground());
        
        paint(bufG);
 
        g.drawImage(bufImage,0,0,this);

	}
}
