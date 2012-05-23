package dsp;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;

public class PCP {
	
	private FFT fft;
	private int samplingRate;
	// Log base 2
	public static float lb( float x ){ 
	  return (float) (Math.log( x ) / Math.log( 2.0f )); 
	}
	
	public void createProfile(FFT f, int samplinRate){	
		this.samplingRate = samplinRate;
		this.fft = f;
		int bins = samplinRate / 2 + 1;
		float[] p = new float[bins];		
		float[] pcp = new float[24];
		Vector<Point> range = new Vector<Point>();
		
		// Initialize array with values
		for(int i = 0; i < pcp.length; i++){
			pcp[i] = 0;
		}
		
		int[] b = this.getBandsForNotes();
		for(int s = 0; s < b.length - 1; s++){
			if(s == 0){
				if(b[s] + 1 < b[s + 1]){
					 range.add(new Point(b[s], b[s] + 1));
				} else{
					range.add(new Point(b[s], b[s]));
				}		
			} else{
				if(b[s] + 1 < b[s + 1]){					
					int start = b[s] - range.lastElement().y - 1;
					range.add(new Point(b[s] - start, b[s] + 1));
				} else{
					int start = b[s] - range.lastElement().y - 1;
					range.add(new Point(b[s], b[s]));
				}
			}
		}
		
		for(int v = 0; v < range.size(); v++){
			float amp = 0;
			for(int r = range.get(v).x; r <= range.get(v).y; r++){
				int numberBins = range.get(v).y - range.get(v).x;
				amp += f.getBand(r) / numberBins;
			}
			System.out.println( v + " Amp: " + amp);
			pcp[v] = amp;
		}
		
		
		for(int k = 0; k < pcp.length; k++){
			//if(k == 0){
				//p[k] = -1;
			//} else{
				//p[k] = Math.abs(Math.round((12f * lb(((43f / 65.4f))) % 12)));
				//double dist = 2 * Math.abs((12*Math.log(pcp[k]/440f) / Math.log(2)) - 12 * Math.log(110f / 440f) / Math.log(2));
				//System.out.println("Dist: " + dist);
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
		int[] bands = new int[24];
		for(int o = 2; o <= 3; o++){
			for(int n = 0; n < 12; n++){
				bands[n] = this.fft.freqToIndex(this.calculateFrequencyOfNote(o, n));
			}
		}
		return bands;
	}
	
	private float calculateFrequencyOfNote(int octave, int note){
    	float freq = (float) (Math.pow(2, ((note - 9f) / 12f + octave - 4f)) * 440f);
    	System.out.println("note: " + freq);
    	return freq;
    }
	
	private String getNoteName(float freq){
		for(int i = 0; 0 < 12; i++){
			if(freq % this.calculateFrequencyOfNote(2, i) == 0){
				if(i == 0){
					return "C";
				} else if(i == 1){
					return "C#";
				} else if(i == 2){
					return "D";
				} else if(i == 3){
					return "D#";
				} else if(i == 4){
					return "E";
				} else if(i == 5){
					return "F";
				} else if(i == 6){
					return "F#";
				} else if(i == 7){
					return "G";
				} else if(i == 8){
					return "G#";
				} else if(i == 9){
					return "A";
				} else if(i == 10){
					return "A#";
				} else if(i == 11){
					return "H";
				}
			}
		}
	}
	
	private void calculateRegions(){
		
	}

}
