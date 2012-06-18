package music_notation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class MusicXML {
	private Element root;
	private Document doc;
	private DocType doctype;
	private Element partList;
	private Element part;
	private Element measure;
	private Integer measureCount;
	
	public MusicXML(){
		this.measureCount = new Integer(1);
		
		// Create XML document
		this.doc = new Document();
		
		// Create MusicXML Doctype
		this.doctype = new DocType("score-partwise", "-//Recordare//DTD MusicXML 3.0 Partwise//EN", "http://www.musicxml.org/dtds/partwise.dtd");

		// Create root element
		this.root = new Element("score-partwise");
		this.root.setAttribute("version", "3.0");

		// Create part list containing all parts
		this.partList = new Element("part-list");
		
		// Create part with id and name
		Element scorePart = new Element("score-part");
		scorePart.setAttribute("id", "1");
		Element partName = new Element("part-name");		
		partName.addContent(new Text("Tabs"));				
		scorePart.addContent(partName);
		
		// Attach part to part list
		this.partList.addContent(scorePart);
				
		// Attach the part list to the root element 
		this.root.addContent(partList);
		
		// Create a part an set id
		this.part = new Element("part");
		this.part.setAttribute("id", "1");		
		
	}
	
	public void writeMusicXML(){
		
		//Document doc = new Document();
		
		// Create MusicXML Doctype
		//DocType doctype = new DocType("score-partwise", "-//Recordare//DTD MusicXML 3.0 Partwise//EN", "http://www.musicxml.org/dtds/partwise.dtd");
		
		// Create root element
		//Element root = new Element("score-partwise");
		//root.setAttribute("version", "3.0");
		
		// Create part list containing all parts
		//Element partList = new Element("part-list");
		
		// Create part with id and name
		//Element scorePart = new Element("score-part");
		//scorePart.setAttribute("id", "1");
		//Element partName = new Element("part-name");		
		//partName.addContent(new Text("Tabs"));				
		//scorePart.addContent(partName);
		
		// Attach part to part list
		//partList.addContent(scorePart);
		
		// Attach the part list to the root element 
		//root.addContent(partList);
		
		// Create a part an set id
		//Element part = new Element("part");
		//part.setAttribute("id", "1");			
		
		// Create measure attribute
		/*Element measure = new Element("measure");
		measure.setAttribute("number", "1");
		measure.setAttribute("width", "485");
		
		Element attributes = new Element("attributes");
		
		Element divison = new Element("divisions");
		divison.addContent(new Text("1"));
		
		Element key = new Element("key");
		Element fifths = new Element("fifths");
		fifths.addContent(new Text("0"));
		Element mode = new Element("mode");
		mode.addContent(new Text("major"));
		key.addContent(fifths);
		key.addContent(mode);
		
		attributes.addContent(divison);
		attributes.addContent(key);
		
		Element clef = new Element("clef");
		Element sign = new Element("sign");
		sign.addContent(new Text("TAB"));
		Element line = new Element("line");
		line.addContent(new Text("5"));
		clef.addContent(sign);
		clef.addContent(line);
		
		attributes.addContent(clef);
		this.setTuning(attributes);
		measure.addContent(attributes);*/
		
		// Create note attribute
		/*Element note = new Element("note");
		
		Element pitch = new Element("pitch");
		Element step = new Element("step");
		step.addContent(new Text("C"));
		Element octave = new Element("octave");
		octave.addContent(new Text("4"));
		pitch.addContent(step);
		pitch.addContent(octave);
		
		note.addContent(pitch);
		
		Element duration = new Element("duration");
		duration.addContent(new Text("4"));
		note.addContent(duration);
		
		Element type = new Element("type");
		type.addContent(new Text("quarter"));
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
		this.measure.addContent(note);*/
		
		// Attach the measure to the part
		/*this.part.addContent(this.measure);
		
		// Attach the part to the root
		this.root.addContent(part);
		
		// Attach the DocType to document
		this.doc.setDocType(doctype);
		
		// Attach root to the document
		this.doc.setRootElement(root);
		
		// Set the format for the XML document
		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");
		
		// Write XML to file
		XMLOutputter xmlOut = new XMLOutputter(format);
		try {
			xmlOut.output(doc, new FileOutputStream("file.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}*/

	}
	
	public void finalize(){
		// Attach the measure to the part
		//this.part.addContent(this.measure);
		
		// Attach the part to the root
		this.root.addContent(part);
		
		// Attach the DocType to document
		this.doc.setDocType(doctype);
		
		// Attach root to the document
		this.doc.setRootElement(root);
		
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
	
	public void writeNote(String s, String f){
	
	}
	
	public void writeChord(Vector<Note> chord){
		int tagCount = 0;
		Iterator<Note> it = chord.iterator();
		
		// Create measure attribute
		//this.measure = new Element("measure");
		//this.measure.setAttribute("number", this.measureCount.toString());
		//this.measure.setAttribute("width", "485");
		
		Element m = new Element("measure");
		m.setAttribute("number", this.measureCount.toString());
		m.setAttribute("width", "485");
		
		if(this.measureCount == 1){
			Element attributes = new Element("attributes");
			
			Element divison = new Element("divisions");
			divison.addContent(new Text("1"));
			
			Element key = new Element("key");
			Element fifths = new Element("fifths");
			fifths.addContent(new Text("0"));
			Element mode = new Element("mode");
			mode.addContent(new Text("major"));
			key.addContent(fifths);
			key.addContent(mode);
			
			attributes.addContent(divison);
			attributes.addContent(key);
			
			Element clef = new Element("clef");
			Element sign = new Element("sign");
			sign.addContent(new Text("TAB"));
			Element line = new Element("line");
			line.addContent(new Text("5"));
			clef.addContent(sign);
			clef.addContent(line);
			
			attributes.addContent(clef);
			this.setTuning(attributes);
			//this.measure.addContent(attributes);
			m.addContent(attributes);
		}
		
		
		
		while(it.hasNext()){
			Note n = it.next();
			
			// Create note attribute
			Element note = new Element("note");
			
			if(tagCount > 0){
				Element chordTag = new Element("chord");
				note.addContent(chordTag);
			}
			
			Element pitch = new Element("pitch");
			Element step = new Element("step");
			step.addContent(new Text(n.getName()));
			Element octave = new Element("octave");
			octave.addContent(new Text(n.getOctave().toString()));
			pitch.addContent(step);
			pitch.addContent(octave);
			
			note.addContent(pitch);
			
			Element duration = new Element("duration");
			duration.addContent(new Text("1"));
			note.addContent(duration);
			
			Element type = new Element("type");
			type.addContent(new Text("quarter"));
			note.addContent(type);
			
			// Create notations attribute
		/*	Element notations = new Element("notations");
			
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
			note.addContent(notations);*/
			
			// Attach note to measure
			//this.measure.addContent(note);
			m.addContent(note);
			tagCount++;
		}
		this.measureCount++;
		this.part.addContent(m);
	}
	
	/**
	 * Set tuning of the guitar via staff element
	 * @param attr Attribute to attach the tuning settings (normally the attribute element)
	 */
	private void setTuning(Element attr){
		Element staffDetails = new Element("staff-details");
		Element staffLines = new Element("staff-lines");
		staffLines.addContent(new Text("6"));
		staffDetails.addContent(staffLines);
		
		Element staffTuning_1 = new Element("staff-tuning");
		staffTuning_1.setAttribute("line", "1");
		Element tuningStep_1 = new Element("tuning-step");
		tuningStep_1.addContent(new Text("E"));
		Element tuningOctave_1 = new Element("tuning-octave");
		tuningOctave_1.addContent(new Text("2"));
		staffTuning_1.addContent(tuningStep_1);
		staffTuning_1.addContent(tuningOctave_1);
		staffDetails.addContent(staffTuning_1);
		
		Element staffTuning_2 = new Element("staff-tuning");
		staffTuning_2.setAttribute("line", "2");
		Element tuningStep_2 = new Element("tuning-step");
		tuningStep_2.addContent(new Text("A"));
		Element tuningOctave_2 = new Element("tuning-octave");
		tuningOctave_2.addContent(new Text("2"));
		staffTuning_2.addContent(tuningStep_2);
		staffTuning_2.addContent(tuningOctave_2);
		staffDetails.addContent(staffTuning_2);
		
		Element staffTuning_3 = new Element("staff-tuning");
		staffTuning_3.setAttribute("line", "3");
		Element tuningStep_3 = new Element("tuning-step");
		tuningStep_3.addContent(new Text("D"));
		Element tuningOctave_3 = new Element("tuning-octave");
		tuningOctave_3.addContent(new Text("3"));
		staffTuning_3.addContent(tuningStep_3);
		staffTuning_3.addContent(tuningOctave_3);
		staffDetails.addContent(staffTuning_3);
		
		Element staffTuning_4 = new Element("staff-tuning");
		staffTuning_4.setAttribute("line", "4");
		Element tuningStep_4 = new Element("tuning-step");
		tuningStep_4.addContent(new Text("G"));
		Element tuningOctave_4 = new Element("tuning-octave");
		tuningOctave_4.addContent(new Text("3"));
		staffTuning_4.addContent(tuningStep_4);
		staffTuning_4.addContent(tuningOctave_4);
		staffDetails.addContent(staffTuning_4);
		
		Element staffTuning_5 = new Element("staff-tuning");
		staffTuning_5.setAttribute("line", "5");
		Element tuningStep_5 = new Element("tuning-step");
		tuningStep_5.addContent(new Text("B"));
		Element tuningOctave_5 = new Element("tuning-octave");
		tuningOctave_5.addContent(new Text("3"));
		staffTuning_5.addContent(tuningStep_5);
		staffTuning_5.addContent(tuningOctave_5);
		staffDetails.addContent(staffTuning_5);
		
		Element staffTuning_6 = new Element("staff-tuning");
		staffTuning_6.setAttribute("line", "6");
		Element tuningStep_6 = new Element("tuning-step");
		tuningStep_6.addContent(new Text("E"));
		Element tuningOctave_6 = new Element("tuning-octave");
		tuningOctave_6.addContent(new Text("4"));
		staffTuning_6.addContent(tuningStep_6);
		staffTuning_6.addContent(tuningOctave_6);
		staffDetails.addContent(staffTuning_6);
		
		attr.addContent(staffDetails);
		
	}

}
