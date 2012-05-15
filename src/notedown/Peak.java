package notedown;

public class Peak {
	public float startTime;
	public float endTime;
	
	public Peak(float start, float end){
		this.startTime = start;
		this.endTime = end;
	}
	
	public float getStartTime() {
		return startTime;
	}
	public void setStartTime(float startTime) {
		this.startTime = startTime;
	}
	public float getEndTime() {
		return endTime;
	}
	public void setEndTime(float endTime) {
		this.endTime = endTime;
	}
	
}
