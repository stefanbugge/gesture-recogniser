package dk.loeschcke.matrix.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MatrixFrame extends JFrame  {

	private static final long serialVersionUID = 1L;
	
	private JPanel views;
	private JPanel buttons;

	public MatrixFrame(String name) {
		super(name);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setExtendedState(Frame.MAXIMIZED_BOTH);  
		
		views = new JPanel(new GridLayout(2, 2, 10, 10));
		
		buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		
		add(views, BorderLayout.WEST);
		add(buttons, BorderLayout.EAST);
	}

	public void addView(JPanel view) {
		views.add(view);
		views.setPreferredSize(new Dimension(900,900));
	}
	
	public void addButton(JButton button) {
		buttons.add(button);
		buttons.setPreferredSize(new Dimension(300,900));
	}
	

}
