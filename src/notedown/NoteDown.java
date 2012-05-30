package notedown;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import music_notation.MusicXML;

import jm.util.Read;
import capture.AudioCapture;
import capture.WaveDecoder;
import dsp.FFT;
import dsp.PCP;

public class NoteDown {

	public static void main(String[] args) {
		int sampleSize = 16384;
		int samplingRate = 44100;
		float[] samples = new float[sampleSize];
		float[] tmpSamples = new float[sampleSize];
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
		
		//float data[] = Read.audio("/Users/christian/Music/e1.wav");
		
		System.out.println("Audio byte length: " + ac.getAudioBytes().length);

		// Initialize instance for onset detection
		OnsetDetection od = new OnsetDetection(sampleSize);
		PCP p = new PCP();

		// Read the audio file
		WaveDecoder decoder = null;
		try {
			decoder = new WaveDecoder(new FileInputStream("/Users/christian/Music/chord_c.wav"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Initialize an fft instance 
		FFT fft = new FFT(sampleSize, samplingRate);
		MusicAnalyser ma = new MusicAnalyser();

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
		
		
		FFT fft2 = new FFT(sampleSize, samplingRate);
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
				ma.getNotesInSignal(fft2);
				p.createProfile(fft2, sampleSize);
			}				
		}
		
		Iterator it = ma.getMaxFrequencys().iterator();
		float last = 0f;
	    while (it.hasNext()) {
	    	last = (float) it.next();
	        System.out.println("Max: " + last);	        
	    }
	    Similarity s = new Similarity();
	    // Find Chords/Notes
	    //if(ma.wasChordPlayed()){
	    	for(int c = 0; c < ma.getChord().length; c++){
	    		//System.out.print(" " + ma.getChord()[c]);
	    		s.cosineSimilarity(ma.getChord());
	    	}
	    //} else{
	    //	System.out.println("Note: " + ma.getNameOfNote(last));
	    //}
	    
	    
		try {
			MusicXML xml = new MusicXML();
			xml.writeNote("3", "5");
			xml.finalize();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {			
			e.printStackTrace();
		}
		
	}
}
