package location_visualization;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;



public class GraphPloter{
	
	private XYSeriesCollection data;
	private JFreeChart chart;
	private final ChartPanel chartPanel;
	private XYSeries seriesYaw= new XYSeries("Yaw");
	private XYSeries seriesRoll= new XYSeries("Roll");
	private XYSeries seriesPitch= new XYSeries("Pitch");
	private int count_yaw = 0;
	private int count_roll = 0;
	private int count_pitch = 0;
	public GraphPloter() {
	    data = new XYSeriesCollection();
	    data.addSeries(seriesYaw);
	    data.addSeries(seriesRoll);
	    data.addSeries(seriesPitch);
	    chart = ChartFactory.createXYLineChart(
	    		"",
	        "", 
	        "", 
	        data,
	        PlotOrientation.VERTICAL,
	        true,
	        true,
	        false
	    );
	    
	    chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new java.awt.Dimension(300,175));
		FlowLayout flowLayout = (FlowLayout) chartPanel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
	}
	
	public JPanel getChartPanel() {
		return chartPanel;
	}
	
	public void addPoint(double x, double y, char c) {
		if (c=='y') {
			this.seriesYaw.add(x, y);
		}else if(c=='r') {
			this.seriesRoll.add(x, y);
		}else if (c == 'p') {
			this.seriesPitch.add(x, y);
		}
		
		chartPanel.repaint();
	}
	public void addPoint(double y, char c) {
		if (c=='y') {
			this.seriesYaw.add(count_yaw, y);
			count_yaw ++;
		}else if(c=='r') {
			this.seriesRoll.add(count_roll, y);
			count_roll ++;
		}else if (c == 'p') {
			this.seriesPitch.add(count_pitch, y);
			count_pitch++;
		}
		chartPanel.repaint();
	}

}
