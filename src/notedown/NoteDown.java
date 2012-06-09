package notedown;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import music_notation.Chord;
import music_notation.MusicXML;

import jm.util.Read;
import capture.AudioCapture;
import capture.WaveDecoder;
import dsp.DFT;
import dsp.FFT;
import dsp.PCP;

public class NoteDown {

	public static void main(String[] args) {
		int sampleSize = 16384;
		int samplingRate = 44100;
		float[] samples = new float[sampleSize];
		float[] samples2 = new float[sampleSize];
		float[] tmpSamples = new float[sampleSize];
		ArrayList<float[]> originalAudioSamples = new ArrayList<float[]>();
		Vector<float[]> audioVec = new Vector<float[]>();

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

		System.out.println("Audio byte length: " + ac.getAudioBytes().length);

		// Initialize instance for onset detection
		OnsetDetection od = new OnsetDetection(sampleSize);
		PCP p = new PCP();
		DFT dft = new DFT(sampleSize, samplingRate);

		// Read the audio file
		WaveDecoder decoder = null;
		WaveDecoder decoder2 = null;
		try {
			decoder = new WaveDecoder(new FileInputStream("/Users/christian/Music/chord_cd.wav"));
			decoder2 = new WaveDecoder(new FileInputStream("/Users/christian/Music/chord_cd.wav"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Initialize an fft instance
		FFT fft = new FFT(sampleSize, samplingRate);

		// Initialize an for music analysis
		MusicAnalyser ma = new MusicAnalyser();

		// Use Hamming-window for the signal
		fft.window(FFT.HAMMING);

		int count = 0;

		// Read samples from audio file
		while (decoder.readSamples(samples) > 0) {
			// Store untouched samples
			// System.arraycopy(samples, 0, tmpSamples, 0, samples.length);
			// originalAudioSamples.add(tmpSamples);
			//audioVec.add(samples);

			// Perform FFT
			fft.forward(samples);
			// fftAudioSamples.add(samples);

			// Calculate the spectrum flux for the given sample
			od.setAudioData(samples);
			audioVec.add(fft.getSpectrum());
			od.calculateSpectrumFlux(fft.getSpectrum());
			// System.out.println(count + " Power C: " + fft.getFreq(130));
			// System.out.println(count + " Power D: " + fft.getFreq(146));
			count++;
		}
		System.out.println("Sample count: " + originalAudioSamples.size());

		// Find peaks/onsets an store them in a list
		od.findPeaks();
		ArrayList<Peak> peaks = od.getTimeOfPeaks();
		int startIndex = 0;
		int endIndex = 0;
		FFT fft2 = new FFT(sampleSize, samplingRate);
		fft2.window(FFT.HAMMING);

		/*
		for (int x = 0; x < peaks.size(); x++) {
			int sampleCount = 0;
			System.out.println("Start: " + peaks.get(x).getStartTime());
			System.out.println("Start index: " + od.getIndex(peaks.get(x).getStartTime()));
			startIndex = od.getIndex(peaks.get(x).getStartTime());

			System.out.println("Ende: " + peaks.get(x).getEndTime());
			System.out.println("End index: " + od.getIndex(peaks.get(x).getEndTime()));
			endIndex = od.getIndex(peaks.get(x).getEndTime());
			try {
				decoder2 = new WaveDecoder(new FileInputStream("/Users/christian/Music/chord_ae.wav"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			while (decoder2.readSamples(samples2) > 0) {
				if (sampleCount >= startIndex && sampleCount <= endIndex) {
					System.out.println("IN IF");
					fft2.forward(samples2);

					// Search for the notes included in the sample
					ma.getNotesInSignal(fft2);

					Similarity s = new Similarity();

					// Find Chords and Notes
					if (ma.wasChordPlayed()) {
						s.cosineSimilarity(ma.getChord());
						Chord foundChord = s.getChordWithHighestSimilarity();
						System.out.println("Chord: " + foundChord.getChordName());
					} else {
						Iterator<Float> it = ma.getMaxFrequencys().iterator();
						float last = 0f;
						while (it.hasNext()) {
							last = it.next();
						}
						System.out.println("Note: " + ma.getNameOfNote(last));
					}

					//dft.performDFT2(samples2);
				}
				sampleCount++;
			}
		}*/
		
		while (decoder2.readSamples(samples2) > 0) {			
			if(sampleCount == 15){
				fft2.forward(samples2);
				dft.performDFT2(samples2);
			}
			sampleCount++;
		}
		for (int x = 0; x < peaks.size(); x++) {
			startIndex = od.getIndex(peaks.get(x).getStartTime());
			endIndex = od.getIndex(peaks.get(x).getEndTime());
		  // Transform found samples 
		for(int z = startIndex; z <= endIndex;z++){ 
			
			float[] buffer = audioVec.elementAt(z); 
			
		  
		  //System.arraycopy(originalAudioSamples.get(z), 0, buffer, 0,originalAudioSamples.get(z).length); 
			//fft2.forward(buffer);
		  
		  // Search for the notes included in the sample
		  //ma.getNotesInSignal(fft2);
			ma.getNotesInSignal(buffer);
		  //p.createProfile(fft2, sampleSize);
		  Similarity s = new Similarity();
		  
		  // Find Chords and Notes 
		 if(ma.wasChordPlayed()){
			  s.cosineSimilarity(ma.getChord()); 
			  Chord foundChord = s.getChordWithHighestSimilarity(); 
			  System.out.println("Chord: " + foundChord.getChordName()); 
		  } else{ 
			  Iterator<Float> it = ma.getMaxFrequencys().iterator(); 
			  float last = 0f; 
			  while(it.hasNext()) { 
				  last = it.next(); 
			  } 
			  System.out.println("Note: " + ma.getNameOfNote(last)); 
		  } 
		}
		}
		
		 

		/*
		 * Similarity s = new Similarity(); // Find Chords and Notes
		 * if(ma.wasChordPlayed()){ s.cosineSimilarity(ma.getChord()); Chord
		 * foundChord = s.getChordWithHighestSimilarity();
		 * System.out.println("Chord: " + foundChord.getChordName()); } else{
		 * Iterator<Float> it = ma.getMaxFrequencys().iterator(); float last =
		 * 0f; while (it.hasNext()) { last = it.next(); }
		 * System.out.println("Note: " + ma.getNameOfNote(last)); }
		 */

		MusicXML xml = new MusicXML();

	}
}
