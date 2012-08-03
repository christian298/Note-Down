package notedown;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import view.Console;

import music_notation.Chord;
import music_notation.MusicXML;
import music_notation.Note;

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
		
		// Initialize startindex and endindex for onset-detection
		int startIndex = 0;
		int endIndex = 0;
		
		// Inputfile
		String input = "/Users/christian/Music/Chords/Note(gdg)_H3F3C4.wav";
		
		// Switch recording on or off
		boolean withRecording = false;
		
		Console console = new Console();
		
		// Get available audio devices
		Mixer.Info[] aInfos = AudioSystem.getMixerInfo();
		console.showAvailibleAudioDevices(aInfos);
		
		// Set audio device for recording
		AudioCapture audioCapture = new AudioCapture(aInfos[1]);
		
		// If true start recording from audio device
		if(withRecording){
			// Start and stop recording
			System.out.println("Press ENTER to start the recording.");
			try {
				System.in.read();
				audioCapture.startRecording();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Press ENTER to stop the recording.");
			try {
				System.in.read();
				audioCapture.stopRecording();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Captured bytes: " + audioCapture.getAudioBytes().length);
		}
		
		

		// Initialize instance for onset detection
		OnsetDetection onsetDetection = new OnsetDetection(sampleSize);
		//PCP pcp = new PCP();
		//DFT dft = new DFT(sampleSize, samplingRate);

		// Read the audio file
		WaveDecoder decoder = null;
		WaveDecoder decoder2 = null;
		try {
			decoder = new WaveDecoder(new FileInputStream(input));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Initialize an fft instance
		FFT fft = new FFT(sampleSize, samplingRate);

		// Initialize an for music analysis
		MusicAnalyser musicAnalyser = new MusicAnalyser();

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
			onsetDetection.setAudioData(samples);
			//audioVec.add(fft.getSpectrum());
			onsetDetection.calculateSpectrumFlux(fft.getSpectrum());
			count++;
		}
		
		//System.out.println("Sample count: " + originalAudioSamples.size());

		// Find peaks/onsets an store them in a list
		List<Float> peakValues = onsetDetection.findPeaks();
		console.printPeaks(peakValues);
		ArrayList<Peak> peaks = onsetDetection.getPeaks();
		
		
		FFT fft2 = new FFT(sampleSize, samplingRate);
		fft2.window(FFT.HAMMING);
		
		MusicXML xml = new MusicXML();
		
		System.out.println("**** Segments with Peaks ****");
		for (int x = 0; x < peaks.size(); x++) {
			int sampleCount = 0;
			System.out.println("Start time:\t" + peaks.get(x).getStartTime());
			System.out.println("Start index:\t" + onsetDetection.getIndex(peaks.get(x).getStartTime()));
			startIndex = onsetDetection.getIndex(peaks.get(x).getStartTime());

			System.out.printf("%n");

			System.out.println("Ende time:\t" + peaks.get(x).getEndTime());
			System.out.println("End index:\t" + onsetDetection.getIndex(peaks.get(x).getEndTime()));
			endIndex = onsetDetection.getIndex(peaks.get(x).getEndTime());
			System.out.printf("%n");
			
			// Read samples from file
			try {
				decoder2 = new WaveDecoder(new FileInputStream(input));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			boolean moreThanOneSample = false;
			int counter = 0;
			
			while (decoder2.readSamples(samples2) > 0) {
				if (sampleCount >= startIndex && sampleCount <= endIndex) {
					
					
					// FFT for samples with chords
					fft2.forward(samples2);

					// Search for the notes in the sample
					musicAnalyser.getNotesInSignal(fft2);
					
					Similarity similarity = new Similarity();
					musicAnalyser.sortNoteStorage();
					
					console.printFoundNotes(musicAnalyser.getNoteStoreage());
					
					//System.out.println("first in store: "+ musicAnalyser.getNoteStoreage().get(0).getName());
					if(counter == (endIndex - startIndex)){
						// Find Chords and Notes
						//if (ma.wasChordPlayed()) {
						if (musicAnalyser.chordPlayed()) {
							similarity.cosineSimilarity(musicAnalyser.getChord(), musicAnalyser.getNoteStoreage());
							//similarity.hamming(musicAnalyser.getChord());
							Chord foundChord = similarity.getChordWithHighestSimilarity();
							System.out.println("Identified Chord: " + foundChord.getChordName());
							xml.writeChord(foundChord.getXMLFriendlyVector());
						} else {
							Iterator<Float> it = musicAnalyser.getMaxFrequencys().iterator();
							float last = 0f;
							while (it.hasNext()) {
								last = it.next();
							}
							//System.out.println("Note: " + musicAnalyser.getNameOfNote(last));
							if(musicAnalyser.maxAmp() != null){
								System.out.println("Identified note: " + musicAnalyser.maxAmp().getName());
								xml.writeNote(musicAnalyser.getNameOfNote(last), musicAnalyser.getOcteveOfNote(last));
							}
						}
						musicAnalyser.cleanNoteStorage();
						counter = 0;
					}
					//dft.performDFT2(samples2);
					//System.out.println("Storage: " + musicAnalyser.getNoteStoreage().size());
					
					Iterator<Note> nIt = musicAnalyser.getNoteStoreage().iterator();
					int noteCount = 0;
					while(nIt.hasNext() && noteCount < 4){
						Note n = nIt.next();
						System.out.println("Freq: " + n.getFrequency() + " Amp: " + n.getAmplitude());
						noteCount++;
						
					}
					
					if((endIndex - startIndex) >= 1){
						moreThanOneSample = true;
						counter++;
					} else{
						musicAnalyser.cleanNoteStorage();
					}
					
					/*if(counter == (endIndex - startIndex)){
						ma.cleanNoteStorage();
						counter = 0;
					}*/
					
					
					//ma.cleanNoteStorage();
					
				}
				sampleCount++;
			}
		}
		xml.finalize();
	}
}
