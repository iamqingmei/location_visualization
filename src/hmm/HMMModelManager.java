package hmm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import hmm.MapRoadManager.MapRoad;
import location_visualization.managers.MapPointManager.MapPoint;
import param.Parameters;
import hmm.MapRoadManager;


public class HMMModelManager {
	// This class is running all the HMM Model Calculation
	private Map<MapRoad, HashMap<MapRoad, Double>> roadToRoadPossibility;
	private static HMMModelManager instance;
	
	private HMMModelManager() {
		this.roadToRoadPossibility = new HashMap<MapRoad, HashMap<MapRoad, Double>>();
	}
	
	public static HMMModelManager getInstance() {
		if (instance == null) {
			instance = new HMMModelManager();
		}
		return instance;
	}
	
	public void setRoadToRoadPossibility() {
		// calculate the possibility matrix from Road to another Road
		// the possibility matrix is Map<MapRoad, HashMap<MapRoad, Double>> roadToRoadPossibility
		MapRoadManager mapRoadManager = MapRoadManager.getInstance();
		ArrayList<MapRoad> allRoads = mapRoadManager.getAllRoads();
		for (int i=0; i < allRoads.size(); i++) {
			HashMap<MapRoad, Double> secondKey = new HashMap<>();
			for (int j=0; j<allRoads.size(); j++) {
				secondKey.put(allRoads.get(j), calculateRoadToRoadPossibility(allRoads.get(i), allRoads.get(j)));
			}
			roadToRoadPossibility.put(allRoads.get(i),secondKey);
		}
	}
	private class HMMElement{
		// a HMMElement class
		// contains a observation, state and its viterbi value
		private MapPoint observation;
		private MapRoad state;
		private double v;
		public HMMElement(MapPoint a, MapRoad b, double v) {
			this.observation = a;
			this.state = b;
			this.v = v;
		}
		
		public MapPoint getObservation() {
			return this.observation;
		}
		
		public MapRoad getState() {
			return this.state;
		}
		
		public double getV() {
			return v;
		}
	}
	
