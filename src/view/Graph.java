package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

public class Graph extends Frame{
	Stroke drawingStroke = new BasicStroke(2);
	public void paint(Graphics g){
		short[] samples = null;  // the samples to render - get this from step 1.

		   int height = getHeight();
		   int middle = height/2;

		   for (int i=0; i<samples.length; i++)
		   {
		      int sample = samples[i];
		      int amplitude = sample*middle;
		      g.drawLine(i, middle+amplitude, i, middle-amplitude); 
		   }  
	}
}
