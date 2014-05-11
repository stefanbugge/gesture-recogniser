package dk.loeschcke.matrix.view.frame;

import dk.loeschcke.matrix.controller.DefaultController;
import dk.loeschcke.matrix.helper.MatrixHelper;
import dk.loeschcke.matrix.helper.PointV;
import dk.loeschcke.matrix.model.PixelFrame;
import dk.loeschcke.matrix.model.PixelFrame2;
import dk.loeschcke.matrix.util.Library;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 18/08/13
 * Time: 19.21
 * To change this template use File | Settings | File Templates.
 */
public class PointCloudPanel extends FramePanel {


    private int[] pixels;
    private int SCALE = 10;

    public PointCloudPanel(int width, int height) {
        super(width, height);
        pixels = new int[Library.FRAME_WIDTH*Library.FRAME_HEIGHT*SCALE*SCALE];
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(DefaultController.FRAME_UPDATE)) {
            PixelFrame2 frame = (PixelFrame2) evt.getNewValue();
//            PointV max = frame.getMax();
            List<PointV> maxPoints = frame.getMaxPoints();
            if (maxPoints != null) {

                for (PointV p : maxPoints) {
                    if (p != null) {
                        int x = (int) (p.X * SCALE);
                        int y = (int) (p.Y * SCALE);
                        int index = MatrixHelper.getIndex(x, y, Library.FRAME_WIDTH*SCALE);
                        pixels[index] = Math.min(50 + (int) p.V, 255);
                    }
                }
                // scale it to 500x500
                int[] scaledPixels = MatrixHelper.resizePixels(pixels, Library.FRAME_WIDTH*SCALE, Library.FRAME_HEIGHT*SCALE, width, height);
                image.getRaster().setPixels(0,0,width, height,scaledPixels);

                repaint();
            }
//            if (max != null) {
//                // 7x7 coming in - scale to 70x70
//                int x = (int) Math.round(max.X * SCALE);
//                int y = (int) Math.round(max.Y * SCALE);
//
//                int index = MatrixHelper.getIndex(x, y, Library.FRAME_WIDTH*SCALE);
//
//                pixels[index] = Math.min(50 + (int) max.V, 255);
//
//                // scale it to 500x500
//                int[] scaledPixels = MatrixHelper.resizePixels(pixels, Library.FRAME_WIDTH*SCALE, Library.FRAME_HEIGHT*SCALE, width, height);
//                image.getRaster().setPixels(0,0,width, height, scaledPixels);
//
//                repaint();
//            } else {
//                //pixels = new int[Library.FRAME_HEIGHT*Library.FRAME_WIDTH];
//            }
        } else {
            log.debug("Reset Frame Blank");
            pixels = new int[Library.FRAME_WIDTH*Library.FRAME_HEIGHT*SCALE*SCALE];
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
    }
}
