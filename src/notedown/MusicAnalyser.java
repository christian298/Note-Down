package notedown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.sun.org.apache.bcel.internal.generic.NEW;

import dsp.FFT;

public class MusicAnalyser {
    private ArrayList<Float> ampList = new ArrayList<Float>();
    private Map<Float, Float> notes = new HashMap<Float, Float>();
    private Set<Float> maxFreq = new HashSet<Float>();
    private boolean chordPlayed;
    private int sameFrequencyCount;
    
    public MusicAnalyser(){
    	this.chordPlayed = false;
    }
    
	public void getNotesInSignal(FFT f){
		// Octave 2 to 5
		for(int o = 2; o <= 5; o++){
			// Notes 0 to 11
			for(int n = 0; n <= 11; n++){
				float amp = f.getFreq(this.calculateFrequencyOfNote(o, n));
				if(amp > 0.00){
					//System.out.println("Amp: " + amp);
					this.notes.put(amp, this.calculateFrequencyOfNote(o, n));
				}
			}
		}
		this.findMaxAmplitude();
	}
	
	/**
	 * Find the frequency with max. amplitude
	 */
	private void findMaxAmplitude(){
		float tmpAmpl = 0;
		float tmpNote = 0;
		this.sameFrequencyCount = 0;
		Iterator it = this.notes.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        if((float)pairs.getKey() > tmpAmpl){
	        	tmpAmpl = (float)pairs.getKey();
	        	tmpNote = (float)pairs.getValue();
	        	if(tmpAmpl == (float)pairs.getKey()){
	        		this.sameFrequencyCount++;
	        	}
	        }	        	        
	        System.out.println("Amp: " + tmpAmpl + " Note: " + tmpNote);
	        maxFreq.add(tmpNote);
	    }	    	    
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
    	float freq = (float) (Math.pow(2, ((note - 9f) / 12f + octave - 4f)) * 440f);
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
    	if(this.sameFrequencyCount > 3){
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
    	System.out.println("size: " + this.maxFreq.size());
	    while (it.hasNext()) {
	    	System.out.println("next: " + this.getNoteIndex(it.next()));
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
}
