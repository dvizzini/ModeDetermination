package mode.acc;

import java.util.*;

public class Interpolator {
	
	boolean success = false;
	double[] timeStamp;
	double[] magnitude;
	int period;
	
	public Interpolator (double[] timeStampRaw, double[] magnitudeRaw, int periodInput, int N, double maxTime) {

		List<Double> interpolatedList = new ArrayList<Double>();
		List<Double> indexedTimesList = new ArrayList<Double>();			
		final double TIME_ZERO = timeStampRaw[0];
		double timeIndex = TIME_ZERO;
		int index = 0;
		double timeBefore;
		double timeAfter;
		double magBefore;
		double magAfter;
		period = periodInput;
	
		while (timeIndex - TIME_ZERO < (int)maxTime*60000) {
			//interpolate
			interpolatedList.clear();
			indexedTimesList.clear();
			
			for (int i=0;i<N;i++) {
				if (index >= 0) {
					interpolatedList.add(magnitudeRaw[index]);//direct match
				} else {//not direct match, linearly interpolate
					timeBefore = timeStampRaw[-index - 2];
					if ((timeAfter = timeStampRaw[-index - 1]) > timeIndex + period) {break;}//check to be sure there is always something within a period to either side
					magBefore = magnitudeRaw[-index - 2];
					magAfter = magnitudeRaw[-index - 1];
					interpolatedList.add((magAfter-magBefore)/(timeAfter-timeBefore)*(timeIndex-timeBefore)+magBefore);
				}
				indexedTimesList.add(timeIndex);//direct match
				timeIndex = timeIndex + period;
				index = Arrays.binarySearch(timeStampRaw, timeIndex);
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
	}
}