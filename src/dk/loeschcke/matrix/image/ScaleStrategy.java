package dk.loeschcke.matrix.image;

public interface ScaleStrategy {

	int[] scale(int[] pixels, int w, int h, int w2, int h2);
}
