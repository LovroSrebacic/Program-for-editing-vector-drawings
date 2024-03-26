package main.java.listener;

import main.java.graphics.GraphicalObject;

public interface GraphicalObjectListener {
	public void graphicalObjectChanged(GraphicalObject go);
	public void graphicalObjectSelectionChanged(GraphicalObject go);
}
