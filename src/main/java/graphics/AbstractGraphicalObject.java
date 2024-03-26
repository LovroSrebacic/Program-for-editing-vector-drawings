package main.java.graphics;

import java.util.ArrayList;
import java.util.List;

import main.java.listener.GraphicalObjectListener;
import main.java.util.GeometryUtil;

public abstract class AbstractGraphicalObject implements GraphicalObject{
	
	private Point[] hotPoints;
	private boolean[] hotPointSelected;
	private boolean selected;
	private List<GraphicalObjectListener> listeners;
	
	public AbstractGraphicalObject(Point[] hotPoints) {
		this.hotPoints = hotPoints;
		hotPointSelected = new boolean[hotPoints.length];
		listeners = new ArrayList<GraphicalObjectListener>();
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public int getNumberOfHotPoints() {
		return hotPoints.length;
	}

	@Override
	public Point getHotPoint(int index) {
		if(index < hotPoints.length) {
			return hotPoints[index];
		}
		
		return null;
	}

	@Override
	public void setHotPoint(int index, Point point) {
		if(index < hotPoints.length) {
			hotPoints[index] = point;
		}
	}

	@Override
	public boolean isHotPointSelected(int index) {
		if(index < hotPointSelected.length) {
			return hotPointSelected[index];
		}
		return false;
	}

	@Override
	public void setHotPointSelected(int index, boolean selected) {
		if(index < hotPointSelected.length) {
			hotPointSelected[index] = selected;
		}
	}

	@Override
	public double getHotPointDistance(int index, Point mousePoint) {
		if(index >= hotPoints.length) throw new IndexOutOfBoundsException(index + " is greater than " + hotPoints.length);
		return GeometryUtil.distanceFromPoint(hotPoints[index], mousePoint);
	}

	@Override
	public void translate(Point delta) {
		for (Point point : hotPoints) {
			point.translate(delta);
		}
	}

	@Override
	public void addGraphicalObjectListener(GraphicalObjectListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeGraphicalObjectListener(GraphicalObjectListener listener) {
		listeners.remove(listener);
	}
	
	public void notifyListeners() {
		for (GraphicalObjectListener listener : listeners) {
			listener.graphicalObjectChanged(this);
		}
	}

}
