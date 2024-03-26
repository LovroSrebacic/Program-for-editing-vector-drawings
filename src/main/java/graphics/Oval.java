package main.java.graphics;

import main.java.util.GeometryUtil;

public class Oval extends AbstractGraphicalObject {

	public Oval() {
		super(new Point[] { new Point(0, 10), new Point(10, 0) });
	}

	public Oval(Point lowerHotPoint, Point rightHotPoint) {
		super(new Point[] { lowerHotPoint, rightHotPoint });
	}

	@Override
	public Rectangle getBoundingBox() {
		Point lowerHotPoint = getHotPoint(0);
		Point rightHotPoint = getHotPoint(1);

		int x = lowerHotPoint.getX() - (rightHotPoint.getX() - lowerHotPoint.getX());
		int y = rightHotPoint.getY() - (lowerHotPoint.getY() - rightHotPoint.getY());

		int width = 2 * (rightHotPoint.getX() - lowerHotPoint.getX());
		int height = 2 * (lowerHotPoint.getY() - rightHotPoint.getY());

		return new Rectangle(x, y, width, height);
	}

	@Override
	public double selectionDistance(Point mousePoint) {
		// check if it's clicked inside oval
		Point lowerHotPoint = getHotPoint(0);
		Point rightHotPoint = getHotPoint(1);
		int a = rightHotPoint.getX() - lowerHotPoint.getX();
		int b = lowerHotPoint.getY() - rightHotPoint.getY();
		int p = lowerHotPoint.getX();
		int q = rightHotPoint.getY();
		double elipseEquation = Math.pow(mousePoint.getX() - p, 2) / Math.pow(a, 2)
				+ Math.pow(mousePoint.getY() - q, 2) / Math.pow(b, 2);
		if (elipseEquation <= 1) {
			return 0;// click is inside oval
		}

		Point[] points = getPoints(20);
		double min = GeometryUtil.distanceFromPoint(points[0], mousePoint);
		for (int i = 1; i < points.length; i++) {
			double distance = GeometryUtil.distanceFromPoint(points[i], mousePoint);
			if (distance < min) {
				min = distance;
			}
		}
		return min;
	}

	@Override
	public String getShapeName() {
		return "Oval";
	}

	@Override
	public GraphicalObject duplicate() {
		return new Oval(new Point(getHotPoint(0)), new Point(getHotPoint(1)));
	}
	
	/**
	 * 
	 * parametric equation of ellipse:
	 * x = a*sin(t)
	 * y = b*cos(t)
	 * 
	 */
	private Point[] getPoints(int numOfPoints) {
		Point lowerHotPoint = getHotPoint(0);
		Point rightHotPoint = getHotPoint(1);
		Point center = new Point(lowerHotPoint.getX(), rightHotPoint.getY());
		int a = rightHotPoint.getX() - lowerHotPoint.getX();
		int b = lowerHotPoint.getY() - rightHotPoint.getY();
		
		Point[] points = new Point[numOfPoints];
		for (int i = 0; i < numOfPoints; i++) {
			double t = (2*Math.PI/numOfPoints) * i;
			int x = (int)(a * Math.cos(t)) + center.getX();
			int y = (int)(b * Math.sin(t)) + center.getY();
			points[i] = new Point(x, y);
		}
		return points;
	}
}
