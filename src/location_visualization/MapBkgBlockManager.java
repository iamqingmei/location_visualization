package location_visualization;

import java.io.BufferedReader;
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
	      BufferedReader in = new BufferedReader(new FileReader("../assets/blockMap.txt"));
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
		setBlocks();
	}
	
	private void setBlocks() {
	    for (int c = 0; c < Parameters.MAP_BACKGOUND_BLOCK_NUM; c++) {
  	  		for (int i = 0; i < Parameters.MAP_BACKGOUND_BLOCK_NUM; i++) {
			    	 component.addBlock((int) (i*blockPixelSize),(int) (c*blockPixelSize),(int)blockPixelSize,(int)blockPixelSize);
			}
		}
		
	}
	
	
}
