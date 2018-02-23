package location_visualization;

import java.util.ArrayList;

import javax.swing.JTextArea;


public class ComPortParser {
	private ArrayList<Integer> intBuffer = new ArrayList<>();
	private static ComPortParser instance = null;
	private JTextArea showArea = null;
	
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
   	
   	public void appendInt(Integer a) {
   		intBuffer.add(a);
   		showArea.setText(intBuffer.toString());
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
//		for (int last= intBuffer.size()-1; last >= 7; last--) {
//			if ((intBuffer.get(last) == 10) && (intBuffer.get(last -1) == 13)){
//				String x_1_str = Integer.toBinaryString(intBuffer.get(last-8));
//				String x_2_str = Integer.toBinaryString(intBuffer.get(last-7));
//				String y_1_str = Integer.toBinaryString(intBuffer.get(last-6));
//				String y_2_str = Integer.toBinaryString(intBuffer.get(last-5));
//				res.add((float) combineTwoBinaryStr(x_1_str, x_2_str));
//				res.add((float) combineTwoBinaryStr(y_1_str, y_2_str));
//				idx = last + 1;
//			}
//		}
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
	}

}
