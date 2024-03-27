package main.java.state;

import main.java.graphics.GraphicalObject;
import main.java.graphics.Point;
import main.java.render.Renderer;

public class IdleState implements State {

	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {}

	@Override
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {}

	@Override
	public void mouseDragged(Point mousePoint) {}

	@Override
	public void keyPressed(int keyCode) {}

	@Override
	public void afterDraw(Renderer r, GraphicalObject go) {}

	@Override
	public void afterDraw(Renderer r) {}

	@Override
	public void onLeaving() {}

}
