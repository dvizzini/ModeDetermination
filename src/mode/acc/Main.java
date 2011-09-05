package mode.acc;

public class Main {
	
	public static void main(String[] args) throws Exception{
		Reader reader = new Reader("acc.csv");
		Interpolator interpolator= new Interpolator(reader.timeStamp, reader.magnitude, 200, 16, 2.5);
		double[] magnitude = interpolator.magnitude;
		double[] features = new Analyzer(magnitude, interpolator.period).features;
		for (int i=0;i<magnitude.length;i++) {
			System.out.println(("magnitude[").concat(Integer.toString(i)).concat("]: ").concat(Double.toString(magnitude[i])));			
		}
		for (int i=0;i<features.length;i++) {
			System.out.println(("features[").concat(Integer.toString(i)).concat("]: ").concat(Double.toString(features[i])));			
		}
	}
	
}