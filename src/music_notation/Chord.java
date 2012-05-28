package music_notation;

public class Chord {
	private short[] chord;
	
	public Chord(short[] c){
		this.chord = c;
	}
	
	public short[] getChordVector(){
		return this.chord;
	}
}
