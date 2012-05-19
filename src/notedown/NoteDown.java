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
import dsp.PCP;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

public class NoteDown {

	public static void main(String[] args) {
		float[] samples = new float[16384];
		float[] samples2 = new float[1024];
		ArrayList<float[]> originalAudioSamples = new ArrayList<float[]>();		
		
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

		// Initialize instance for onset detection
		OnsetDetection od = new OnsetDetection(1024);
		PCP p = new PCP();

		// Read the audio file
		WaveDecoder decoder = null;
		try {
			decoder = new WaveDecoder(new FileInputStream("/Users/christian/Music/sin196.wav"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Initialize an fft instance 
		FFT fft = new FFT(16384, 44100);
		
		// Use Hamming-window for the signal
		fft.window(FFT.HAMMING);		
		int count = 0;
		// Read samples from audio file
		while (decoder.readSamples(samples) > 0) {
			// Store untouched samples
			//originalAudioSamples.add(samples);
			
			// Perform FFT
			fft.forward(samples);
			originalAudioSamples.add(samples);
			// Calculate the spectrum flux for the given sample
			od.setAudioData(samples);			
			p.createProfile(fft, 16384);
			
			od.calculateSpectrumFlux(fft.getSpectrum());		
			//System.out.println(count + " Power: " + fft.getFreq(87));
			count++;
		}
		System.out.println("Sample count: " + originalAudioSamples.size());
		
		
		// Find peaks/onsets an store them in a list
		od.findPeaks();
		ArrayList<Peak> peaks = od.getTimeOfPeaks();
		int startIndex = 0;
		int endIndex = 0;
		
		
		MusicAnalyser ma = new MusicAnalyser();
		FFT fft2 = new FFT(16384, 44100);
		fft2.window(FFT.HAMMING);
		
		for (int x = 0; x < peaks.size(); x++) {
			System.out.println("Start: " + peaks.get(x).getStartTime());
			System.out.println("Start index: " + od.getIndex(peaks.get(x).getStartTime()));
			startIndex = od.getIndex(peaks.get(x).getStartTime());

			System.out.println("Ende: " + peaks.get(x).getEndTime());
			System.out.println("End index: " + od.getIndex(peaks.get(x).getEndTime()));
			endIndex = od.getIndex(peaks.get(x).getEndTime());
			
			for(int z = startIndex; z <= endIndex; z++){							
				fft2.forward(originalAudioSamples.get(z));		
				//System.out.println("Pow: " + fft2.getBand(2));
				//ma.getNotesInSignal(fft2);
			}					
		}
	}
}
