package location_visualization;

import java.util.ArrayList;
import java.util.logging.Logger;

import param.Parameters;

public class MapPointManager {
	private class MapPoint {
		private float x;
		private float y;
		
		public MapPoint(float theX, float theY) {
			this.x = theX;
			this.y = theY;
		}
		
		public ArrayList<Float> getCoordination() {
			ArrayList<Float> res = new ArrayList<> ();
			res.add(this.x);
			res.add(this.y);
			return res;
		}
	}

	private final static Logger LOGGER = Logger.getLogger(MapPointManager.class.getName());
   private static MapPointManager instance = null;
   private ArrayList<MapPoint> allPoints = new ArrayList<MapPoint>();
   private MapPoint pivot_point;
   private float turning_degree;
   private float scale = 1;
   
   private float start_coor = 0;
   private float end_coor = Parameters.MAP_MAXHEIGHT_COOR;
   
   float min_x = 0;
   float max_x = Parameters.MAP_MAXWIDTH_COOR;
   float min_y = 0;
   float max_y = Parameters.MAP_MAXHEIGHT_COOR;
   
   protected MapPointManager() {
      // Exists only to defeat instantiation.
   }
   public static MapPointManager getInstance() {
      if(instance == null) {
         instance = new MapPointManager();
      }
      return instance;
   }
   
   public ArrayList<ArrayList<Integer>> addPoint(float x, float y) {
	allPoints.add(new MapPoint(x, y));
//	LOGGER.info("Add new point:" + allPointsToString());
	fitInMapSize();
	
	ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
	for (MapPoint i : allPoints) {
		res.add(getMapCoordination(i.x, i.y));
	}

	return res;
   }
   
   private ArrayList<Integer> getMapCoordination(float x, float y) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		x = (x - start_coor) / scale;
		y = (y - start_coor) / scale;
		res.add((int) Math.round(x * Parameters.MAP_PIXEL_MULTIPLIER + Parameters.MAP_MARGIN));
		res.add((int)Math.round(Parameters.MAP_MAXHEIGHT_COOR * Parameters.MAP_PIXEL_MULTIPLIER + Parameters.MAP_MARGIN
				- y * Parameters.MAP_PIXEL_MULTIPLIER));
//		LOGGER.info(res.toString());
		return res;
	   }
   
   public String allPointsToString() {
	StringBuilder sBuilder = new StringBuilder();
	for (MapPoint mapPoint: allPoints) {
		sBuilder.append(mapPoint.getCoordination().toString());
	}
	return sBuilder.toString();
}
   
	public void setPivot(float theX, float theY) {
		this.pivot_point = new MapPoint(theX, theY);
	}
	
	public void setTurningDegree(float f) {
		this.turning_degree = f;
//		System.out.println("setTurningDegree" + f);
	}
	
	private MapPoint turnMapPoint (MapPoint mp) {
		
		double distance = Math.sqrt(Math.pow(Math.abs(mp.x - this.pivot_point.x),2) + Math.pow(Math.abs(mp.y - this.pivot_point.y), 2));
		
		double new_degree;
		
		if (distance == 0) {
			return mp;
		}
		new_degree = Math.asin(Math.abs(mp.y - this.pivot_point.y) / distance) - Math.toRadians(this.turning_degree);
		if (mp.x - this.pivot_point.x > 0) {
			if (mp.y - this.pivot_point.y < 0) {
				new_degree = Math.toRadians(270) + new_degree;
			}
		}
		else {
			if (mp.y - this.pivot_point.y < 0) {
				new_degree = Math.toRadians(180) + new_degree;
			}
			else {
				new_degree = Math.toRadians(90) + new_degree;
			}
		}
		System.out.println(new_degree);

		mp.x =  (float) (distance * Math.cos(new_degree) + this.pivot_point.x);
		mp.y =  (float) (distance * Math.sin(new_degree) + this.pivot_point.y);
		
		LOGGER.info(mp.getCoordination().toString());
		return mp;
	}
	
	public String bottomPointString() {
		return (start_coor + "," + start_coor);
	}
	
	public String topPointString() {
		return ((start_coor + Parameters.MAP_MAXWIDTH_COOR*scale) + "," + (start_coor + Parameters.MAP_MAXHEIGHT_COOR*scale));
	}
	
	
	public ArrayList<ArrayList<Integer>> turn() {
		
		for (int i = 0; i < allPoints.size(); i++) {
			allPoints.set(i, turnMapPoint(allPoints.get(i)));
		}
		LOGGER.info("turn: " + allPointsToString());
		fitInMapSize();
		
		ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
		for (MapPoint i : allPoints) {
			res.add(getMapCoordination(i.x, i.y));
		}
		return res;
	}
	
	private void fitInMapSize(){
		
		ArrayList<Float> all_x = new ArrayList<Float>();
		ArrayList<Float> all_y = new ArrayList<Float>();
		for (MapPoint mapPoint:allPoints) {
			all_x.add(mapPoint.x);
			all_y.add(mapPoint.y);
		}

		this.min_x = Utils.getMin(all_x);
		this.max_x = Utils.getMax(all_x);
		this.min_y = Utils.getMin(all_y);
		this.max_y = Utils.getMax(all_y);
		
		// check range
		if (max_x > max_y) {
			this.end_coor = max_x;
		}
		else {
			this.end_coor = max_y;
		}
		
		if (min_x < min_y) {
			this.start_coor = min_x;
		}
		else {
			this.start_coor = min_y;
		}
			
		this.scale = ((this.end_coor - this.start_coor) / Parameters.MAP_MAXHEIGHT_COOR);
		
//		LOGGER.info(mapParametersToString());
	}
	
	public String mapParametersToString() {
		return ("scale: " + this.scale + " start_coor: " + 
				this.start_coor + " min_x: " + this.min_x + 
				" max_x: " + this.max_x + " min_y: " + this.min_y + " max_y: " + 
				this.max_y);
	}

}
