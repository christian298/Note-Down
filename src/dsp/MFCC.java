package dsp;

public class MFCC {
/*
	private int my_rint(double x){
		if(2*x == (double)this.my_rint(2*x)){
			x += 0.0001;
		}
		return (int)my_rint(x);
	}
	
	public MFCC(float[] signal){
		double lowestFrequency = 65.406391;
		double linearFilters = 13;
		double linearSpacing = 66.6666;
		double logFilters = 27;
		double logSpacing = 1.0711703;
		double fftSize = 512;
		double cepstralCoefficients = 13;
		double windowSize = 256;
		double samplingRate = 44100;
		double totalFilters,
	    mfccDCTMatrix,
	    preEmphasized,
	    first,
	    last,
	    empreinte,
	    maxi,
	    earMag, ceps, loga, windowStep, cols, frameRate = 100;
		
		int i, j, k;
		
		totalFilters = linearFilters + logFilters;
		double[] freqs = new double[(int) (totalFilters + 2)];
		
		for(i = 0; i < linearFilters; i++){
			freqs[i] = lowestFrequency + i * linearSpacing;
		}
		for(i = (int)linearFilters; i < (int)totalFilters + 2; i++){
			freqs[i] = freqs[(int)linearFilters - 1] * Math.pow(logSpacing, i - linearFilters + 1);
		}
		double[] lower = new double[(int) totalFilters];
		double[] upper = new double[(int) totalFilters];
		double[] center = new double[(int) totalFilters];
		double[] triangleHeight = new double[(int) totalFilters];
		double[] fftData = new double[(int) fftSize];
		for(i = 0; i < (int)totalFilters; i++){
			lower[i] = freqs[i];
		}
		for(i = 1; i < (int) totalFilters + 1; i++){
			center[i - 1] = freqs[i];
		}
		for(i = 2; i < (int)totalFilters + 2; i++){
			upper[i - 2] = freqs[i];
		}
		double[] hamWindow = new double[(int)windowSize];
		double[] mfccFilterWeights = new double[(int) (totalFilters * fftSize)];
		for (i = 0; i < totalFilters; i++){
			triangleHeight[i] = 2 / (upper[i] - lower[i]);
		}
		double[] fftFreqs = new double[(int)fftSize];
		for (i = 0; i < fftSize; i++){
			fftFreqs[i] = (i / fftSize) * samplingRate;
		}
		for (i = 0; i < totalFilters; i++){
		    for (j = 0; j < (int)fftSize; j++){
		      mfccFilterWeights[(int)(fftSize * i + j)] = ((fftFreqs[j] > lower[i]) && (fftFreqs[j] <= center[i]))
			* triangleHeight[i] * (fftFreqs[j] - lower[i]) / (center[i] - lower[i]) +
			((fftFreqs[j] > center[i]) && (fftFreqs[j] <upper[i])) * triangleHeight[i] * (upper[i] - fftFreqs[j]) /
			(upper[i] - center[i]);
		    }
		}
	}*/
}
