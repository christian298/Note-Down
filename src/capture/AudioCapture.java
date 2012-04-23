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
	private TargetDataLine _line;
	// Format of the audio data
	private AudioFileFormat.Type _targetType;
	private AudioInputStream _audioInputStream;
	private AudioFormat _audioFormat; 
	private Mixer.Info _inputDevice;
	private File _output;
	private File testFile;
	
	// Constructor
	public AudioCapture(Mixer.Info input) {
		_targetType = AudioFileFormat.Type.WAVE;
		_audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 2, 4, 44100.0F, false);
		_inputDevice = input;
		_output = new File("recOutput");
		this.testFile = new File("a.wav");
	}
			
	public void startRecording(){
		try {
			_line = AudioSystem.getTargetDataLine(_audioFormat, _inputDevice);
			_line.open(_audioFormat);
			_line.start();
			_audioInputStream = new AudioInputStream(_line);
			super.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void stopRecording(){
		_line.stop();
		_line.close();
	}
	
	// Get the audio data
	public AudioInputStream getAudio(){		
		try {
			AudioInputStream in = AudioSystem.getAudioInputStream(_output);
			//int frameLength = (int) in.getFrameLength();
			//int frameSize = (int) in.getFormat().getFrameSize();
			//int numOfChannels = in.getFormat().getChannels();
			//float sampleRate = _audioFormat.getSampleRate();
			
			//bytes = new byte[frameLength * frameSize];
			//in.read(bytes);
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
		try {
			AudioSystem.write(_audioInputStream, _targetType, this.testFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
