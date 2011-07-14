/**
 * 
 */
package path.wiser.mobile.util;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import path.wiser.mobile.geo.Blog;
import path.wiser.mobile.geo.POI;

/**
 * @author andrewnisbet
 * 
 */
public class KMLDocument
{
	private Document	doc		= null;
	private Element		docRoot	= null;

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

		// Start creating the xml tree.
		Element root = doc.createElement( "kml" );
		root.setAttribute( "xmlns", "http://www.opengis.net/kml/2.2" ); // xmlns="http://www.opengis.net/kml/2.2"
		doc.appendChild( root );
		docRoot = doc.createElement( "Document" );
		doc.appendChild( docRoot );
	}

	public void output( POI poi )
	{
		switch (poi.getType())
		{
		case TRACE:
			outputTrace( poi );
			break;
		case BLOG:
			outputBlog( poi );
			break;
		default:
			outputIncident( poi );
		}

	}

	private void outputIncident( POI poi )
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Output a Blog to XML
	 * 
	 * @param poi
	 */
	private void outputBlog( POI poi )
	{
		// Example of a serialized POI
		// <?xml version="1.0" encoding="UTF-8"?>
		// <kml xmlns="http://www.opengis.net/kml/2.2">
		// <Document>
		// <Placemark>
		// <name>Entity references example</name>
		// <description>
		// &lt;h1&gt;Entity references are hard to type!&lt;/h1&gt;
		// &lt;p&gt;&lt;font color="green"&gt;Text is
		// &lt;i&gt;more readable&lt;/i&gt;
		// and &lt;b&gt;easier to write&lt;/b&gt;
		// when you can avoid using entity references.&lt;/font&gt;&lt;/p&gt;
		// </description>
		// <Point>
		// <coordinates>102.594411,14.998518</coordinates>
		// </Point>
		// </Placemark>
		// </Document>
		// </kml>
		Element placeMark = doc.createElement( "Placemark" );
		// add the title of the blog.
		placeMark.appendChild( getName( poi ) );
		placeMark.appendChild( getDescription( poi ) );
		placeMark.appendChild( getCoordinates( (Blog) poi ) );
		this.docRoot.appendChild( placeMark );
	}

	private void outputTrace( POI poi )
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @param poi a blog poi.
	 * @return XML node of the correctly formed coordinates of the blog.
	 */
	private Node getCoordinates( Blog poi )
	{
		Element point = doc.createElement( "Point" );
		Element coordinates = doc.createElement( "coordinates" );
		Text text = doc.createTextNode( poi.getCoordinates() );
		coordinates.appendChild( text );
		point.appendChild( coordinates );
		return point;
	}

	/**
	 * @param poi
	 * @return XML node of the correctly formed description element.
	 */
	private Node getDescription( POI poi )
	{
		Element description = doc.createElement( "description" );
		Text text = doc.createTextNode( poi.getDescription() );
		description.appendChild( text );
		return description;
	}

	/**
	 * @param poi
	 * @return a node with the correctly formed XML name of the POI.
	 */
	private Node getName( POI poi )
	{
		Element title = doc.createElement( "name" );
		Text text = doc.createTextNode( poi.getPoiTitle() );
		title.appendChild( text );
		return title;
	}

	public void write()
	{
		// Output the XML
		TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans = null;
		try
		{
			trans = transfac.newTransformer();
		}
		catch (TransformerConfigurationException e)
		{
			e.printStackTrace();
		}

		trans.setOutputProperty( OutputKeys.INDENT, "yes" );
		// serialize tree to string
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult( sw );
		DOMSource source = new DOMSource( doc );
		try
		{
			trans.transform( source, result );
		}
		catch (TransformerException e)
		{
			e.printStackTrace();
		}

		System.out.println( sw.toString() );

	}

}
