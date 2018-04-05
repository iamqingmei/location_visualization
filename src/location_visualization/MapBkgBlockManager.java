package location_visualization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
	
	public boolean loadMapFromTXT(String f_path) throws IOException {
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
			return false;
		}
	      
	      line = in.readLine();
	      if (line.startsWith("min_coor: ")) {
	    	  		line = line.substring("min_coor: ".length(),line.indexOf(";"));
	    	  		Parameters.MAP_MINHEIGHT_COOR = Integer.valueOf(line);
	    	  		Parameters.MAP_MINWIDTH_COOR = Integer.valueOf(line);
	      }
	      else {
	    	  in.close();
	    	  	return false;
	      }
	      
	      line = in.readLine();
	      if (line.startsWith("max_coor: ")) {
  	  		line = line.substring("max_coor: ".length(),line.indexOf(";"));
  	  		Parameters.MAP_MAXHEIGHT_COOR = Integer.valueOf(line);
  	  		Parameters.MAP_MAXWIDTH_COOR = Integer.valueOf(line);
		    }
		    else {
		    	in.close();
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
