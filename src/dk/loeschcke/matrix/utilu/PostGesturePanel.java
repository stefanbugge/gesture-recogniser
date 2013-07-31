package dk.loeschcke.matrix.utilu;

import java.awt.image.BufferedImage;
import java.util.List;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import gesturefun.PointR;
import dk.loeschcke.matrix.helper.MatrixHelper;
import dk.loeschcke.matrix.util.FrameListener;
import dk.loeschcke.matrix.util.Library;
import dk.loeschcke.matrix.util.PostRawPanel;

public class PostGesturePanel extends MarvinImagePanel implements FrameListener {
	
private static final Logger log = LoggerFactory.getLogger(PostRawPanel.class);
	
	private int WIDTH;
	private int HEIGHT;
	
	private int SCALE = 10;
	
	private int[] pixels;
	
	public PostGesturePanel(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
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
			
			BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_GRAY);
	        bi.getRaster().setPixels(0,0,WIDTH, HEIGHT, scaledPixels);
			
	        image = new MarvinImage(bi);
			image.update();
			setImage(image);
		}
	}

	@Override
	public void reset() {
		pixels = new int[Library.FRAME_WIDTH*Library.FRAME_HEIGHT*SCALE*SCALE];
	}

}
