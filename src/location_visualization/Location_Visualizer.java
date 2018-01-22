package location_visualization;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
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
	    System.out.println(getAvailableSerialPorts().toString());
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
		contentPane.add(_inputPanel, BorderLayout.SOUTH);
		contentPane.add(_MapPanel, BorderLayout.CENTER);
		
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
		TextField xInput = new TextField("0", 2);
		TextField yInput = new TextField("0", 2);
		_inputPanel.add(xInput);
		_inputPanel.add(yInput);
		
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
		
		_inputPanel.add(btn_submit);
		
		
		return;
		
	}

	/**
     * @return    A HashSet containing the CommPortIdentifier for all serial ports that are not currently being used.
     */
    public static HashSet getAvailableSerialPorts() {
        HashSet h = new HashSet();
        Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
        
        while (thePorts.hasMoreElements()) {
            CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
            switch (com.getPortType()) {
            case CommPortIdentifier.PORT_SERIAL:
                try {
                    CommPort thePort = com.open("CommUtil", 50);
                    thePort.close();
                    h.add(com);
                } catch (PortInUseException e) {
                    System.out.println("Port, "  + com.getName() +  ", is in use.");
                } catch (Exception e) {
                    System.err.println("Failed to open port " + com.getName());
                    e.printStackTrace();
                }
            }
        }
        return h;
    }


	    
}
