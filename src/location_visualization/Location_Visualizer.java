package location_visualization;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//import gnu.io.CommPortIdentifier;
import param.Parameters;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Location_Visualizer{
	//logger
	private final static Logger LOGGER = Logger.getLogger(Location_Visualizer.class.getName());
	private static LinesComponent comp = new LinesComponent();
	// JFrame for the application
	private static JFrame _appFrame = null;
	// JPanel for laying out different views
	private static JPanel _MapPanel = null;
	private static JPanel _inputPanel = null;
	
	public static void main(String[] args) {
		LOGGER.setLevel(Level.INFO);
	    init_display();
	    
	}
	
	private static void init_display() {
		_appFrame = new JFrame();
		_appFrame.setTitle("Location Visualizer");
//		_appFrame.setSize(new Dimension(900, 870));
		_appFrame.setResizable(false);
		
		_MapPanel = new JPanel();
		_inputPanel = new JPanel();
		
		// Initialize the main MapLayout
		initMapLayout();
		// Initialize the main InputLayout
		initInputLayout();
		
		// Add PanelLayouts to content pane
		Container contentPane = _appFrame.getContentPane();
		// horizontal layout
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		contentPane.add(_inputPanel);
		contentPane.add(_MapPanel);
		
		// Display the application
		_appFrame.pack();
		_appFrame.setLocationRelativeTo(null);
		_appFrame.setVisible(true);
		_appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private static void initMapLayout() {
	    comp.setPreferredSize(new Dimension((int)(Parameters.MAP_MAXWIDTH_COOR * Parameters.MAP_PIXEL_MULTIPLIER + Parameters.MAP_MARGIN * 2), (int)(Parameters.MAP_MAXHEIGHT_COOR * Parameters.MAP_PIXEL_MULTIPLIER + Parameters.MAP_MARGIN * 2)));
	    comp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    _inputPanel.add(comp);
	}

	
	private static void initInputLayout() {
		// vertical layout
		_inputPanel.setLayout(new BoxLayout(_inputPanel, BoxLayout.Y_AXIS));
	    
//		initPortPanel();
		
		initCoorPanel();
		
		initMapAdjustPanel();

		return;
		
	}
	
	private static void initMapAdjustPanel() {
		// Panel to adjust the map
		JPanel map_adjust_panel = new JPanel();
		map_adjust_panel.setLayout(new BoxLayout(map_adjust_panel, BoxLayout.Y_AXIS));
		map_adjust_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		JPanel panel1 = new JPanel();
		JLabel pivot_label = new JLabel("Coordination of pivot: ");
		TextField pivot_x_input = new TextField("0", 2);
		TextField pivot_y_input = new TextField("0", 2);
		panel1.add(pivot_label);
		panel1.add(pivot_x_input);
		panel1.add(pivot_y_input);
		map_adjust_panel.add(panel1);
		
		JPanel panel2 = new JPanel();
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
	
//	private static void initPortPanel() {
////		select the available port
//		JPanel portPanel = new JPanel();
//		JComboBox<String> comboBox= new JComboBox<String>();  
//	    for (CommPortIdentifier obj : TwoWaySerialComm.getAvailableSerialPorts()) {
//	        comboBox.addItem(obj.getName());
//	      } 
//	    portPanel.add(comboBox);
//	    
//	    // buttom to confirm the port selected
//	    JButton btn_confirm_port = new JButton("Confirm");
//	    btn_confirm_port.setFont(new Font("Arial", Font.BOLD, 13));
//	    btn_confirm_port.setFocusPainted(false);
//	    btn_confirm_port.addMouseListener(new MouseAdapter() {
//			public void mousePressed(MouseEvent e) {
//				try
//			        {
//			            (new TwoWaySerialComm()).connect((String)comboBox.getSelectedItem());
//			        }
//			        catch ( Exception ex )
//			        {
//			            // TODO Auto-generated catch block
//			            ex.printStackTrace();
//			        }
//			}
//		});
//	    portPanel.add(btn_confirm_port);
//	    _inputPanel.add(portPanel);
//	}
	
	private static void initCoorPanel() {
		JPanel coorPanel = new JPanel();
//		Key in the coordination manually
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

	



	    
}
