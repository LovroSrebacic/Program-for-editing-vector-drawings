package main.java.state;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import main.java.graphics.GraphicalObject;
import main.java.graphics.Point;
import main.java.graphics.Rectangle;
import main.java.gui.DocumentModel;
import main.java.render.Renderer;

public class SelectShapeState extends IdleState {
	
	private final static int HOT_POINT_BOUNDING_BOX_WIDTH = 3;
	
	private DocumentModel model;
	
	private GraphicalObject selectedGO;
	private int indexOfSelectedHotPoint;
	
	public SelectShapeState(DocumentModel model) {
		this.model = model;
	}
	
	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		GraphicalObject go = model.findSelectedGraphicalObject(mousePoint);
		if(go == null) {
			selectedGO = null;
			model.deselectAll();
			return;
		}
		
		if(!ctrlDown) {
			model.deselectAll();
			go.setSelected(true);
			selectedGO = go;
			
		} else {
			selectedGO = null;
			if(!go.isSelected()) {
				go.setSelected(true);
			} else {
				go.setSelected(false);
			}
		}
	}
	
	@Override
	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
			for (GraphicalObject go : model.getSelectedObjects()) {
				go.translate(new Point(-1, 0));
			}
			break;
			
		case KeyEvent.VK_RIGHT:
			for (GraphicalObject go : model.getSelectedObjects()) {
				go.translate(new Point(1, 0));
			}
			break;

		case KeyEvent.VK_UP:
			for (GraphicalObject go : model.getSelectedObjects()) {
				go.translate(new Point(0, -1));
			}
			break;
			
		case KeyEvent.VK_DOWN:
			for (GraphicalObject go : model.getSelectedObjects()) {
				go.translate(new Point(0, 1));
			}
			break;
			
		case KeyEvent.VK_ADD://numpad plus "+"
		case KeyEvent.VK_PLUS:
			List<GraphicalObject> selectedObjectsPlus = model.getSelectedObjects();
			if(!selectedObjectsPlus.isEmpty() && selectedObjectsPlus.size() == 1) {
				model.increaseZ(selectedObjectsPlus.get(0));
			}
			break;
			
		case KeyEvent.VK_SUBTRACT://numpad minus "-"	
		case KeyEvent.VK_MINUS:
			List<GraphicalObject> selectedObjectsMinus = model.getSelectedObjects();
			if(!selectedObjectsMinus.isEmpty() && selectedObjectsMinus.size() == 1) {
				model.decreaseZ(selectedObjectsMinus.get(0));
			}
			break;
			
		default:
			break;
		}
	}
	
	@Override
	public void mouseDragged(Point mousePoint) {
		if(selectedGO != null && indexOfSelectedHotPoint >= 0) {
			if(selectedGO.isHotPointSelected(indexOfSelectedHotPoint)) {
				selectedGO.setHotPoint(indexOfSelectedHotPoint, mousePoint);
			}
		} else {
			if(selectedGO != null) {
				int index = model.findSelectedHotPoint(selectedGO, mousePoint);
				if(index != -1) {
					selectedGO.setHotPointSelected(index, true);
					indexOfSelectedHotPoint = index;
					selectedGO.setHotPoint(index, mousePoint);
				}
			}
		}
	}
	
	@Override
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		if(selectedGO != null && indexOfSelectedHotPoint >= 0 && indexOfSelectedHotPoint < selectedGO.getNumberOfHotPoints()) {
			selectedGO.setHotPointSelected(indexOfSelectedHotPoint, false);
			indexOfSelectedHotPoint = -1;
		}
	}
	
	@Override
	public void afterDraw(Renderer r, GraphicalObject go) {
		if(!go.isSelected()) return;
		Rectangle rect = go.getBoundingBox();
		//drawing bounding box
		r.drawLine(new Point(rect.getX(), rect.getY()), new Point(rect.getX() + rect.getWidth(), rect.getY()));//upper line
		r.drawLine(new Point(rect.getX(), rect.getY()), new Point(rect.getX(), rect.getY() + rect.getHeight()));//left line
		r.drawLine(new Point(rect.getX(), rect.getY() + rect.getHeight()), new Point(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight()));//bottom line
		r.drawLine(new Point(rect.getX() + rect.getWidth(), rect.getY()), new Point(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight()));//right line
		
		
		if(selectedGO != null && selectedGO.equals(go)) {
			//draw hot points
			List<Point> hotPoints = new ArrayList<Point>();
			for (int i = 0; i < go.getNumberOfHotPoints(); i++) {
				hotPoints.add(go.getHotPoint(i));
			}
			for (Point point : hotPoints) {
				r.drawLine(new Point(point.getX() - HOT_POINT_BOUNDING_BOX_WIDTH, point.getY() - HOT_POINT_BOUNDING_BOX_WIDTH), 
						new Point(point.getX() + HOT_POINT_BOUNDING_BOX_WIDTH, point.getY() - HOT_POINT_BOUNDING_BOX_WIDTH));//upper line
				r.drawLine(new Point(point.getX() - HOT_POINT_BOUNDING_BOX_WIDTH, point.getY() - HOT_POINT_BOUNDING_BOX_WIDTH),
						new Point(point.getX() - HOT_POINT_BOUNDING_BOX_WIDTH, point.getY() + HOT_POINT_BOUNDING_BOX_WIDTH));//left line
				r.drawLine(new Point(point.getX() - HOT_POINT_BOUNDING_BOX_WIDTH, point.getY() + HOT_POINT_BOUNDING_BOX_WIDTH),
						new Point(point.getX() + HOT_POINT_BOUNDING_BOX_WIDTH, point.getY() + HOT_POINT_BOUNDING_BOX_WIDTH));//bottom line
				r.drawLine(new Point(point.getX() + HOT_POINT_BOUNDING_BOX_WIDTH, point.getY() - HOT_POINT_BOUNDING_BOX_WIDTH),
						new Point(point.getX() + HOT_POINT_BOUNDING_BOX_WIDTH, point.getY() + HOT_POINT_BOUNDING_BOX_WIDTH));//right line
			}
		}
	}
	
	@Override
	public void onLeaving() {
		selectedGO = null;
		model.deselectAll();
	}
}
