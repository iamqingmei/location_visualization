package location_visualization;

import java.util.ArrayList;

public class Utils {
	  // Method for getting the maximum value
	  public static int getMax(int[] inputArray){ 
	    int maxValue = inputArray[0]; 
	    for(int i=1;i < inputArray.length;i++){ 
	      if(inputArray[i] > maxValue){ 
	         maxValue = inputArray[i]; 
	      } 
	    } 
	    return maxValue; 
	  }
	 
	  // Method for getting the minimum value
	  public static int getMin(int[] inputArray){ 
	    int minValue = inputArray[0]; 
	    for(int i=1;i<inputArray.length;i++){ 
	      if(inputArray[i] < minValue){ 
	        minValue = inputArray[i]; 
	      } 
	    } 
	    return minValue; 
	  } 
	  
	// Method for getting the maximum value
	  public static float getMax(ArrayList<Float> inputArray){ 
	    float maxValue = inputArray.get(0); 
	    for(int i=1;i < inputArray.size();i++){ 
	      if(inputArray.get(i) > maxValue){ 
	         maxValue = inputArray.get(i); 
	      } 
	    } 
	    return maxValue; 
	  }
	 
	  // Method for getting the minimum value
	  public static float getMin(ArrayList<Float> inputArray){ 
	    float minValue = inputArray.get(0); 
	    for(int i=1;i<inputArray.size();i++){ 
	      if(inputArray.get(i) < minValue){ 
	        minValue = inputArray.get(i); 
	      } 
	    } 
	    return minValue; 
	  }
	  
	  public static String bytesToBinaryString( byte[] bytes )
	  {
	      StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
	      for( int i = 0; i < Byte.SIZE * bytes.length; i++ )
	          sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
	      return sb.toString();
	  }

	  
	  public static byte[] bytesFromBinaryString( String s )
	  {
	      int sLen = s.length();
	      byte[] toReturn = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
	      char c;
	      for( int i = 0; i < sLen; i++ )
	          if( (c = s.charAt(i)) == '1' )
	              toReturn[i / Byte.SIZE] = (byte) (toReturn[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
	          else if ( c != '0' )
	              throw new IllegalArgumentException();
	      return toReturn;
	  }
	  
	  public static Integer binaryToDecimal(String binary) {
		  return Integer.parseInt(binary, 2);
	  }
}
