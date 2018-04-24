package hmm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.management.InstanceAlreadyExistsException;
import javax.swing.text.StyledEditorKit.ForegroundAction;

import hmm.MapRoadManager.MapRoad;
import param.Parameters;
import hmm.MapRoadManager;


public class HMMModelManager {
	private Map<MapRoad, HashMap<MapRoad, Float>> roadToRoadPossibility;
	private static HMMModelManager instance;
	
	private HMMModelManager() {
		this.roadToRoadPossibility = new HashMap<MapRoad, HashMap<MapRoad, Float>>();
	}
	
	public static HMMModelManager getInstance() {
		if (instance == null) {
			instance = new HMMModelManager();
		}
		return instance;
	}
	
	public void setRoadToRoadPossibility() {
		MapRoadManager mapRoadManager = MapRoadManager.getInstance();
		ArrayList<MapRoad> allRoads = mapRoadManager.getAllRoads();
		for (int i=0; i < allRoads.size(); i++) {
			HashMap<MapRoad, Float> secondKey = new HashMap<>();
			for (int j=0; j<allRoads.size(); j++) {
				secondKey.put(allRoads.get(j), calculateRoadToRoadPossibility(allRoads.get(i), allRoads.get(j)));
			}
			roadToRoadPossibility.put(allRoads.get(i),secondKey);
		}
	}
	
	public 
	
	private float calculateRoadToRoadPossibility(MapRoad a, MapRoad b) {
		if (a==b) {
			// same road
			return (float)3/5;
		}
		else if (a.getDirection() != b.getDirection()) {
			// one of road is horizontal and another is vertical
			if (ifIntersect(a, b)){
				// intersect
				// directly connected
				return (float) 2/5;
			}
			return 0;
		}
		else if (ifInDirectConnected(a, b)) {// both of the road is horizontal or verical
			// indirectly connected by one road
			return (float) 1/5;
		}
		return 0;
	}
	
	
	private boolean ifInDirectConnected(MapRoad aMapRoad, MapRoad bMapRoad) {
		if(aMapRoad.getDirection() != bMapRoad.getDirection()) {
			return false;
		}
		MapRoadManager mapRoadManager = MapRoadManager.getInstance();
		ArrayList<MapRoad> allRoads = mapRoadManager.getAllRoads();
		boolean[] aConnectedRoads = new boolean[allRoads.size()];
		boolean[] bConnectedRoads = new boolean[allRoads.size()];

		
		for (int i=0; i < allRoads.size(); i++) {
			if (ifIntersect(aMapRoad, allRoads.get(i))) {
				aConnectedRoads[i] = true;
			}
			if (ifIntersect(bMapRoad, allRoads.get(i))) {
				bConnectedRoads[i] = true;
			}
		}
		for (int i=0; i < aConnectedRoads.length; i++) {
			if (aConnectedRoads[i]==true) {
				if(bConnectedRoads[i]==true) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	private boolean ifIntersect(MapRoad aMapRoad, MapRoad bMapRoad) {
		if (aMapRoad.getDirection() != bMapRoad.getDirection()){
			// one of road is horizontal and another is vertical
			MapRoad hMapRoad = bMapRoad;
			MapRoad vMapRoad = aMapRoad;
			if (aMapRoad.getDirection() == Parameters.ROAD_HORIZONTAL) {
				// a is horizontal, b is verticle
				hMapRoad = aMapRoad;
				vMapRoad = bMapRoad;	
			}
			if (inBetween(hMapRoad.getEndingCoor()[1], vMapRoad.getStartingCoor()[1], vMapRoad.getEndingCoor()[1])) {
				if (inBetween(vMapRoad.getEndingCoor()[0], hMapRoad.getStartingCoor()[0], hMapRoad.getStartingCoor()[0])) {
					// intersection
					return true;
				}
			}
		}
		return false;
	}
		
	private boolean inBetween(float a, float b, float c) {
		// return true if a is in between the range b to c
		float small = b;
		float big = c;
		if (b>c) {
			small = c;
			big = b;
		}
		if (a<small) {
			return false;
		}
		if (a>big) {
			return false;
		}
		return true;
	}
	
	
}
