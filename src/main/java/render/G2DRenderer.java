package main.java.render;

import java.awt.Color;
import java.awt.Graphics2D;

import main.java.graphics.Point;

public class G2DRenderer implements Renderer{
	
	private Graphics2D g2d;
	
	public G2DRenderer(Graphics2D g2d) {
		this.g2d = g2d;
	}

	@Override
	public void drawLine(Point start, Point end) {
		g2d.setColor(Color.BLUE);
		g2d.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
	}

	@Override
	public void fillPolygon(Point[] points) {
		int[] xpoints = new int[points.length];
		int[] ypoints = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			xpoints[i] = points[i].getX();
			ypoints[i] = points[i].getY();
		}
		
		g2d.setColor(Color.BLUE);
		g2d.fillPolygon(xpoints, ypoints, points.length);
		g2d.setColor(Color.RED);
		g2d.drawPolygon(xpoints, ypoints, points.length);
	}

}
