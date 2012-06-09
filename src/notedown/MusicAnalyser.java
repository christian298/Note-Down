package notedown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import music_notation.Note;

import com.sun.org.apache.bcel.internal.generic.NEW;

import dsp.FFT;

public class MusicAnalyser {
    private ArrayList<Float> ampList = new ArrayList<Float>();
    private Map<Float, Float> notes = new HashMap<Float, Float>();
    private Vector<Note> noteStore = new Vector<Note>();
    private Set<Float> maxFreq = new HashSet<Float>();
    private boolean chordPlayed;
    private int sameFrequencyCount;
    private short[] chordVec; 
    private float amplitudeThreshold;
    private float[] spectrum;
    public MusicAnalyser(){
    	this.chordPlayed = false;
    	this.amplitudeThreshold = 2f;
    }
    
    /**
     * Calculates the amplitude for all 12 notes
     * @param f The fft instance
     */
	public void getNotesInSignal(FFT f){
		// Octave 2 to 5
		for(int o = 2; o <= 5; o++){
			// Notes 0 to 11
			for(int n = 0; n <= 11; n++){
				// Get frequency of every note
				float freq = this.calculateFrequencyOfNote(o, n);
				// Amplitude for note
				float amp = f.getFreq(freq);

				// Get only notes with amplitudes over the given threshold
				if(amp > this.amplitudeThreshold){
					//this.notes.put(amp, this.calculateFrequencyOfNote(o, n));
					this.notes.put(freq, amp);
					if(this.storeNote(freq, amp)){
						this.noteStore.add(new Note(freq, amp));
					}
				}
			}
		}
		this.findMaxAmplitude();
		//this.maxAmp();
	}
	
	public void getNotesInSignal(float[] spec){
		this.spectrum = spec;
		// Octave 2 to 5
		for(int o = 2; o <= 5; o++){
			// Notes 0 to 11
			for(int n = 0; n <= 11; n++){
				// Get frequency of every note
				float freq = this.calculateFrequencyOfNote(o, n);
				// Amplitude for note
				float amp = this.getFreq(freq);

				// Get only notes with amplitudes over the given threshold
				if(amp > this.amplitudeThreshold){
					//this.notes.put(amp, this.calculateFrequencyOfNote(o, n));
					this.notes.put(freq, amp);
					if(this.storeNote(freq, amp)){
						this.noteStore.add(new Note(freq, amp));
					}
				}
			}
		}
		this.findMaxAmplitude();
		//this.maxAmp();
	}
	
	public int freqToIndex(float freq){
	    float fraction = freq / (float) 44100f;
	    int i = Math.round(16384 * fraction);
	    return i;
	  }
	 public float getFreq(float freq)
	  {
	    return getBand(freqToIndex(freq));
	  }
	 public float getBand(int i)
	  {
	    if (i < 0) i = 0;
	    if (i > this.spectrum.length - 1) i = this.spectrum.length - 1;
	    return this.spectrum[i];
	  }
		
