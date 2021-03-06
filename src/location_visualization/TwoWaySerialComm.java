package location_visualization;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;

public class TwoWaySerialComm
{
	// this class manages the input and output stream from the serial port 
	private int baud_rate;
	private OutputStream out;
	private InputStream in;
    private SerialPort serialPort;
    private CommPort commPort;
    
    public TwoWaySerialComm()
    {
        super();
        
    }
    
    void connect ( String portName, int br ) throws Exception
    {
    	this.baud_rate = br;
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            this.commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                this.serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(this.baud_rate,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                this.in = serialPort.getInputStream();
                this.out = serialPort.getOutputStream();
                
                (new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out))).start();

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
        
    }
    
	/**
	 * Disconnect the serial communication.
	 */
	public void disconnect() {
		if (this.isConnected()) {
			try {
				this.out.close();
				this.in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.commPort.close();
			System.out.println("Disconnected from Port " + commPort.getName());
			this.commPort = null;
		} else {
			System.out.println("There is nothing to disconnect");
		}
	}
	
	/**
	 * Check if the device is connected.
	 * @return
	 */
	public boolean isConnected(){
		return (this.commPort!=null);
	}
    
    public void write_to_com_port(String s) throws IOException {
//    	OutputStream out;
    	this.out.write(s.getBytes());
    	System.out.println("send: " + s);
    }
    
   
    /** */
    public static class SerialReader implements Runnable 
    {
        InputStream in;
        ComPortParser comPortParser = ComPortParser.getInstance();
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            System.out.println(buffer.length);
            try
            {
                while (( len = this.in.read(buffer)) > -1 )
                {
	                	if (len > 0) {
	                		System.out.println("------------");
	                		comPortParser.appendByteArray(Arrays.copyOfRange(buffer, 0, len));
	                	}
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }

    /** */
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
        
        public void run ()
        {
            try
            {                
                int c = 0;
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                }                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }
    
    
    
    /**
     * @return    A HashSet containing the CommPortIdentifier for all serial ports that are not currently being used.
     */
    public static HashSet<CommPortIdentifier> getAvailableSerialPorts() {
        HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
        Enumeration<?> thePorts = CommPortIdentifier.getPortIdentifiers();
        
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