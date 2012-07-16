package notedown;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
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
    
    /**
     * Initialize MusicAnalyser instance
     */
    public MusicAnalyser(){
    	this.chordPlayed = false;
    	this.amplitudeThreshold = 2f;
    }
    
    /**
     * Initialize MusicAnalyser instance
     * @param threshold The threshold for the amplitude value. Only amplitudes over the threshold will be processed
     */
    public MusicAnalyser(float threshold){
    	this.chordPlayed = false;
    	this.amplitudeThreshold = threshold;
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
					String name = this.getNameOfNote(freq);
					System.out.println("Freq: " + freq + "Amp: " + amp + " Name: " + name);
					this.notes.put(freq, amp);
					
					if(this.storeNote(freq, amp, name)){
						this.noteStore.add(new Note(freq, amp, name));
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
					String name = this.getNameOfNote(freq);
					if(this.storeNote(freq, amp, name)){
						this.noteStore.add(new Note(freq, amp, name));
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
	
	public float getFreq(float freq){
	    return getBand(freqToIndex(freq));
	}
	
	public float getBand(int i){
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
	        //System.out.println("Amp: " + tmpAmpl + " Note: " + tmpNote);
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
	
	/**
	 * Check if note should be stored. Only store a notes with higher amplitude than present
	 * @param f Frequency
	 * @param a Amplitude
	 * @return true or false
	 */
	private boolean storeNote(float f, float a, String name){
		boolean saveTheNote = true;
		Iterator<Note> noteIt = this.noteStore.iterator();		
		while(noteIt.hasNext()){			
			Note n = noteIt.next();
			if(n.getName() == name){
			//if(n.getFrequency() == f){
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
    	float freq = (float) (Math.pow(2, ((note - 9f) / 12f + octave - 4f)) * 440f);
    	return freq;
    }
    
    public Set<Float> getMaxFrequencys(){
    	return this.maxFreq;
    }
    
    /**
     * Get name of note for a given frequency
     * @param f Frequency
     * @return Name as string
     */
    public String getNameOfNote(float f){  
    	String name = null;
    	int i = this.getNoteIndex(f);
    			
    	if(i == 0){
			name =  "C";
		} else if(i == 1){
			name = "C#";
		} else if(i == 2){
			name = "D";
		} else if(i == 3){
			name = "D#";
		} else if(i == 4){
			name = "E";
		} else if(i == 5){
			name = "F";
		} else if(i == 6){
			name = "F#";
		} else if(i == 7){
			name = "G";
		} else if(i == 8){
			name = "G#";
		} else if(i == 9){
			name = "A";
		} else if(i == 10){
			name = "A#";
		} else if(i == 11){
			name = "H";
		}
    			
    	return name;
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
    
    public boolean chordPlayed(){
    	if(this.noteStore.size() > 1){
    		return true;
    	} else{
    		return false;
    	}
    }
/*    
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
*/    
    public short[] getChord(){
    	short[] chord = new short[12];
    	Vector<Integer> tmp = new Vector<Integer>();
    	Iterator<Note> it = this.noteStore.iterator();
		int noteCount = 0;
    	
    	while(it.hasNext() && noteCount < 3){
    		tmp.add(this.getNoteIndex(it.next().getFrequency()));
    		noteCount++;
    	}
    	
    	for(int i = 0; i < chord.length; i++){
    		if(tmp.contains(i)){
    			chord[i] = 1;
    		} else{
    			chord[i] = 0;
    		}
    	}
    	
    	return chord;
    }
    
    /**
     * Get the index by the frequency a note
     * @param f Frequency
     * @return The index of the note (for c = 0 or for d = 3)
     */
    private int getNoteIndex(float f){
    	Map<Integer, Float> rMap = new TreeMap<Integer, Float>();
    	int index = 0; 
    	float tmp = f % this.calculateFrequencyOfNote(0, 0);
    	for(int i = 0; i < 12; i++){
        	float rest = 100;
    		for(int o = 0; o < 5; o++){
    			float tmpRest = f % this.calculateFrequencyOfNote(o, i);
    			if(tmpRest < rest ){
    				rest = tmpRest;
    			}    			
    		}
    		rMap.put(i, rest);
		}
    	
    	return entriesSortedByValues(rMap).first().getKey();
    }
    
    /**
     * Sorting a map by values
     * @param map Map to sort
     * @return Sorted map
     */
    static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
            new Comparator<Map.Entry<K,V>>() {
                @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                    return e1.getValue().compareTo(e2.getValue());
                }
            }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
    
    
    /**
     * Get all recognized notes
     * @return Vector with notes
     */
    public Vector<Note> getNoteStoreage(){
    	return this.noteStore;
    }
    
    /**
     * Delete all entries in the note Vector
     */
    public void cleanNoteStorage(){
    	this.noteStore.clear();
    }
    
    /**
     * Sorts the notes in the vector by amplitude value 
     */
    public void sortNoteStorage(){
    	Collections.sort(this.noteStore, new Comparator<Note>() {
    		   public int compare(Note o1, Note o2){
    		      return o2.getAmplitude().compareTo(o1.getAmplitude());
    		   }
    	});
   }   
   
   public void removeSameNotes(){
	   Iterator<Note> it1 = this.noteStore.iterator();
	   Iterator<Note> it2 = this.noteStore.iterator();
	   while(it1.hasNext()){
		   Note n1 = it1.next();
		   it2.next();
		   while(it2.hasNext()){
			   Note n2 = it2.next();
			   if(n2.getFrequency() % n1.getFrequency() == 0){
				   if(n2.getAmplitude() > n1.getAmplitude()){
					   it1.remove();
				   } else{
					   it2.remove();
				   }
			   }
		   }
	   }
   }
   
   public Integer getOcteveOfNote(float freq){
	   int oct = 0;
	   for(int o = 2; o <= 5; o++){
			for(int n = 0; n <= 11; n++){
				float f = this.calculateFrequencyOfNote(o, n);
				if(f == freq){
					oct = o;
					break;
				}
			}
	   }
	   return oct;
   }
}
