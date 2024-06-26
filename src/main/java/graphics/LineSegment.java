package main.java.graphics;

import java.awt.Dimension;
import java.util.List;
import java.util.Stack;

import main.java.render.Renderer;
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

	@Override
	public void render(Renderer r) {
		r.drawLine(getHotPoint(0), getHotPoint(1));	
	}

	@Override
	public String getShapeID() {
		return "@LINE";
	}

	@Override
	public void save(List<String> rows) {
		Point start = getHotPoint(0);
		Point end = getHotPoint(1);
		rows.add(String.format("%s %d %d %d %d", getShapeID(), start.getX(), start.getY(), end.getX(), end.getY()));
	}

	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		String[] coordinates = data.trim().split(" ");
		setHotPoint(0, new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));//start point
		setHotPoint(1, new Point(Integer.parseInt(coordinates[2]), Integer.parseInt(coordinates[3])));//end point
		stack.push(this);
	}

}
