package location_visualization;

import java.util.ArrayList;
import java.util.Collections;


public class CommandGenerator {
	static Integer CMDcnt = 0;
	public static String generateCommand(ArrayList<String> s, ArrayList<Boolean> enable) {
		CMDcnt++;
//        // Request data
//        TransmitBuffer.RequestData = RequestData;

        // convert 
        String COMTransmitBuf = "";
        COMTransmitBuf += "S"; // Start frame
        COMTransmitBuf += Integer.toBinaryString(CMDcnt);
        // channel 1
        for (int channel = 0; channel < 4; channel ++) {
        		if (enable.get(channel)){
                COMTransmitBuf += "1";
            }
            else{
                COMTransmitBuf += "0";            
            }  
        		StringBuilder sb = new StringBuilder();

            for (int i = 0; i < 6; i++) {
            		sb.append(Integer.toHexString(Integer.parseInt(s.get( 7 * channel + i))));
            		if (sb.length() < 8) {
            	    sb.insert(0, '0'); // pad with leading zero if needed
            		}
            		COMTransmitBuf += sb.toString();
            		sb.setLength(0);
            }
            
            sb.append(Integer.toHexString(Integer.parseInt(s.get(7 * channel + 6))));
	    		if (sb.length() < 2) {
	    			sb.insert(0, '0'); // pad with leading zero if needed
	    		}
            COMTransmitBuf += sb.toString();
        }
        for (int i = 4; i < 9; i++) {
        	 	// 4 Start stop stimulation
            
            // 5 IR sensor on/off
            
            // 6 CO2 sensor on off
            
            // 7 Mic on off
            
            // 8 Ambient sensor on off

        		if (enable.get(i)){                        
                COMTransmitBuf += "1";
            }
            else{
                COMTransmitBuf += "0";
            }
        }
        
        String RequestData = "11";
        // TODO for anto load, any number except 00 or 255(FF)
        COMTransmitBuf += RequestData;
        
        
//        // Request data
//        COMTransmitBuf += TransmitBuffer.RequestData.ToString("X2");
               
        COMTransmitBuf += String.join("", Collections.nCopies(239 - COMTransmitBuf.length(), "\n"));
        COMTransmitBuf += "X";
        
        return COMTransmitBuf;
	}
}
