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
import path.wiser.mobile.geo.Trace;

/**
 * @author andrewnisbet
 * 
 */
public class KMLDocument
{
	private static final String	LINE_TYPE		= "userLineType";
	private Document			doc				= null;
	private Element				docRoot			= null;
	private boolean				includeStyle	= true;

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
		// <?xml version="1.0" encoding="UTF-8"?>
		// <kml xmlns="http://www.opengis.net/kml/2.2">
		// <Document>
		// <Style id="userLineType">
		// <LineStyle>
		// <color>7f00ffff</color>
		// <width>1</width>
		// </LineStyle>
		// <PolyStyle>
		// <color>7f00ffff</color>
		// </PolyStyle>
		// </Style>
		// <Placemark>
		// <name>My Favourite Trace</name>
		// <description>Best Route through River Valley</description>
		// <styleUrl>#userLineType</styleUrl>
		// <LineString>
		// <altitudeMode>relativeToGround</altitudeMode>
		// <!-- tack on ',<altitude>' for traces with altitude -->
		// <coordinates> -112.2550785337791,36.07954952145647
		// -112.2549277039738,36.08117083492122
		// -112.2552505069063,36.08260761307279
		// -112.2564540158376,36.08395660588506
		// -112.2580238976449,36.08511401044813
		// -112.2595218489022,36.08584355239394
		// -112.2608216347552,36.08612634548589
		// -112.262073428656,36.08626019085147
		// -112.2633204928495,36.08621519860091
		// -112.2644963846444,36.08627897945274
		// -112.2656969554589,36.08649599090644
		// </coordinates>
		// </LineString>
		// </Placemark>
		// </Document>
		// </kml>
		if (this.includeStyle)
		{
			this.docRoot.appendChild( getTraceStyle() );
			this.includeStyle = false;
		}
		Element placeMark = doc.createElement( "Placemark" );
		// add the title of the trace.
		placeMark.appendChild( getName( poi ) );
		placeMark.appendChild( getDescription( poi ) );
		// add the styling for the line type.
		Element styleUrl = doc.createElement( "styleUrl" );
		Text text = doc.createTextNode( "#" + LINE_TYPE );
		styleUrl.appendChild( text );
		placeMark.appendChild( styleUrl );

		Element lineString = doc.createElement( "LineString" );
		// Altitude mode
		Element altitudeMode = doc.createElement( "altitudeMode" );
		text = doc.createTextNode( "relativeToGround" );
		altitudeMode.appendChild( text );
		lineString.appendChild( altitudeMode );
		// now the coordinates
		Element coordinates = doc.createElement( "coordinates" );
		coordinates.appendChild( getCoordinates( (Trace) poi ) );
		lineString.appendChild( coordinates );
		placeMark.appendChild( lineString );

		this.docRoot.appendChild( placeMark );
	}

	private Node getCoordinates( Trace poi )
	{
		// -112.2550785337791,36.07954952145647
		// -112.2549277039738,36.08117083492122
		// -112.2552505069063,36.08260761307279
		// ...
		Text text = doc.createTextNode( poi.getCoordinates() );

		return text;
	}

	/**
	 * Note that you need only call this once per document.
	 * 
	 * @return a line style for traces.
	 */
	private Node getTraceStyle()
	{
		// <Style id="userLineType">
		// <LineStyle>
		// <color>7f00ffff</color>
		// <width>1</width>
		// </LineStyle>
		// <PolyStyle>
		// <color>7f00ffff</color>
		// </PolyStyle>
		// </Style>
		Element style = doc.createElement( "Style" );
		style.setAttribute( "id", LINE_TYPE );
		Element lineStyle = doc.createElement( "LineStyle" );
		Element colour = doc.createElement( "color" );
		Text text = doc.createTextNode( "7f00ffff" );
		colour.appendChild( text );
		lineStyle.appendChild( colour );
		Element width = doc.createElement( "width" );
		text = doc.createTextNode( "1" );
		width.appendChild( text );
		lineStyle.appendChild( width );
		style.appendChild( lineStyle );
		// now for the PolyStyle
		Element polyStyle = doc.createElement( "PolyStyle" );
		colour = doc.createElement( "color" );
		text = doc.createTextNode( "7f00ffff" );
		colour.appendChild( text );
		polyStyle.appendChild( colour );
		style.appendChild( style );

		return style;
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

	/**
	 * Writes the XML tree to media.
	 */
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
		// TODO write XML to somewhere more useful.
		System.out.println( sw.toString() );

	}

	/**
	 * Retrieves the list from disk.
	 * 
	 * @param poiList
	 * @return true if the operation was successful and false otherwise.
	 */
	public boolean deserialize( PoiList poiList )
	{
		// read the file and parse it into an XML tree.
		// get the elements and add them to the poiList.
		return false;
	}

}
