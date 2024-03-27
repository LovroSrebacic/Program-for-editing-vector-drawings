package main.java.state;

import main.java.graphics.GraphicalObject;
import main.java.graphics.Point;
import main.java.gui.DocumentModel;

public class AddShapeState extends IdleState {
	
	private GraphicalObject prototype;
	private DocumentModel model;

	public AddShapeState(DocumentModel model, GraphicalObject prototype) {
		this.prototype = prototype;
		this.model = model;
	}
	
	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
			GraphicalObject go = prototype.duplicate();
			go.translate(mousePoint);
			model.addGraphicalObject(go);
	}
}
