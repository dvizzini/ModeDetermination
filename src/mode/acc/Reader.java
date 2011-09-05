package mode.acc;

import java.io.*;
import java.util.*;

public class Reader {

	boolean success = false;
	double[] timeStamp;
	double[] magnitude;
	
	public Reader(String str, int period, int N, double maxTime) {//maxTime in minutes
		try {
			String line;
			BufferedReader bufRdr = new BufferedReader(new FileReader(new File(str)));
			List<Double> timeStampList = new ArrayList<Double>();
			List<Double> magnitudeList = new ArrayList<Double>();
			List<Double> interpolatedList = new ArrayList<Double>();
			List<Double> indexedTimesList = new ArrayList<Double>();			
			double twoNorm;
			double time;
			
			while ((line = bufRdr.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(line,",");

				time = Double.parseDouble(st.nextToken());
				timeStampList.add(time);
				
				twoNorm = Math.pow(Math.pow(Double.parseDouble(st.nextToken()),2) + Math.pow(Double.parseDouble(st.nextToken()),2) + Math.pow(Double.parseDouble(st.nextToken()),2),.5);
				magnitudeList.add(twoNorm);
			}	
			
			final double TIME_ZERO = timeStampList.get(0);
			double timeIndex = TIME_ZERO;
			int index = 0;
			double timeBefore;
			double timeAfter;
			double magBefore;
			double magAfter;

			while (timeIndex - TIME_ZERO < (int)maxTime*60000) {
				//interpolate
				interpolatedList.clear();
				indexedTimesList.clear();
				
				for (int i=0;i<N;i++) {
					if (index >= 0) {
						interpolatedList.add(magnitudeList.get(index));//direct match
					} else {//not direct match, linearly interpolate
						timeBefore = timeStampList.get(-index - 2);
						if ((timeAfter = timeStampList.get(-index - 1)) > timeIndex + period) {break;}//check to be sure there is always something within a period to either side
						magBefore = magnitudeList.get(-index - 2);
						magAfter = magnitudeList.get(-index - 1);
						interpolatedList.add((magAfter-magBefore)/(timeAfter-timeBefore)*(timeIndex-timeBefore)+magBefore);
					}
					indexedTimesList.add(timeIndex);//direct match
					timeIndex = timeIndex + period;
					index = Collections.binarySearch(timeStampList, timeIndex);
				}
				
				if (interpolatedList.size() == N) {
					//constuct arrays	
					timeStamp = new double[N];
					magnitude = new double[N];
					success = true;
					for (int i=0;i<N;i++) {
						timeStamp[i] = indexedTimesList.get(i);
						magnitude[i] = interpolatedList.get(i);
					}					
					break;
				}
			}
			
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	public double[] getTimeStamp() {
		if (this.success) {
			return timeStamp;		
		} else {
			return null;
		}
	}

	public double[] getMagnitude() {
		if (success) {
			return this.magnitude;		
		} else {
			return null;
		}
	}	
}