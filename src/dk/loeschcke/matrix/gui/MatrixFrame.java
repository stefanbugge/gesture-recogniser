package dk.loeschcke.matrix.gui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MatrixFrame extends JFrame implements Observer {

	private JPanel panel;

	public MatrixFrame(String name, JPanel panel) {
		super(name);
		this.panel = panel;
	}

	@Override
	public void update(Observable o, Object arg) {
		panel.repaint();
	}

}
