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
    private Map<String, Number> fretboard = new HashMap<String, Number>();
    private ArrayList<Float> ampList = new ArrayList<Float>();
    private Map<Float, Float> notes = new HashMap<Float, Float>();
    private Set<Float> maxFreq = new HashSet<Float>();

    public MusicAnalyser(){
    	this.fillFretboardMap();
    }
    
	public void getNotesInSignal(FFT f){
		// Octave 2 to 5
		for(int o = 2; o <= 5; o++){
			// Notes 0 to 11
			for(int n = 0; n <= 11; n++){
				float amp = f.getFreq(this.calculateFrequencyOfNote(o, n));
				if(amp > 1.00){
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
		Iterator it = this.notes.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        if((float)pairs.getKey() > tmpAmpl){
	        	tmpAmpl = (float)pairs.getKey();
	        	tmpNote = (float)pairs.getValue();
	        }	        
	        System.out.println("Amp: " + tmpAmpl + " Note: " + tmpNote);
	        maxFreq.add(tmpNote);
	    }	    	    
	}
	
    private void fillFretboardMap(){
    	this.fretboard.put("E0", 82.41f);
    	this.fretboard.put("E1", 87.31f);
    	this.fretboard.put("E2", 92.50f);
    	this.fretboard.put("E3", 98.00f);
    	this.fretboard.put("E4", 103.80f);
    	this.fretboard.put("E5", 110.00f);
    	this.fretboard.put("E6", 116.5f);
    	this.fretboard.put("E7", 123.50f);
    	this.fretboard.put("E7", 123.50f);
    	this.fretboard.put("E8", 130.80f);
    	this.fretboard.put("A0", 110.00f);
    	this.fretboard.put("A1", 116.50f);
    	this.fretboard.put("A2", 123.50f);
    	this.fretboard.put("D0", 146.80f);
    	this.fretboard.put("D1", 155.60f);
    	this.fretboard.put("D2", 164.80f);
    	this.fretboard.put("G0", 196.00f);
    	this.fretboard.put("G1", 207.60f);
    	this.fretboard.put("G2", 220.00f);
    	this.fretboard.put("secE5", 440.00f);
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
}
