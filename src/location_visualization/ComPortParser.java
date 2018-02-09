package location_visualization;

import java.util.ArrayList;


public class ComPortParser {
	private ArrayList<Integer> intBuffer = new ArrayList<>();
	private static ComPortParser instance = null;
	
	protected ComPortParser() {
	   	// Exists only to defeat instantiation.
   	}
   	public static ComPortParser getInstance() {
	   	if(instance == null) {
    	  	instance = new ComPortParser();
      	}
      	return instance;
   	}
   	
   	public void appendInt(Integer a) {
   		intBuffer.add(a);
	}
   	
   	public ArrayList<Float> ifCoordination() {
   		
   		ArrayList<Float> res = new ArrayList<>();
   		int idx = 0;
		for (int last= intBuffer.size()-1; last >= 9; last--) {
			if ((intBuffer.get(last) == 10) && (intBuffer.get(last -1) == 13)){
				String x_1_str = Integer.toBinaryString(intBuffer.get(last-8));
				String x_2_str = Integer.toBinaryString(intBuffer.get(last-7));
				String y_1_str = Integer.toBinaryString(intBuffer.get(last-6));
				String y_2_str = Integer.toBinaryString(intBuffer.get(last-5));
				res.add((float) combineTwoBinaryStr(x_1_str, x_2_str));
				res.add((float) combineTwoBinaryStr(y_1_str, y_2_str));
				idx = last + 1;
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
	}

}
