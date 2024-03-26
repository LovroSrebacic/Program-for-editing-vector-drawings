package main.java.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import main.java.graphics.GraphicalObject;
import main.java.graphics.LineSegment;
import main.java.graphics.Oval;

public class GUI extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 700;
	
	private DocumentModel model;
	private Canvas canvas;
	private Map<String, GraphicalObject> prototypes;

	public GUI(List<GraphicalObject> objects) {
		model = new DocumentModel();
		prototypes = new HashMap<String, GraphicalObject>();
		initGUI(objects);
	}
	
	private void initGUI(List<GraphicalObject> objects) {
		pack();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double screenHeight = screenSize.getHeight();
		setLocation((int) screenWidth / 2 - (WINDOW_WIDTH / 2), (int) screenHeight / 2 - (WINDOW_HEIGHT / 2));
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setTitle("Vector drawings editor");
		
		addComponents(objects);
	}
	
	private void addComponents(List<GraphicalObject> objects) {
		canvas = new Canvas(model);
		add(canvas, BorderLayout.CENTER);
		
		for(GraphicalObject go : objects) {
			prototypes.put(go.getShapeID(), go);
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				List<GraphicalObject> objects = new ArrayList<GraphicalObject>();
				objects.add(new LineSegment());
				objects.add(new Oval());
				new GUI(objects).setVisible(true);
			}
		});
	}
}
