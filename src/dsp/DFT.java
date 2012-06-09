package dsp;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;

public class DFT{
	
	private int n;
	private float samplingRate;
	private float _T;
	private float _h;

	public DFT(int sampleSize, float rate){
		this.n = sampleSize;
		this.samplingRate = rate;
	}
	
	public void performDFT(double[] x){		
		double f[] = new double [n / 2];
		for(int j = 0; j < n/2; j++){
			double firstSummation = 0;
			double secondSummation = 0;
			
			for(int k = 0; k < n; k++){
				double twoPInjk = (2* Math.PI * j * k / n);
				firstSummation += x[k] * Math.cos(twoPInjk);
				secondSummation += x[k] * Math.sin(twoPInjk);
			}
			f[j] = Math.abs(Math.sqrt(Math.pow(firstSummation, 2) + Math.pow(secondSummation, 2)));
			
			double amplitude = 2 * f[j] / n;
			double frequency = j * _h / _T * samplingRate;			
			System.out.println("amp: " + amplitude + "  freq: " + frequency);
		}
	}
	
	public void performDFT2(float[] x){
		float f[] = new float [this.n / 2];
		float rex[] = new float[this.n / 2];
		float imx[] = new float[this.n / 2];
		float frequency = 0f;
		float magnitude[] = new float[this.n / 2];
		float maxMagnitude = 0f;		
		float m[] = new float[12];

		for(int c = 0; c < 12; c++){
			m[c] = 0;
		}
		for(int k = 0; k < (this.n / 2); k++){
			rex[k] = 0;
			imx[k] = 0;
		}
		for(int k = 0; k < (this.n / 2); k++){
			for(int i = 0; i < this.n - 1; i++){
				rex[k] = (float) (rex[k] + x[i] * Math.cos(2 * Math.PI * k * i / this.n));
				imx[k] = (float) (imx[k] - x[i] * Math.sin(2 * Math.PI * k * i / this.n));
			}
			frequency = (44100 * k) / this.n;
			if(frequency > 0 && frequency < 455){
				magnitude[k] = (float) Math.pow((rex[k] * rex[k] + imx[k] * imx[k]), 0.5);														
				System.out.println("Freq: " + frequency + " Mag: " + magnitude[k]);					
			}
		}
		
		for(int y = 0; y < magnitude.length; y++){
			if(magnitude[y] >= maxMagnitude){
				maxMagnitude = magnitude[y];
				System.out.println("MaxMag: " + maxMagnitude + " Freq: " + (44100 * y) / this.n);				
			}
		}
		
				
	}
}
