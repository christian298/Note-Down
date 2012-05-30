package music_notation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.*;

public class MusicXML {
	private XMLStreamWriter writer;
	
	public MusicXML() throws FileNotFoundException, XMLStreamException, FactoryConfigurationError{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// root elements
			Document doc = docBuilder.newDocument();
			// doctype
			DOMImplementation domImpl = doc.getImplementation();
			DocumentType doctype = domImpl.createDocumentType("score-partwise",
				    "-//Recordare//DTD MusicXML 3.0 Partwise//EN",
				    "http://www.musicxml.org/dtds/partwise.dtd");
			doc.appendChild(doctype);
			Element scoreElement = doc.createElement("score-partwise");
			doc.appendChild(scoreElement);
			
			Element rootElement = doc.createElement("technical");
			scoreElement.appendChild(rootElement);
		
			
			// string elements
			Element string = doc.createElement("string");
			string.appendChild(doc.createTextNode("5"));
			rootElement.appendChild(string);
			
			// fret elements
			Element fret = doc.createElement("fret");
			fret.appendChild(doc.createTextNode("3"));
			rootElement.appendChild(fret);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("file.xml"));
	 
			transformer.transform(source, result); 
			System.out.println("File saved!");
			
		} catch (ParserConfigurationException | TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
 		
		/*FileOutputStream fos = new FileOutputStream("test.xml");
		this.writer = XMLOutputFactory.newInstance().createXMLStreamWriter(fos, "UTF-8");
		this.writer.writeStartDocument();
		this.writer.writeDTD("<!DOCTYPE score-partwise PUBLIC \"-//Recordare//DTD MusicXML 3.0 Partwise//EN\" \"http://www.musicxml.org/dtds/partwise.dtd\">");
		this.writer.writeStartElement("score-partwise version=\"3.0\"");*/
	}
	
	public void finalize(){
		try {
			//this.writer.writeEmptyElement("/score-partwise>");
			this.writer.writeEndDocument();			
			this.writer.close();			
		} catch (XMLStreamException e) {			
			e.printStackTrace();
		}
	}
	
	public void writeNote(String s, String f){
		try {
			this.writer.writeStartElement("technical");
			
			this.writer.writeStartElement("string");
		    this.writer.writeCharacters("4");
		    this.writer.writeEndElement();
			
		    this.writer.writeStartElement("fret");
		    this.writer.writeCharacters("1");
		    this.writer.writeEndElement();
		    
		    this.writer.writeEndElement();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	public void writeChord(){
		
	}

}
