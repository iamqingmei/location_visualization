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
	LOGGER.info("Add new point:" + allPointsToString());
	fitInMapSize();
	
	ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
	for (MapPoint i : allPoints) {
		res.add(getMapCoordination(i.x, i.y));
	}
	return res;
   }
   
   private ArrayList<Integer> getMapCoordination(float x, float y) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		res.add((int) Math.round(x * Parameters.MAP_PIXEL_MULTIPLIER + Parameters.MAP_MARGIN));
		res.add((int)Math.round(Parameters.MAP_MAXHEIGHT_COOR * Parameters.MAP_PIXEL_MULTIPLIER + Parameters.MAP_MARGIN
				- y * Parameters.MAP_PIXEL_MULTIPLIER));
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
	
	private void turnMapPoint (MapPoint mp) {
		
		double distance = Math.sqrt(Math.pow(Math.abs(mp.x - this.pivot_point.x),2) + Math.pow(Math.abs(mp.y - this.pivot_point.y), 2));
		
		double new_degree;
		
		if ((mp.y - this.pivot_point.y) == 0){
			 new_degree = 0 + Math.toRadians(this.turning_degree);
		}
		else{
			 new_degree = Math.asin((mp.y - this.pivot_point.y) / distance) + Math.toRadians(this.turning_degree);
		}
		
//		System.out.println("Distance: " + distance);
//		System.out.println("new degree: " + new_degree * Math.PI / 180);
		mp.x =  (float) (distance * Math.cos(new_degree) + this.pivot_point.x);
		mp.y =  (float) (distance * Math.sin(new_degree) + this.pivot_point.y);

		
		System.out.println("[" + mp.x + ", " + mp.y +"]");
	}
	
	public ArrayList<ArrayList<Integer>> turn() {
		for (MapPoint mapPoint:allPoints) {
			turnMapPoint(mapPoint);
		}
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
			all_x.add(mapPoint.y);
		}
		float min_x = Utils.getMin(all_x);
		float max_x = Utils.getMax(all_x);
		float min_y = Utils.getMin(all_y);
		float max_y = Utils.getMax(all_y);
		
		
		// check range
		if ((max_x - min_x) > Parameters.MAP_MAXWIDTH_COOR) {
			float scale_x = ((max_x - min_x) / Parameters.MAP_MAXWIDTH_COOR);
			for (MapPoint mapPoint:allPoints) {
				mapPoint.x = (mapPoint.x - min_x) / scale_x + min_x;
			}
			max_x = Parameters.MAP_MAXWIDTH_COOR + min_x;
		}
		
		if ((max_y - min_y) > Parameters.MAP_MAXHEIGHT_COOR) {
			float scale_y = ((max_x - min_x) / Parameters.MAP_MAXWIDTH_COOR);
			for (MapPoint mapPoint:allPoints) {
				mapPoint.y = (mapPoint.y - min_y) / scale_y + min_y;
			}
			max_y = Parameters.MAP_MAXHEIGHT_COOR + min_y;
		}
			
		// check mini and max value
		float shift = 0;
		if (max_y > Parameters.MAP_MAXHEIGHT_COOR) {
			shift = max_y - Parameters.MAP_MAXHEIGHT_COOR;
			for (MapPoint mapPoint:allPoints) {
				mapPoint.y = mapPoint.y - shift;
			}
		}
		if (min_y < 0) {
			shift = min_y - 0;
			for (MapPoint mapPoint:allPoints) {
				mapPoint.y = mapPoint.y - shift;
			}
		}
		
		if (max_x > Parameters.MAP_MAXWIDTH_COOR) {
			shift = max_x - Parameters.MAP_MAXWIDTH_COOR;
			for (MapPoint mapPoint:allPoints) {
				mapPoint.x = mapPoint.x - shift;
			}
		}
		if (min_x < 0) {
			shift = min_x - 0;
			for (MapPoint mapPoint:allPoints) {
				mapPoint.x = mapPoint.x - shift;
			}
		}
	}
	

}
