package dk.loeschcke.matrix.image;

import java.awt.image.BufferedImage;

public interface ScaleStrategy {

	int[] scale(int[] pixels, int w, int h, int w2, int h2);
}
