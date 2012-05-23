package music_notation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class MusicXML {
	private XMLStreamWriter writer;
	
	public MusicXML() throws FileNotFoundException, XMLStreamException, FactoryConfigurationError{
		FileOutputStream fos = new FileOutputStream("test.xml");
		this.writer = XMLOutputFactory.newInstance().createXMLStreamWriter(fos, "UTF-8");
		this.writer.writeDTD("<!DOCTYPE score-partwise PUBLIC \"-//Recordare//DTD MusicXML 3.0 Partwise//EN\" \"http://www.musicxml.org/dtds/partwise.dtd\"");
	}
	
	public void finalize(){
		try {
			this.writer.close();
		} catch (XMLStreamException e) {			
			e.printStackTrace();
		}
	}
	
	public void writeNote(String s, String f){
		try {
			this.writer.writeStartElement("technical");
			this.writer.writeAttribute("string", "4");
			this.writer.writeAttribute("fret", "1");
			this.writer.writeEndElement();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	public void writeChord(){
		
	}

}
