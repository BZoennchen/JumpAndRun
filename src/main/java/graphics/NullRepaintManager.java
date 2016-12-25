package graphics;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

public class NullRepaintManager extends RepaintManager
{
	public static void install(){
		RepaintManager repaintManager = new NullRepaintManager();
		repaintManager.setDoubleBufferingEnabled(false);
		RepaintManager.setCurrentManager(repaintManager);
	}
	
	public void addInvalidComponent(JComponent c){};
	public void addDirtyRegion(JComponent c, int x, int y, int w, int h){};
	public void markCompletelyDirty(JComponent c){};
	public void paintDirtyRegions(){};
}
