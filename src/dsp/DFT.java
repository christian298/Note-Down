package dsp;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;

public class DFT extends Fourier{
	DFT(int ts, float sr) {
		super(ts, sr);
		// TODO Auto-generated constructor stub
	}

	private int _n;
	private float _sampleRate;
	private float _T;
	private float _h;

	public void performDFT(double[] x){		
		double f[] = new double [_n / 2];
		for(int j = 0; j < _n/2; j++){
			double firstSummation = 0;
			double secondSummation = 0;
			
			for(int k = 0; k < _n; k++){
				double twoPInjk = (2* Math.PI * j * k / _n);
				firstSummation += x[k] * Math.cos(twoPInjk);
				secondSummation += x[k] * Math.sin(twoPInjk);
			}
			f[j] = Math.abs(Math.sqrt(Math.pow(firstSummation, 2) + Math.pow(secondSummation, 2)));
			
			double amplitude = 2 * f[j] / _n;
			double frequency = j * _h / _T * _sampleRate;			
			System.out.println("amp: " + amplitude + "  freq: " + frequency);
		}
	}
	
	public void performDFT2(double[] x, int n){
		System.out.println("n: " + n);
		double f[] = new double [n / 2];
		double rex[] = new double[n / 2];
		double imx[] = new double[n / 2];
		int N = n;
		double frequency = 0;
		double magnitude[] = new double [n / 2];
		double maxMagnitude = 0.0;		
		double m[] = new double[12];
		for(int c = 0; c < 12; c++){
			m[c] = 0;
		}
		for(int k = 0; k < (n / 2); k++){
			rex[k] = 0;
			imx[k] = 0;
		}
		for(int k = 0; k < (n / 2); k++){
			for(int i = 0; i < n; i++){
				rex[k] = rex[k] + x[i] * Math.cos(2 * Math.PI * k * i / N);
				imx[k] = imx[k] - x[i] * Math.sin(2 * Math.PI * k * i / N);
			}
			frequency = (44100 * k) /n;
			//if(frequency > 0 && frequency < 455){
				magnitude[k] = (Math.sqrt(rex[k] * rex[k] + imx[k] * imx[k]));														
				System.out.println("Freq: " + frequency + " Mag: " + magnitude[k]);					
			//}
		}
		
		for(int y = 0; y < magnitude.length; y++){
			if(magnitude[y] >= maxMagnitude){
				maxMagnitude = magnitude[y];
				System.out.println("MaxMag: " + maxMagnitude + " Freq: " + (44100 * y) /n);				
			}
		}
		
				
	}

	@Override
	protected void allocateArrays() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBand(int i, float a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scaleBand(int i, float s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forward(float[] buffer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inverse(float[] buffer) {
		// TODO Auto-generated method stub
		
	}
}
