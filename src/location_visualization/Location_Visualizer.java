package location_visualization;

import java.awt.*;
import java.awt.event.*;


import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.tc33.jheatchart.HeatChart;

//import gnu.io.CommPortIdentifier;
import param.Parameters;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

public class Location_Visualizer{
	//logger
	private final static Logger LOGGER = Logger.getLogger(Location_Visualizer.class.getName());
	private static LinesComponent comp = new LinesComponent();
	private static ComPortParser comPortParser = ComPortParser.getInstance();
	// JFrame for the application
	private static JFrame _appFrame = null;
	// JPanel for laying out different views
	private static JPanel _MapPanel = null;
	private static JPanel _IRPanel = null;
	private static JPanel _inputPanel = null;
	private static JPanel _GraphPanel = null;
	private static JPanel _SimulationPanel = null;


	
	public static void main(String[] args) {
		LOGGER.setLevel(Level.INFO);
	    init_display();
	    
	}
	
	private static void init_display() {
		_appFrame = new JFrame();
		_appFrame.setTitle("Location Visualizer");
//		_appFrame.setSize(new Dimension(900, 870));
		_appFrame.setResizable(true);
		
		_MapPanel = new JPanel();
		_inputPanel = new JPanel();
		_GraphPanel = new JPanel();
		_SimulationPanel = new JPanel();
		_IRPanel = new JPanel();
		// Initialize the main MapLayout
		initMapLayout();
		// Initialize the main InputLayout
		initInputLayout();
		
		initIRPanel();
		
		initSimulationLayout();
		
		initGraphPanel();
		// Add PanelLayouts to content pane
		Container contentPane = _appFrame.getContentPane();
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		
		JPanel topPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		
		topPanel.add(_GraphPanel);
		
		
		bottomPanel.add(_inputPanel);
		bottomPanel.add(_MapPanel);
		bottomPanel.add(_IRPanel);
		
		leftPanel.add(topPanel);
		leftPanel.add(bottomPanel);
		contentPane.add(leftPanel, BorderLayout.WEST);
		contentPane.add(_SimulationPanel, BorderLayout.EAST);

		
		
		

		
	
		
		// Display the application
		_appFrame.pack();
		_appFrame.setLocationRelativeTo(null);
		_appFrame.setVisible(true);
		_appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private static void initGraphPanel() {
//		initGraphVariableSelection();
		JPanel graph_1 = new JPanel();
		JPanel graph_2 = new JPanel();
		JPanel graph_3 = new JPanel();
		graph_1.setLayout(new BoxLayout(graph_1, BoxLayout.Y_AXIS));
		graph_2.setLayout(new BoxLayout(graph_2, BoxLayout.Y_AXIS));
		graph_3.setLayout(new BoxLayout(graph_3, BoxLayout.Y_AXIS));
		
		JPanel panel1 = new JPanel();
		graph_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "mic", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		addAttribute("mic", panel1, false);
		graph_1.add(panel1);
		
		JPanel panel2 = new JPanel();
		graph_2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Co2", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		addAttribute("Co2", panel2, false);
		addAttribute("TVOC", panel2, false);
		graph_2.add(panel2);
		
		JPanel panel3 = new JPanel();
		graph_3.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "ambient", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		addAttribute("Temp", panel3, false);
		addAttribute("Humidity", panel3, false);
		addAttribute("Pressure", panel3, false);
		graph_3.add(panel3);
		
		GraphPloter graphPloter1 = new GraphPloter();
		GraphPloter graphPloter2 = new GraphPloter();
		GraphPloter graphPloter3 = new GraphPloter();
		graph_1.add(graphPloter1.getChartPanel());
		graph_2.add(graphPloter2.getChartPanel());
		graph_3.add(graphPloter3.getChartPanel());
		
		_GraphPanel.add(graph_1);
		_GraphPanel.add(graph_2);
		_GraphPanel.add(graph_3);
		
	}
	
//	private static void initGraphVariableSelection() {
//		JPanel panel = new JPanel();
//		JLabel lblSelectTheVariable = new JLabel("Select the Variable to Be Displayed: ");
//		panel.add(lblSelectTheVariable);
//		
//		JComboBox<String> comboBox = new JComboBox<String>();
//		comboBox.addItem("Variable1");
//		comboBox.addItem("Variable2");
//		comboBox.addItem("Variable3");
//		panel.add(comboBox);
//		
//		_GraphPanel.add(panel);
//	}
	
	
	private static void initSimulationLayout() {
		_SimulationPanel = new JPanel();
		_SimulationPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Simulation", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		_SimulationPanel.setLayout(new BoxLayout(_SimulationPanel, BoxLayout.Y_AXIS));
		
		JPanel channel12 = new JPanel();
		JPanel channel34 = new JPanel();
		SimulationPanel simPanel1 = new SimulationPanel("Channel 1", channel12);
		SimulationPanel simPanel2 = new SimulationPanel("Channel 2", channel12);
		SimulationPanel simPanel3 = new SimulationPanel("Channel 3", channel34);
		SimulationPanel simPanel4 = new SimulationPanel("Channel 4", channel34);
		
		
		JButton btn_sim_start = new JButton("Start");
		btn_sim_start.setFont(new Font("Arial", Font.BOLD, 13));
		btn_sim_start.setFocusPainted(false);
		btn_sim_start.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				LOGGER.info("Simulation Start button clicked");
			}
		});
		JButton btn_sim_stop = new JButton("Stop");
		btn_sim_stop.setFont(new Font("Arial", Font.BOLD, 13));
		btn_sim_stop.setFocusPainted(false);
		btn_sim_stop.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				LOGGER.info("Simulation Stop button clicked");
			}
		});
		
		
		
		JPanel btns = new JPanel();
		btns.add(btn_sim_start);
		btns.add(btn_sim_stop);
		
		_SimulationPanel.add(channel12);
		_SimulationPanel.add(channel34);
		_SimulationPanel.add(btns);

		
	}
	
	private static void initIRPanel() {
		_IRPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "IR Sensor", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		_IRPanel.setLayout(new BoxLayout(_IRPanel, BoxLayout.Y_AXIS));
		
		JCheckBox irCheckBox = new JCheckBox("IR Sensor");
		_IRPanel.add(irCheckBox);
		
		
		_IRPanel.add(new IRHeatMap());
		
		
	}
	
	private static class SimulationPanel {
		private JPanel simPanel;
		public JCheckBox enableCheckBox;
		public JTextField durationText;
		public JTextField periodText;
		public JTextField onTime1Text;
		public JTextField onTime2Text;
		public JTextField offTime1Text;
		public JTextField offTime2Text;
		public JTextField amplitudeText;
		
		public SimulationPanel(String name, JPanel parent) {
			this.simPanel = new JPanel();
			this.enableCheckBox = new JCheckBox("enable/disable");
			
			parent.add(this.simPanel);
			this.simPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), name, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			this.simPanel.setLayout(new BoxLayout(this.simPanel, BoxLayout.Y_AXIS));
			
			System.out.println("here");
//			duration period on time 1 on time 2 off time 1 off time 2 amplitude 
			this.durationText = addAttribute("duration", this.simPanel);
			this.periodText = addAttribute("period", this.simPanel);
			this.onTime1Text = addAttribute("on time 1", this.simPanel);
			this.onTime2Text = addAttribute("on time 2", this.simPanel);
			this.offTime1Text = addAttribute("off time 1", this.simPanel);
			this.offTime2Text = addAttribute("off time 2", this.simPanel);
			this.amplitudeText = addAttribute("amplitude", this.simPanel);
		}
	}
	
	private static void initMapLayout() {
	    comp.setBackground(Color.WHITE);
	    comp.setPreferredSize(new Dimension((int)(Parameters.MAP_MAXWIDTH_COOR * Parameters.MAP_PIXEL_MULTIPLIER + Parameters.MAP_MARGIN * 2), (int)(Parameters.MAP_MAXHEIGHT_COOR * Parameters.MAP_PIXEL_MULTIPLIER + Parameters.MAP_MARGIN * 2)));
	    comp.setBorder(new TitledBorder("Location Visualization"));
	    _MapPanel.add(comp);
	}

	
	private static void initInputLayout() {
		// vertical layout
		_inputPanel.setLayout(new BoxLayout(_inputPanel, BoxLayout.Y_AXIS));
	    
		initPortPanel();
		
		initCoorPanel();
		
		initMapAdjustPanel();
		
		initVariablePanel();

		return;
		
	}
	
	
	private static JTextField addAttribute(String attiName, JPanel panel) {
		
		JPanel sim_attri_panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) sim_attri_panel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		panel.add(sim_attri_panel);
		
		JLabel lblNewLabel = new JLabel(attiName);
		sim_attri_panel.add(lblNewLabel);
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		
		JTextField txtAttribute = new JTextField("Nil", 4);
		
		
		lblNewLabel.setLabelFor(txtAttribute);
		sim_attri_panel.add(txtAttribute);
		
		return txtAttribute;
	}
	
