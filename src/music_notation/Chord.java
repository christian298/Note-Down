package music_notation;

public class Chord {
	private short[] chord;
	private String name;
	
	public Chord(short[] c){
		this.chord = c;
	}
	
	public Chord(short[] c, String n){
		this.chord = c;
		this.name = n;
	}
	
	public short[] getChordVector(){
		return this.chord;
	}
	
	public String getChordName(){
		return this.name;
	}
}
