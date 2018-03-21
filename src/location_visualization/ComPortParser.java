package location_visualization;

import java.awt.List;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
	private JTextField RMSSoundNoiseTF;
	private LinesComponent comp;
	private int CMDCount = -99;
	private GraphPloter yaw_gp;
	private GraphPloter pitch_gp;
	private GraphPloter roll_gp;
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
   	
   	public void setvbatTF(JTextField a) {
		this.vbat_tf = a;
	}
   	
   	public void setRMSSoundNoiseTF(JTextField a) {
		this.RMSSoundNoiseTF = a;
	}
   	
   	public void appendByte(byte a) {
   		byteBuffer.add(a);
   		showByteBufferInTextField();
   		UpdateMap();
	}
   	
   	public void appendByteArray(byte[] a) {
		for (byte t: a) {
			byteBuffer.add(t);
		}
		showByteBufferInTextField();
		// TODO check S&X starting and ending string
		if (byteBuffer.size() == 240) {
			parse(byteBuffer);
			byteBuffer.clear();
		}
		
		numberOfReceivedata++;
		
		UpdateMap();
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
   	
   	private void UpdateMap() {
   		ArrayList<Float> res = ifCoordination();
		if( res.size() % 2 != 0){
			LOGGER.warning("Coordination has sth wrong!");
			return;
		}
		if (res.size() > 0) {
			MapPointManager mapPointManager = MapPointManager.getInstance();
			for (int i = 0; i < res.size() / 2; i++) {
				comp.setPoints(mapPointManager.addPoint(res.get(2*i), res.get(2*i + 1)), mapPointManager.topPointString(), mapPointManager.bottomPointString());
			}
		}
	}
   	
   	public void showByteBufferInTextField() {
   		String str = "";
   		int c = 0;
		for (byte i: byteBuffer) {
			str += (char) i;
			c ++;
			if (c==50) {
				str += "\n";
				c = 0;
			}
		}
   		showArea.setText(str);
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
		System.out.println(byteBuffer.toString());
		String str = "";
		int c = 0;
		
		for (byte i: byteBuffer) {
			str += (char) i;
			c ++;
			if (c==50) {
				str += "\n";
				c = 0;
			}
		}
		
		System.out.println(str);
	}
//   	D1777400
//   	S4901019A00011E85D14100B96E42DD1777400000000000FF7
//   	F40000000000000A039C41ACD42F23287C18491BCC20000000
//   	00000000000000000000000000000000000000000000000000
//   	000803F0000803F000000002C893C3A67DB2071C347A469D23
//   	B00000000203A0608203A83211E3A518C1B3A1CX
   	public void parse(ArrayList<Byte> a) {
   		double RMSSoundNoise = 0;
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
		
		ArrayList<Byte> byteArray = new ArrayList<>(a.subList(1, a.size()-1));
		
//		int curCMDCount = Integer.valueOf(Character.toString((char) (byte) byteArray.get(0)) + Character.toString((char) (byte) byteArray.get(1)));
		int curCMDCount = Utils.combineBytes(byteArray.get(0),byteArray.get(1));
		LOGGER.info("CMDCount: " + curCMDCount);
		if (curCMDCount == this.CMDCount) {
			LOGGER.info("CMDCount didn't change: " + curCMDCount);
			return;
		}
		this.CMDCount = curCMDCount;
//		int CMDcountSub = Integer.valueOf(Character.toString((char) (byte) byteArray.get(2)) + Character.toString((char) (byte) byteArray.get(3)));
		int CMDcountSub = Utils.combineBytes(byteArray.get(2),byteArray.get(3));
		LOGGER.info("CMDcountSub: " + CMDcountSub);
		byte bytetemp;
		switch (CMDcountSub) {
			case 1:
				LOGGER.info("case 1");
	            //Double CO2val = 0.0;
	            bytetemp = Utils.combineBytes(byteArray.get(4), byteArray.get(5));
	            int valtemp;
	            valtemp = bytetemp << 8;
	            bytetemp = Utils.combineBytes(byteArray.get(6), byteArray.get(7));
	            valtemp |= bytetemp;
	            double CO2val = (double) (valtemp);
	            co2_tf.setText(String.valueOf(CO2val));
	            
	            //Double TVOCval = 0.0;
	            bytetemp =  Utils.combineBytes(byteArray.get(8), byteArray.get(9));
		        valtemp = (bytetemp<<8);
	            bytetemp = Utils.combineBytes(byteArray.get(10), byteArray.get(11));
	            valtemp |= bytetemp;
	            double TVOCval =  (double) (valtemp);
	            TVOC_tf.setText(String.valueOf(TVOCval));
	        
	            
	            double TempVal = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(12, 20)));
	            temperature_tf.setText(String.format("%.2f", TempVal));
	            double HumidityVal = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(20, 28)));
	            humi_tf.setText(String.format("%.2f", HumidityVal));
	            double VbatVal = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(28, 36)));
	            vbat_tf.setText(String.format("%.2f", VbatVal));
	            
	            double yawVal = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(36, 44)));
	            double pitchVal = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(44, 52)));
	            double rollVal = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(52, 60)));
	            yaw_gp.addPoint(yawVal);
	            pitch_gp.addPoint(pitchVal);
	            roll_gp.addPoint(rollVal);
	            double yaw_biasVal = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(60, 68)));
	            
	            double AccXval = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(68, 76)));
	            double AccYval = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(76, 84)));
	            double AccZval = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(84, 92)));
	            
	            double speed_x = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(100, 108)));
	            double speed_y = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(108, 116)));
	            
	            double position_x = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(124, 132)));
	            double position_y = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(132, 140)));
	            MapPointManager mapPointManager = MapPointManager.getInstance();
	            comp.setPoints(mapPointManager.addPoint((float)position_x,(float)position_y), mapPointManager.topPointString(), mapPointManager.bottomPointString());
	            
	            double[] p_matrix = new double[3];
	            p_matrix[0] = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(148, 156)));
	            p_matrix[1] = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(156, 164)));
	            p_matrix[2] = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(164, 172)));
	            double mag_angle = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(172, 180)));
	            
	            double airPressureVal = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(184, 192)));
	            pressure_tf.setText((String.format("%.2f", airPressureVal)));
	            RMSSoundNoise = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(192,200)));
	            RMSSoundNoiseTF.setText((String.format("%.5f", RMSSoundNoise)));
	            double ambTempVal = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(200, 208)));
	            amb_tf.setText((String.format("%.2f", ambTempVal)));
	            
				break;
			case 2:
	            RMSSoundNoise = Utils.convertToFloatFromBytes(new ArrayList<Byte>( byteArray.subList(4,12)));
	            RMSSoundNoiseTF.setText((String.format("%.5f", RMSSoundNoise)));
				break;
			case 3:
	            // Frequency

				break;
			case 4:
				
				break;
			default:
				break;
		}
		
   	}
}
