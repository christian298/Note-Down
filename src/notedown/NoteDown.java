package notedown;

import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import capture.AudioCapture;
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
		double[] adata = ac.getAudioData(ac.getAudio());
		//DFT dft = new DFT();
		//double audioData[] = dft.transform(ac.getAudio());		
		//dft.performDFT2(adata, 4096);
				
		FFT fft = new FFT();
		//double audioData[] = fft.transform(ac.getAudio());
		fft.performFFT(adata);
		//for(int x = 0; x < 200; x++){
		//	System.out.println("adataRe: " + adata[2*x]);
		//	System.out.println("adataIM: " + adata[2*x+1]);
		//}	
		/*
		System.out.println("adata: " + audioData[80000]);
		for(int x = 2; x < 1024 / 2 - 1; x++){
			double e = Math.pow(audioData[x], 2);
			double hfc = Math.abs(e) * x;
			System.out.println("HFC: " + Math.abs(e) * x);
			double df = (hfc / hfc) * (hfc / e);
			System.out.println("df: " + df);
		}
		*/
		//Goertzel g = new Goertzel(110, 193536, 8.7);
		//System.out.println("G: " + g.getPower(audioData, 0));
		//OnsetDetection os = new OnsetDetection(audioData);
						
	}
}