	public ArrayList<float[]> adjustPoints(ArrayList<MapPoint> points){
		int count = 0; // the count of number points which has been processed
		float curRoadDistance;
		ArrayList<HMMElement> allHMMElement = new ArrayList<>(); 
		HashMap<HMMElement, HMMElement> thePreviousHMMElements = new HashMap<>(); // <curElement, prevElement>
		ArrayList<MapRoad> allRoads = MapRoadManager.getInstance().getAllRoads();
		Double[] preViterbiValues = new Double[allRoads.size()];
		float[] startPossibility = new float[allRoads.size()];
		
		// calculate startPossibility
		float start_X = points.get(0).getCoordination().get(0);
		float start_Y = points.get(0).getCoordination().get(1);
		for (int i=0;i<allRoads.size();i++) {
			curRoadDistance = allRoads.get(i).getDistanceFromPointToRoad(start_X, start_Y);
			startPossibility[i] = 1/curRoadDistance;
		}
		
		MapPoint prePoint = null; 
		for (MapPoint curPoint: points) {
			
			Double[] viterbi = new Double[allRoads.size()];
			float pointX = curPoint.getCoordination().get(0);
			float pointY = curPoint.getCoordination().get(1);
			
			double allRoadsToCurPointDistanceSum=0;
			
			for (MapRoad road:allRoads) {
				allRoadsToCurPointDistanceSum+=road.getDistanceFromPointToRoad(pointX, pointY);
			}
			double pRoadToPointSum = 0;
			for (MapRoad road:allRoads) {
				curRoadDistance = road.getDistanceFromPointToRoad(pointX, pointY);
				pRoadToPointSum += 1/curRoadDistance*allRoadsToCurPointDistanceSum;
			}
			if(count==0) {
				// it is the first point
				for (int i=0;i<allRoads.size();i++) {
					curRoadDistance = allRoads.get(i).getDistanceFromPointToRoad(pointX, pointY);
					double pRoadToPoint = 1/curRoadDistance*allRoadsToCurPointDistanceSum; // P(road|point)
					double pPointToRoad = pRoadToPoint/pRoadToPointSum; // P(point|road)
					viterbi[i]=pPointToRoad*startPossibility[i];
				}
				normalize(viterbi);
				for (int i=0;i<allRoads.size();i++) {
					HMMElement curEle = new HMMElement(curPoint, allRoads.get(i),viterbi[i]);
					allHMMElement.add(curEle);
					thePreviousHMMElements.put(curEle,null);
				}
				
			}
			else {
				int[] ancestorIdx = new int[allRoads.size()];
				for (int i=0;i<allRoads.size();i++) {
					curRoadDistance = allRoads.get(i).getDistanceFromPointToRoad(pointX, pointY);
					double pRoadToPoint = 1/curRoadDistance*allRoadsToCurPointDistanceSum; // P(road|point)
					double emit = pRoadToPoint/pRoadToPointSum;
					
					int maxJ = 0;
					double maxV = 0;
					
					for (int j=0;j<allRoads.size();j++) {
						//from road j to road i
						double trans = roadToRoadPossibility.get(allRoads.get(j)).get(allRoads.get(i));
						// P(point|road) = P(road|point)*P(point)/P(road)
						// P(point) is a constant value so ignore it.
						double preV = preViterbiValues[j];
						double curV = preV*trans*emit;
						if (curV>maxV) {
							maxJ=j;
							maxV=curV;
						}
					}
					viterbi[i] = maxV;
					ancestorIdx[i] = maxJ;
				}
				normalize(viterbi);
				
				for (int i=0;i<allRoads.size();i++) {
					HMMElement curEle = new HMMElement(curPoint, allRoads.get(i),viterbi[i]);
					allHMMElement.add(curEle);
					MapRoad ancescorRoad = allRoads.get(ancestorIdx[i]);
					thePreviousHMMElements.put(curEle, findMatchElement(allHMMElement, ancescorRoad, prePoint));
				}
			}
			prePoint = curPoint;
			preViterbiValues = viterbi;
			count++;
		}
		
		int maxJ = 0;
		double maxV = 0;
		// for the last point, find the road which has largest viterbi value
		for (int j=0;j<allRoads.size();j++) {
			if (preViterbiValues[j]>maxV) {
				maxJ=j;
				maxV=preViterbiValues[j];
			}
		}
		
		HMMElement[] resultPath = new HMMElement[points.size()];
		// Set the last point first
		// then retrieve back to the first point
		resultPath[points.size()-1] = findMatchElement(allHMMElement, allRoads.get(maxJ), prePoint);
		for (int i=resultPath.length-2;i>=0;i--) {
			resultPath[i] = thePreviousHMMElements.get(resultPath[i+1]);
		}
		
		// project each point to the desinated path
		ArrayList<float[]> result = new ArrayList<>();
		for (int i=0;i<resultPath.length;i++) {
			MapPoint curPoint = resultPath[i].getObservation();
			MapRoad curRoad = resultPath[i].getState();
			float x = curPoint.getCoordination().get(0);
			float y = curPoint.getCoordination().get(1);
			result.add(MapRoadManager.getInstance().projectPointTORoad(curRoad, new float[] {x,y}));
		}
		return result;
	}
	
	
	
	private HMMElement findMatchElement(ArrayList<HMMElement> allHMMElement, MapRoad mapRoad, MapPoint mapPoint) {
		for(HMMElement ele:allHMMElement) {
			if (ele.getObservation()==mapPoint) {
				if (ele.getState()==mapRoad) {
					return ele;
				}
			}
		}
		return null;
	}

	private double roadPossibility(MapRoad aMapRoad) {
		// calculate the possibility of the given road.
		ArrayList<MapRoad> allRoads = MapRoadManager.getInstance().getAllRoads();
		float curRoadLength = aMapRoad.getLength();
		float sum = 0;
		for (MapRoad temp:allRoads) {
			sum+=temp.getLength();
		}
		return curRoadLength/sum;
	}
	
	
	
	private void normalize (Double[] doubles) {
		// normalize the array,
		// hence the sum of the array becomes 1
		double sum = 0;
		for (Double d:doubles) {
			sum+=d;
		}
		for (int i=0;i<doubles.length;i++) {
			doubles[i] = doubles[i]/sum;
		}
	}
	
	private double calculateRoadToRoadPossibility(MapRoad a, MapRoad b) {
		// transition possibility
		if (a==b) {
			// same road
			return 3/5;
		}
		else if (a.getDirection() != b.getDirection()) {
			// one of road is horizontal and another is vertical
			if (ifIntersect(a, b)){
				// intersect
				// directly connected
				return 2/5;
			}
			return 0;
		}
		else if (ifInDirectConnected(a, b)) {// both of the road is horizontal or verical
			// indirectly connected by one road
			return 1/5;
		}
		return 0;
	}
	
	
	private boolean ifInDirectConnected(MapRoad aMapRoad, MapRoad bMapRoad) {
		// check of road A and road B can be connected by another road.
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
		// check if road A and B intersects with each other.
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
