package location_visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JComponent;

public class LinesComponent extends JComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinkedList<Line> lines = new LinkedList<Line>();
	private LinkedList<ArrayList<Integer>> points = new LinkedList<ArrayList<Integer>>();
	private ArrayList<Integer> pivot_point;
	private float turning_degree;

	private static class Line{
	    final int x1; 
	    final int y1;
	    final int x2;
	    final int y2;   
//	    final Color color;
	
	    public Line(int x1, int y1, int x2, int y2, Color color) {
	        this.x1 = x1;
	        this.y1 = y1;
	        this.x2 = x2;
	        this.y2 = y2;
//	        this.color = color;
	    }               
	}
	

	public void addLine(int x1, int x2, int x3, int x4) {
	    addLine(x1, x2, x3, x4, Color.black);
	}

	public void addLine(int x1, int x2, int x3, int x4, Color color) {
	    lines.add(new Line(x1,x2,x3,x4, color));        
	    repaint();
	}
	
	public void addPoint(ArrayList<Integer> coordination) {
		points.add(coordination);
		if (points.size() > 1) {
			addLine(points.get(points.size() - 2).get(0), points.get(points.size() - 2).get(1), points.get(points.size() - 1).get(0), points.get(points.size() - 1).get(1));
		}
		repaint();
	}
	
	public void clearLines() {
	    lines.clear();
	    repaint();
	}
	
	public void setPivot(ArrayList<Integer> p) {
		this.pivot_point = p;
	}
	
	public void setTurningDegree(float f) {
		this.turning_degree = f;
	}
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    
	    for (Line line : lines) {
	        g.setColor(Color.RED);
	        g.drawLine(line.x1, line.y1, line.x2, line.y2);
	    }
	    for (ArrayList<Integer> point: points) {
	        g.setColor(Color.RED);
	        g.fillOval(point.get(0) - 5 , point.get(1) - 5, 10, 10);
	        System.out.println(point.toString());
	    }
	    
	}
}