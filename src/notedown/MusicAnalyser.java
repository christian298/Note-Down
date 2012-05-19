package notedown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dsp.FFT;

public class MusicAnalyser {
    private Map<String, Number> fretboard = new HashMap<String, Number>();
    private ArrayList<Float> ampList = new ArrayList<Float>();
    private Map<Float, Float> notes = new HashMap<Float, Float>();

    public MusicAnalyser(){
    	this.fillFretboardMap();
    }
    
	public void getNotesInSignal(FFT f){
		/*for(int i = 0; i < this.fretboard.size(); i++){
			float amp = f.getFreq((float) this.fretboard.values().toArray()[i]);
			if(amp > 20.00){
				System.out.println(this.fretboard.values().toArray()[i] + " Am: " + amp);
				this.notes.put((Number) this.fretboard.values().toArray()[i], amp);
			}
		}*/
				
		// Octave 2 to 5
		for(int o = 2; o <= 5; o++){
			// Notes 0 to 11
			for(int n = 0; n <= 11; n++){
				float amp = f.getFreq(this.calculateFrequencyOfNote(o, n));
				if(amp > 5.00){
					//System.out.println("Amp: " + amp);
					this.notes.put(amp, this.calculateFrequencyOfNote(o, n));
				}
			}
		}
		this.findMaxAmplitude();
	}
	
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
	        //System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        //System.out.println("Amp: " + tmpAmpl + " Note: " + tmpNote);
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
    	float freq = (float) (Math.pow(2, ((note - 9) / 12 + octave - 4)) * 440);
    	return freq;
    }
    
    private void getHighestAmplitude(){
    	
    }
}
