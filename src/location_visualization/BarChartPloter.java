package location_visualization;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class BarChartPloter {
	private XYSeriesCollection data;
	private JFreeChart barChart;
	private final ChartPanel chartPanel;
	private XYSeries seriesPre= new XYSeries("Pressure");
	private XYSeries seriesSound = new XYSeries("Sound");
	
	public BarChartPloter() {
	    data = new XYSeriesCollection();
	    data.addSeries(seriesPre);
	    data.addSeries(seriesSound);
		this.barChart = ChartFactory.createXYBarChart("", "", false, "", data, PlotOrientation.VERTICAL, true, true, false);
	    chartPanel = new ChartPanel(this.barChart);
	    chartPanel.setPreferredSize(new java.awt.Dimension(300,175));
		FlowLayout flowLayout = (FlowLayout) chartPanel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
//		XYPlot xyPlot = this.barChart.getXYPlot();
//		XYBarRenderer br = (XYBarRenderer) xyPlot.getRenderer();
//		br.setMargin(0.02);
		
	}
	public JPanel getChartPanel() {
		return chartPanel;
	}
	
	public void setSeriesPressure(float[] x, float[] y) {
		this.seriesPre.clear();
		for (int i = 0;i < x.length; i++) {
			seriesPre.add(x[i], y[i]);
		}
		chartPanel.repaint();
	}
	
	public void setSeriesSound(float[] x, float[] y) {
		this.seriesSound.clear();
		for (int i = 0;i < x.length; i++) {
			seriesSound.add(x[i], y[i]);
		}
		chartPanel.repaint();
	}
}
