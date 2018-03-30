package location_visualization;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ComPortParser {
	private final static Logger LOGGER = Logger.getLogger(ComPortParser.class.getName());
	private ArrayList<Byte> byteBuffer = new ArrayList<>();
	private static ComPortParser instance = null;
	private JTextArea showArea = null;
	private static int numberOfReceivedata = 0;
	private JTextField co2_tf;
	private JTextField TVOC_tf;
	private JTextField temperature_tf;
	private JTextField humi_tf;
	private JTextField pressure_tf;
	private JTextField amb_tf;
	private JTextField vbat_tf;
	private JTextField RMSSoundNoiseTF, speedx_TF, speedy_TF;
	private LinesComponent comp;
	private IRHeatMap irHeatMap;
	private int CMDCount = -99;
	private GraphPloter yaw_gp;
	private GraphPloter pitch_gp;
	private GraphPloter roll_gp;
	private int CO2val, TVOCval;
	private float TempVal, HumidityVal, VbatVal, pitchVal, yawVal, rollVal, position_x,position_y,airPressureVal, ambTempVal,RMSSoundNoise;
	private float speed_x,speed_y;
    private float[] p_matrix = new float[3];
    private float[] FSound = new float[4];
    private float[] ASound = new float[4];
    private float[] RMSNoiseSet = new float[4];
    private float[] Fpressure = new float[4];
    private float[] Apressure = new float[4];
    private float[] IRimage32x32 = new float[1024];
    
	protected ComPortParser() {
	   	// Exists only to defeat instantiation.
   	}
   	public static ComPortParser getInstance() {
	   	if(instance == null) {
    	  	instance = new ComPortParser();
      	}
      	return instance;
   	}
   	
   	public void setShowArea(JTextArea area ) {
		this.showArea = area;
	}
   	
   	public void setco2TF(JTextField a) {
		this.co2_tf = a;
	}
   	
	public void setIRHeatMap(IRHeatMap a) {
		this.irHeatMap = a;
	}
   	
   	public void setHumiTF(JTextField a) {
		this.humi_tf = a;
	}
   	
   	public void setTVOCTF(JTextField a) {
		this.TVOC_tf = a;
	}
   	
   	public void setpressureTF(JTextField a) {
		this.pressure_tf = a;
	}
   	
   	public void settempTF(JTextField a) {
		this.temperature_tf = a;
	}
   	
   	public void setambTF(JTextField a) {
		this.amb_tf = a;
	}
   	
   	public void setSpeedXTF(JTextField a) {
   		this.speedx_TF = a;
   	}
   	
	public void setSpeedYTF(JTextField a) {
   		this.speedy_TF = a;
   	}
   	
   	public void setvbatTF(JTextField a) {
		this.vbat_tf = a;
	}
   	
   	public void setRMSSoundNoiseTF(JTextField a) {
		this.RMSSoundNoiseTF = a;
	}
   	
   	public void appendByte(byte a) {
   		byteBuffer.add(a);
   		showByteBufferInTextField();
   		updateGUI();
	}
   	
   	public void appendByteArray(byte[] a) {
		for (byte t: a) {
			byteBuffer.add(t);
		}
		showByteBufferInTextField();
		numberOfReceivedata++;
		updateGUI();
   	}
   	
   	
   	public int getNumberOfReceivedata() {
		return numberOfReceivedata;
	}
   	public void setMapComp(LinesComponent lc) {
   		this.comp = lc;
   	}
   	
   	public void setYawGP(GraphPloter g) {
		this.yaw_gp = g;
	}
   	public void setPitchGP(GraphPloter g) {
		this.pitch_gp = g;
	}
   	public void setRollGP(GraphPloter g) {
		this.roll_gp = g;
	}
   	
   	private void updateGUI() {
//   		if (byteBuffer.size() >= 240) {
//			ArrayList<Integer> idxS = new ArrayList<>();
//			for (int idx = 0; idx<byteBuffer.size(); idx++) {
//				// put the idx of 'S' in byteBuffer to idxS
//				if ((char)(byte)byteBuffer.get(idx) == 'S') {
//					idxS.add(0);
//				}
//			}
//			if (idxS.size() == 0) {
//				// no 'S' in byteBuffer
//				byteBuffer.clear();
//			}
//			else if (idxS.size() == 1) {
//				// only one 'S' in byteBuffer, remove all useless bytes before 'S'
//				byteBuffer = new ArrayList<>(byteBuffer.subList(idxS.get(0), byteBuffer.size()));
//				if (byteBuffer.size() >= 240) {
//					parse(new ArrayList<>(byteBuffer.subList(0, 240)));
//				}
//			}
//			else { // idxS.size >= 1 
//				// there are many 'S' in byteBuffer
//				for (int idx = 0; idx<idxS.size()-1;idx++) {
//					int dif = idxS.get(idx) - idxS.get(idx + 1);
//					if (dif >= 240) {
//						parse(new ArrayList<>(byteBuffer.subList(idxS.get(idx), idxS.get(idx) + 240)));
//					}
//				}
//				byteBuffer = new ArrayList<>(byteBuffer.subList(idxS.get(idxS.size()-1), byteBuffer.size()));
//			}
//		}
   		if (byteBuffer.size() == 240) {
   			parse(byteBuffer);
   			byteBuffer.clear();
   		}
	}
   	
   	public void showByteBufferInTextField() {
   		showArea.setText(ByteBufferToString());
   	}

   	
   	public ArrayList<Float> ifCoordination() {
   		ArrayList<Float> res = new ArrayList<>();
   		int idx = 0;
   		for (int start = 0; start < byteBuffer.size() - 8; start++) {
   			if ((byteBuffer.get(start) == 1) && (byteBuffer.get(start +1) == 5)){
   				String x_1_str = Integer.toBinaryString(byteBuffer.get(start+2));
				String x_2_str = Integer.toBinaryString(byteBuffer.get(start+3));
				String y_1_str = Integer.toBinaryString(byteBuffer.get(start+4));
				String y_2_str = Integer.toBinaryString(byteBuffer.get(start+5));
				res.add((float) combineTwoBinaryStr(x_1_str, x_2_str));
				res.add((float) combineTwoBinaryStr(y_1_str, y_2_str));
				idx = start + 6;
   			}
   		}
		for (int i = 0; i < idx; i++) {
			byteBuffer.remove(0);
		}
		
		return res;
	}
   	
   	private Integer combineTwoBinaryStr(String a, String b) {
   		a = convertTo8Difit(a);
   		b = convertTo8Difit(b);
//   		System.out.println(a+" " + b);
   		return Utils.binaryToDecimal(a + b);
   	}
   	
   	private String convertTo8Difit(String a) {
   		for (int i = 0; i < 9-a.length(); i++) {
			a = "0" + a;
		}
   		return a;
   	}
   	
   	public void printByteBuffer() {
		System.out.println(ByteBufferToString());
	}
   	
   	private String ByteBufferToString() {
   		String str = "";
		int c = 0;
		
		for (byte i: byteBuffer) {
			if ((char) i != '\n') {
				str += (char) i;
				c ++;
				if (c==50) {
					str += "\n";
					c = 0;
				}
			}	
		}
		return str;
   	}
//   	D1777400
//   	S4901019A00011E85D14100B96E42DD1777400000000000FF7
//   	F40000000000000A039C41ACD42F23287C18491BCC20000000
//   	00000000000000000000000000000000000000000000000000
//   	000803F0000803F000000002C893C3A67DB2071C347A469D23
//   	B00000000203A0608203A83211E3A518C1B3A1CX
   	public void parse(ArrayList<Byte> a) {
   		
   		char start_char = (char) (byte) a.get(0);
   		if (a.size() != 240) {
   			LOGGER.info("size is not 240");
   			printByteBuffer();
   			return;
   		}
   		char end_char = (char) (byte) a.get(239);
		if (start_char != 'S') {
			LOGGER.info("Not start with S");
			printByteBuffer();
			return;
		}
		if (end_char != 'X') {
			LOGGER.info("Not end with X");
			printByteBuffer();
			return;
		}
		
		ArrayList<Byte> byteArray = new ArrayList<>();
		for (byte t:a.subList(1, 239)) {
			String string = Character.toString((char) t);
			byteArray.add((byte)Integer.parseInt(string, 16));
		}
		
		int curCMDCount = Utils.combineBytes(byteArray.get(0),byteArray.get(1));
		LOGGER.info("CMDCount: " + curCMDCount);
		if (curCMDCount == this.CMDCount) {
			LOGGER.info("CMDCount didn't change: " + curCMDCount);
			return;
		}
		this.CMDCount = curCMDCount;
		int CMDcountSub = Utils.combineBytes(byteArray.get(2),byteArray.get(3));
		LOGGER.info("CMDcountSub: " + CMDcountSub);
		int bytetemp;
		int IdxHex2Float;
		int valtemp;
		switch (CMDcountSub) {
			case 1:
				LOGGER.info("case 1");
	            //Double CO2val = 0.0;
	            bytetemp = Utils.combineBytesToUnsignInt(byteArray.get(4), byteArray.get(5));
	            valtemp =  bytetemp << 8;
	            bytetemp = Utils.combineBytesToUnsignInt(byteArray.get(6), byteArray.get(7));
	            valtemp |= bytetemp;
	            this.CO2val = valtemp;
	            this.co2_tf.setText(String.valueOf(this.CO2val));
	            
	            //Double TVOCval = 0.0;
	            bytetemp =  Utils.combineBytesToUnsignInt(byteArray.get(8), byteArray.get(9));
		        valtemp = (bytetemp<<8);
	            bytetemp = Utils.combineBytesToUnsignInt(byteArray.get(10), byteArray.get(11));
	            valtemp |= bytetemp;
	            this.TVOCval = valtemp;
	            this.TVOC_tf.setText(String.valueOf(this.TVOCval));
	        
	            
	            this.TempVal = Utils.convertToFloatFromBytes(( byteArray.subList(12, 20)));
	           
	            temperature_tf.setText(String.format("%.2f", this.TempVal));
	            this.HumidityVal = Utils.convertToFloatFromBytes(( byteArray.subList(20, 28)));
	            humi_tf.setText(String.format("%.2f", this.HumidityVal));
	            this.VbatVal = Utils.convertToFloatFromBytes(( byteArray.subList(28, 36)));
	           
	           
	            vbat_tf.setText(String.format("%.2f", this.VbatVal));
	            
	            this.yawVal = Utils.convertToFloatFromBytes(( byteArray.subList(36, 44)));
	            this.pitchVal = Utils.convertToFloatFromBytes(( byteArray.subList(44, 52)));
	            String s ="";
	            for (byte b:a.subList(45, 45+8)) {
	            	s+=(char)b;
	            }
	            System.out.println(s);
	            System.out.println(pitchVal);
	            
	            this.rollVal = Utils.convertToFloatFromBytes(( byteArray.subList(52, 60)));
	            yaw_gp.addPoint(yawVal);
	            pitch_gp.addPoint(pitchVal);
	            roll_gp.addPoint(rollVal);
//	            float yaw_biasVal = Utils.convertToFloatFromBytes(( byteArray.subList(60, 68)));
//	            
//	            float AccXval = Utils.convertToFloatFromBytes(( byteArray.subList(68, 76)));
//	            float AccYval = Utils.convertToFloatFromBytes(( byteArray.subList(76, 84)));
//	            float AccZval = Utils.convertToFloatFromBytes(( byteArray.subList(84, 92)));
//	            
	             speed_x = Utils.convertToFloatFromBytes(( byteArray.subList(100, 108)));
	            speed_y = Utils.convertToFloatFromBytes(( byteArray.subList(108, 116)));
	            this.speedx_TF.setText((String.format("%.2f", this.speed_x)));
	            this.speedy_TF.setText((String.format("%.2f", this.speed_y)));
	            this.position_x = Utils.convertToFloatFromBytes(( byteArray.subList(124, 132)));
	            this.position_y = Utils.convertToFloatFromBytes(( byteArray.subList(132, 140)));
	            MapPointManager mapPointManager = MapPointManager.getInstance();
	            comp.setPoints(mapPointManager.addPoint(this.position_x,this.position_y), mapPointManager.topPointString(), mapPointManager.bottomPointString());
	            
	            
	            this.p_matrix[0] = Utils.convertToFloatFromBytes(( byteArray.subList(148, 156)));
	            this.p_matrix[1] = Utils.convertToFloatFromBytes(( byteArray.subList(156, 164)));
	            this.p_matrix[2] = Utils.convertToFloatFromBytes(( byteArray.subList(164, 172)));
//	            float mag_angle = Utils.convertToFloatFromBytes(( byteArray.subList(172, 180)));
	            
	            this.airPressureVal = Utils.convertToFloatFromBytes(( byteArray.subList(184, 192)));
	            pressure_tf.setText((String.format("%.2f", this.airPressureVal)));
	            this.RMSSoundNoise = Utils.convertToFloatFromBytes(( byteArray.subList(192,200)));
	            RMSSoundNoiseTF.setText((String.format("%.5f", this.RMSSoundNoise)));
	            this.ambTempVal = Utils.convertToFloatFromBytes(( byteArray.subList(200, 208)));
	            amb_tf.setText((String.format("%.2f", this.ambTempVal)));
	            
				break;
			case 2:
	            this.RMSSoundNoise = Utils.convertToFloatFromBytes(( byteArray.subList(4,12)));
	            RMSSoundNoiseTF.setText((String.format("%.5f", this.RMSSoundNoise)));
	            
	            IdxHex2Float = 0;
	            for (int iqu = 0; iqu < 14; iqu++)
	            {
	                IdxHex2Float = 12 + 8 * iqu;
	                FSound[iqu] = Utils.convertToFloatFromBytes(( byteArray.subList(IdxHex2Float, IdxHex2Float+8)));
	            }	            
	          
	            for (int iqu = 0; iqu < 14; iqu++)
	            {
	            		IdxHex2Float = 124 + 8 * iqu;
	                ASound[iqu] = Utils.convertToFloatFromBytes(( byteArray.subList(IdxHex2Float, IdxHex2Float+8)));
	            }
				break;
			case 3:
	            for (int iqu = 0; iqu < 29; iqu++)
	            {
	                IdxHex2Float = 4 + 8 * iqu;
	                RMSNoiseSet[iqu] =Utils.convertToFloatFromBytes(( byteArray.subList(IdxHex2Float, IdxHex2Float+8)));
	            }
				break;
			case 4:
				IdxHex2Float = 4;
				this.airPressureVal = Utils.convertToFloatFromBytes(( byteArray.subList(IdxHex2Float, IdxHex2Float+8)));
				for (int iqu = 0; iqu < 14; iqu++){
	                IdxHex2Float = 12 + 8 * iqu;
	                this.Fpressure[iqu] = Utils.convertToFloatFromBytes(( byteArray.subList(IdxHex2Float, IdxHex2Float+8)));
	            }
	            // Amplitude
	            for (int iqu = 0; iqu < 14; iqu++)
	            {
	                IdxHex2Float = 124 + 8 * iqu;
	                this.Apressure[iqu] = Utils.convertToFloatFromBytes(( byteArray.subList(IdxHex2Float, IdxHex2Float+8))); 
	            }
				break;
			case 5:
				int CurrentPix;
	            bytetemp = Utils.combineBytesToUnsignInt(byteArray.get(234), byteArray.get(235));
	            valtemp =  bytetemp << 8;
	            bytetemp = Utils.combineBytesToUnsignInt(byteArray.get(236), byteArray.get(237));
	            valtemp |= bytetemp;
	            CurrentPix = valtemp;

	            CurrentPix = CurrentPix - 28;
	            
            		for (int iqu = 0; iqu < 28; iqu++){
                    IdxHex2Float = 4 + 8 * iqu;
                    if (CurrentPix < 1024)
                    {
                        this.IRimage32x32[CurrentPix] = Utils.convertToFloatFromBytes(( byteArray.subList(IdxHex2Float, IdxHex2Float+8)));
                        CurrentPix++;
                    }
                    
	            }
                if (CurrentPix > 1020){
                     irHeatMap.setData(IRimage32x32);
                }   
				break;
			default:
				break;
		}
		
   	}
}
