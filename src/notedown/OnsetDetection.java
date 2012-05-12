/**
 * Find onsets via the spectral flux algorithm
 * @author Christian Sandvo§
 */

package notedown;

import java.util.ArrayList;
import java.util.List;

public class OnsetDetection {

	public float[] audio;
	private float[] spectrum;
	private float[] lastSpectrum;
	private List<Float> spectralFlux;
	private List<Float> threshold;
	private List<Float> cleanedSpectralFlux;
	private List<Float> peaks;

	/**
	 * Initialize OnsetDetection instance
	 * 
	 * @param sampleSize
	 *            Size of samples used for the fft (1024, 2048, ...)
	 */
	public OnsetDetection(int sampleSize) {
		this.spectrum = new float[sampleSize / 2 + 1];
		this.lastSpectrum = new float[sampleSize / 2 + 1];
		this.spectralFlux = new ArrayList<Float>();
		this.threshold = new ArrayList<Float>();
		this.cleanedSpectralFlux = new ArrayList<Float>();
		this.peaks = new ArrayList<Float>();
	}

	/**
	 * @return Returns all found onsets as List
	 */
	public List<Float> getOnsets() {
		return this.peaks;
	}

	/**
	 * Calculate the spectral flux for each sample and store values in list
	 * 
	 * @param fftSpectrum
	 *            The spectrum of the current sample
	 */
	public void calculateSpectrumFlux(float[] fftSpectrum) {
		System.arraycopy(this.spectrum, 0, this.lastSpectrum, 0,
				this.spectrum.length);
		System.arraycopy(fftSpectrum, 0, this.spectrum, 0, this.spectrum.length);
		float flux = 0;
		for (int i = 0; i < spectrum.length; i++) {
			float value = (spectrum[i] - lastSpectrum[i]);
			flux += value < 0 ? 0 : value;
		}
		spectralFlux.add(flux);
		this.findPeaks();
	}

	private void findPeaks() {
		for (int x = 0; x < this.spectralFlux.size(); x++) {
			int start = Math.max(0, x - 10);
			int end = Math.min(this.spectralFlux.size() - 1, x + 10);
			float mean = 0;
			for (int j = start; j <= end; j++) {
				mean += this.spectralFlux.get(j);
			}
			mean /= (end - start);
			this.threshold.add(mean * 1.5f);
		}
		for (int t = 0; t < this.threshold.size(); t++) {
			if (this.threshold.get(t) <= this.spectralFlux.get(t)) {
				this.cleanedSpectralFlux.add(this.spectralFlux.get(t)
						- this.threshold.get(t));
			} else {
				this.cleanedSpectralFlux.add((float) 0);
			}
		}
		for (int p = 0; p < this.cleanedSpectralFlux.size() - 1; p++) {
			if (this.cleanedSpectralFlux.get(p) > this.cleanedSpectralFlux
					.get(p + 1)) {
				this.peaks.add(this.cleanedSpectralFlux.get(p));
			} else {
				this.peaks.add((float) 0);
			}
		}
		for (int y = 0; y < this.peaks.size(); y++) {
			System.out.println(" Peaks: " + this.peaks.get(y));
		}
	}

	public int getIndex(int time) {
		double sammplinRate = 44100.0;
		int index = (int) (time * sammplinRate);
		return index;
	}

	public float getTimeOfPeaks() {		
		float time = 0;
		for(int i = 0; i < this.peaks.size(); i++){
			if(this.peaks.get(i) >= 5){
				time = i * (1024 / 44100);
			}
		}		 
		return time;
	}

	public void setAudioData(float[] audioData) {
		this.audio = audioData;
	}
	
	public List<Float> getPeaks(){
		return this.peaks;
	}
}
