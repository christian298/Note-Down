package dsp;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;

public class DFT {
	private int _n;
	private float _sampleRate;
	private float _T;
	private float _h;

	public double[] transform(AudioInputStream in){
		float sampleRate = in.getFormat().getSampleRate();
		System.out.println("Frame length: " + in.getFrameLength());
		float T = in.getFrameLength() / in.getFormat().getFrameRate();
		int n = (int) (T *  sampleRate) / 2;
		_n = n;
		_T = T;
		_sampleRate = sampleRate;
		float h = (T / n);
		_h = h;
		System.out.println("Sample rate: " + sampleRate);
		System.out.println("T: " + T);
		System.out.println("n: " + n);
		System.out.println("h: " + h);
		byte[] abytes = new byte[(int)in.getFrameLength() * in.getFormat().getFrameSize()];
		try {
			in.read(abytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean isEndian = in.getFormat().isBigEndian();
		
		double x[] = new double[n];
		
		for(int i = 0; i < n * 2; i += 2){
			int b1 = abytes[i];
			int b2 = abytes[1 + 1];
			if(b1 < 0) b1 += 0x100;
			if(b2 < 0) b2 += 0x100;
			
			int value;
			
			if (!isEndian){
				value = (b1 << 8) + b2;
			}else{
				value = b1 + (b2 << 8);
			}
			
			x[i/2] = value;
			
		}
		return x;
	}
	
	public static double lb( double x ){ 
	  return Math.log( x ) / Math.log( 2.0 ); 
	}
	
	public void performDFT(double[] x){
		double f[] = new double [_n / 2];
		for(int j = 0; j < _n/2; j++){
			double firstSummation = 0;
			double secondSummation = 0;
			
			for(int k = 0; k < _n; k++){
				double twoPInjk = (2* Math.PI * j * k / _n);
				firstSummation += x[k] * Math.cos(twoPInjk);
				secondSummation -= x[k] * Math.sin(twoPInjk);
			}
			f[j] = Math.abs(Math.sqrt(Math.pow(firstSummation, 2) + Math.pow(secondSummation, 2)));
			
			double amplitude = 2 * f[j] / _n;
			double frequency = j * _h / _T * _sampleRate;
			if(frequency > 220 && frequency < 250){
				System.out.println("amp: " + amplitude + "  freq: " + frequency);
			}
		}
	}
	
	public void performDFT2(double[] x){
		//double xx[] = new double[_n];
		double f[] = new double [_n / 2];
		double rex[] = new double[_n / 2];
		double imx[] = new double[_n / 2];
		int N = _n;
		double frequency = 0;
		double magnitude[] = new double [_n / 2];
		double maxMagnitude = 0.0;
		int fref = 440;
		double pk;
		double m[] = new double[12];
		for(int c = 0; c < 12; c++){
			m[c] = 0;
		}
		for(int k = 0; k < (_n / 2); k++){
			rex[k] = 0;
			imx[k] = 0;
		}
		for(int k = 0; k < (_n / 2); k++){
			for(int i = 0; i < _n; i++){
				rex[k] = rex[k] + x[i] * Math.cos(2 * Math.PI * k * i / N);
				imx[k] = imx[k] - x[i] * Math.sin(2 * Math.PI * k * i / N);
			}
			frequency = (_sampleRate * k) /_n;
			if(frequency > 220 && frequency < 255){
				magnitude[k] = (Math.sqrt(rex[k] * rex[k] + imx[k] * imx[k]));														
				System.out.println("Freq: " + frequency + " Mag: " + magnitude[k]);	
				pk = Math.round((12 * lb(frequency/fref)) % 12);
				if(pk == -1) {
					m[0] += 1;
				} else if (pk == -2) {
					m[1] += 1;
				} else if (pk == -3) {
					m[2] += 1;
				} else if (pk == -4) {
					m[3] += 1;
				} else if (pk == -5) {
					m[4] += 1;
				} else if (pk == -6) {
					m[5] += 1;
				} else if (pk == -7) {
					m[6] += 1;
				} else if (pk == -8) {
					m[7] += 1;
				} else if (pk == -9) {
					m[8] += 1;
				} else if (pk == -10) {
					m[9] += 1;
				} else if (pk == -11) {
					m[10] += 1;
				} else if (pk == -12) {
					m[11] += 1;
				}
				System.out.println("pk: " + pk);
			}
		}
		for(int y = 0; y < magnitude.length; y++){
			if(magnitude[y] >= maxMagnitude){
				maxMagnitude = magnitude[y];
				System.out.println("MaxMag: " + maxMagnitude + " Freq: " + (_sampleRate * y) /_n);				
			}
		}
		
				
	}
}
