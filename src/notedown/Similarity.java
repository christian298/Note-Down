package notedown;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import music_notation.Chord;
import music_notation.Note;
/**
 * Computes the similarity between a detected chord and the template chords
 * @author christian
 *
 */
public class Similarity {
	private Vector<Chord> chordTemplates;
	private Chord highestSimilarity;
	
	public Similarity(){
		this.chordTemplates = new Vector<Chord>();
		this.fillChordTemplates();
	}
	
	/**
	 * Find a matching Template for the detected Chord
	 * @param detectedChord The detected Chord
	 */
	public void cosineSimilarity(short[] detectedChord, Vector<Note> notes){
		float tmpCos = 0;
		float sameCos = 0;
		Chord chord;
		Chord tmpChord = null;
		Chord sameChord = null;
		Iterator<Chord> it = chordTemplates.iterator();	
		Vector<Chord> sameSim = new Vector<Chord>();
		Map<Chord, Float> same = new HashMap<Chord, Float>();
		
		while(it.hasNext()){
			chord = it.next();
			short[] tmpChordVec = chord.getChordVector();
			int eukl = 0;
			int aSum = 0;
			int bSum = 0;
			for(int i = 0; i < 12; i++){
				eukl += tmpChordVec[i] * detectedChord[i];
				aSum += Math.pow(tmpChordVec[i], 2);
				bSum += Math.pow(detectedChord[i], 2);
			}
			float cos = (float) (eukl / (Math.sqrt(aSum) * Math.sqrt(bSum)));
			
			if(cos >= tmpCos){
				tmpCos = cos;
				tmpChord = chord;
//				sameSim.add(chord);
				same.put(chord, cos);
			}

			System.out.println("cos: " + cos);
		}
		Chord c = this.findPossibleChord(same, tmpCos, notes, tmpChord);
		System.out.println("C: " + c.getChordName());
		this.highestSimilarity = tmpChord;
	}
	
	/**
	 * If more than one chord has the same high Cosine-Similarity find a possible chord by the amplitudes of the found notes in the signal 
	 * @param sameSim Map with same similarity values
	 * @param highestCos The highest found similarity value
	 * @param notes Vector with found notes from the audiosignal
	 * @param tmpChord Last found chord template
	 * @return returns a possible chord
	 */
	private Chord findPossibleChord(Map<Chord, Float> sameSim, float highestCos, Vector<Note> notes, Chord tmpChord){
		Iterator<Float> it = sameSim.values().iterator();
		Vector<Chord> tmp = new Vector<Chord>();
		int count = 0;
		Chord c = tmpChord;
		
		Iterator<Entry<Chord, Float>> i = sameSim.entrySet().iterator();
	    while (i.hasNext()) {
	        Entry<Chord, Float> pairs = i.next();
	        if((float)pairs.getValue() == highestCos){
	        	count++;
	        	tmp.add((Chord) pairs.getKey());
	        }
	    }
			
		if(tmp.size() > 1){
			Iterator<Chord> cIt = tmp.iterator();
			while(cIt.hasNext()){
				c = cIt.next();
				if(notes.get(0).getName() == c.getChordName()){					
					return c;					
				}
			}
		}
		return c;
	}
	
	private void fillChordTemplates(){
		short[] c = {1,0,0,0,1,0,0,1,0,0,0,0};
		short[] d = {0,0,1,0,0,0,1,0,0,1,0,0};
		short[] e = {0,0,0,0,1,0,0,0,1,0,0,1};
		short[] f = {1,0,0,0,0,1,0,0,0,1,0,0};
		short[] g = {0,0,1,0,0,0,0,1,0,0,0,1};
		short[] a = {0,1,0,0,1,0,0,0,0,1,0,0};
		short[] h = {0,0,0,1,0,0,1,0,0,0,0,1};
		
		this.chordTemplates.add(new Chord(c, "C"));
		this.chordTemplates.add(new Chord(d, "D"));
		this.chordTemplates.add(new Chord(e, "E"));
		this.chordTemplates.add(new Chord(f, "F"));
		this.chordTemplates.add(new Chord(g, "G"));
		this.chordTemplates.add(new Chord(a, "A"));
		this.chordTemplates.add(new Chord(h, "H"));
		
	}
	
	public Chord getChordWithHighestSimilarity(){
		return this.highestSimilarity;
	}
	
	/**
	 * Calculate the hamming distance 
	 */
	public void hamming(short[] detectedChord){
		Iterator<Chord> it = this.chordTemplates.iterator();
		Chord highestDist = null;
		int tmpDist = 100;
		while(it.hasNext()){
			int dist = 0;
			Chord tmpChord = it.next();
			short[] c = tmpChord.getChordVector();
			for(int i = 0; i< c.length; i++){
				if(detectedChord[i] != c[i]){
					dist++;
				}
			}
			if(dist < tmpDist){
				tmpDist = dist;
				highestDist = tmpChord;
			}
		}		
		this.highestSimilarity = highestDist;
	}
	
	public void dice(){
		Iterator<Chord> it = this.chordTemplates.iterator();
		
		while(it.hasNext()){
			
		}
	}
}
