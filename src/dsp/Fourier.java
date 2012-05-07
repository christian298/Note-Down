package dsp;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;

public class Fourier {
	
	private int _n;
	private float _sampleRate;
	private float _T;
	private float _h;
	
	public double[] transform(AudioInputStream in){
		float sampleRate = in.getFormat().getSampleRate();
		System.out.println("Frame length: " + in.getFrameLength());
		float T = in.getFrameLength() / in.getFormat().getFrameRate();
		int n = (int) (T *  sampleRate) / 2;
		set_n(n);
		_T = T;
		set_sampleRate(sampleRate);
		float h = (T / n);
		_h = h;
		System.out.println("Sample rate: " + sampleRate);
		System.out.println("T: " + T);
		System.out.println("n: " + n);
		System.out.println("h: " + h);
		byte[] abytes = new byte[(int)in.getFrameLength() * in.getFormat().getFrameSize()];
		try {
			in.read(abytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean isEndian = in.getFormat().isBigEndian();
		
		double x[] = new double[n];
		
		for(int i = 0; i < n * 2; i += 2){
			int b1 = abytes[i];
			int b2 = abytes[1 + 1];
			if(b1 < 0) b1 += 0x100;
			if(b2 < 0) b2 += 0x100;
			
			int value;
			
			if (!isEndian){
				value = (b1 << 8) + b2;
			}else{
				value = b1 + (b2 << 8);
			}
			
			x[i/2] = value;
			
		}
		return x;
	}

	public int get_n() {
		return _n;
	}

	public void set_n(int _n) {
		this._n = _n;
	}

	public float get_sampleRate() {
		return _sampleRate;
	}

	public void set_sampleRate(float _sampleRate) {
		this._sampleRate = _sampleRate;
	}
}
