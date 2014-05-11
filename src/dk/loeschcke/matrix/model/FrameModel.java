package dk.loeschcke.matrix.model;

import dk.loeschcke.matrix.controller.DefaultController;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 18/08/13
 * Time: 16.18
 * To change this template use File | Settings | File Templates.
 */
public class FrameModel extends AbstractModel {

    private PixelFrame2 frame;


    public PixelFrame2 getFrame() {
        return frame;
    }

    public void setFrame(PixelFrame2 frame) {
        PixelFrame2 oldFrame = this.frame;
        this.frame = frame;

        firePropertyChange(DefaultController.FRAME_UPDATE, oldFrame, frame);
    }

    public void setBlankFrame(PixelFrame2 frame) {
        System.out.println("setBlankFrame");
        PixelFrame2 oldFrame = this.frame;
        this.frame = frame;
        firePropertyChange(DefaultController.FRAME_RESET, oldFrame, frame);
    }
}
