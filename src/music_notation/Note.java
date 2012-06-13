package music_notation;

public class Note{
	private Float amplitude;
	private float frequency;
	private String name;
	
	public Note(float f, float a){
		this.amplitude = new Float(a);
		this.frequency = f;
	}
	
	public Note(float f, float a, String n){
		this.amplitude = new Float(a);
		this.frequency = f;
		this.name = n;
	}
	
	public Float getAmplitude(){
		return this.amplitude;
	}
	
	public float getFrequency(){
		return this.frequency;
	}

	public String getName(){
		return this.name;
	}	
	
}
