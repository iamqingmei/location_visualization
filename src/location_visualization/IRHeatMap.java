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
		
//		map.setTitle("This is my heat chart title");
//		map.setXAxisLabel("X Axis");
//		map.setYAxisLabel("Y Axis");
		this.setPreferredSize(new java.awt.Dimension(200, 200));
		for (int i=0; i<32;i++) {
			for (int j=0; j<32;j++) {
				this.data[i][j] = i*0.2 + j*0.01;
			}
		}
		map = new HeatChart(data);
		map.setShowXAxisValues(false);
		map.setShowYAxisValues(false);
		repaint();
	}
	
	public void setData(double[][] d) {
		this.data = d;
		repaint();
	}
	
	public void setData(double[] d) {
		for (int i=0; i<32;i++) {
			for (int j=0; j<32;j++) {
				this.data[i][j] = d[i*32 + j];
			}
		}
		
		repaint();
	}
	
	public void setData(float[] d) {
		for (int i=0; i<32;i++) {
			for (int j=0; j<32;j++) {
				this.data[i][j] = d[i*32 + j];
			}
		}
		repaint();
	}
			
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
		this.map = new HeatChart(data);
		this.map.setShowXAxisValues(false);
		this.map.setShowYAxisValues(false);
        g.drawImage(map.getChartImage(), 0, 0, this); // see javadoc for more info on the parameters 
    }
}
