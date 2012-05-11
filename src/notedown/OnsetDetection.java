package notedown;

public class OnsetDetection {
	
	public double[] audio;
	public double[] segment;
	public double threshold;
	public int startIndex;
	public int stopIndex;
	public boolean foundStart;
	public boolean foundEnd;
	
	public OnsetDetection(double[] audioData){
		this.audio = audioData;
		this.threshold = 1000;
		this.segmentation(audioData);
		this.foundStart = false;
		this.foundEnd = false;
	}
	
	public double[] getSegment(){
		return this.segment;
	}
	
	private void segmentation(double[] audioData){
		int audioLength = audioData.length;
		double oneSec = audioLength / 88200;
		
		System.out.println("AudioLength: " + audioLength);
		
		for(int x = 0; x <= oneSec; x++){		
			double mag = Math.pow(audioData[2*x], 2) + Math.pow(audioData[2*x+1], 2);
			//System.out.println("Mag: " + mag);
			if(mag >= this.threshold && !this.foundStart){				
				this.startIndex = x;
				this.foundStart = true;				
				System.out.println("foundStart at: " + this.startIndex + " with Mag: " + mag);
			}else if (this.foundStart && !this.foundEnd && mag < this.threshold){								
				this.stopIndex = x;
				this.foundEnd = true;				
				System.out.println("foundStop at: " + this.stopIndex + " with Mag: " + mag);
				this.foundStart = false;
			}
			for(int k = 0; k <= oneSec; k++){
				double hfc =  Math.pow(audioData[2*k],2) * k;
				System.out.println("hfc: " + hfc);
			}
		}				
	}
	
	public int getIndex(int time){
		double sammplinRate = 44100.0;
		int index = (int)(time * sammplinRate);
		return index;
	}
}
