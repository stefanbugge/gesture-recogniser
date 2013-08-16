package dk.loeschcke.matrix.gui.panels;

import $N.PointR;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JPanel;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import dk.loeschcke.matrix.gui.FrameListener;
import dk.loeschcke.matrix.helper.MatrixHelper;
import dk.loeschcke.matrix.util.Library;

public class PostGesturePanel extends JPanel implements FrameListener {
	
private static final Logger log = LoggerFactory.getLogger(PostRawPanel.class);
	
	private int WIDTH;
	private int HEIGHT;
	
	private int SCALE = 10;
	
	private int[] pixels;
	
	private BufferedImage buf;
	
	public PostGesturePanel(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		buf = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
		reset();
	}

	@Override
	public void update(int[] unscaledPixels, PointR max) {
		if (max != null) {
			// 7x7 coming in - scale to 70x70
			int x = (int) Math.round(max.X * SCALE);
			int y = (int) Math.round(max.Y * SCALE);
			
			int index = MatrixHelper.getIndex(x, y, Library.FRAME_WIDTH*SCALE);
			
			pixels[index] = 255;

			// scale it to 500x500
			int[] scaledPixels = MatrixHelper.resizePixels(pixels, Library.FRAME_WIDTH*SCALE, Library.FRAME_HEIGHT*SCALE, WIDTH, HEIGHT);
	        buf.getRaster().setPixels(0,0,WIDTH, HEIGHT, scaledPixels);
	        
	        repaint();
		}
	}

	@Override
	public void reset() {
		pixels = new int[Library.FRAME_WIDTH*Library.FRAME_HEIGHT*SCALE*SCALE];
	}

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(buf, 0, 0, null); // see javadoc for more info on the parameters            
    }
}