	/**
	 * Find the frequency with max. amplitude
	 */
	private void findMaxAmplitude(){
		float tmpAmpl = 0;
		float tmpNote = 0;
		Iterator<Entry<Float, Float>> it = this.notes.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Float, Float> pairs = it.next();
	        if((float)pairs.getValue() > tmpAmpl){
	        	//tmpAmpl = (float)pairs.getKey();
	        	//tmpNote = (float)pairs.getValue();
	        	tmpAmpl = (float)pairs.getValue();
	        	tmpNote = (float)pairs.getKey();
	        }	        	        
	        System.out.println("Amp: " + tmpAmpl + " Note: " + tmpNote);
	        maxFreq.add(tmpNote);
	    }	    	    
	}
	
	private void maxAmp(){
		Iterator<Note> it = this.noteStore.iterator();
		while(it.hasNext()){
			Note n = it.next();
			System.out.println("F: " + n.getFrequency() + "A: " + n.getAmplitude());
		}
	}
	
	private boolean storeNote(float f, float a){
		boolean saveTheNote = true;
		Iterator<Note> noteIt = this.noteStore.iterator();		
		while(noteIt.hasNext()){
			Note n = noteIt.next();
			if(n.getFrequency() == f){
				if(n.getAmplitude() < a){
					saveTheNote = true;
					noteIt.remove();
				} else{
					saveTheNote = false;
				}
			}
		}
		return saveTheNote;
	}
   
    /**
     * Calculate the frequency for an given note.
     * 0 = C
     * 1 = C#
     * 2 = D
     * 3 = D#
     * 4 = E
     * 5 = F
     * 6 = F#
     * 7 = G
     * 8 = G#
     * 9 = A
     * 10 = A#
     * 11 = B
     * @param octave The octave of the Note
     * @param note Number of the Note
     * @return Returns the frequency of an note as float
     */
    public float calculateFrequencyOfNote(int octave, int note){
    	float freq = (float) (Math.pow(2f, ((note - 9f) / 12f + octave - 4f)) * 440f);
    	return freq;
    }
    
    public Set<Float> getMaxFrequencys(){
    	return this.maxFreq;
    }
    
    public String getNameOfNote(float f){
    	for(int i = 0; 0 < 12; i++){
			if(f % this.calculateFrequencyOfNote(2, i) == 0){
				if(i == 0){
					return "C";
				} else if(i == 1){
					return "C#";
				} else if(i == 2){
					return "D";
				} else if(i == 3){
					return "D#";
				} else if(i == 4){
					return "E";
				} else if(i == 5){
					return "F";
				} else if(i == 6){
					return "F#";
				} else if(i == 7){
					return "G";
				} else if(i == 8){
					return "G#";
				} else if(i == 9){
					return "A";
				} else if(i == 10){
					return "A#";
				} else if(i == 11){
					return "H";
				}
			}
		}
    }
    
    public float[] autoCorrelate(float[] signal,int windowlength, int windowshift) {
        int signallenth = signal.length;
        if (signallenth < windowlength+windowshift)
            return null;
        float[] autocorrelation = new float[windowlength];
        // loop over the magnitude of the autocorrelation
        for (int lag=0;lag<windowlength;lag++) {
            // loop over the sum
            for (int n=0;n<windowshift;n++) {
                autocorrelation[lag]+=signal[n]*signal[n+lag];
            }
        }
        return autocorrelation;
    }
    
    public boolean wasChordPlayed(){
    	if(this.maxFreq.size() <= 1){
    		this.chordPlayed = false;
    	} else{
    		this.chordPlayed = true;
    	}
    	return this.chordPlayed;
    }
    
    public short[] getChord(){   
    	Vector<Integer> tmp = new Vector<Integer>();
    	Iterator<Float> it = this.maxFreq.iterator();    	
    	int start = this.maxFreq.size() - 3;
    	int counter = 0;
    	short[] chord = new short[12];

    	while (it.hasNext()) {
	    	tmp.add(this.getNoteIndex(it.next()));
	    }
	    
	    for(int i = 0; i < chord.length; i++){
	    	if(tmp.contains(i)){
	    		chord[i] = 1;
	    	} else {
	    		chord[i] = 0;
	    	}
	    }
	    
	    return chord;
	    
    }
    
    private int getNoteIndex(float f){
    	int index = 0; 
    	int rest = 0;
    	int tmp = (int)f % (int)this.calculateFrequencyOfNote(2, 0);
    	for(int i = 0; i < 12; i++){
    		rest = (int)f % (int)this.calculateFrequencyOfNote(2, i);
    		//System.out.println("rest: " + rest);
			if( rest == 0){
				index =  i;
			} else{
				for(int x = 0; x < 12; x++){
					// Search nearest frequency
					int r = (int)f % (int)this.calculateFrequencyOfNote(2, x);
					if(r < tmp){
						tmp = r;
						index = x;
					}
				}
			}
		}
    	return index;
    }
    
    public Vector<Note> getNoteStoreage(){
    	return this.noteStore;
    }
}
