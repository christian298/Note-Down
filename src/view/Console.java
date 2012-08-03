package view;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.sound.sampled.Mixer;

import music_notation.Note;

/**
 * Print messages to console
 * @author Christian Sandvoss
 *
 */
public class Console {
	
	/**
	 * Print values of found peaks
	 * @param peaks List with peaks
	 */
	public void printPeaks(List<Float> peaks){
		System.out.println("**** Found peak values ****");
		for (int y = 0; y < peaks.size(); y++) {
			System.out.println(y + "\t" + "Peaks:\t" + peaks.get(y));
		}
		System.out.printf("%n");
	}
	
	
	/**
	 * Print all available audio devices
	 * @param aInfos Array with found audio devices
	 */
	public void showAvailibleAudioDevices(Mixer.Info[] aInfos){
		System.out.println("**** All availible audio devices ****");
		for (int i = 0; i < aInfos.length; i++) {
			System.out.println(i + ". Audio devices: " + aInfos[i]);
		}
		System.out.printf("%n");
	}
	
	/**
	 * Print name, frequency and amplitude of note
	 * @param notes Vector with note objects
	 */
	public void printFoundNotes(Vector<Note> notes){
		System.out.printf("%n");
		System.out.println("**** Notes in signal ****");
		Iterator<Note> it = notes.iterator();
		
		while(it.hasNext()){
			Note n = it.next();
			System.out.println("Note: " + n.getName() + "\t Frequency: " + n.getFrequency() + "\t Amplitude: " + n.getAmplitude());
		}
		System.out.printf("%n");
	}
}
