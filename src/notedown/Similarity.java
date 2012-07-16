package notedown;
import java.util.Iterator;
import java.util.Vector;

import music_notation.Chord;
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
	public void cosineSimilarity(short[] detectedChord){
		float tmpCos = 0;
		Chord chord;
		Chord tmpChord = null;
		Iterator<Chord> it = chordTemplates.iterator();
		
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
			//System.out.println("cos: " + cos);
			if(cos > tmpCos){
				tmpCos = cos;
				tmpChord = chord;
			}
		}
		this.highestSimilarity = tmpChord;
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
