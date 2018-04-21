package hmm;

import java.util.ArrayList;
import java.util.logging.Logger;

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
		float oneBlockWidth = (Parameters.MAP_MAX_X_COOR - Parameters.MAP_MIN_X_COOR) / blkMap.length;// in map coordination
		
		
	}
}
