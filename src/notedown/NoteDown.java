package notedown;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import capture.AudioCapture;
import capture.WaveDecoder;
import dsp.DFT;
import dsp.FFT;
import dsp.Goertzel;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

public class NoteDown {

	public static void main(String[] args) {
		
		// Get available audio devices
		Mixer.Info[] aInfos = AudioSystem.getMixerInfo();
		for(int i = 0; i < aInfos.length; i++){
			System.out.println("Audio devices: " + aInfos[i]);
		}
		
		AudioCapture ac = new AudioCapture(aInfos[1]);
		
		// Start and stop recording
		System.out.println("Press ENTER to start the recording.");
		try {
			System.in.read();
			ac.startRecording();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Press ENTER to stop the recording.");
		try {
			System.in.read();
			ac.stopRecording();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		
		float[] adata = ac.getAudioData(ac.getAudioFromFile());
		//DFT dft = new DFT();
		//double audioData[] = dft.transform(ac.getAudio());		
		//dft.performDFT2(adata, 4096);
		System.out.println("Audio byte length: " + ac.getAudioBytes().length);
				
		//FFT fft = new FFT(1024, 44100);
		//double audioData[] = fft.transform(ac.getAudio());
		//fft.performFFT(adata);
		//fft.forward(adata);
		//for(int x = 0; x < 1024; x++){
			/*System.out.println("adataRe: " + adata[2*x]);
			System.out.println("adataIM: " + adata[2*x+1]);
			double mag = Math.pow(adata[2*x], 2) + Math.pow(adata[2*x+1], 2);
			System.out.println("Mag: " + mag);
			System.out.println("db: " + (10 * Math.log10((Math.abs(mag)))));*/
			//System.out.println("Values: " + adata[x]);
		//}
				
					
		
		//OnsetDetection os = new OnsetDetection(adata);
		//Goertzel g = new Goertzel(660, 1024, 1.0);
	     // System.out.println("G: " + (g.myAlgo(600.0f, 1024, samples)));
	      
		
		WaveDecoder decoder = null;
		try {
			decoder = new WaveDecoder( new FileInputStream( "/Users/christian/Music/silence.wav"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}							
	      FFT fft = new FFT( 1024, 44100 );
	      fft.window( FFT.HAMMING );
	      float[] samples = new float[1024];
	      float[] spectrum = new float[1024 / 2 + 1];
	      float[] lastSpectrum = new float[1024 / 2 + 1];
	      List<Float> spectralFlux = new ArrayList<Float>( );
	      List<Float> threshold = new ArrayList<Float>( );
	 
	      int x = 0;
	      while( decoder.readSamples( samples ) > 0 )
	      {	
	    	 System.out.println(x + " Samples length: " + samples.length);
	    	 x++;
	         fft.forward( samples );
	         //System.out.println("Freq: " + fft.getFreq(440.0f));
	         System.arraycopy( spectrum, 0, lastSpectrum, 0, spectrum.length ); 
	         System.arraycopy( fft.getSpectrum(), 0, spectrum, 0, spectrum.length );
	         
	          
	         float flux = 0;
	         for( int i = 0; i < spectrum.length; i++ )	
	         {
	            float value = (spectrum[i] - lastSpectrum[i]);
	            flux += value < 0? 0: value;
	         }
	         spectralFlux.add( flux );					
	      }		
	      /*for(int x = 0; x < spectralFlux.size(); x++){
	    	  System.out.println(x + " Flux: " + spectralFlux.get(x));
	      }*/
	}
}
