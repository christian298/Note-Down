package dsp;

import java.util.*;

public class Goertzel {
    private double[] hamming;
    private double rw, iw;
    private Map<String, Number> fretboard = new HashMap<String, Number>();
    private double freq;

    
    /** 
     * Creates a new instance of the Goertzel Filter for predefined frequency of the signal. 
     * 
     * @param the length of this signal is samples.
     * @param len the length of the signal is seconds.
     * @param the frequency of the signal.
     */
    public Goertzel(int f, int N, double len) {
        hamming = new double[N];
        double o = 2* Math.PI / N;
        for (int i = 0; i < N; i++) {
            hamming[i] = (0.54-0.46 * Math.cos(o* i));
        }
        
        rw = 2.0 * Math.cos(o* len * f);
        iw = Math.sin(o * len * f);
        
        // Add key/value pairs to map
        this.fillFretboardMap();
        this.freq = f;
    }
    
    /**
     * Calculates power of the specified frequence of the specified signal.
     * 
     * @param f the frequence value.
     * @param signal sampled signal.
     * @param offset index of the first sample of the signal.
     * @param len the length of signal in samples
     * @param scale the length of signal in seconds
     * @return the power of the specified frequency
     */
    public double getPower(double f, double[] signal, int offset, int len, double scale) {
        int N = len;
        int M = N + offset;
        
        double o = 2* Math.PI / N;
        
        //hamming window
        for (int i = offset; i < M; i++) {
            signal[i] *= (0.54-0.46 * Math.cos(o* i));
        }
        
        //Goertzel filter
        double realW = 2.0 * Math.cos(o* scale* f);
        double imagW = Math.sin(o * scale* f);
        
        double d1 = 0.0;
        double d2 = 0.0;
        double y = 0;
        
        for (int n = offset; n < M; ++n) {
            y  = signal[n] + realW*d1 - d2;
            d2 = d1;
            d1 = y;
        }
        
        double resultr = 0.5*realW*d1 - d2;
        double resulti = imagW*d1;
        
        return Math.sqrt( (resultr * resultr) + (resulti * resulti));
    } 

    /**
     * Calculates power of the specified frequence of the specified signal.
     * 
     * @param signal sampled signal.
     * @param offset index of the first sample of the signal.
     * @return the power of the specified frequency
     */
    public double getPower(double[] signal, int offset) {
        int M = hamming.length + offset;
        double power;
        //hamming window
        for (int i = offset; i < M; i++) {
            signal[i] *= hamming[i-offset];
        }
        
        //Goertzel filter
        double d1 = 0.0;
        double d2 = 0.0;
        double y = 0;
        
        for (int n = offset; n < M; ++n) {
            y  = signal[n] + rw*d1 - d2;
            d2 = d1;
            d1 = y;
        }
        
        double resultr = 0.5*rw*d1 - d2;
        double resulti = iw*d1;
        
        power = Math.sqrt( (resultr * resultr) + (resulti * resulti));
        
        // Find note in Map
        if(this.fretboard.containsValue(this.freq)){
        	for(int x = 0; x < this.fretboard.size(); x++){
        		if(this.fretboard.values().toArray()[x].equals(this.freq)){
        			System.out.println("Note: " + this.fretboard.keySet().toArray()[x]);
        		}
        	}
        }
        
        return power;
    }
    
    private void fillFretboardMap(){
    	this.fretboard.put("E0", 82.41);
    	this.fretboard.put("E1", 87.31);
    	this.fretboard.put("E2", 92.50);
    	this.fretboard.put("E3", 98.00);
    	this.fretboard.put("E4", 103.80);
    	this.fretboard.put("E5", 110.00);
    	this.fretboard.put("E6", 116.5);
    	this.fretboard.put("E7", 123.50);
    	this.fretboard.put("E7", 123.50);
    	this.fretboard.put("secE5", 440.00);
    }
    public void myAlgo(double targetFreq, int n, double[] audioData){
    	double real = 2 * Math.cos(2 * Math.PI * targetFreq / n);
    	double imag = Math.sin(2 * Math.PI * targetFreq / n);
    	double y = 0;
    	double d1 = 0;
    	double d2 = 0;
    	
    	for(int x = 0; x < n; x++){
    		y = audioData[x] + real * d1 - d2;
    		d2 = d1;
    		d1 = y;
    	}
    	double resultReal = 0.5 * real * d1 - d2;
    	double resultImag = imag * d1;
    }
}
