package dk.loeschcke.matrix.image;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class DefaultScaleStrategy implements ScaleStrategy {

	@Override
	public BufferedImage scale(int[] pixels, int width, int height, int newWidth, int newHeight) {

	    double scaleX = (double)newWidth/width;
	    double scaleY = (double)newHeight/height;
	    
	    AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
	    AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BICUBIC);
	    
	    BufferedImage image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        image.getRaster().setPixels(0,0,width,height,pixels);

	    return bilinearScaleOp.filter( image,  new BufferedImage(newWidth,newHeight, image.getType()));
	}

}