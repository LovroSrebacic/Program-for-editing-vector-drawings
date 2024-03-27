package main.java.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import main.java.graphics.CompositeShape;
import main.java.graphics.GraphicalObject;
import main.java.graphics.LineSegment;
import main.java.graphics.Oval;
import main.java.graphics.Point;
import main.java.listener.DocumentModelListener;
import main.java.render.SVGRenderer;
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

		loadAction.putValue(Action.NAME, "Load");
		toolbar.add(loadAction);

		saveAction.putValue(Action.NAME, "Save");
		toolbar.add(saveAction);

		svgExportAction.putValue(Action.NAME, "SVG export");
		toolbar.add(svgExportAction);

		for (GraphicalObject go : objects) {
			if (!(go instanceof CompositeShape)) {
				AbstractAction action = new CanvasAction(go);
				action.putValue(Action.NAME, go.getShapeName());
				toolbar.add(action);
			}
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

	private Action svgExportAction = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("SVG export");
			if (fc.showSaveDialog(GUI.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			String path = fc.getSelectedFile().getPath();
			if (!path.endsWith(".svg")) {
				path += ".svg";
			}

			SVGRenderer svgRenderer = new SVGRenderer(path);
			List<GraphicalObject> objects = model.list();
			for (GraphicalObject go : objects) {
				go.render(svgRenderer);
			}

			try {
				svgRenderer.close();
				JOptionPane.showMessageDialog(GUI.this, "SVG file successfully generated.", "INFO",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(GUI.this, "While exporting to file " + path + ": " + e1, "ERROR",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	};

	private Action saveAction = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Save");
			if (fc.showSaveDialog(GUI.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			String path = fc.getSelectedFile().getPath();
			if (!path.endsWith(".graph")) {
				path += ".graph";
			}

			List<GraphicalObject> objects = model.list();
			List<String> rows = new ArrayList<String>();
			for (GraphicalObject go : objects) {
				go.save(rows);
			}

			try {
				FileWriter fw = new FileWriter(new File(path));
				for (String row : rows) {
					fw.write(row);
					fw.write("\n");
				}
				fw.flush();
				fw.close();
				JOptionPane.showMessageDialog(GUI.this, "File successfully generated.", "INFO",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(GUI.this, "While writing to file " + path + ": " + e1, "ERROR",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	};

	private Action loadAction = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Load");
			if (fc.showOpenDialog(GUI.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			File file = fc.getSelectedFile();

			try {
				List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
				Stack<GraphicalObject> objects = new Stack<GraphicalObject>();
				GraphicalObject go = null;
				String shapeId = null;
				int index = -1;

				boolean error = false;
				String errorMessage = null;

				for (String line : lines) {
					if (line.startsWith("@")) {
						index = line.indexOf(' ');
						if (index != -1) {
							shapeId = line.substring(0, index);
							go = prototypes.get(shapeId);
							if (go != null) {
								if (go instanceof CompositeShape) {
									int numOfObjects = Integer.parseInt(line.substring(index + 1));
									GraphicalObject[] gObjects = new GraphicalObject[numOfObjects];
									for (int i = 0; i < numOfObjects; i++) {
										gObjects[i] = objects.pop();
									}

									((CompositeShape) go).setObjects(gObjects);
									go.duplicate().load(objects, line.substring(index + 1));
								} else {
									go.duplicate().load(objects, line.substring(index + 1));
								}
							} else {
								error = true;
								errorMessage = "Unknown shape: " + shapeId;
								break;
							}
						} else {
							error = true;
							errorMessage = "File malformatted in line: " + line;
							break;
						}
					} else {
						error = true;
						errorMessage = "File malformatted in line: " + line;
						break;
					}
				}

				if (!error) {
					for (GraphicalObject graphicalObject : objects) {
						model.addGraphicalObject(graphicalObject);
					}
				} else {
					JOptionPane.showMessageDialog(GUI.this, errorMessage, "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(GUI.this, "While reading file " + file.getName() + ": " + e1, "ERROR",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
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
				objects.add(new CompositeShape());
				new GUI(objects).setVisible(true);
			}
		});
	}
}
