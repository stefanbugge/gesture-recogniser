package dk.loeschcke.matrix.util;

import gesturefun.PointR;

import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.reflect.generics.visitor.Reifier;

import dk.loeschcke.matrix.image.CustomScaleStrategy;
import dk.loeschcke.matrix.image.ScaleStrategy;
import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginLoader;

@SuppressWarnings("serial")
public class PreRawPanel extends MarvinImagePanel implements FrameListener {

	private static final Logger log = LoggerFactory.getLogger(PreRawPanel.class);
	
	private int WIDTH;
	private int HEIGHT;
	
	public PreRawPanel(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
	}
	
	@Override
	public void update(int[] pixels, PointR max) {
		pixels = resizePixels(pixels, Library.FRAME_WIDTH, Library.FRAME_HEIGHT, WIDTH, HEIGHT);
		
		BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
        bi.getRaster().setPixels(0,0,WIDTH, HEIGHT,pixels);
		
        image = new MarvinImage(bi);
		image.update();
		setImage(image);
	}
	
	public int[] resizePixels(int[] pixels,int w1,int h1,int w2,int h2) {
	    int[] temp = new int[w2*h2] ;
	    // EDIT: added +1 to account for an early rounding problem
	    int x_ratio = (int)((w1<<16)/w2) +1;
	    int y_ratio = (int)((h1<<16)/h2) +1;
	    //int x_ratio = (int)((w1<<16)/w2) ;
	    //int y_ratio = (int)((h1<<16)/h2) ;
	    int x2, y2 ;
	    for (int i=0;i<h2;i++) {
	        for (int j=0;j<w2;j++) {
	            x2 = ((j*x_ratio)>>16) ;
	            y2 = ((i*y_ratio)>>16) ;
	            temp[(i*w2)+j] = pixels[(y2*w1)+x2] ;
	        }                
	    }                
	    return temp ;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
