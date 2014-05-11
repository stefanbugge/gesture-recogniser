package dk.loeschcke.matrix.controller;

import dk.loeschcke.matrix.model.PixelFrame;
import dk.loeschcke.matrix.model.PixelFrame2;
import dk.loeschcke.matrix.util.Library;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 18/08/13
 * Time: 16.22
 * To change this template use File | Settings | File Templates.
 */
public class DefaultController extends AbstractController {

    public static final String FRAME_UPDATE = "Frame";
    public static final String FRAME_RESET = "BlankFrame";

    public void updateFrame(PixelFrame2 frame) {
        setModelProperty(FRAME_UPDATE, frame);
    }

    public void resetFrame() {
        setModelProperty(FRAME_RESET, new PixelFrame2(new int[] {}, 2, 16, 16));
    }


}
