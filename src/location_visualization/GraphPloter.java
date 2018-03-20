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
	private XYSeries series= new XYSeries("Random Data");
	
	public GraphPloter() {
	    series.add(0.0,0.0);
	    series.add(1.0,2.0);
	    series.add(2.0,4.0);
	    series.add(3.0,6.0);
	    series.add(4.0,8.0);

	    data = new XYSeriesCollection();
	    data.addSeries(series);
//	    chart = ChartFactory.createXYLineChart(
//	    		"",
//	        "X", 
//	        "Y", 
//	        data,
//	        PlotOrientation.VERTICAL,
//	        false,
//	        true,
//	        false
//	    );
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
	}
	
	public JPanel getChartPanel() {
		return chartPanel;
	}
	
	public void addPoint(double x, double y) {
		series.add(x, y);
		chartPanel.repaint();
	}

}
