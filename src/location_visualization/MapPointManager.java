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
   protected MapPointManager() {
      // Exists only to defeat instantiation.
   }
   public static MapPointManager getInstance() {
      if(instance == null) {
         instance = new MapPointManager();
      }
      return instance;
   }
   
   public ArrayList<Integer> addPoint(float x, float y) {
	allPoints.add(new MapPoint(x, y));
	LOGGER.info("Add new point:" + allPointsToString());
	ArrayList<Integer> res = new ArrayList<Integer>();
	res.add((int) Math.round(x * Parameters.MAP_PIXEL_MULTIPLIER + Parameters.MAP_MARGIN));
	res.add((int)Math.round(Parameters.MAP_MAXHEIGHT_COOR * Parameters.MAP_PIXEL_MULTIPLIER + Parameters.MAP_MARGIN
			- y * Parameters.MAP_PIXEL_MULTIPLIER));
	return res;
   }
   
   public ArrayList<Integer> getMapCoordination(float x, float y) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		res.add((int) Math.round(x * Parameters.MAP_PIXEL_MULTIPLIER + Parameters.MAP_MARGIN));
		res.add((int)Math.round(Parameters.MAP_MAXHEIGHT_COOR * Parameters.MAP_PIXEL_MULTIPLIER + Parameters.MAP_MARGIN
				- y * Parameters.MAP_PIXEL_MULTIPLIER));
		return res;
	   }
   
   public String allPointsToString() {
	StringBuilder sBuilder = new StringBuilder();
	for (int i = 0; i < allPoints.size(); i ++) {
		sBuilder.append(allPoints.get(i).getCoordination().toString());
	}
	return sBuilder.toString();
}
}
