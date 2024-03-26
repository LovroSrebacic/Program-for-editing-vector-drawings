package main.java.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JComponent;

import main.java.graphics.GraphicalObject;
import main.java.render.G2DRenderer;

public class Canvas extends JComponent{

	private static final long serialVersionUID = 1L;
	
	private DocumentModel model;
	
	public Canvas(DocumentModel model) {
		this.model = model;
		setFocusable(true);
		requestFocusInWindow();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		G2DRenderer renderer = new G2DRenderer((Graphics2D)g);
		List<GraphicalObject> objects = model.list();
		for (GraphicalObject go : objects) {
			go.render(renderer);
		}
		requestFocusInWindow();
	}

}
