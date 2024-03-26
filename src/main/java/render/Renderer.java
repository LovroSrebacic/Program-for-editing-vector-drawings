package main.java.render;

import main.java.graphics.Point;

public interface Renderer {
	public void drawLine(Point start, Point end);
	public void fillPolygon(Point[] points);
}
