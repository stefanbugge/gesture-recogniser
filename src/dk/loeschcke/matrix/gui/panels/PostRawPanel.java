package dk.loeschcke.matrix.gui.panels;

import $N.PointR;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.reflect.generics.visitor.Reifier;

import dk.loeschcke.matrix.gui.FrameListener;
import dk.loeschcke.matrix.helper.MatrixHelper;
import dk.loeschcke.matrix.image.CustomScaleStrategy;
import dk.loeschcke.matrix.image.ScaleStrategy;
import dk.loeschcke.matrix.util.Library;
import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginLoader;

@SuppressWarnings("serial")
public class PostRawPanel extends JPanel implements FrameListener {

	private static final Logger log = LoggerFactory.getLogger(PostRawPanel.class);
	
	private int WIDTH;
	private int HEIGHT;
	
	private int SCALE = 10;

	private BufferedImage buf;
	
	public PostRawPanel(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		buf = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
	}
	
	@Override
	public void update(int[] pixels, PointR max) {
		if (max != null) {
			// 7x7 coming in - scale to 70x70
			pixels = new int[Library.FRAME_WIDTH*Library.FRAME_HEIGHT*SCALE*SCALE];
			int x = (int) (max.X * SCALE);
			int y = (int) (max.Y * SCALE);
			int index = MatrixHelper.getIndex(x, y, Library.FRAME_WIDTH*SCALE);
			pixels[index] = 255;
			
			// scale it to 500x500
			pixels = MatrixHelper.resizePixels(pixels, Library.FRAME_WIDTH*SCALE, Library.FRAME_HEIGHT*SCALE, WIDTH, HEIGHT);
			
	        buf.getRaster().setPixels(0,0,WIDTH, HEIGHT,pixels);
			
	        repaint();
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buf, 0, 0, null); // see javadoc for more info on the parameters            
    }
}
