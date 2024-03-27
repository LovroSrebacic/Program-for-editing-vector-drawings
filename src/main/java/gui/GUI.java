package main.java.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import main.java.graphics.GraphicalObject;
import main.java.graphics.LineSegment;
import main.java.graphics.Oval;
import main.java.graphics.Point;
import main.java.listener.DocumentModelListener;
import main.java.state.AddShapeState;
import main.java.state.EraserState;
import main.java.state.IdleState;
import main.java.state.SelectShapeState;
import main.java.state.State;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 700;

	private DocumentModel model;
	private Canvas canvas;
	private Map<String, GraphicalObject> prototypes;

	private State currentState;

	public GUI(List<GraphicalObject> objects) {
		model = new DocumentModel();
		prototypes = new HashMap<String, GraphicalObject>();
		currentState = new IdleState();
		initGUI(objects);
	}

	private void initGUI(List<GraphicalObject> objects) {
		pack();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double screenHeight = screenSize.getHeight();
		setLocation((int) screenWidth / 2 - (WINDOW_WIDTH / 2), (int) screenHeight / 2 - (WINDOW_HEIGHT / 2));
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setTitle("Vector drawings editor");

		addComponents(objects);
		addToolbar(objects);
		addListeners();
	}

	private void addComponents(List<GraphicalObject> objects) {
		canvas = new Canvas(model, currentState);
		add(canvas, BorderLayout.CENTER);

		for (GraphicalObject go : objects) {
			prototypes.put(go.getShapeID(), go);
		}
	}

	private void addToolbar(List<GraphicalObject> objects) {
		JToolBar toolbar = new JToolBar();

		for (GraphicalObject go : objects) {
			AbstractAction action = new CanvasAction(go);
			action.putValue(Action.NAME, go.getShapeName());
			toolbar.add(action);
		}
		
		selectAction.putValue(Action.NAME, "Select");
		toolbar.add(selectAction);
		
		eraserAction.putValue(Action.NAME, "Eraser");
		toolbar.add(eraserAction);

		toolbar.setFloatable(false);
		add(toolbar, BorderLayout.NORTH);
	}

	private void addListeners() {
		model.addDocumentModelListener(new DocumentModelListener() {
			@Override
			public void documentChanged() {
				canvas.repaint();
			}
		});

		canvas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					currentState.onLeaving();
					currentState = new IdleState();
					canvas.setCurrentState(currentState);
				} else {
					currentState.keyPressed(e.getKeyCode());
				}
			}
		});

		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int x = (int) e.getPoint().getX();
				int y = (int) e.getPoint().getY();
				currentState.mouseDown(new Point(x, y), e.isShiftDown(), e.isControlDown());
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				int x = (int) e.getPoint().getX();
				int y = (int) e.getPoint().getY();
				currentState.mouseUp(new Point(x, y), e.isShiftDown(), e.isControlDown());
			}
		});

		canvas.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = (int) e.getPoint().getX();
				int y = (int) e.getPoint().getY();
				currentState.mouseDragged(new Point(x, y));
			}
		});
	}

	private Action selectAction = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			currentState.onLeaving();
			currentState = new SelectShapeState(model);
			canvas.setCurrentState(currentState);
		}
	};
	
	private Action eraserAction = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			currentState.onLeaving();
			currentState = new EraserState(model);
			canvas.setCurrentState(currentState);
		}
	};

	private class CanvasAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		private GraphicalObject go;

		public CanvasAction(GraphicalObject go) {
			this.go = go;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			currentState.onLeaving();
			currentState = new AddShapeState(model, go);
			canvas.setCurrentState(currentState);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				List<GraphicalObject> objects = new ArrayList<GraphicalObject>();
				objects.add(new LineSegment());
				objects.add(new Oval());
				new GUI(objects).setVisible(true);
			}
		});
	}
}
