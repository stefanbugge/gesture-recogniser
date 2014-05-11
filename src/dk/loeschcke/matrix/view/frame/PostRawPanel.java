package dk.loeschcke.matrix.view.frame;

import dk.loeschcke.matrix.controller.DefaultController;
import dk.loeschcke.matrix.helper.MatrixHelper;
import dk.loeschcke.matrix.helper.PointV;
import dk.loeschcke.matrix.model.PixelFrame;
import dk.loeschcke.matrix.model.PixelFrame2;
import dk.loeschcke.matrix.util.Library;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 18/08/13
 * Time: 19.14
 * To change this template use File | Settings | File Templates.
 */
public class PostRawPanel  extends FramePanel {

    private int SCALE = 10;

    public PostRawPanel(int width, int height) {
        super(width, height);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(DefaultController.FRAME_UPDATE)) {
            PixelFrame2 frame = (PixelFrame2) evt.getNewValue();
            //PointV max = frame.getMax();
            List<PointV> maxPoints = frame.getMaxPoints();
            if (maxPoints != null) {

                int[] pixels = new int[Library.FRAME_WIDTH*Library.FRAME_HEIGHT*SCALE*SCALE];
                for (PointV p : maxPoints) {
                    if (p != null) {
                        int x = (int) (p.X * SCALE);
                        int y = (int) (p.Y * SCALE);
                        int index = MatrixHelper.getIndex(x, y, Library.FRAME_WIDTH*SCALE);
                        pixels[index] = Math.min(50 + (int) p.V, 255);
                    }
                }
                // scale it to 500x500
                pixels = MatrixHelper.resizePixels(pixels, Library.FRAME_WIDTH*SCALE, Library.FRAME_HEIGHT*SCALE, width, height);

                image.getRaster().setPixels(0,0,width, height,pixels);

                repaint();
            }
//            if (max != null) {
//                // 7x7 coming in - scale to 70x70
//                int[] pixels = new int[Library.FRAME_WIDTH*Library.FRAME_HEIGHT*SCALE*SCALE];
//                int x = (int) (max.X * SCALE);
//                int y = (int) (max.Y * SCALE);
//                int index = MatrixHelper.getIndex(x, y, Library.FRAME_WIDTH*SCALE);
//                pixels[index] = Math.min(50 + (int) max.V, 255);
//
//                // scale it to 500x500
//                pixels = MatrixHelper.resizePixels(pixels, Library.FRAME_WIDTH*SCALE, Library.FRAME_HEIGHT*SCALE, width, height);
//
//                image.getRaster().setPixels(0,0,width, height,pixels);
//
//                repaint();
//            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
    }

}
