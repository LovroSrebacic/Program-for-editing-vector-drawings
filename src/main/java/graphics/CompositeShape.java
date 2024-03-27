package main.java.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import main.java.listener.GraphicalObjectListener;
import main.java.render.Renderer;

public class CompositeShape implements GraphicalObject{
	
	private GraphicalObject[] objects;
	private boolean selected;
	private List<GraphicalObjectListener> listeners;
	
	public CompositeShape() {
		this.selected = false;
		listeners = new ArrayList<GraphicalObjectListener>();
	}
	
	public CompositeShape(GraphicalObject[] objects, boolean selected) {
		this.objects = objects;
		this.selected = selected;
		listeners = new ArrayList<GraphicalObjectListener>();
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
		notifySelectionListeners();
	}

	@Override
	public int getNumberOfHotPoints() {
		return 0;
	}

	@Override
	public Point getHotPoint(int index) {
		return null;
	}

	@Override
	public void setHotPoint(int index, Point point) {}

	@Override
	public boolean isHotPointSelected(int index) {
		return false;
	}

	@Override
	public void setHotPointSelected(int index, boolean selected) {}

	@Override
	public double getHotPointDistance(int index, Point mousePoint) {
		return Double.MAX_VALUE;
	}

	@Override
	public void translate(Point delta) {
		for (GraphicalObject object : objects) {
			object.translate(delta);
		}
		notifyObservers();
	}

	@Override
	public Rectangle getBoundingBox() {
		Rectangle rect = objects[0].getBoundingBox();
		int minX = rect.getX();
		int maxX = rect.getX() + rect.getWidth();
		int minY = rect.getY();
		int maxY = rect.getY() + rect.getHeight();
		
		for (int i = 1; i < objects.length; i++) {
			rect = objects[i].getBoundingBox();
			minX = rect.getX() < minX ? rect.getX() : minX;
			maxX = rect.getX() + rect.getWidth() > maxX ? rect.getX() + rect.getWidth() : maxX;
			minY = rect.getY() < minY ? rect.getY() : minY;
			maxY = rect.getY() + rect.getHeight() > maxY ? rect.getY() + rect.getHeight() : maxY;
		}
		
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}
	
	public GraphicalObject[] getObjects() {
		return objects;
	}
	
	public void setObjects(GraphicalObject[] objects) {
		this.objects = objects;
	}

	@Override
	public double selectionDistance(Point mousePoint) {
		if(objects.length == 0) {
			return Double.MAX_VALUE;
		}
		
		double[] distances = new double[objects.length];
		for (int i = 0; i < objects.length; i++) {
			distances[i] = objects[i].selectionDistance(mousePoint);
		}
		
		double min = distances[0];
		for (int i = 1; i < distances.length; i++) {
			if(distances[i] < min) {
				min = distances[i];
			}
		}
		return min;
	}

	@Override
	public void addGraphicalObjectListener(GraphicalObjectListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeGraphicalObjectListener(GraphicalObjectListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void render(Renderer r) {
		for (GraphicalObject object : objects) {
			object.render(r);
		}
	}

	@Override
	public String getShapeName() {
		return "Composite";
	}

	@Override
	public GraphicalObject duplicate() {
		GraphicalObject[] gObjects = new GraphicalObject[objects.length];
		for (int i = 0; i < objects.length; i++) {
			gObjects[i] = objects[i].duplicate();
		}
		return new CompositeShape(gObjects, false);
	}
	
	private void notifyObservers() {
		for (GraphicalObjectListener graphicalObjectListener : listeners) {
			graphicalObjectListener.graphicalObjectChanged(this);
		}
	}
	
	public void notifySelectionListeners() {
		for (GraphicalObjectListener listener : listeners) {
			listener.graphicalObjectSelectionChanged(this);
		}
	}

	@Override
	public String getShapeID() {
		return "@COMP";
	}

	@Override
	public void save(List<String> rows) {
		
	}

	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		
	}

}
