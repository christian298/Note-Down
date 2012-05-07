package dsp;

public class PCP {
	
	// Log base 2
	public static double lb( double x ){ 
	  return Math.log( x ) / Math.log( 2.0 ); 
	}
	
	public void createProfile(double frequency){
		double pk;
		int fref = 440;
		
		pk = Math.round((12 * lb(frequency/fref)) % 12);
	}

}
