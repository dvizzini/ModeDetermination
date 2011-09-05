package mode.acc;

public class Main {
	
	public static void main(String[] args) throws Exception{
		double[] magnitude = new Reader("acc.csv", 200, 16, 2.0).getMagnitude();
		double[] features = new Analyzer(magnitude, 200).features;
		for (int i=0;i<magnitude.length;i++) {
			System.out.println(("magnitude[").concat(Integer.toString(i)).concat("]: ").concat(Double.toString(magnitude[i])));			
		}
		for (int i=0;i<features.length;i++) {
			System.out.println(("features[").concat(Integer.toString(i)).concat("]: ").concat(Double.toString(features[i])));			
		}
	}
	
}