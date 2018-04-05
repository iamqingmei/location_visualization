package location_visualization;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.tc33.jheatchart.HeatChart;

import param.Parameters;

public class IRHeatMap extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double[][] data = new double[32][32];
	private 	HeatChart map;
	
	public IRHeatMap() {
		
//		map.setTitle("This is my heat chart title");

		this.setPreferredSize(new java.awt.Dimension(240, 280));
		for (int i=0; i<32;i++) {
			for (int j=0; j<32;j++) {
				this.data[i][j] = ((i + j) - 32 / 4);
			}
		}
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
	
	private void convertRange() {
		for (int i=0; i<32;i++) {
			for (int j=0; j<32;j++) {
				if (this.data[i][j] > Parameters.MAX_IR_VAL) {
					this.data[i][j] = Parameters.MAX_IR_VAL;
				}
				if (this.data[i][j] < Parameters.MIN_IR_VAL) {
					this.data[i][j] = Parameters.MIN_IR_VAL;
				}
			}
		}
	}
			
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        convertRange();
		this.map = new HeatChart(data, Parameters.MIN_IR_VAL, Parameters.MAX_IR_VAL);
		
		this.map.setShowXAxisValues(false);
		this.map.setShowYAxisValues(false);
		this.map.setCellSize(new Dimension(6,6));
        g.drawImage(map.getChartImage(), 0, 0, this); // see javadoc for more info on the parameters 
    }
}
