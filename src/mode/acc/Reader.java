package mode.acc;

import java.io.*;
import java.util.*;

public class Reader {

	double[] timeStamp;
	double[] magnitude;
	
	public Reader(String str) {//maxTime in minutes
		try {
			String line;
			BufferedReader bufRdr = new BufferedReader(new FileReader(new File(str)));
			List<Double> timeStampList = new ArrayList<Double>();
			List<Double> magnitudeList = new ArrayList<Double>();
			double twoNorm;
			double time;
			
			while ((line = bufRdr.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(line,",");

				time = Double.parseDouble(st.nextToken());
				timeStampList.add(time);
				
				twoNorm = Math.pow(Math.pow(Double.parseDouble(st.nextToken()),2) + Math.pow(Double.parseDouble(st.nextToken()),2) + Math.pow(Double.parseDouble(st.nextToken()),2),.5);
				magnitudeList.add(twoNorm);
			}
			
			timeStamp = new double[timeStampList.size()];
			magnitude= new double[timeStampList.size()];
			
			for (int i=0;i<timeStamp.length;i++) {
				timeStamp[i] = timeStampList.get(i);
				magnitude[i] = magnitudeList.get(i);
			}					
						
		} catch (IOException e) {
			System.out.println(e);
		}
	}	
}