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
public class PreRawPanel extends JPanel implements FrameListener {

	private static final Logger log = LoggerFactory.getLogger(PreRawPanel.class);
	
	private int WIDTH;
	private int HEIGHT;

	private BufferedImage buf;
	
	public PreRawPanel(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		buf = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
	}
	
	@Override
	public void update(int[] pixels, PointR max) {
		pixels = MatrixHelper.resizePixels(pixels, Library.FRAME_WIDTH, Library.FRAME_HEIGHT, WIDTH, HEIGHT);
		
        buf.getRaster().setPixels(0,0,WIDTH, HEIGHT,pixels);
        repaint();
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
