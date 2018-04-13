package location_visualization.managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import location_visualization.LinesComponent;
import param.Parameters;

public class MapBkgBlockManager {
	static MapBkgBlockManager instance;
	private int[][] blkMap; // blkMap has shape of [blkMapSize][blkMapSize]
	private float blockPixelSize;
	private int totalBlkNumber=64;
	private LinesComponent component;
	
	protected MapBkgBlockManager() {
	      
	}
	
	private void setVariables() {
		blkMap = new int[totalBlkNumber][totalBlkNumber];
		blockPixelSize = (Parameters.MAP_SIZE / totalBlkNumber);
	}
   
	
	public static MapBkgBlockManager getInstance(){
		if(instance == null) {
			instance = new MapBkgBlockManager();
		}
		return instance;
	}
	
	public void setLinesComponent(LinesComponent l) {
		this.component = l;
//		setBlocks();
	}
	
	public void setBlocks() {
		component.clearBlocks();
	    for (int c = 0; c < totalBlkNumber; c++) {
  	  		for (int i = 0; i < totalBlkNumber; i++) {
  	  			if (this.blkMap[c][i] == 1) {
			    	 component.addBlock(Math.round(i*blockPixelSize)+ Parameters.MAP_MARGIN,Math.round(c*blockPixelSize)+ Parameters.MAP_MARGIN,(int)Math.ceil(blockPixelSize),(int)Math.ceil(blockPixelSize));
  	  			}
  	  		}
		}
	}
	
	public boolean loadMapFromTXT(String f_path,StringBuilder warning_msg) throws IOException {
		
	      BufferedReader in = new BufferedReader(new FileReader(f_path));
	      String line;
	    
	      line = in.readLine();
	      if (line.startsWith("blkMapSize: ")){
	    	  		line = line.substring("blkMapSize: ".length(),line.indexOf(";"));
	    	  		this.totalBlkNumber = Integer.valueOf(line);
	    	  		setVariables();
	      }
	      else {
	    	  in.close();
	    	  warning_msg.setLength(0);
	    	  warning_msg.append("Cannot get blkMapSize");
			return false;
		}
	      
	      line = in.readLine();
	      if (line.startsWith("min_x_coor: ")) {
	    	  		line = line.substring("min_x_coor: ".length(),line.indexOf(";"));
	    	  		Parameters.MAP_MIN_X_COOR = Float.parseFloat(line);
	      }
	      else {
	    	  in.close();
	    	  warning_msg.setLength(0);
	    	  warning_msg.append("Cannot get min_x_coor");
	    	  	return false;
	      }
	      
	      line = in.readLine();
	      if (line.startsWith("min_y_coor: ")) {
	    	  		line = line.substring("min_y_coor: ".length(),line.indexOf(";"));
	    	  		Parameters.MAP_MIN_Y_COOR = Float.parseFloat(line);
	      }
	      else {
	    	  in.close();
	    	  warning_msg.setLength(0);
	    	  warning_msg.append("Cannot get min_y_coor");
	    	  	return false;
	      }
	      
	      line = in.readLine();
	      if (line.startsWith("max_x_coor: ")) {
  	  		line = line.substring("max_x_coor: ".length(),line.indexOf(";"));
  	  		Parameters.MAP_MAX_X_COOR = Float.parseFloat(line);
		    }
		    else {
		    	in.close();
		    	  warning_msg.setLength(0);
		    	  warning_msg.append("Cannot get max_x_coor");
		  	  	return false;
		    }
	      
	      line = in.readLine();
	      if (line.startsWith("max_y_coor: ")) {
  	  		line = line.substring("max_y_coor: ".length(),line.indexOf(";"));
  	  		Parameters.MAP_MAX_Y_COOR = Float.parseFloat(line);
		    }
		    else {
		    	in.close();
		    	  warning_msg.setLength(0);
		    	  warning_msg.append("Cannot get max_y_coor");
		  	  	return false;
		    }
	      
	      if (Parameters.MAP_MAX_X_COOR - Parameters.MAP_MIN_X_COOR != Parameters.MAP_MAX_Y_COOR - Parameters.MAP_MIN_Y_COOR) {
	    		in.close();	
		    	  warning_msg.setLength(0);
		    	  warning_msg.append("It is not a square! max_y_coor - min_y_coor must equal to max_x_coor - min_x_coor!");
	    		return false;
	      }
	      
	      for(int c=0; c<totalBlkNumber;c++) {
	    	  		line = in.readLine();
	    	  		String[] tString = line.split(",");
	    	  		for (int i = 0; i < totalBlkNumber; i++) {
				    	 blkMap[c][i] = Integer.valueOf(tString[i]);
				}
			}
	      in.close();
	      setBlocks();
	      return true;
	}
	
}
