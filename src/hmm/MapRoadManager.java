package hmm;

import java.util.ArrayList;
import java.util.PrimitiveIterator.OfDouble;
import java.util.logging.Logger;

import javax.xml.stream.events.EndDocument;

import param.Parameters;

public class MapRoadManager {
	private ArrayList<MapRoad> allRoads;
	private static MapRoadManager instance = null;
	private final static Logger LOGGER = Logger.getLogger(MapRoadManager.class.getName());
	
	public class MapRoad{
		private float[] starting_coor = new float[2];
		private float[] end_coor = new float[2];
		private String direction;
		private float roadWidth;
		
		public MapRoad(float[] s, float[] e, String d, float w) {
			this.direction = d;
			this.starting_coor = s;
			this.end_coor = e;
			this.roadWidth = w;
		}
		
		public float getRoadWidth() {
			return this.roadWidth;
		}
		public float[] getStartingCoor() {
			return this.starting_coor;
		}
		
		public float[] getEndingCoor() {
			return this.end_coor;
		}
		
		public String getDirection() {
			return this.direction;
		}
		
		public float getDistanceFromPointToRoad(float x, float y) {
			if (this.direction == Parameters.ROAD_HORIZONTAL) {
				if ((x<this.end_coor[0]) & (x>this.starting_coor[0])){
					return Math.abs(y-this.starting_coor[1]);
				}
				else {
					float x_dif = Math.min(Math.abs(x-this.end_coor[0]), Math.abs(x-this.starting_coor[0]));
					float y_dif = Math.abs(y-this.starting_coor[1]);
					return (float) Math.sqrt(x_dif * x_dif + y_dif*y_dif);
				}
			}
			else { // Vertical Road
				if ((y<this.end_coor[1]) & (y>this.starting_coor[1])){
					return Math.abs(x-this.starting_coor[0]);
				}
				else {
					float x_dif = Math.abs(x-this.starting_coor[0]);
					float y_dif = Math.min(Math.abs(y-this.end_coor[1]), Math.abs(y-this.starting_coor[1]));
					return (float) Math.sqrt(x_dif * x_dif + y_dif*y_dif);
				}
			}
		}
	}
	private MapRoadManager() {
		this.allRoads = new ArrayList<>();
	}
	
	public static MapRoadManager getInstance() {
		if(instance == null) {
			instance = new MapRoadManager();
		}
		return instance;
	}
	
	public void addMapRoad(float[] a, float[] b, float w) {
		if (a[0] == b[0]) {
			if (a[1]>b[1]) {
				allRoads.add(new MapRoad(b, a, Parameters.ROAD_VERTICLE, w));
			}
			else {
				allRoads.add(new MapRoad(a, b, Parameters.ROAD_VERTICLE, w));
			}
		}
		else if(a[1] == b[1]) {
			if (a[0]>b[0]) {
				allRoads.add(new MapRoad(b, a, Parameters.ROAD_HORIZONTAL, w));
			}
			else {
				allRoads.add(new MapRoad(a, b, Parameters.ROAD_HORIZONTAL, w));
			}
		}
		else {
			LOGGER.warning("The Road is not horizontal not verticle, please check!");
		}
	}
	
	public ArrayList<MapRoad> getAllRoads(){
		return allRoads;
	}
	
	public void setAllRoads(int[][] blkMap) {
		int n = blkMap.length;
		float oneBlockWidth = (Parameters.MAP_MAX_X_COOR - Parameters.MAP_MIN_X_COOR) / blkMap.length;// in map coordination
		
		for (int i = 0; i<n;i++) {
			int start = -1;
			int stop = -1;
			for (int j=0;j<n;j++) {
				if (blkMap[i][j] == 1) {
					if (start == -1) {
						start = j;
					}
					stop = j;
				}
				else {
					if (start != -1) {
						if ((stop != -1) & (start!=stop)){
							//register 
							float centerY = (n-i)*oneBlockWidth -oneBlockWidth/2 + Parameters.MAP_MIN_Y_COOR;
			  	  			float x1 = start*oneBlockWidth + oneBlockWidth/2 + Parameters.MAP_MIN_X_COOR;
			  	  			float x2 = stop*oneBlockWidth + oneBlockWidth/2 + Parameters.MAP_MIN_X_COOR;
			  	  			registerNewRoad(x1, x2, centerY, centerY, oneBlockWidth);
						}
						start = -1;
						stop = -1;
					}
				}
			}
		}
		for (int i = 0; i<n;i++) {
			int start = -1;
			int stop = -1;
			for (int j=0;j<n;j++) {
				if (blkMap[j][i] == 1) {
					if (start == -1) {
						start = j;
					}
					stop = j;
				}
				else {
					if (start != -1) {
						if ((stop != -1) & (start!=stop)){
							//register 
							float centerX = i*oneBlockWidth +oneBlockWidth/2 + Parameters.MAP_MIN_X_COOR;
			  	  			float y1 = (n-start)*oneBlockWidth + oneBlockWidth/2 + Parameters.MAP_MIN_Y_COOR;
			  	  			float y2 = (n-stop)*oneBlockWidth + oneBlockWidth/2 + Parameters.MAP_MIN_Y_COOR;
			  	  			registerNewRoad(centerX, centerX, y1, y2, oneBlockWidth);
						}
						start = -1;
						stop = -1;
					}
				}
			}
		}
		
	}
	
	private void registerNewRoad(float x1, float x2, float y1, float y2, float oneBlockWidth) {
//		for (MapRoad aMapRoad:this.allRoads) {
//			if (aMapRoad.direction == Parameters.ROAD_HORIZONTAL) {
//				if (aMapRoad.starting_coor[0]==x1) {
//					if(aMapRoad.end_coor[0]==x2) {
//						if(aMapRoad.starting_coor[1] == y1 + oneBlockWidth) {
//							aMapRoad.roadWidth = aMapRoad.roadWidth+oneBlockWidth;
//							aMapRoad.starting_coor[1] = y1 + oneBlockWidth/2;
//							aMapRoad.end_coor[1] = y1 + oneBlockWidth/2;
//							return;
//						}
//					}
//				}
//			}
//			if (aMapRoad.direction == Parameters.ROAD_VERTICLE) {
//				if (aMapRoad.starting_coor[1]==y1) {
//					if(aMapRoad.end_coor[1]==y2) {
//						if(aMapRoad.starting_coor[0] == x1 - oneBlockWidth) {
//							aMapRoad.roadWidth = aMapRoad.roadWidth+oneBlockWidth;
//							aMapRoad.starting_coor[0] = x1 - oneBlockWidth/2;
//							aMapRoad.end_coor[0] = x1 - oneBlockWidth/2;
//							return;
//						}
//					}
//				}
//			}
//		}
		addMapRoad(new float[] {x1,y1}, new float[] {x2,y2}, oneBlockWidth);
	}
	
	private boolean contains(ArrayList<int[]> a, int[] l) {
		for (int[] i:a) {
			if ((i[0]==l[0]) & (i[1]==l[1])) {
				return true;
			}
		}
		return false;
	}
	
	private int find(ArrayList<int[]> a, int[] l) {
		for (int[] i:a) {
			if ((i[0]==l[0]) & (i[1]==l[1])) {
				return i[2];
			}
		}
		return -1;
	}
}
