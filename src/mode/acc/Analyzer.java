package mode.acc;

public class Analyzer {
	
	public double[] features;
	
	public Analyzer(double[] magnitudeInput, int periodMilliseconds) {
				
		//avoid unit confusion (ms to s)
		double period = ((double)periodMilliseconds)/1000;

		//set N
		int N = (int)Math.pow(2,Math.ceil(Math.log(magnitudeInput.length)/Math.log(2)));
		double[] magnitude = new double[N];

		//get input into vector used in subsequent calcs
		for (int i=0;i<magnitudeInput.length;i++) {
			magnitude[i] = magnitudeInput[i];
		}
		
		//pad vector with zeros if necessary
		for (int i=magnitudeInput.length;i<N;i++) {
			magnitude[i] = 0;
		}

		//declare normalized array
		Complex[] magComplex = new Complex[N];
		double magMean = mean(magnitude);
		for (int i=0;i<N;i++){
			magComplex[i] = new Complex(magnitude[i] - magMean,(double)0);
		}
		
		//run fft and get amplitudes up to but not including Nyquist
		int NTrunc = N%2==0?N/2-1:N/2;
		double[] amp = new double[NTrunc];
		
		Complex[] magTransformed = FFT.fft(magComplex);
		for (int i=0;i<NTrunc;i++){
			amp[i] = magTransformed[i].abs();
		}
		
		//number of 1 Hz length subbands included in the bandwidth
		//If last band does not have a full 1 Hz, it is not counted
		int num1HzBins = (int)(1/(period*2));

		//number of transformed points in a 1 Hz band
		int binLen = (int)(N*period);
		
		//sum of Fourier amplitudes
		features = new double[num1HzBins+6];
		int j = binLen/2;
		for (int i=0;i<num1HzBins;i++){
			features[i] = 0;
			while (j<binLen*(i+1)+binLen/2){
				features[i] += amp[j];
				j++;
			}
		}
		
		//mean of magnitudes
		features[num1HzBins] = mean(magnitude);
		
		//normalized variance of magnitudes 
		features[num1HzBins+1] = var(magnitude)/max(magnitude)[0];
		
		//total energy (not divided by 2 pi)
		features[num1HzBins+2] = 0;
		for (int i=0;i<NTrunc;i++) {
			features[num1HzBins+2] += amp[i]*amp[i];
		}
		
		//sum of coefficients in [1 Hz,min(Nyquist, 5 Hz))
		int maxIndex = Math.min(5*binLen,amp.length);
		features[num1HzBins+3] = 0;
		for (int i=((N/period)%period==0?binLen-1:binLen);i<maxIndex;i++) {
			features[num1HzBins+3] += amp[i];
		}
		
		//location of max coefficient
		features[num1HzBins+4] = max(amp)[1];
		
		//normalized max coefficient
		features[num1HzBins+5] = max(amp)[0]/sum(amp);

	}
	
	public static double sum(double[] p) {//unbiased estimator of variance
	    double sum = 0;
	    for (int i=0; i<p.length; i++) {
	        sum += p[i];
	    }
	    return sum;
	}//end method var

	public static double[] max(double[] p) {
	    double[] forReturn = new double[2];
		double max= p[0];
	    int maxI = 0;
	    
	    for (int i=0; i<p.length; i++) {
	        if (p[i] > max) {
	        	max = p[i];
	        	maxI = i;
	        }
	    }
	    
	    forReturn[0] = max;
	    forReturn[1] = maxI;
	    return forReturn;
	}//end method max

	public static double mean(double[] p) {
	    double sum = 0;  // sum of all the elements
	    for (int i=0; i<p.length; i++) {
	        sum += p[i];
	    }
	    return sum / p.length;
	}//end method mean

	public static double var(double[] p) {//unbiased estimator of variance
	    double variance = 0;
	    double mean = mean(p);
	    for (int i=0; i<p.length; i++) {
	        variance += (p[i] - mean) * (p[i] - mean);
	    }
	    return variance / (p.length-1);
	}//end method var
}