package location_visualization;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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
	  
	  public static int combineBytesToUnsignInt(byte a, byte b) {
		return a << 4 | b;
	}
	  
	  public static byte combineBytes(byte a, byte b) {
			return (byte) (a << 4 | b);
	}
	
	  public static float convertToFloatFromBytes(List<Byte> thebyteArray) {
		    int[] arrayTemp = {0,0,0,0};
		    int bytetemp = combineBytesToUnsignInt(thebyteArray.get(0), thebyteArray.get(1));
		    arrayTemp[0] = bytetemp;
		    bytetemp = combineBytesToUnsignInt(thebyteArray.get(2), thebyteArray.get(3));
		    arrayTemp[1] = bytetemp;
		    bytetemp = combineBytesToUnsignInt(thebyteArray.get(4), thebyteArray.get(5));
		    arrayTemp[2] = bytetemp;
		    bytetemp = combineBytesToUnsignInt(thebyteArray.get(6), thebyteArray.get(7));
		    arrayTemp[3] = bytetemp;
			return toSingle(arrayTemp);
		}
//	  
//	  public static float convertToFloatFromBytes(Byte[] thebyteArray) {
//		  // the length of thebyteArray must be 8
//		  if (thebyteArray.length != 8) {
//			  return 0.0f;
//		  }
//		    byte[] arrayTemp = {0,0,0,0};//		    byte bytetemp = combineBytes(thebyteArray[0], thebyteArray[1]);
//		    arrayTemp[0] = bytetemp;
//		    bytetemp = combineBytes(thebyteArray[2], thebyteArray[3]);
//		    arrayTemp[1] = bytetemp;
//		    bytetemp = combineBytes(thebyteArray[4], thebyteArray[5]);
//		    arrayTemp[2] = bytetemp;
//		    bytetemp = combineBytes(thebyteArray[6], thebyteArray[7]);
//		    arrayTemp[3] = bytetemp;
//				return toSingle(arrayTemp);
//			}
//	  
//	  
//	  
//	  public static float toSingle(byte[] ba) {
//		ByteBuffer buf = ByteBuffer.wrap(ba);
//		float outp = buf.getFloat();
//		return outp;
//	}
	  public static float toSingle(int[] intA) {
		  int theInt = ((intA[3]& 0x7F) << 24)|(intA[2]<<16)|(intA[1]<<8)|(intA[0]);
		  float f = Float.intBitsToFloat(theInt);
		  if ((intA[3] & 0x80) > 0) {
			  f = f*-1;//		
		  }
		return f;
	}
}
