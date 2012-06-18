package music_notation;

import java.util.Vector;

public class Chord {
	private short[] chord;
	private String name;
	private Vector<Note> notes = new Vector<Note>();
	private Vector<Note> xmlChordVector = new Vector<Note>();
	
	public Chord(short[] c){
		this.chord = c;
	}
	
	public Chord(Vector<Note> notes){
		this.notes = notes;
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
	
	public void musicXMLFormat(){
		// transform to musicxml friendly format
	}
	
	private void transformChordVectorToXMLVector(){
		if(this.name == "C"){
			this.xmlChordVector.add(new Note("C", 4));
			this.xmlChordVector.add(new Note("E", 3));
			this.xmlChordVector.add(new Note("G", 2));
		} else if(this.name == "D"){
			this.xmlChordVector.add(new Note("D", 4));
			this.xmlChordVector.add(new Note("F#", 4));
			this.xmlChordVector.add(new Note("A", 3));
		} else if(this.name == "E"){
			this.xmlChordVector.add(new Note("E", 3));
			this.xmlChordVector.add(new Note("G#", 3));
			this.xmlChordVector.add(new Note("H", 2));
		} else if(this.name == "F"){
			this.xmlChordVector.add(new Note("F", 4));
			this.xmlChordVector.add(new Note("A", 3));
			this.xmlChordVector.add(new Note("C", 3));
		} else if(this.name == "G"){
			this.xmlChordVector.add(new Note("G", 4));
			this.xmlChordVector.add(new Note("H", 2));
			this.xmlChordVector.add(new Note("D", 4));
		} else if(this.name == "A"){
			this.xmlChordVector.add(new Note("A", 3));
			this.xmlChordVector.add(new Note("C#", 4));
			this.xmlChordVector.add(new Note("E", 3));
		} else if(this.name == "H"){
			this.xmlChordVector.add(new Note("H", 3));
			this.xmlChordVector.add(new Note("D#", 4));
			this.xmlChordVector.add(new Note("F#", 3));
		}
	}
	
	/**
	 * Get a version of to Chord with information needed for xml writing
	 * @return A vector with notes
	 */
	public Vector<Note> getXMLFriendlyVector(){
		this.transformChordVectorToXMLVector();
		return this.xmlChordVector;
	}
}
