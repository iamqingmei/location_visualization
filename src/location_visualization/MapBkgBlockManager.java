package location_visualization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import param.Parameters;

public class MapBkgBlockManager {
	static MapBkgBlockManager instance;
	private int[][] blkMap;
	private float blockPixelSize;
	private LinesComponent component;
	
	protected MapBkgBlockManager() throws IOException {
	      blkMap = new int[Parameters.MAP_BACKGOUND_BLOCK_NUM][Parameters.MAP_BACKGOUND_BLOCK_NUM];
	      blockPixelSize = (Parameters.MAP_PIXEL_MULTIPLIER * Parameters.MAP_MAXHEIGHT_COOR / Parameters.MAP_BACKGOUND_BLOCK_NUM);
	}
	
	
	public static MapBkgBlockManager getInstance(){
		if(instance == null) {
			try {
				instance = new MapBkgBlockManager();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	public void setLinesComponent(LinesComponent l) {
		this.component = l;
//		setBlocks();
	}
	
	public void setBlocks() {
		component.clearBlocks();
	    for (int c = 0; c < Parameters.MAP_BACKGOUND_BLOCK_NUM; c++) {
  	  		for (int i = 0; i < Parameters.MAP_BACKGOUND_BLOCK_NUM; i++) {
  	  			if (this.blkMap[c][i] == 1) {
			    	 component.addBlock((int) (i*blockPixelSize),(int) (c*blockPixelSize),(int)Math.ceil(blockPixelSize),(int)Math.ceil(blockPixelSize));
  	  			}
  	  		}
  	  		
		}
	}
	
	public void loadMapFromTXT(String f_path) throws IOException {
	      BufferedReader in = new BufferedReader(new FileReader(f_path));
	      String line;
	      for (int c = 0; c < Parameters.MAP_BACKGOUND_BLOCK_NUM; c++) {
	    	  		line = in.readLine();
	    	  		String[] tString = line.split(",");
	    	  		for (int i = 0; i < Parameters.MAP_BACKGOUND_BLOCK_NUM; i++) {
				    	 blkMap[c][i] = Integer.valueOf(tString[i]);
				}
			}
	      in.close();
	      setBlocks();
	}
	
	
}
