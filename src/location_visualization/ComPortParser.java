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
	private ArrayList<Integer> intBuffer = new ArrayList<>();
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
   	
   	public void appendInt(Integer a) {
   		intBuffer.add(a);
   		showIntBufferInTextField();
   		UpdateMap();
	}
   	
   	public void appendIntArray(ArrayList<Integer> a) {
   		intBuffer.addAll(a);
   		parse(a);
   		numberOfReceivedata++;
   		showIntBufferInTextField();
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
   	
   	public void showIntBufferInTextField() {
   		String str = "";
   		int c = 0;
		for (int i: intBuffer) {
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
   		for (int start = 0; start < intBuffer.size() - 8; start++) {
   			if ((intBuffer.get(start) == 1) && (intBuffer.get(start +1) == 5)){
   				String x_1_str = Integer.toBinaryString(intBuffer.get(start+2));
				String x_2_str = Integer.toBinaryString(intBuffer.get(start+3));
				String y_1_str = Integer.toBinaryString(intBuffer.get(start+4));
				String y_2_str = Integer.toBinaryString(intBuffer.get(start+5));
				res.add((float) combineTwoBinaryStr(x_1_str, x_2_str));
				res.add((float) combineTwoBinaryStr(y_1_str, y_2_str));
				idx = start + 6;
   			}
   		}
		for (int i = 0; i < idx; i++) {
			intBuffer.remove(0);
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
   	
   	public void printIntBuffer() {
		System.out.println(intBuffer.toString());
		String str = "";
		int c = 0;
		
		for (int i: intBuffer) {
			str += (char) i;
			c ++;
			if (c==50) {
				str += "\n";
				c = 0;
			}
		}
		
		System.out.println(str);
	}
   	
   	public void parse(ArrayList<Integer> a) {
   		double RMSSoundNoise = 0;
   		char start_char = (char) ((int)a.get(0));
   		char end_char = (char) ((int)a.get(239));
		if (start_char != 'S') {
			LOGGER.info("Not start with S");
			return;
		}
		if (end_char != 'X') {
			LOGGER.info("Not end with X");
			return;
		}
		
		
		
		ArrayList<Byte> byteArray = new ArrayList<>();
		for (Integer i:a.subList(1, a.size()-1)) {
			byteArray.add((byte) (int) i);
		}
		
		int curCMDCount = combineNumbers((ArrayList<Integer>)a.subList(1, 3));
		LOGGER.info("CMDCount: " + curCMDCount);
		if (curCMDCount == this.CMDCount) {
			LOGGER.info("CMDCount didn't change: " + curCMDCount);
			return;
		}
		this.CMDCount = curCMDCount;
		
		
		int CMDcountSub = combineNumbers((ArrayList<Integer>)a.subList(3, 5));
		byte bytetemp;
		switch (CMDcountSub) {
			case 1:
				LOGGER.info("case 1");
	            //Double CO2val = 0.0;
	            bytetemp = combineBytes(byteArray.get(4), byteArray.get(5));
	            int valtemp;
	            valtemp = bytetemp << 8;
	            bytetemp = combineBytes(byteArray.get(6), byteArray.get(7));
	            valtemp |= bytetemp;
	            double CO2val = (double) (valtemp);
	            co2_tf.setText(String.valueOf(CO2val));
	            
	            //Double TVOCval = 0.0;
	            bytetemp =  combineBytes(byteArray.get(8), byteArray.get(9));
		        valtemp = (bytetemp<<8);
	            bytetemp = combineBytes(byteArray.get(10), byteArray.get(11));
	            valtemp |= bytetemp;
	            double TVOCval =  (double) (valtemp);
	            TVOC_tf.setText(String.valueOf(TVOCval));
	        
	            
	            double TempVal = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(12, 20));
	            temperature_tf.setText(String.format("%f2", TempVal));
	            double HumidityVal = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(20, 28));
	            humi_tf.setText(String.format("%f2", HumidityVal));
	            double VbatVal = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(28, 36));
	            vbat_tf.setText(String.format("%f2", VbatVal));
	            
	            double yawVal = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(36, 44));
	            double pitchVal = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(44, 52));
	            double rollVal = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(52, 60));
	            yaw_gp.addPoint(yawVal);
	            pitch_gp.addPoint(pitchVal);
	            roll_gp.addPoint(rollVal);
	            double yaw_biasVal = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(60, 68));
	            
	            double AccXval = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(68, 76));
	            double AccYval = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(76, 84));
	            double AccZval = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(84, 92));
	            
	            double speed_x = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(100, 108));
	            double speed_y = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(108, 116));
	            
	            double position_x = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(124, 132));
	            double position_y = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(132, 140));
	            MapPointManager mapPointManager = MapPointManager.getInstance();
	            comp.setPoints(mapPointManager.addPoint((float)position_x,(float)position_y), mapPointManager.topPointString(), mapPointManager.bottomPointString());
	            
	            double[] p_matrix = new double[3];
	            p_matrix[0] = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(148, 156));
	            p_matrix[1] = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(156, 164));
	            p_matrix[2] = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(164, 172));
	            double mag_angle = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(172, 180));
	            
	            double airPressureVal = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(184, 192));
	            pressure_tf.setText((String.format("%f2", airPressureVal)));
	            RMSSoundNoise = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(192,200));
	            RMSSoundNoiseTF.setText((String.format("%f5", RMSSoundNoise)));
	            double ambTempVal = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(200, 208));
	            amb_tf.setText((String.format("%f2", ambTempVal)));
	            
				break;
			case 2:
	            RMSSoundNoise = convertToFloatFromBytes((ArrayList<Byte>) byteArray.subList(4,12));
	            RMSSoundNoiseTF.setText((String.format("%f5", RMSSoundNoise)));
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
   	
   	private int combineNumbers(ArrayList<Integer> ints) {
   		int n = ints.size();
   		int res = 0;
   		for (int i = 1; i<=n; i++) {
   			res += ints.get(n-i) * (Math.pow(10, i-1)); 
   		}
		return res;
	}
   	
   	private byte combineBytes(byte a, byte b) {
		return (byte) ((byte)a << 4 | b);
	}
   	
   	private float convertToFloatFromBytes(ArrayList<Byte> thebyteArray) {
        byte[] arrayTemp = {0,0,0,0};
        byte bytetemp = combineBytes(thebyteArray.get(0), thebyteArray.get(1));
        arrayTemp[0] = bytetemp;
        bytetemp = combineBytes(thebyteArray.get(2), thebyteArray.get(3));
        arrayTemp[1] = bytetemp;
        bytetemp = combineBytes(thebyteArray.get(4), thebyteArray.get(5));
        arrayTemp[2] = bytetemp;
        bytetemp = combineBytes(thebyteArray.get(6), thebyteArray.get(7));
        arrayTemp[3] = bytetemp;
    		return toSingle(arrayTemp);
    	}
   	
   	private float toSingle(byte[] ba) {
		ByteBuffer buf = ByteBuffer.wrap(ba);
		float outp = buf.getFloat();
//		float f = ByteBuffer.wrap(ba).order(ByteOrder.LITTLE_ENDIAN).getFloat();
		return outp;
   	}
        
	

}
