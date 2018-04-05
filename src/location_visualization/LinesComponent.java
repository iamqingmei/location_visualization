package location_visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JComponent;

import param.Parameters;

public class LinesComponent extends JComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Color LINE_COLOR = Color.RED;
	private static final Color BLOCK_COLOR = Color.BLACK;
	private ArrayList<Line> lines = new ArrayList<Line>();
	private ArrayList<Block> blocks = new ArrayList<Block>();
	private ArrayList<ArrayList<Integer>> points = new ArrayList<ArrayList<Integer>>();
	private String bottom_left_text = "0,0";
	private String top_right_text = "10,10";

	private static class Line{
	    final int x1; // pixel
	    final int y1; // pixel
	    final int x2; // pixel
	    final int y2; // pixel
	
	    public Line(int x1, int y1, int x2, int y2) {
	        this.x1 = x1;
	        this.y1 = y1;
	        this.x2 = x2;
	        this.y2 = y2;
	    }               
	}
	
	private static class Block{
	    final int x; 
	    final int y;
	    final int width;
	    final int height;   
	
	    public Block(int x, int y, int w, int h) {
	        this.x = x;
	        this.y = y;
	        this.width = w;
	        this.height = h;
	    }               
	}
	
	
	public void addBlock(int x, int y, int w, int h) {
	    blocks.add(new Block(x,y,w,h));        
	    repaint();
	}


	public void addLine(int x1, int x2, int x3, int x4) {
	    lines.add(new Line(x1,x2,x3,x4));        
	    repaint();
	}
	
//	public void addPoint(ArrayList<Integer> coordination) {
//		points.add(coordination);
//		if (points.size() > 1) {
//			addLine(points.get(points.size() - 2).get(0), points.get(points.size() - 2).get(1), points.get(points.size() - 1).get(0), points.get(points.size() - 1).get(1));
//		}
//		repaint();
//	}
	
	public void clearLines() {
	    lines.clear();
	    repaint();
	}
	
	public void clearBlocks() {
	    blocks.clear();
	    repaint();
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
	    
	    for (Block block : blocks) {
	        g.setColor(BLOCK_COLOR);
	        g.drawRect(block.x, block.y, block.width, block.height); 
	    }   
	    
	    for (Line line : lines) {
	        g.setColor(LINE_COLOR);
	        g.drawLine(line.x1, line.y1, line.x2, line.y2);
	    }     
	    

	    
	    for (ArrayList<Integer> point: points) {
	    		g.setColor(Color.RED);
	    		g.fillOval(point.get(0) - 5 , point.get(1) - 5, 10, 10);
//	    		System.out.println(point.toString());
	    } 
	    
	    g.drawString(bottom_left_text, (int)Parameters.MAP_MARGIN/2, 
	    		(int) (Parameters.MAP_MAXHEIGHT_COOR * Parameters.MAP_PIXEL_MULTIPLIER + Parameters.MAP_MARGIN*1.5 + 5));
	    g.drawString(top_right_text, (int)(Parameters.MAP_MARGIN*1.5 - top_right_text.length()*5 + Parameters.MAP_MAXWIDTH_COOR * Parameters.MAP_PIXEL_MULTIPLIER),
	    		(int) (Parameters.MAP_MARGIN/2));
	}
	
	public void setPoints(ArrayList<ArrayList<Integer>> p, String top, String bottom) {
		this.points = p;
		
		clearLines();
		for (int i = 1; i < points.size(); i++){
			addLine(points.get(i-1).get(0), points.get(i-1).get(1), points.get(i).get(0), points.get(i).get(1));
		}
		
		this.top_right_text = top;
		this.bottom_left_text = bottom;
		repaint();
		
	}
	

}