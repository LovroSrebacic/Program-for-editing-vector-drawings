package main.java.util;

import java.awt.geom.Line2D;

import main.java.graphics.Point;

public class GeometryUtil {

	public static double distanceFromPoint(Point point1, Point point2) {
		double distance = 0.0;
		
		distance += Math.pow((point1.getX() - point2.getX()), 2);
		distance += Math.pow((point1.getY() - point2.getY()), 2);
		
		return Math.sqrt(distance);
	}
	
	public static double distanceFromLineSegment(Point start, Point end, Point point) {
		return Line2D.ptSegDist(start.getX(), start.getY(), end.getX(), end.getY(), point.getX(), point.getY());
	}
}
