package main.java.render;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.java.graphics.Point;

public class SVGRenderer implements Renderer {

	private List<String> lines;
	private String pathToFile;
	
	public SVGRenderer(String pathToFile) {
		this.pathToFile = pathToFile;
		lines = new ArrayList<String>();
		lines.add("<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n");
	}
	
	public void close() throws IOException {
		lines.add("</svg>");
		FileWriter fw = new FileWriter(new File(pathToFile));
		for (String line : lines) {
			fw.write(line);
		}
		fw.flush();
		fw.close();
	}
	
	@Override
	public void drawLine(Point start, Point end) {
		lines.add(String.format("\t<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:#0000ff;\"/>\n", start.getX(), start.getY(), end.getX(), end.getY()));
	}

	@Override
	public void fillPolygon(Point[] points) {
		StringBuilder sb = new StringBuilder();
		sb.append("\t<polygon points=\"");
		for (Point point : points) {
			sb.append(point.getX()).append(',').append(point.getY()).append(' ');
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("\" style=\"stroke:#ff0000; fill:#0000ff;\"/>\n");
		lines.add(sb.toString());
	}
}
