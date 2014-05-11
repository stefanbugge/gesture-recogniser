package dk.loeschcke.matrix.view.frame;

import dk.loeschcke.matrix.view.AbstractViewPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 18/08/13
 * Time: 16.38
 * To change this template use File | Settings | File Templates.
 */
public abstract class FramePanel extends AbstractViewPanel {

    protected static final Logger log = LoggerFactory.getLogger(FramePanel.class);

    protected final int width;
    protected final int height;

    protected BufferedImage image;

    public FramePanel(int width, int height) {
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    }

}
