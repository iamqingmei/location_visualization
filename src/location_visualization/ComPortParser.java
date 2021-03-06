package location_visualization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import location_visualization.managers.MapPointManager;


public class ComPortParser {
	// This class parse all the command received 
	// show the parsed values to the corresponding text field or graph.
	// save the parsed values
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
//	private GraphPloter pitch_gp;
//	private GraphPloter roll_gp;
	private BarChartPloter amplitudeBar;
	private SingleLineGraphPloter amplitudePlot;
	private int CO2val, TVOCval;
	private float TempVal, HumidityVal, VbatVal, pitchVal, yawVal, rollVal, position_x,position_y,airPressureVal, ambTempVal,RMSSoundNoise;
	private float speed_x,speed_y;
    private float[] p_matrix = new float[3];
    private float[] FSound = new float[14];
    private float[] ASound = new float[14];
    private float[] RMSNoiseSet = new float[29];
    private float[] Fpressure = new float[14];
    private float[] Apressure = new float[14];
    private float[] IRimage32x32 = new float[1024];
    private StringBuilder saveContent = new StringBuilder();
    
	protected ComPortParser() {
	   	// Exists only to defeat instantiation.
		for (int i =0;i<1024;i++) {
			this.IRimage32x32[i] = 0;
		}
		saveContent.append("TYPE,VALUE,TIMESTAMP");
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
   	
   	public void setAmplitudeBar(BarChartPloter a) {
   		this.amplitudeBar = a;
   	}
   	
   	public void setAmplitudePlot(SingleLineGraphPloter a) {
   		this.amplitudePlot = a;
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
//   	public void setPitchGP(GraphPloter g) {
//		this.pitch_gp = g;
//	}
//   	public void setRollGP(GraphPloter g) {
//		this.roll_gp = g;
//	}
   	
   	private void updateGUI() {
   		if (byteBuffer.size() >= 240) {
   			int firstSIdx = -1;
//			ArrayList<Integer> idxS = new ArrayList<>();
			for (int idx = 0; idx<byteBuffer.size(); idx++) {
				// put the idx of 'S' in byteBuffer to idxS
				if ((char)(byte)byteBuffer.get(idx) == 'S') {
//					idxS.add(0);
					firstSIdx = idx;
					break;
				}
			}
			if (firstSIdx != -1) {
				if (byteBuffer.size() - firstSIdx >= 240) {
					if (parse(new ArrayList<>(byteBuffer.subList(firstSIdx, firstSIdx+240))) == true) {
						// it is a valid command
						// remove all the elements before the first 'S'.
						this.byteBuffer = new ArrayList<>(byteBuffer.subList(firstSIdx+240, byteBuffer.size()));
					}
					else {
						// it is not a valid command
						// remove all the elements before and indluding the first 'S'.
						this.byteBuffer = new ArrayList<>(byteBuffer.subList(firstSIdx+1, byteBuffer.size()));
					}
				}
			}else {
				// there is no 'S' in the byteBuffer
				byteBuffer.clear();
			}
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
		}
//   		if (byteBuffer.size() == 240) {
//   			parse(byteBuffer);
//   			byteBuffer.clear();
//   		}
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
				c++;
				if (c>=39) {
					str+="\n";
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
   	public boolean parse(ArrayList<Byte> a) {
   		
   		char start_char = (char) (byte) a.get(0);
   		if (a.size() != 240) {
   			LOGGER.info("size is not 240");
   			printByteBuffer();
   			return false;
   		}
   		char end_char = (char) (byte) a.get(239);
		if (start_char != 'S') {
			LOGGER.info("Not start with S");
			printByteBuffer();
			return false;
		}
		if (end_char != 'X') {
			LOGGER.info("Not end with X");
			printByteBuffer();
			return false;
		}
		
		ArrayList<Byte> byteArray = new ArrayList<>();
		for (byte t:a.subList(1, 239)) {
			String string = Character.toString((char) t);
			byteArray.add((byte)Integer.parseInt(string, 16));
		}
		
		int curCMDCount = Utils.combineBytesToUnsignInt(byteArray.get(0),byteArray.get(1));
		LOGGER.info("CMDCount: " + curCMDCount);
		if (curCMDCount == this.CMDCount) {
			LOGGER.info("CMDCount didn't change: " + curCMDCount);
			return false;
		}
		this.CMDCount = curCMDCount;
		int CMDcountSub = Utils.combineBytesToUnsignInt(byteArray.get(2),byteArray.get(3));
		LOGGER.info("CMDcountSub: " + CMDcountSub);
		int bytetemp;
		int IdxHex2Float;
		int valtemp;
		long timestamp=new Timestamp(System.currentTimeMillis()).getTime();
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
	            this.saveContent.append("CO2,"+ String.valueOf(this.CO2val) + "," + timestamp);
	            
	            //Double TVOCval = 0.0;
	            bytetemp =  Utils.combineBytesToUnsignInt(byteArray.get(8), byteArray.get(9));
		        valtemp = (bytetemp<<8);
	            bytetemp = Utils.combineBytesToUnsignInt(byteArray.get(10), byteArray.get(11));
	            valtemp |= bytetemp;
	            this.TVOCval = valtemp;
	            this.TVOC_tf.setText(String.valueOf(this.TVOCval));
	            this.saveContent.append("TVOC,"+ String.valueOf(this.TVOCval) + "," + timestamp);
	            
	            this.TempVal = Utils.convertToFloatFromBytes(( byteArray.subList(12, 20)));
	            temperature_tf.setText(String.format("%.2f", this.TempVal));
	            this.saveContent.append("TEMPERATURE,"+ String.valueOf(this.TempVal) + "," + timestamp);
	            
	            this.HumidityVal = Utils.convertToFloatFromBytes(( byteArray.subList(20, 28)));
	            humi_tf.setText(String.format("%.2f", this.HumidityVal));
	            this.saveContent.append("HUMIDITY,"+ String.valueOf(this.HumidityVal) + "," + timestamp);
	            
	            this.VbatVal = Utils.convertToFloatFromBytes(( byteArray.subList(28, 36)));
	            vbat_tf.setText(String.format("%.2f", this.VbatVal));
	            this.saveContent.append("VBATTERY,"+ String.valueOf(this.VbatVal) + "," + timestamp);
	            
	            this.yawVal = Utils.convertToFloatFromBytes(( byteArray.subList(36, 44)));
	            this.pitchVal = Utils.convertToFloatFromBytes(( byteArray.subList(44, 52)));
	            this.rollVal = Utils.convertToFloatFromBytes(( byteArray.subList(52, 60)));
	            
//	            String s ="";
//	            for (byte b:a.subList(45, 45+8)) {
//	            	s+=(char)b;
//	            }
//	            System.out.println(s);
//	            System.out.println(pitchVal);
	            
	            yaw_gp.addPoint(yawVal,'y');
	            yaw_gp.addPoint(pitchVal,'p');
	            yaw_gp.addPoint(rollVal,'r');
	            this.saveContent.append("YAW,"+ String.valueOf(this.yawVal) + "," + timestamp);
	            this.saveContent.append("PITCH,"+ String.valueOf(this.pitchVal) + "," + timestamp);
	            this.saveContent.append("ROLL,"+ String.valueOf(this.rollVal) + "," + timestamp);
//	            pitch_gp.addPoint(pitchVal);
//	            roll_gp.addPoint(rollVal);
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
	            this.saveContent.append("SPEEDX,"+ String.valueOf(this.speed_x) + "," + timestamp);
	            this.saveContent.append("SPEEDY,"+ String.valueOf(this.speed_y) + "," + timestamp);
	            
	            this.position_x = Utils.convertToFloatFromBytes(( byteArray.subList(124, 132)));
	            this.position_y = Utils.convertToFloatFromBytes(( byteArray.subList(132, 140)));
	            MapPointManager mapPointManager = MapPointManager.getInstance();
	            mapPointManager.addPoint(this.position_x,this.position_y);
	            comp.setPoints(mapPointManager.getAllPointsPixelCoor(), mapPointManager.topPointString(), mapPointManager.bottomPointString());
	            this.saveContent.append("POSITIONX,"+ String.valueOf(this.position_x) + "," + timestamp);
	            this.saveContent.append("POSITIONY,"+ String.valueOf(this.position_y) + "," + timestamp);
	            
	            this.p_matrix[0] = Utils.convertToFloatFromBytes(( byteArray.subList(148, 156)));
	            this.p_matrix[1] = Utils.convertToFloatFromBytes(( byteArray.subList(156, 164)));
	            this.p_matrix[2] = Utils.convertToFloatFromBytes(( byteArray.subList(164, 172)));
//	            float mag_angle = Utils.convertToFloatFromBytes(( byteArray.subList(172, 180)));
	            
	            this.airPressureVal = Utils.convertToFloatFromBytes(( byteArray.subList(184, 192)));
	            pressure_tf.setText((String.format("%.2f", this.airPressureVal)));
	            this.saveContent.append("AIRPRESSURE,"+ String.valueOf(this.airPressureVal) + "," + timestamp);
	            
	            this.RMSSoundNoise = Utils.convertToFloatFromBytes(( byteArray.subList(192,200)));
	            RMSSoundNoiseTF.setText((String.format("%.5f", this.RMSSoundNoise)));
	            this.saveContent.append("RMS,"+ String.valueOf(this.RMSSoundNoise) + "," + timestamp);
	            
	            this.ambTempVal = Utils.convertToFloatFromBytes(( byteArray.subList(200, 208)));
	            amb_tf.setText((String.format("%.2f", this.ambTempVal)));
	            this.saveContent.append("AMB,"+ String.valueOf(this.ambTempVal) + "," + timestamp);
	            
				break;
			case 2:
	            this.RMSSoundNoise = Utils.convertToFloatFromBytes(( byteArray.subList(4,12)));
	            RMSSoundNoiseTF.setText((String.format("%.5f", this.RMSSoundNoise)));
	            this.saveContent.append("RMS,"+ String.valueOf(this.RMSSoundNoise) + "," + timestamp);
	            
	            IdxHex2Float = 0;
	            
	            for (int iqu = 0; iqu < 14; iqu++)
	            {
	                IdxHex2Float = 12 + 8 * iqu;
	                FSound[iqu] = Utils.convertToFloatFromBytes(( byteArray.subList(IdxHex2Float, IdxHex2Float+8)));
	                this.saveContent.append("FSound_"+iqu+","+ String.valueOf(FSound[iqu]) + "," + timestamp);
	            }	            
	            for (int iqu = 0; iqu < 14; iqu++)
	            {
	            		IdxHex2Float = 124 + 8 * iqu;
	                ASound[iqu] = Utils.convertToFloatFromBytes(( byteArray.subList(IdxHex2Float, IdxHex2Float+8)));
	                this.saveContent.append("ASound_"+iqu+","+ String.valueOf(ASound[iqu]) + "," + timestamp);
	            }
	            this.amplitudeBar.setSeriesSound(this.FSound, this.ASound);
				break;
			case 3:
	            for (int iqu = 0; iqu < 29; iqu++)
	            {
	                IdxHex2Float = 4 + 8 * iqu;
	                RMSNoiseSet[iqu] =Utils.convertToFloatFromBytes(( byteArray.subList(IdxHex2Float, IdxHex2Float+8)));
	                this.saveContent.append("RMSNoiseSet_"+iqu+","+ String.valueOf(RMSNoiseSet[iqu]) + "," + timestamp);
	                amplitudePlot.setSeries(RMSNoiseSet);
	            }
				break;
			case 4:
				IdxHex2Float = 4;
				this.airPressureVal = Utils.convertToFloatFromBytes(( byteArray.subList(IdxHex2Float, IdxHex2Float+8)));
	            pressure_tf.setText((String.format("%.2f", this.airPressureVal)));
	            this.saveContent.append("AIRPRESSURE,"+ String.valueOf(this.airPressureVal) + "," + timestamp);
	            
				for (int iqu = 0; iqu < 14; iqu++){
	                IdxHex2Float = 12 + 8 * iqu;
	                this.Fpressure[iqu] = Utils.convertToFloatFromBytes(( byteArray.subList(IdxHex2Float, IdxHex2Float+8)));
	                this.saveContent.append("Fpressure_"+iqu+","+ String.valueOf(Fpressure[iqu]) + "," + timestamp);
	            }
	            // Amplitude
	            for (int iqu = 0; iqu < 14; iqu++)
	            {
	                IdxHex2Float = 124 + 8 * iqu;
	                this.Apressure[iqu] = Utils.convertToFloatFromBytes(( byteArray.subList(IdxHex2Float, IdxHex2Float+8))); 
	                this.saveContent.append("Apressure_"+iqu+","+ String.valueOf(Apressure[iqu]) + "," + timestamp);
	            }
//	            this.amplitudeBar.setSeriesPressure(this.Fpressure, this.Apressure);
				break;
			case 5:
				int CurrentPix;
	            bytetemp = Utils.combineBytesToUnsignInt(byteArray.get(234), byteArray.get(235));
	            valtemp =  bytetemp << 8;
	            bytetemp = Utils.combineBytesToUnsignInt(byteArray.get(236), byteArray.get(237));
	            valtemp |= bytetemp;
	            CurrentPix = valtemp;

	            CurrentPix = CurrentPix - 28;

	            if (CurrentPix < 0) {
		            System.out.println(CurrentPix);
		            printByteBuffer();
		            return true;
	            }
	          
            	for (int iqu = 0; iqu < 28; iqu++){
                    IdxHex2Float = 4 + 8 * iqu;
                    if (CurrentPix < 1024)
                    {
                        this.IRimage32x32[CurrentPix] = Utils.convertToFloatFromBytes(( byteArray.subList(IdxHex2Float, IdxHex2Float+8)));
                        this.saveContent.append("IRimage32x32_"+CurrentPix+","+ String.valueOf(IRimage32x32[CurrentPix]) + "," + timestamp);
                        CurrentPix++;
                    }
                    
	            }
                if (CurrentPix > 1020){
                     irHeatMap.setData(IRimage32x32);
                     System.out.println(Arrays.toString(IRimage32x32));
                }   
				break;
			default:
				break;
		}
		return true;
   	}
   	
   	public void save() {
   		// save content and clear buffer
   		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        new File("./data/").mkdirs();
   		try (PrintWriter out = new PrintWriter("./data/" + sdf.format(timestamp)+".csv")) {
   		    out.println(saveContent.toString());
   	   		saveContent.setLength(0);
   	   		saveContent.append("TYPE,VALUE,TIMESTAMP");
   		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

   		
   	}
}
