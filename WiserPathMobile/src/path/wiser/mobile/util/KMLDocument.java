/**
 * 
 */
package path.wiser.mobile.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import path.wiser.mobile.geo.POI;

/**
 * @author andrewnisbet
 * 
 */
public class KMLDocument
{
	private Document	doc	= null;

	public KMLDocument()
	{
		// We need a Document
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try
		{
			docBuilder = dbfac.newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		Document doc = docBuilder.newDocument();

		// //////////////////////
		// Creating the XML tree

		// create the root element and add it to the document
		Element root = doc.createElement( "root" );
		doc.appendChild( root );

		// //create a comment and put it in the root element
		// Comment comment = doc.createComment("Just a thought");
		// root.appendChild(comment);
		//
		// //create child element, add an attribute, and add to root
		// Element child = doc.createElement("child");
		// child.setAttribute("name", "value");
		// root.appendChild(child);
		//
		// //add a text element to the child
		// Text text = doc.createTextNode("Filler, ... I could have had a foo!");
		// child.appendChild(text);
		//
		// /////////////////
		// //Output the XML
		//
		// //set up a transformer
		// TransformerFactory transfac = TransformerFactory.newInstance();
		// Transformer trans = transfac.newTransformer();
		// trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		// trans.setOutputProperty(OutputKeys.INDENT, "yes");
		//
		// //create string from xml tree
		// StringWriter sw = new StringWriter();
		// StreamResult result = new StreamResult(sw);
		// DOMSource source = new DOMSource(doc);
		// trans.transform(source, result);
		// String xmlString = sw.toString();
	}

	public void output( POI myHead )
	{
		// TODO Auto-generated method stub

	}

	public void write()
	{
		// TODO Auto-generated method stub

	}

}
