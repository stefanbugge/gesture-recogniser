package dk.loeschcke.matrix.gui.panels;

import $N.PointR;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.loeschcke.matrix.gui.FrameListener;
import dk.loeschcke.matrix.image.CustomScaleStrategy;
import dk.loeschcke.matrix.image.ScaleStrategy;
import dk.loeschcke.matrix.util.Library;
import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;

@SuppressWarnings("serial")
public class PreSmoothedPanel extends JPanel implements FrameListener {

	private static final Logger log = LoggerFactory.getLogger(PreSmoothedPanel.class);
	
	private int WIDTH = 500;
	private int HEIGHT = 500;
	
	private ScaleStrategy scaleStrategy;

	private BufferedImage buf;
	
	public PreSmoothedPanel(int width, int height, ScaleStrategy scaleStrategy) {
		this.scaleStrategy = scaleStrategy;

		this.WIDTH = width;
		this.HEIGHT = height;
		buf = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
	}
	
	@Override
	public void update(int[] pixels, PointR max) {
		
		pixels = scaleStrategy.scale(pixels, Library.FRAME_WIDTH, Library.FRAME_HEIGHT, WIDTH, HEIGHT);
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
