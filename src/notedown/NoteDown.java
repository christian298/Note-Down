package notedown;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

import capture.AudioCapture;
import fourier.DFT;

public class NoteDown {

	public static void main(String[] args) {
		// Get available audio devices
		Mixer.Info[] aInfos = AudioSystem.getMixerInfo();
		for(int i = 0; i < aInfos.length; i++){
			System.out.println("Audio devices: " + aInfos[i]);
		}
		
		AudioCapture ac = new AudioCapture(aInfos[2]);
		
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
		
		DFT dft = new DFT();
		int date[] = dft.transform(ac.getAudio());
		dft.performDFT2(date);
		
		 
	}

}