private static JTextField addAttribute(String attiName, JPanel panel, boolean setEditable) {
		
		JPanel sim_attri_panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) sim_attri_panel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		panel.add(sim_attri_panel);
		
		JLabel lblNewLabel = new JLabel(attiName);
		sim_attri_panel.add(lblNewLabel);
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		
		JTextField txtAttribute = new JTextField("Nil", 4);
		if (setEditable == false) {
			txtAttribute.setBackground(new Color(222, 222, 222));
		}
		txtAttribute.setEditable(setEditable);
		
		lblNewLabel.setLabelFor(txtAttribute);
		sim_attri_panel.add(txtAttribute);
		
		return txtAttribute;
	}
	
	
	private static void initMapAdjustPanel() {
		// Panel to adjust the map
		JPanel map_adjust_panel = new JPanel();
		map_adjust_panel.setLayout(new BoxLayout(map_adjust_panel, BoxLayout.Y_AXIS));
		map_adjust_panel.setBorder(new TitledBorder("Map Adjustment"));
		
		JPanel panel1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel1.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		JLabel pivot_label = new JLabel("Coordination of pivot: ");
		TextField pivot_x_input = new TextField("0", 2);
		TextField pivot_y_input = new TextField("0", 2);
		panel1.add(pivot_label);
		panel1.add(pivot_x_input);
		panel1.add(pivot_y_input);
		map_adjust_panel.add(panel1);
		
		JPanel panel2 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel2.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setHgap(0);
		JLabel turning_degree_label = new JLabel("Turning degree (clockwise): " );
		TextField turning_degree = new TextField("0", 2);
		panel2.add(turning_degree_label);
		panel2.add(turning_degree);
		
		JButton btn_submit_pivot = new JButton("Submit");
		btn_submit_pivot.setFont(new Font("Arial", Font.BOLD, 13));
		btn_submit_pivot.setFocusPainted(false);
		btn_submit_pivot.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				LOGGER.info("Pivot Submit button clicked");
				MapPointManager mapPointManager = MapPointManager.getInstance();
				
				mapPointManager.setPivot(Float.parseFloat(pivot_x_input.getText()), 
						Float.parseFloat(pivot_y_input.getText()));
				
				mapPointManager.setTurningDegree(Float.parseFloat(turning_degree.getText()));
				
				comp.setPoints(mapPointManager.turn(), mapPointManager.topPoint(), mapPointManager.bottomPoint());
			}
		});
		

		panel2.add(btn_submit_pivot);
		map_adjust_panel.add(panel2);
		_inputPanel.add(map_adjust_panel);
	}
	
	private static void initPortPanel() {
		
//		select the available port
		JPanel portPanel = new JPanel();
	    portPanel.setBorder(new TitledBorder("COM Port Selection"));
		portPanel.setLayout(new BoxLayout(portPanel, BoxLayout.Y_AXIS));
		JPanel namePanel = new JPanel();
		JPanel baudPanel = new JPanel();
		
		

	    
		namePanel.add(new JLabel("Port Name: "));
		JComboBox<String> comboBox= new JComboBox<String>();  
//	    for (CommPortIdentifier obj : TwoWaySerialComm.getAvailableSerialPorts()) {
//	        comboBox.addItem(obj.getName());
//	      } 
		comboBox.addItem("COMTesting1");
		comboBox.addItem("COMTesting2");
		namePanel.add(comboBox);
	    
		baudPanel.add(new JLabel("Baud Rate: "), BorderLayout.LINE_START);
	    JComboBox<String> comboBox_2 = new JComboBox<String>();
	    comboBox_2.addItem("1200");
	    comboBox_2.addItem("2400");
	    comboBox_2.addItem("4800");
	    comboBox_2.addItem("19200");
	    comboBox_2.addItem("38400");
	    comboBox_2.addItem("57600");
	    comboBox_2.addItem("115200");
	    baudPanel.add(comboBox_2);
	    
	    portPanel.add(namePanel);
	    
	    // buttom to confirm the port selected
	    JButton btn_confirm_port = new JButton("Confirm");
	    namePanel.add(btn_confirm_port);
	    btn_confirm_port.setFont(new Font("Arial", Font.BOLD, 13));
	    btn_confirm_port.setFocusPainted(false);
	    btn_confirm_port.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				try
			        {
						LOGGER.info((String)comboBox.getSelectedItem() + " is selected");
//			            (new TwoWaySerialComm()).connect((String)comboBox.getSelectedItem());
//			            new KeepUpdatingMap().execute();
			            (new Thread(new KeepMapUpdating())).start();
			        }
			        catch ( Exception ex )
			        {
			            // TODO Auto-generated catch block
			            ex.printStackTrace();
			        }
			}
		});
	    
	    JButton btn_stop_port = new JButton("Stop");
	    baudPanel.add(btn_stop_port);
	    btn_stop_port.setFont(new Font("Arial", Font.BOLD, 13));
	    btn_stop_port.setFocusPainted(false);
	    btn_stop_port.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				try
			        {
						// TODO
			        }
			        catch ( Exception ex )
			        {
			            // TODO Auto-generated catch block
			            ex.printStackTrace();
			        }
			}
		});
	    
	    portPanel.add(baudPanel);
	    _inputPanel.add(portPanel);
	    
	    JPanel autoLoadPanel = new JPanel();
	    autoLoadPanel.setLayout(new BoxLayout(autoLoadPanel, BoxLayout.Y_AXIS));
	    portPanel.add(autoLoadPanel);
	    
	    JCheckBox chckbxAutoLoad = new JCheckBox("Auto Load");
	    chckbxAutoLoad.setAlignmentX(Component.CENTER_ALIGNMENT);
	    autoLoadPanel.add(chckbxAutoLoad);
	    
	    JTextArea textArea = new JTextArea(4,10);
	    textArea.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
	    textArea.setMargin(new Insets(0, 15, 0, 15));
	    textArea.setEditable(false);
	    textArea.setBackground(new Color(222, 222, 222));
	   
	    comPortParser.setShowArea(textArea);
	    autoLoadPanel.add(textArea);
	}
	
	private static void initVariablePanel() {
		JPanel variablePanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) variablePanel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		variablePanel.setBorder(BorderFactory.createTitledBorder("Variables"));
		JPanel variableGroup1 = new JPanel();
		variableGroup1.setLayout(new BoxLayout(variableGroup1, BoxLayout.Y_AXIS));
		variablePanel.add(variableGroup1);
		addAttribute("variable A", variableGroup1);
		addAttribute("variable B", variableGroup1);
		addAttribute("variable C", variableGroup1);
		addAttribute("variable D", variableGroup1);
		addAttribute("variable E", variableGroup1);
		JPanel variableGroup2 = new JPanel();
		variableGroup2.setLayout(new BoxLayout(variableGroup2, BoxLayout.Y_AXIS));
		variablePanel.add(variableGroup2);
		addAttribute("variable F", variableGroup2);
		addAttribute("variable G", variableGroup2);
		addAttribute("variable H", variableGroup2);
		addAttribute("variable I", variableGroup2);
		addAttribute("variable J", variableGroup2);
		
		_inputPanel.add(variablePanel);
		
		
	}
	
	private static void initCoorPanel() {
		JPanel coorPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) coorPanel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
