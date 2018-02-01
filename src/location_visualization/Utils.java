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
}
