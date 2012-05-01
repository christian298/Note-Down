package notedown;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.xml.crypto.Data;

import capture.AudioCapture;
import dsp.DFT;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

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
		double date[] = dft.transform(ac.getAudio());
		System.out.println("date length before: " + date.length);
		
		//dft.performDFT2(date);
		DoubleFFT_1D fft = new DoubleFFT_1D(date.length);
		fft.realForward(date);
		System.out.println("date length after: " + date.length);
		/*for(int i = 0; i < date.length / 2; i++){
			System.out.println("Mag: " + Math.sqrt(Math.sqrt(date[2*i]) + Math.sqrt(date[2*i+1])));
		}*/
	}

}
