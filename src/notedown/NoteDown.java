package notedown;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

import jm.util.Read;
import capture.AudioCapture;
import capture.WaveDecoder;
import dsp.FFT;
import dsp.PCP;

public class NoteDown {

	public static void main(String[] args) {
		float[] samples = new float[16384];
		float[] tmpSamples = new float[16384];
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

		//float[] adata = ac.getAudioData(ac.getAudioFromFile());
		float data[] = Read.audio("/Users/christian/Music/e1.wav");

		System.out.println("Audio byte length: " + ac.getAudioBytes().length);

		// Initialize instance for onset detection
		OnsetDetection od = new OnsetDetection(16384);
		PCP p = new PCP();

		// Read the audio file
		WaveDecoder decoder = null;
		try {
			decoder = new WaveDecoder(new FileInputStream("/Users/christian/Music/e1.wav"));
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
			System.arraycopy(samples, 0, tmpSamples, 0, samples.length);
			originalAudioSamples.add(tmpSamples);
			
			// Perform FFT
			fft.forward(samples);
			originalAudioSamples.add(samples);
			// Calculate the spectrum flux for the given sample
			od.setAudioData(samples);						
			
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
		System.out.println("Peak size: " + peaks.size());
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
				ma.getNotesInSignal(fft2);
				//System.out.println("z: " + z);
				//p.createProfile(fft2, 16384);
				System.out.println("max: " + ma.getMaxFrequencys().size());
				System.out.println("name: " + ma.getNameOfNote(82.4069f));
			}				
		}
	}
}
