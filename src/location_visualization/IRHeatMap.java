package location_visualization;

import java.awt.Graphics;

import javax.swing.JPanel;

import org.tc33.jheatchart.HeatChart;

public class IRHeatMap extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// TODO 32 bit
	
	private double[][] data = new double[32][32];
	private 	HeatChart map;
	
	public IRHeatMap() {
		map = new HeatChart(data);
//		map.setTitle("This is my heat chart title");
//		map.setXAxisLabel("X Axis");
//		map.setYAxisLabel("Y Axis");
		this.setPreferredSize(new java.awt.Dimension(200, 200));
		map.setShowXAxisValues(false);
		map.setShowYAxisValues(false);

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
