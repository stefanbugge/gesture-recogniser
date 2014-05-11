package dk.loeschcke.matrix.view.frame;

import dk.loeschcke.matrix.controller.DefaultController;
import dk.loeschcke.matrix.helper.MatrixHelper;
import dk.loeschcke.matrix.model.PixelFrame;
import dk.loeschcke.matrix.model.PixelFrame2;
import dk.loeschcke.matrix.util.Library;

import java.awt.*;
import java.beans.PropertyChangeEvent;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 18/08/13
 * Time: 16.47
 * To change this template use File | Settings | File Templates.
 */
public class RawPanel extends FramePanel {

    public RawPanel(int width, int height) {
        super(width, height);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(DefaultController.FRAME_UPDATE)) {
            PixelFrame2 frame = (PixelFrame2) evt.getNewValue();

            int[] pixels = MatrixHelper.resizePixels(frame.getPixels(), Library.FRAME_WIDTH, Library.FRAME_HEIGHT, width, height);

            image.getRaster().setPixels(0, 0, width, height, pixels);
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
    }

}
