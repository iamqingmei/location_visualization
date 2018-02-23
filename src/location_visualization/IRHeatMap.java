package location_visualization;

import java.awt.Graphics;

import javax.swing.JPanel;

import org.tc33.jheatchart.HeatChart;

public class IRHeatMap extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double[][] data = new double[][]{{0,5,1,3,2,4,7,6},{1,5,3,6,0,7,4,2},{0,7,1,6,5,3,2,4},
		{1,2,5,0,4,6,3,7},{6,3,0,7,2,1,5,4},{1,3,0,6,2,7,4,5},{1,0,5,4,2,7,6,3},{3,6,1,4,5,7,0,2}};
	private 	HeatChart map;
	
	public IRHeatMap() {
		map = new HeatChart(data);
//		map.setTitle("This is my heat chart title");
//		map.setXAxisLabel("X Axis");
//		map.setYAxisLabel("Y Axis");
		this.setPreferredSize(new java.awt.Dimension(350, 350));
	}
	
	public void setData(double[][] d) {
		this.data = d;
	}
	

			
			
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(map.getChartImage(), 0, 0, this); // see javadoc for more info on the parameters 
    }
}
