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
		for (int i = 0; i < aInfos.length; i++) {
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
		// DFT dft = new DFT();
		// double audioData[] = dft.transform(ac.getAudio());
		// dft.performDFT2(adata, 4096);
		System.out.println("Audio byte length: " + ac.getAudioBytes().length);

		// FFT fft = new FFT(1024, 44100);
		// double audioData[] = fft.transform(ac.getAudio());
		// fft.performFFT(adata);
		// fft.forward(adata);
		// for(int x = 0; x < 1024; x++){
		/*
		 * System.out.println("adataRe: " + adata[2*x]);
		 * System.out.println("adataIM: " + adata[2*x+1]); double mag =
		 * Math.pow(adata[2*x], 2) + Math.pow(adata[2*x+1], 2);
		 * System.out.println("Mag: " + mag); System.out.println("db: " + (10 *
		 * Math.log10((Math.abs(mag)))));
		 */
		// System.out.println("Values: " + adata[x]);
		// }

		OnsetDetection od = new OnsetDetection(1024);

		WaveDecoder decoder = null;
		try {
			decoder = new WaveDecoder(new FileInputStream(
					"/Users/christian/Music/a.wav"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		FFT fft = new FFT(1024, 44100);
		fft.window(FFT.HAMMING);
		float[] samples = new float[1024];
		ArrayList<float[]> full = new ArrayList<float[]>();		
		while (decoder.readSamples(samples) > 0) {
			full.add(samples);
			fft.forward(samples);
			od.setAudioData(samples);
			od.calculateSpectrumFlux(fft.getSpectrum());
		}
		od.findPeaks();
		ArrayList<Peak> peaks = od.getTimeOfPeaks();
		int start = 0;
		int end = 0;
		
		for (int x = 0; x < peaks.size(); x++) {
			System.out.println("Start: " + peaks.get(x).getStartTime());
			System.out.println("Start index: " + od.getIndex(peaks.get(x).getStartTime()));
			start = od.getIndex(peaks.get(x).getStartTime());

			System.out.println("Ende: " + peaks.get(x).getEndTime());
			System.out.println("End index: " + od.getIndex(peaks.get(x).getEndTime()));
			end = od.getIndex(peaks.get(x).getEndTime());
			
			float[] segment = new float[end - start]; 
			int i = 0;				 
			for(int s = start; s < end; s ++){ 
				  segment[i] = adata[s]; 
				  i++; 
			}
			System.out.println("adata length: " + adata.length);
			Goertzel g = new Goertzel(220, end - start, peaks.get(x).getEndTime() - peaks.get(x).getStartTime());
			System.out.println("goertzl_1: " + g.getPower(segment, 0));

		}
		 
	
		
		//Goertzel g2 = new Goertzel(207, end - start, 1.0);

		 //System.out.println("goertzl_2: " + g2.getPower(segment, 0));

	}
}