//		Key in the coordination manually
		coorPanel.setBorder(BorderFactory.createTitledBorder("Add Points Manually"));
	    JLabel coorLabel = new JLabel("Coordination of new point: ");
		TextField xInput = new TextField("0", 2);
		TextField yInput = new TextField("0", 2);
		coorPanel.add(coorLabel);
		coorPanel.add(xInput);
		coorPanel.add(yInput);
		
//		submit the coordination manually
		JButton btn_submit_new_point = new JButton("Submit");
		btn_submit_new_point.setFont(new Font("Arial", Font.BOLD, 13));
		btn_submit_new_point.setFocusPainted(false);
		btn_submit_new_point.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				LOGGER.info("Submit button clicked");
				float x = Float.parseFloat(xInput.getText());
				float y = Float.parseFloat(yInput.getText());
				MapPointManager mapPointManager = MapPointManager.getInstance();
				
				comp.setPoints(mapPointManager.addPoint(x, y), mapPointManager.topPoint(), mapPointManager.bottomPoint());
			}
		});
		coorPanel.add(btn_submit_new_point);
		_inputPanel.add(coorPanel);
	}

	
    public static class KeepMapUpdating implements Runnable 
    {
        private volatile boolean shutdown;
        
        public void run ()
        {
            try
            {
            	while(!shutdown) {
            		Thread.sleep(1000);
            		ArrayList<Float> res = comPortParser.ifCoordination();
    				if( res.size() % 2 != 0){
    					LOGGER.warning("Coordination has sth wrong!");
    					comPortParser.printIntBuffer();
    					this.shutdown();
    				}
    				if (res.size() > 0) {
    					
    					MapPointManager mapPointManager = MapPointManager.getInstance();
    					
    					for (int i = 0; i < res.size() / 2; i++) {
    						comp.setPoints(mapPointManager.addPoint(res.get(2*i), res.get(2*i + 1)), mapPointManager.topPoint(), mapPointManager.bottomPoint());
    					}
            	}
            }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }            
        }
        public void shutdown() {
        	shutdown = true;
        }
}

	
	    
}
