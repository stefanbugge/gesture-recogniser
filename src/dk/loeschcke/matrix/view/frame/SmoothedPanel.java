package dk.loeschcke.matrix.view.frame;

import dk.loeschcke.matrix.controller.DefaultController;
import dk.loeschcke.matrix.model.PixelFrame;
import dk.loeschcke.matrix.image.ScaleStrategy;
import dk.loeschcke.matrix.model.PixelFrame2;
import dk.loeschcke.matrix.util.Library;

import java.awt.*;
import java.beans.PropertyChangeEvent;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 18/08/13
 * Time: 17.42
 * To change this template use File | Settings | File Templates.
 */
public class SmoothedPanel extends FramePanel {

    private final ScaleStrategy scaleStrategy;

    public SmoothedPanel(int width, int height, ScaleStrategy scaleStrategy) {
        super(width, height);

        this.scaleStrategy = scaleStrategy;

    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals(DefaultController.FRAME_UPDATE)) {
            PixelFrame2 frame = (PixelFrame2) evt.getNewValue();

            int[] pixels = scaleStrategy.scale(frame.getPixels(), Library.FRAME_WIDTH, Library.FRAME_HEIGHT, width, height);
            image.getRaster().setPixels(0,0,width, height,pixels);
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
    }
}
