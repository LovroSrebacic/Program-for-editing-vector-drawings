package main.java.graphics;

import java.util.List;
import java.util.Stack;

import main.java.listener.GraphicalObjectListener;
import main.java.render.Renderer;

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

	// drawing the object
	public void render(Renderer r);

	// prototype support methods
	public String getShapeName();

	public GraphicalObject duplicate();

	// support for save and load
	public String getShapeID();

	public void save(List<String> rows);

	public void load(Stack<GraphicalObject> stack, String data);

}
