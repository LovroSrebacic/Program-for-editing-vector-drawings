package main.java.graphics;

import java.awt.Dimension;

import main.java.util.GeometryUtil;

public class LineSegment extends AbstractGraphicalObject{

	public LineSegment() {
		super(new Point[] {new Point(0, 0), new Point(10, 0)});
	}
	
	public LineSegment(Point hotPoint1, Point hotPoint2) {
		super(new Point[]{ hotPoint1, hotPoint2 });
	}

	@Override
	public Rectangle getBoundingBox() {
		Point point1 = getHotPoint(0);
		Point point2 = getHotPoint(1);
		
		int x = point1.getX() < point2.getX() ? point1.getX() : point2.getX();
		int y = point1.getY() < point2.getY() ? point1.getY() : point2.getY();
		
		Dimension dimension = new Dimension(Math.abs(point1.getX() - point2.getX()), Math.abs(point1.getY() - point2.getY()));
		
		return new Rectangle(x, y, dimension.width, dimension.height);
	}

	@Override
	public double selectionDistance(Point mousePoint) {
		return GeometryUtil.distanceFromLineSegment(getHotPoint(0), getHotPoint(1), mousePoint);
	}

	@Override
	public String getShapeName() {
		return "Line";
	}

	@Override
	public GraphicalObject duplicate() {
		return new LineSegment(new Point(getHotPoint(0)), new Point(getHotPoint(1)));
	}

}
