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
	
	/**
	 * 
	 */
	private XYSeriesCollection data;
	private JFreeChart chart;
	private final ChartPanel chartPanel;
	
	public GraphPloter() {
		
	    final XYSeries series = new XYSeries("Random Data");
	    series.add(1.0, 500.2);
	    series.add(5.0, 694.1);
	    series.add(4.0, 100.0);
	    series.add(12.5, 734.4);
	    series.add(17.3, 453.2);
	    series.add(21.2, 500.2);
//	    series.add(21.9, null);
	    series.add(25.6, 734.4);
	    series.add(30.0, 453.2);
	    
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

}
