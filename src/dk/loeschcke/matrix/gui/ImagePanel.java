package dk.loeschcke.matrix.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel implements Observer {

    private BufferedImage image;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters       
    }

	@Override
	public void update(Observable o, Object arg) {
		image = (BufferedImage) arg;
	}

}