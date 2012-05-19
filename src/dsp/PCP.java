package dsp;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;

public class PCP {
	
	private FFT fft;
	
	// Log base 2
	public static float lb( float x ){ 
	  return (float) (Math.log( x ) / Math.log( 2.0f )); 
	}
	
	public void createProfile(FFT f, int samplinRate){	
		this.fft = f;
		int bins = samplinRate / 2 + 1;
		float[] p = new float[bins];		
		float[] pcp = new float[12];
		//Point range = new Point();
		Vector<Point> range = new Vector<Point>();
		
		for(int i = 0; i < pcp.length; i++){
			pcp[i] = 0;
		}
		
		int[] b = this.getBandsForNotes();
		for(int s = 0; s < b.length - 1; s++){
			if(s == 0){
				if(b[s] + 1 < b[s + 1]){
					 range.add(new Point(b[s] + 1, b[s + 1]));
				} else{
					range.add(new Point(b[s], b[s]));
				}		
			} else{
				if(b[s] + 1 < b[s + 1]){					
					int start = b[s] - range.lastElement().y;
					range.add(new Point(start, b[s + 1]));
				} else{
					int start = b[s] - range.lastElement().y;
					range.add(new Point(start, b[s]));
				}
			}
		}
		
		for(int v = 0; v < range.size(); v++){
			System.out.println("X: " + range.get(v).x);
			System.out.println("Y: " + range.get(v).y);
		}
		
		/*for(int b = 0; b < bins; b++){
			System.out.println("Band: " + f.getBand(b));			
		}*/
		
		for(int k = 0; k < 513; k++){
			//if(k == 0){
				//p[k] = -1;
			//} else{
				p[k] = Math.abs(Math.round((12f * lb(((43f / 65.4f))) % 12)));
			//}
			//System.out.println("p: " + p[k]);
		}	
		
		for(int x = 0; x < p.length; x++){		
			if(p[x] == 0f){
				pcp[0] += Math.pow(p[x], 2);
			} else if(p[x] == 1.0f){
				pcp[1] += Math.pow(p[x], 2);
			} else if(p[x] == 2.0f){
				pcp[2] += Math.pow(p[x], 2);
			} else if(p[x] == 3.0f){
				pcp[3] += Math.pow(p[x], 2);
			} else if(p[x] == 4.0f){
				pcp[4] += Math.pow(p[x], 2);
			} else if(p[x] == 5.0f){
				pcp[5] += Math.pow(p[x], 2);
			} else if(p[x] == 6.0f){
				pcp[6] += Math.pow(p[x], 2);
			} else if(p[x] == 7.0f){
				pcp[7] += Math.pow(p[x], 2);
			} else if(p[x] == 8.0f){
				pcp[8] += Math.pow(p[x], 2);
			} else if(p[x] == 9.0f){
				pcp[9] += Math.pow(p[x], 2);
			} else if(p[x] == 10.0f){
				pcp[10] += Math.pow(p[x], 2);
			} else if(p[x] == 11.0f){
				pcp[11] += Math.pow(p[x], 2);
			}
		}
		for(int y = 0; y < pcp.length; y++){
			//System.out.println( y + " PCP: " + pcp[y]);
		}
	}
	
	private int[] getBandsForNotes(){
		int[] bands = new int[12];
		for(int n = 0; n < 12; n++){
			bands[n] = this.fft.freqToIndex(this.calculateFrequencyOfNote(2, n));
		}
		return bands;
	}
	
	private float calculateFrequencyOfNote(int octave, int note){
    	float freq = (float) (Math.pow(2, ((note - 9) / 12 + octave - 4)) * 440);
    	return freq;
    }

}
