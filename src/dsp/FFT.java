package dsp;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

public class FFT extends Fourier{
	public void performFFT(double[] audioData){
		DoubleFFT_1D fft = new DoubleFFT_1D(4096);
		fft.realForward(audioData);
		for(int i = 0; i < 4096 - 1; i++){
			double freq = (44100 * i) / 4096;
			if(freq > 200 && freq < 460){								
				System.out.println("freq: " + freq + " Mag: " + Math.sqrt(audioData[2*i] * audioData[2*i] + audioData[2*i+1] * audioData[2*i+1]));
			}
		}
	}
	
	public void performFFT2(double[] audioData){
		DoubleFFT_1D fft = new DoubleFFT_1D(10);
		fft.realForward(audioData);
		for(int i = 0; i < 10/2; i++){											
				System.out.println("Mag: " + Math.sqrt(audioData[2*i] * audioData[2*i] + audioData[2*i+1] * audioData[2*i+1]));
		}
	}
}
