package location_visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JComponent;

import param.Parameters;

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
		System.out.println("setPivot:" + p.toString());
		this.pivot_point = p;
	}
	
	public void setTurningDegree(float f) {
		this.turning_degree = f;
		System.out.println("setTurningDegree" + f);
	}
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
//	    
//	 // add background color
//	    Graphics2D g2 = (Graphics2D) g;
//        g2.setColor(getBackground());
//        g2.fillRect(0, 0, (int)(Parameters.MAP_MAXWIDTH_COOR * Parameters.MAP_PIXEL_MULTIPLIER + Parameters.MAP_MARGIN), (int)(Parameters.MAP_MAXHEIGHT_COOR * Parameters.MAP_PIXEL_MULTIPLIER + Parameters.MAP_MARGIN));
//        g2.setColor(Color.WHITE);
//        
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
	
	private ArrayList<Integer> coorAfterTurn (Integer originalX, Integer originalY) {
		Integer pivot_x = this.pivot_point.get(0);
		Integer pivot_y = this.pivot_point.get(1);
		
		double distance = Math.sqrt(Math.pow(Math.abs(originalX - pivot_x),2) + Math.pow(Math.abs(originalY - pivot_y), 2));
		
		double new_degree;
		
		if ((originalY - pivot_y) == 0){
			 new_degree = 0 + Math.toRadians(this.turning_degree);
		}
		else{
			 new_degree = Math.asin((originalY - pivot_y) / distance) + Math.toRadians(this.turning_degree);
		}
		
		System.out.println("Distance: " + distance);
		System.out.println("new degree: " + new_degree * Math.PI / 180);
		Double new_x = distance * Math.cos(new_degree) + pivot_x;
		Double new_y = distance * Math.sin(new_degree) + pivot_y;
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		res.add(new_x.intValue());
		res.add(new_y.intValue());
		
		System.out.print(res.toString());
		return res;
		
	}
	
	public void turn() {
		for (int i = 0 ; i < points.size() ; i++) {
			points.set(i, coorAfterTurn(points.get(i).get(0), points.get(i).get(1)));
		}
		System.out.println(points.toString());
	
		clearLines();
		for (int i = 1; i < points.size(); i++){
			addLine(points.get(i-1).get(0), points.get(i-1).get(1), points.get(i).get(0), points.get(i).get(1));
		}
		repaint();
		
	}
}