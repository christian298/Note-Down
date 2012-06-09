package music_notation;

public class Note {
	private float amplitude;
	private float frequency;
	private String name;
	
	public Note(float f, float a){
		this.amplitude = a;
		this.frequency = f;
	}
	
	public float getAmplitude(){
		return this.amplitude;
	}
	
	public float getFrequency(){
		return this.frequency;
	}
}
