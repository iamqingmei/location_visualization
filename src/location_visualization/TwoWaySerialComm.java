package location_visualization;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashSet;

public class TwoWaySerialComm
{
	private int baud_rate;
	private OutputStream out;
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
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(this.baud_rate,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
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
                		String str = "";
                		for (int i = 0; i<len; i++) {
//                    		str += i + ": " + ((Byte)buffer[i]).intValue();
                    		
                    		comPortParser.appendInt(((Byte)buffer[i]).intValue());
                    		
                    	}
//                		System.out.println(str);
//                		System.out.println("Coordination: " + comPortParser.ifCoordination().toString());
                		comPortParser.printIntBuffer();
                		
                	}
                    	
                }
//                

 
                
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