package capture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioCapture extends Thread{
	// Receives audio data from mixer
	private TargetDataLine tdLine;
	// Format of the audio data
	private AudioFileFormat.Type audioFileFormatType;
	private AudioFormat audioFormat;
	private AudioInputStream audioInputStream;	 
	private Mixer.Info inputDevice;
	private File output;
	private File testFile;
	private boolean recording;
	private ByteArrayOutputStream out;
	private byte[] audioBytes;
	
	// Constructor, set initial values
	public AudioCapture(Mixer.Info input) {
		// Audioformat
		audioFileFormatType = AudioFileFormat.Type.WAVE;
		audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 1, 2, 44100.0F, false);
		
		// Input device (Mic, LineIn, ...)
		inputDevice = input;
		
		// Output file of the recording
		output = new File("/Users/christian/Music/recOutput");
		this.testFile = new File("/Users/christian/Music/sin440.wav");
	}
	
	// start recording from input device
	public void startRecording(){
		try {
			tdLine = AudioSystem.getTargetDataLine(audioFormat, inputDevice);
			tdLine.open(audioFormat);
			tdLine.start();
			audioInputStream = new AudioInputStream(tdLine);
			this.recording = true;
			super.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void stopRecording(){
		this.recording = false;
		tdLine.stop();
		tdLine.close();		
		try {
			this.out.flush();
			this.out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.audioBytes = this.out.toByteArray();
		System.out.println("AudioLenght: " + this.audioBytes.length);
	}
	
	// Get the audio data from file
	public AudioInputStream getAudio(){		
		try {
			System.out.println("td: " + tdLine.available());
			AudioInputStream in = AudioSystem.getAudioInputStream(this.testFile);
			return in;
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public void run(){
		int bufferLengthInBytes = tdLine.getBufferSize();
		byte[] data = new byte[bufferLengthInBytes];
		int numBytesRead = 0;
		this.out = new ByteArrayOutputStream();
		
		while(this.recording){
			System.out.println("Recording...");
			if ((numBytesRead = tdLine.read(data, 0, bufferLengthInBytes)) != -1){
				this.out.write(data, 0, numBytesRead);
			}
		}
		try {
			AudioSystem.write(audioInputStream, audioFileFormatType, output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] getAudioBytes() {
		return this.audioBytes;
	}
	
	public double[] getAudioData(AudioInputStream in){
		byte[] abytes = new byte[(int)in.getFrameLength() * in.getFormat().getFrameSize()];
		double[] audio = new double[(int)in.getFrameLength() * in.getFormat().getFrameSize()];
		float sampleRate = in.getFormat().getSampleRate();		
		float T = in.getFrameLength() / in.getFormat().getFrameRate();
		int n = (int) (T *  sampleRate) / 2;
		
		try {
			in.read(abytes);
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		for(int i = 0; i < n; i++){
			audio[i] = abytes[i];
		}
		return audio;
	}
}
