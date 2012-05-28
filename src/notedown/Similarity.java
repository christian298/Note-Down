package notedown;
import java.util.Iterator;
import java.util.Vector;

import music_notation.Chord;

public class Similarity {
	private Vector<Chord> chordTemplates;
	
	public Similarity(){
		this.chordTemplates = new Vector<Chord>();
		this.fillChordTemplates();
	}
	
	public void cosineSimilarity(short[] detectedChord){
		Iterator<Chord> it = chordTemplates.iterator();
		while(it.hasNext()){
			short[] tmpChord = it.next().getChordVector();
			int eukl = 0;
			int aSum = 0;
			int bSum = 0;
			for(int i = 0; i < 12; i++){
				eukl += tmpChord[i] * detectedChord[i];
				aSum += Math.pow(tmpChord[i], 2);
				bSum += Math.pow(detectedChord[i], 2);
			}
			float cos = (float) (eukl / (Math.sqrt(aSum) * Math.sqrt(bSum)));
			System.out.println("Cos Sim: " + cos);
		}
	}
	
	private void fillChordTemplates(){
		short[] c = {1,0,0,0,0,1,0,1,0,0,0,0};
		short[] d = {0,0,1,0,0,0,0,1,0,1,0,0};
		
		this.chordTemplates.add(new Chord(c));
		this.chordTemplates.add(new Chord(d));
		
	}

}
