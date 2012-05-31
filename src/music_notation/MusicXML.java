package music_notation;

import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class MusicXML {
	
	public MusicXML(){
		// Create XML document
		Document doc = new Document();
		
		// Create MusicXML Doctype
		DocType doctype = new DocType("score-partwise", "-//Recordare//DTD MusicXML 3.0 Partwise//EN", "http://www.musicxml.org/dtds/partwise.dtd");
		
		// Create root element
		Element root = new Element("score-partwise");
		root.setAttribute("version", "3.0");
		
		// Create part list containing all parts
		Element partList = new Element("part-list");
		
		// Create part with id and name
		Element scorePart = new Element("score-part");
		scorePart.setAttribute("id", "1");
		Element partName = new Element("part-name");		
		partName.addContent(new Text("Tabs"));				
		scorePart.addContent(partName);
		
		// Attach part to part list
		partList.addContent(scorePart);
		
		// Attach the part list to the root element 
		root.addContent(partList);
		
		// Create a part an set id
		Element part = new Element("part");
		part.setAttribute("id", "1");			
		
		// Create measure attribute
		Element measure = new Element("measure");
		measure.setAttribute("number", "1");
		measure.setAttribute("width", "485");
		
		Element attributes = new Element("attributes");
		
		Element divison = new Element("divisions");
		divison.addContent(new Text("1"));
		
		Element key = new Element("key");
		Element fifths = new Element("fifths");
		fifths.addContent(new Text("0"));
		key.addContent(fifths);
		
		attributes.addContent(divison);
		attributes.addContent(key);
		measure.addContent(attributes);
		
		// Create note attribute
		Element note = new Element("note");
		
		Element pitch = new Element("pitch");
		Element step = new Element("step");
		step.addContent(new Text("E"));
		Element octave = new Element("octave");
		octave.addContent(new Text("2"));
		pitch.addContent(step);
		pitch.addContent(octave);
		
		note.addContent(pitch);
		
		Element duration = new Element("duration");
		duration.addContent(new Text("4"));
		note.addContent(duration);
		
		Element type = new Element("type");
		type.addContent(new Text("whole"));
		note.addContent(type);
		
		// Create notations attribute
		Element notations = new Element("notations");
		
		// Create root for tab notation
		Element tabRoot = new Element("technical");
		
		// Create element for string number
		Element string = new Element("string");
		string.addContent("5");
		
		// Create element for position on the fretboard
		Element fret = new Element("fret");
		fret.addContent("3");
		
		// Add attach elements to the root 
		tabRoot.addContent(string);
		tabRoot.addContent(fret);		
		
		// Attach the tab-root element to notations 
		notations.addContent(tabRoot);
		
		// Attach notations to note
		note.addContent(notations);
		
		// Attach note to measure
		measure.addContent(note);
		
		// Attach the measure to the part
		part.addContent(measure);
		
		// Attach the part to the root
		root.addContent(part);
		
		// Attach the DocType to document
		doc.setDocType(doctype);
		
		// Attach root to the document
		doc.setRootElement(root);
		
		// Set the format for the XML document
		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");
		
		// Write XML to file
		XMLOutputter xmlOut = new XMLOutputter(format);
		try {
			xmlOut.output(doc, new FileOutputStream("file.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void finalize(){

	}
	
	public void writeNote(String s, String f){
	
	}
	
	public void writeChord(){
		
	}

}
