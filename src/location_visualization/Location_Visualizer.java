package location_visualization;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
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
	    comp.setPreferredSize(new Dimension((int)(MapPointManager.MAXHEIGHT * 40 + 50), (int)(MapPointManager.MAXHEIGHT * 40) + 50));
	    comp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    _inputPanel.add(comp);
	}

	
	private static void initInputLayout() {
		// vertical layout
		_inputPanel.setLayout(new BoxLayout(_inputPanel, BoxLayout.Y_AXIS));
//		select the available port
		JPanel portPanel = new JPanel();
		JComboBox<String> comboBox= new JComboBox<String>();  
	    for (CommPortIdentifier obj : TwoWaySerialComm.getAvailableSerialPorts()) {
	        comboBox.addItem(obj.getName());
	      } 
	    portPanel.add(comboBox);
	    
	    JButton btn_confirm_port = new JButton("Confirm");
	    btn_confirm_port.setFont(new Font("Arial", Font.BOLD, 13));
	    btn_confirm_port.setFocusPainted(false);
	    btn_confirm_port.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				try
			        {
			            (new TwoWaySerialComm()).connect((String)comboBox.getSelectedItem());
			        }
			        catch ( Exception ex )
			        {
			            // TODO Auto-generated catch block
			            ex.printStackTrace();
			        }
			}
		});
	    portPanel.add(btn_confirm_port);
	    _inputPanel.add(portPanel);
	    
		
	    JPanel coorPanel = new JPanel();
//		Key in the coordination manually
		TextField xInput = new TextField("0", 2);
		TextField yInput = new TextField("0", 2);
		coorPanel.add(xInput);
		coorPanel.add(yInput);
		
//		submit the coordination manually
		JButton btn_submit = new JButton("Submit");
		btn_submit.setFont(new Font("Arial", Font.BOLD, 13));
		btn_submit.setFocusPainted(false);
		btn_submit.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				LOGGER.info("Submit button clicked");
				float x = Float.parseFloat(xInput.getText());
				float y = Float.parseFloat(yInput.getText());
				MapPointManager mapPointManager = MapPointManager.getInstance();
				ArrayList<Integer> coor = mapPointManager.addPoint(x, y);
				comp.addPoint(coor);
				
			}
		});
		coorPanel.add(btn_submit);
		_inputPanel.add(coorPanel);

		
		
		
		return;
		
	}

	



	    
}
