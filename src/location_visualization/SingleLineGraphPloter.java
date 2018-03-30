package location_visualization;


import java.awt.FlowLayout;

import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import param.Parameters;

public class SingleLineGraphPloter{
		
	private XYSeriesCollection data;
	private JFreeChart chart;
	private final ChartPanel chartPanel;
	private XYSeries series= new XYSeries("Amplitude");

	private int count = 0;
	public SingleLineGraphPloter() {
	    data = new XYSeriesCollection();
	    data.addSeries(series);
	    chart = ChartFactory.createXYLineChart(
	    		"",
	        "", 
	        "", 
	        data,
	        PlotOrientation.VERTICAL,
	        false,
	        true,
	        false
	    );
	    
	    chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new java.awt.Dimension(300,175));
		FlowLayout flowLayout = (FlowLayout) chartPanel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		
		chart.getXYPlot().getRangeAxis().setRange(0.0, Parameters.MIN_SOUND_RM_RANGE);
	}
	
	public JPanel getChartPanel() {
		return chartPanel;
	}
	
	public void addPoint(double x, double y) {
		this.series.add(x, y);
		
		chartPanel.repaint();
	}
	public void addPoint(double y) {
		this.series.add(count, y);
		count++;
		chartPanel.repaint();
	}
	
	public void setSeries(float[] f) {
		this.series.clear();
		count=0;
		for (int i = 0;i < f.length; i++) {
			addPoint(f[i]);
		}
		chartPanel.repaint();
	}

}


