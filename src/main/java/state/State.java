package main.java.state;

import main.java.graphics.GraphicalObject;
import main.java.graphics.Point;
import main.java.render.Renderer;

public interface State {
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown);
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown);
	public void mouseDragged(Point mousePoint);
	public void keyPressed(int keyCode);
	
	public void afterDraw(Renderer r, GraphicalObject go);
	public void afterDraw(Renderer r);
	
	public void onLeaving();
}
