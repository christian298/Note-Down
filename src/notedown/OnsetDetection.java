package notedown;

public class OnsetDetection {
	
	public double[] audio;
	public double[] segment;
	
	
	public OnsetDetection(double[] audioData){
		this.audio = audioData;
		this.segmentation(audioData);
	}
	
	public double[] getSegment(){
		return this.segment;
	}
	
	private void segmentation(double[] audioData){
		/*for(int i = 0; i < 100; i++){
			System.out.println(i + " AudioData: " + audioData[i]);
		}*/
	}
}
