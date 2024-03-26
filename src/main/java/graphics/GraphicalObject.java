package main.java.graphics;

import main.java.listener.GraphicalObjectListener;

public interface GraphicalObject {
	// object manipulation
	public boolean isSelected();

	public void setSelected(boolean selected);

	public int getNumberOfHotPoints();

	public Point getHotPoint(int index);

	public void setHotPoint(int index, Point point);

	public boolean isHotPointSelected(int index);

	public void setHotPointSelected(int index, boolean selected);

	public double getHotPointDistance(int index, Point mousePoint);

	// geometric operations
	public void translate(Point delta);

	public Rectangle getBoundingBox();

	public double selectionDistance(Point mousePoint);

	// observer methods
	public void addGraphicalObjectListener(GraphicalObjectListener listener);

	public void removeGraphicalObjectListener(GraphicalObjectListener listener);

	// prototype support methods
	public String getShapeName();

	public GraphicalObject duplicate();

}
