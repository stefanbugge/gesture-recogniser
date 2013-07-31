package dk.loeschcke.matrix.util;

import gesturefun.PointR;

import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.loeschcke.matrix.image.CustomScaleStrategy;
import dk.loeschcke.matrix.image.ScaleStrategy;
import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;

@SuppressWarnings("serial")
public class PreSmoothedPanel extends MarvinImagePanel implements FrameListener {

	private static final Logger log = LoggerFactory.getLogger(PreSmoothedPanel.class);
	
	private int WIDTH = 500;
	private int HEIGHT = 500;
	
	private ScaleStrategy scaleStrategy;
	
	public PreSmoothedPanel(int width, int height, ScaleStrategy scaleStrategy) {
		this.scaleStrategy = scaleStrategy;

		this.WIDTH = width;
		this.HEIGHT = height;
	}
	
	@Override
	public void update(int[] pixels, PointR max) {
		BufferedImage bi = scaleStrategy.scale(pixels, Library.FRAME_WIDTH, Library.FRAME_HEIGHT, WIDTH, HEIGHT);
		image = new MarvinImage(bi);
		image.update();
		this.setImage(image);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
