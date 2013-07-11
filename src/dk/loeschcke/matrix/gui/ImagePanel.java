package dk.loeschcke.matrix.gui;

import java.util.Observable;
import java.util.Observer;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;

@SuppressWarnings("serial")
public class ImagePanel extends MarvinImagePanel implements Observer {

    private MarvinImage image;

	@Override
	public void update(Observable o, Object arg) {
		image = (MarvinImage) arg;
		image.update();
		this.setImage(image);
	}

}