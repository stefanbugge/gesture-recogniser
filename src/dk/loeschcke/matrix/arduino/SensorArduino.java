package dk.loeschcke.matrix.arduino;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import marvin.image.MarvinImage;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SensorArduino reads distances from the Arduino with the Ping sensor, and puts
 * these values in a datatype.
 * 
 * @author markus
 * 
 */
public class SensorArduino extends Arduino implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(SensorArduino.class);

	private static final int WIDTH = 7;
	private static final int HEIGHT = 7;

	public SensorArduino(String portname) {
		super(portname);
	}

	@Override
	public void run() {
		connect();
		
		MarvinImagePlugin imagePlugin = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.transform.scale.jar");
		
		try {
			//int[][] multi = new int[WIDTH][HEIGHT];
			int[] pixels = new int[WIDTH*HEIGHT];
			while (!Thread.interrupted()) {

				String line = reader.readLine(); // 1-dim

				String[] split = line.split(",");
				
				if (split.length == WIDTH * HEIGHT) {
					for (int i = 0; i < split.length; i++) {
						try {
							pixels[i] = Integer.parseInt(split[i]);
						} catch (NumberFormatException e) {
							pixels[i] = 0;
						}
					}
//					for (int i = 0; i < WIDTH; i++) {
//						for (int j = 0; j < HEIGHT; j++) {
//							try {
//								multi[i][j] = Integer.parseInt(split[(j * WIDTH) + i]);	
//							} catch (NumberFormatException e) {
//								multi[i][j] = 0;
//							}
//						}
//					}
//					String s = pixels.length + " : " ;
//					for (int i = 0 ; i < pixels.length; i++) {
//						s += pixels[i] + ", ";
//					}
//					System.out.println(s);
				}
				int[] scaledPixels = resizeBilinearGray(pixels, WIDTH, HEIGHT,WIDTH*5,HEIGHT*5 );
				BufferedImage image = getImageFromArray(scaledPixels, WIDTH*5, HEIGHT*5);
				image = getScaledImage(image, 500, 500);
				
				MarvinImage marvinImage = new MarvinImage(image);
				
//				imagePlugin.setAttribute("newWidth", 150);
//				imagePlugin.setAttribute("newHeight", 150);
//				imagePlugin.process(marvinImage, marvinImage);
				
//				MarvinImage clone = marvinImage.clone();
//				imagePlugin.process(marvinImage, clone);
				
				this.setChanged();
				this.notifyObservers(marvinImage);
				
				
				// if (split.length < 2) {
				// continue;
				// }

//				try {
//					int value = Integer.parseInt(line);
//
//				} catch (NumberFormatException e) {
//					log.warn("Number format exception, ignoring.");
//				}
			}
		} catch (IOException e) {
			log.error("Lost connection", e);
		}

		log.debug("Interrupted, exiting.");
		disconnect();
	}
	
	

	public static BufferedImage getImageFromArray(int[] pixels, int width, int height) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        image.getRaster().setPixels(0,0,width,height,pixels);
        //WritableRaster raster = (WritableRaster) image.getData();
        //raster.setPixels(0,0,width,height,pixels);
        return image;
    }
	
	public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
	    int imageWidth  = image.getWidth();
	    int imageHeight = image.getHeight();

	    double scaleX = (double)width/imageWidth;
	    double scaleY = (double)height/imageHeight;
	    AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
	    AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BICUBIC);

	    return bilinearScaleOp.filter(
	        image,
	        new BufferedImage(width, height, image.getType()));
	}
	
	public int[] resizeBilinearGray(int[] pixels, int w, int h, int w2, int h2) {
	    int[] temp = new int[w2*h2] ;
	    int A, B, C, D, x, y, index, gray ;
	    float x_ratio = ((float)(w-1))/w2 ;
	    float y_ratio = ((float)(h-1))/h2 ;
	    float x_diff, y_diff, ya, yb ;
	    int offset = 0 ;
	    for (int i=0;i<h2;i++) {
	        for (int j=0;j<w2;j++) {
	            x = (int)(x_ratio * j) ;
	            y = (int)(y_ratio * i) ;
	            x_diff = (x_ratio * j) - x ;
	            y_diff = (y_ratio * i) - y ;
	            index = y*w+x ;

	            // range is 0 to 255 thus bitwise AND with 0xff
	            A = pixels[index] & 0xff ;
	            B = pixels[index+1] & 0xff ;
	            C = pixels[index+w] & 0xff ;
	            D = pixels[index+w+1] & 0xff ;
	            
	            // Y = A(1-w)(1-h) + B(w)(1-h) + C(h)(1-w) + Dwh
	            gray = (int)(
	                    A*(1-x_diff)*(1-y_diff) +  B*(x_diff)*(1-y_diff) +
	                    C*(y_diff)*(1-x_diff)   +  D*(x_diff*y_diff)
	                    ) ;

	            temp[offset++] = gray ;                                   
	        }
	    }
	    return temp ;
	}
	
}
