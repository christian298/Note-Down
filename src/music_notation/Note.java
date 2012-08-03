package music_notation;

/**
 * Represents a note
 * @author Christian Sandvo§
 *
 */
public class Note{
	private Float amplitude;
	private float frequency;
	private String name;
	private Integer octave;
	
	/**
	 * Create a new note
	 * @param f Frequency
	 * @param a Amplitude
	 */
	public Note(float f, float a){
		this.amplitude = new Float(a);
		this.frequency = f;
	}
	
	/**
	 * Create a new note
	 * @param f Frequency
	 * @param a Amplitude
	 * @param n Name
	 */
	public Note(float f, float a, String n){
		this.amplitude = new Float(a);
		this.frequency = f;
		this.name = n;
	}
	
	/**
	 * Create a new note
	 * @param name Name of the note
	 * @param octave Octave fpr note
	 */
	public Note(String name, int octave){
		this.name = name;
		this.octave = new Integer(octave);
	}
	
	/**
	 * Get amplitude of the note in the signal
	 * @return Amplitude
	 */
	public Float getAmplitude(){
		return this.amplitude;
	}
	
	public void increaseAmplitude(float amp){
		this.amplitude += amp;
	}
	
	/**
	 * The frequency of the note
	 * @return Frequency
	 */
	public float getFrequency(){
		return this.frequency;
	}
	
	/**
	 * The name of the note as string
	 * @return Name
	 */
	public String getName(){
		return this.name;
	}	
	
	public Integer getOctave(){
		return this.octave;
	}
}
