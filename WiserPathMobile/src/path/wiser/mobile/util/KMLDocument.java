/**
 * 
 */
package path.wiser.mobile.util;

import java.io.StringReader;
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
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import path.wiser.mobile.geo.Blog;
import path.wiser.mobile.geo.POI;
import path.wiser.mobile.geo.POI.Type;
import path.wiser.mobile.geo.Trace;
import android.location.Location;
import android.util.Log;

/**
 * @author andrewnisbet
 * 
 */
public class KMLDocument
{
	public enum ExtendedDataType
	{
		IMAGE_PATH, TAGS, IS_INCIDENT
	};

	// How and by what name the serialized documents appear as.
	private static final String	LINE_TYPE					= "userLineType";
	private static final String	TRACE_PATH					= "/trace";
	private static final String	TRACE_FILENAME				= "trace.kml";
	private static final String	BLOG_PATH					= "/blog";
	private static final String	BLOG_FILENAME				= "blog.kml";
	private static final String	INCIDENT_PATH				= "/incident";
	private static final String	INCIDENT_FILENAME			= "incident.kml";
	private static final String	TAG							= "KMLDocument";
	private static final String	KML_DOCUMENT				= "Document";
	private static final String	KML_TITLE					= "name";
	private static final String	KML_DESCRIPTION				= "description";
	private static final String	KML_COORDINATES				= "coordinates";
	private static final int	LATITUDE					= 0;
	private static final int	LONGITUDE					= 1;
	private static final String	KML_DATA_TAG				= "Data";
	private static final String	KML_ATTRIB_NAME				= "name";
	private static final String	KML_TAG_ATTRIB_NAME			= "tag";
	private static final String	KML_VALUE_TAG				= "value";
	private static final String	KML_EXTENDED_DATA			= "ExtendedData";
	private static final String	KML_IMAGEPATH_ATTRIB_NAME	= "imagePath";
	private static final String	KML_DATA					= "Data";
	private static final String	KML_IS_INCIDENT_ATTRIB_NAME	= "isIncident";
	private static final String	KML_LINE_STRING_TAG			= "LineString";
	private static final String	KML_ALTITUDE_MODE			= "altitudeMode";
	private static final String	KML_ALTITUDE_MODE_TEXT		= "relativeToGround";
	private static final String	KML_COORDINATES_TAG			= "coordinates";
	private Document			doc							= null;
	// private Element docRoot = null;
	private boolean				includeStyle				= true;				// true if the line type style needs
																					// to
																					// be
																					// written to the document header
																					// and
																					// false if
																					// that has been done
	private Type				docType;											// type of document to write to file
																					// TRACE BLOG
																					// etc.
	private boolean				isSerializing;

	/**
	 * Use this constructor to when you want to serialize the document to media.
	 * 
	 * @param type of document you want.
	 * @param openForWriting TODO
	 */
	public KMLDocument( Type type, boolean openForWriting )
	{
		this.docType = type;
		// We need a Document
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try
		{
			docBuilder = docBuilderFactory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		doc = docBuilder.newDocument();
		this.isSerializing = openForWriting;
		if (this.isSerializing)
		{
			// Start creating the xml tree.
			Element docRoot = doc.createElement( "kml" );
			docRoot.setAttribute( "xmlns", "http://www.opengis.net/kml/2.2" );
			doc.appendChild( docRoot );
		}
		// else deserializing -- retrieving from media.

	}

	/**
	 * Call this method for each POI and then call the {@link #serialize()} method to write to file.
	 * A POI is not serialized if the {@link POI#isValid()} returns false.
	 * 
	 * @param poi the object to output.
	 */
	public void output( POI poi )
	{
		if (poi == null || poi.isValid() == false)
		{
			return; // this happens for the new element at the end of a list or for non-valid POIs.
		}
		// Create a new document for each POI object.
		Element document = doc.createElement( KML_DOCUMENT );
		Element documentRoot = doc.getDocumentElement();
		if (documentRoot == null)
		{
			Log.e( TAG, "Could not find the document root element." );
			return;
		}

		switch (poi.getType())
		{
		case TRACE:
			outputTrace( poi, document ); // rince and repeat for the other methods.
			break;
		case BLOG:
			outputBlog( poi, document );
			break;
		case INCIDENT:
			outputIncident( poi, document );
			break;
		default:
			Log.e( TAG, "Unknown POI object type, please contact developer for assistance." );
		}

		documentRoot.appendChild( document );

	}

	/**
	 * Outputs an Incident object, which is the equivalent of {@link #outputBlog(POI, Element)}.
	 * 
	 * @param poi
	 * @param documentElement the Document element.
	 */
	private void outputIncident( POI poi, Element documentElement )
	{
		// this is the equivalent of:
		outputBlog( poi, documentElement );
	}

	/**
	 * Output a Blog to XML
	 * 
	 * @param poi
	 * @param documentElement the document element for this, well, document.
	 */
	private void outputBlog( POI poi, Element documentElement )
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
		placeMark.appendChild( getExtendedData( (Blog) poi ) );
		placeMark.appendChild( getCoordinates( (Blog) poi ) );
		documentElement.appendChild( placeMark );
	}

	/**
	 * Adds extended data types to the KML file.
	 * 
	 * @param poi
	 * @return Node of the element externalData.
	 */
	private Node getExtendedData( Blog poi )
	{
		// <ExtendedData>
		// <Data name="imagePath">
		// <value>/path/to/image.jpg</value>
		// </Data>
		// <Data name="tag">
		// <value>LRT,LRT south,LRT downtown,#LRT Edmonton</value>
		// </Data>
		// <!-- incidents use this flag -->
		// <Data name="isIncident">
		// <value>true</value>
		// </Data>
		// </ExtendedData>
		Element extendedData = doc.createElement( KML_EXTENDED_DATA );
		if (poi.hasImage())
		{
			Element dataImage = getExtendedDataElement( ExtendedDataType.IMAGE_PATH, KML_IMAGEPATH_ATTRIB_NAME, poi );
			extendedData.appendChild( dataImage );
		}
		// now add the is incident flag Blogs false Incidents true.
		Element dataIsIncident = getExtendedDataElement( ExtendedDataType.IS_INCIDENT, KML_IS_INCIDENT_ATTRIB_NAME, poi );
		extendedData.appendChild( dataIsIncident );

		Element dataTags = getExtendedDataElement( ExtendedDataType.TAGS, KML_TAG_ATTRIB_NAME, poi );
		extendedData.appendChild( dataTags );

		return extendedData; // which should not be null because there all blogs and incidents have true or false for
								// isIncident.
	}

	/**
	 * @param type type of extended data.
	 * @param attribValue the value for the attribute name="attributeValue"
	 * @param poi the object in question.
	 * @return Data element with child element of value whose text node is the extended data retrieved from the POI
	 *         object.
	 */
	private Element getExtendedDataElement( ExtendedDataType type, String attribValue, POI poi )
	{
		switch (type)
		{
		case IMAGE_PATH:
			Element imageData = doc.createElement( KML_DATA );
			imageData.setAttribute( KML_ATTRIB_NAME, attribValue );
			Element imageValue = doc.createElement( KML_VALUE_TAG );
			Text imageText = doc.createTextNode( ( (Blog) poi ).getImagePath() ); // Blog or incident. Traces don't
																					// have this method.
			imageValue.appendChild( imageText );
			imageData.appendChild( imageValue );
			return imageData;
		case TAGS:
			Element tagsData = doc.createElement( KML_DATA );
			tagsData.setAttribute( KML_ATTRIB_NAME, attribValue );
			Element tagsValue = doc.createElement( KML_VALUE_TAG );
			Text tagsText = doc.createTextNode( poi.getTags().toString() );
			tagsValue.appendChild( tagsText );
			tagsData.appendChild( tagsValue );
			return tagsData;
		case IS_INCIDENT:
			Element isIncidentData = doc.createElement( KML_DATA );
			isIncidentData.setAttribute( KML_ATTRIB_NAME, attribValue );
			Element isIncidentValue = doc.createElement( KML_VALUE_TAG );
			Text isIncidentText = doc.createTextNode( String.valueOf( poi.isIncident() ) );
			isIncidentValue.appendChild( isIncidentText );
			isIncidentData.appendChild( isIncidentValue );
			return isIncidentData;
		default:
			Log.e( TAG, "error in getExtendedDataElement(); writing unknown type to XML." );
			break;
		}
		return null;
	}

	/**
	 * Outputs the Trace specifically.
	 * 
	 * @param poi
	 * @param documentElement the parent document element.
	 */
	private void outputTrace( POI poi, Element documentElement )
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
			documentElement.appendChild( getTraceStyle() );
			this.includeStyle = false;
		}
		Element placeMark = doc.createElement( "Placemark" );
		// add the title of the trace.
		placeMark.appendChild( getName( poi ) );
		placeMark.appendChild( getDescription( poi ) );
		placeMark.appendChild( getExtendedData( (Trace) poi ) );

		// add the styling to the placemark
		Element styleUrl = doc.createElement( "styleUrl" );
		Text text = doc.createTextNode( "#" + LINE_TYPE );
		styleUrl.appendChild( text );
		placeMark.appendChild( styleUrl );

		placeMark.appendChild( getCoordinates( (Trace) poi ) );
		documentElement.appendChild( placeMark );
	}

	/**
	 * Gets the extended data from a Trace object.
	 * 
	 * @param poi trace to get the data from
	 * @return element of externalData and all children.
	 */
	private Node getExtendedData( Trace poi )
	{
		// <ExtendedData>
		// <Data name="tag">
		// <value>LRT,LRT south,LRT downtown,#LRT Edmonton</value>
		// </Data>
		// <!-- incidents use this flag -->
		// <Data name="isIncident">
		// <value>false</value>
		// </Data>
		// </ExtendedData>
		Element extendedData = doc.createElement( KML_EXTENDED_DATA );

		// now add the is incident flag Blogs false Incidents true.
		Element dataIsIncident = getExtendedDataElement( ExtendedDataType.IS_INCIDENT, KML_IS_INCIDENT_ATTRIB_NAME, poi );
		extendedData.appendChild( dataIsIncident );

		Element dataTags = getExtendedDataElement( ExtendedDataType.TAGS, KML_TAG_ATTRIB_NAME, poi );
		extendedData.appendChild( dataTags );

		return extendedData; // which should not be null because there should all POIs answer the question are you an
								// incident?
	}

	/**
	 * @param poi
	 * @return coordinates of the trace as a string of comma separated values lat, long terminated with a new line
	 *         character.
	 */
	private Node getCoordinates( Trace poi )
	{
		// <lineString> contains the altitude and coordinates.
		Element lineString = doc.createElement( KML_LINE_STRING_TAG );
		// Altitude mode
		Element altitudeMode = doc.createElement( KML_ALTITUDE_MODE );
		Text text = doc.createTextNode( KML_ALTITUDE_MODE_TEXT );
		altitudeMode.appendChild( text );
		lineString.appendChild( altitudeMode );
		// now the coordinates
		Element coordinates = doc.createElement( KML_COORDINATES_TAG );
		// -112.2550785337791,36.07954952145647
		// -112.2549277039738,36.08117083492122
		// -112.2552505069063,36.08260761307279
		// ...
		text = doc.createTextNode( poi.getCoordinates() );
		coordinates.appendChild( text );
		lineString.appendChild( coordinates );

		return lineString;
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
	 * Read a single coordinate set from the XML doc and sent them to the Blog object.
	 * 
	 * @param poi a blog poi.
	 * @return XML node of the correctly formed coordinates of the blog.
	 */
	private Node getCoordinates( Blog poi )
	{
		Element point = doc.createElement( "Point" );
		Element coordinates = doc.createElement( KML_COORDINATES );
		Text text = doc.createTextNode( poi.getCoordinates() );
		coordinates.appendChild( text );
		point.appendChild( coordinates );
		return point;
	}

	/**
	 * Creates the KML description elements from the data in the argument poi.
	 * 
	 * @param poi
	 * @return XML node of the correctly formed description element.
	 */
	private Node getDescription( POI poi )
	{
		Element description = doc.createElement( KML_DESCRIPTION );
		Text text = doc.createTextNode( poi.getDescription() );
		description.appendChild( text );
		return description;
	}

	/**
	 * Creates the KML name elements from the data in the argument poi.
	 * 
	 * @param poi
	 * @return a node with the correctly formed XML name of the POI.
	 */
	private Node getName( POI poi )
	{
		Element title = doc.createElement( KML_TITLE );
		Text text = doc.createTextNode( poi.getPoiTitle() );
		title.appendChild( text );
		return title;
	}

	/**
	 * Writes the XML tree to media. To be called after the all the POI objects have {@link #output(POI)}ed.
	 * 
	 * @return true if the document was successfully written and false otherwise.
	 */
	public boolean serialize()
	{
		if (this.isSerializing == false) return false; // not permitted when you requested to deserialize.
		// Output the XML
		TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans = null;
		try
		{
			trans = transfac.newTransformer();
		}
		catch (TransformerConfigurationException e)
		{
			Log.e( TAG, "TransformerConfigurationException " );
			return false;
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
			Log.e( TAG, "TransformerException " );
			return false;
		}
		// for testing
		System.out.println( sw.toString() );
		// now write the data out to a file.
		MediaWriter mediaWriter = new MediaWriter();
		switch (this.docType)
		{
		case TRACE:
			mediaWriter.writeFile( TRACE_PATH, TRACE_FILENAME, sw.toString() );
			return true;
		case BLOG:
			mediaWriter.writeFile( BLOG_PATH, BLOG_FILENAME, sw.toString() );
			return true;
		case INCIDENT:
			mediaWriter.writeFile( INCIDENT_PATH, INCIDENT_FILENAME, sw.toString() );
			return true;
		default:
			Log.e( TAG, "Unknown document type request, contact developer!" );
			break;
		}

		return false;

	}

	/**
	 * Retrieves the list from disk.
	 * 
	 * @param poiList
	 * @return true if the operation was successful and false otherwise.
	 */
	public boolean deserialize( PoiList poiList )
	{
		if (this.isSerializing)
		{
			Log.w( TAG, "Tried to write a document while in read mode." );
			return false;
		}
		DocumentBuilder documentBuilder = null;
		try
		{
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
			return false;
		}

		// read the file and parse it into an XML tree.
		// get the elements and add them to the poiList.
		MediaReader mediaReader = new MediaReader();
		String input = null;
		switch (this.docType)
		{
		case TRACE:
			input = mediaReader.readFile( TRACE_PATH, TRACE_FILENAME );
			break;
		case BLOG:
			input = mediaReader.readFile( BLOG_PATH, BLOG_FILENAME );
			break;
		case INCIDENT:
			input = mediaReader.readFile( INCIDENT_PATH, INCIDENT_FILENAME );
			break;
		default:
			Log.e( TAG, "Unknown document type request to read in, contact developer!" );
			return false;
		}

		if (isInputDocumentParsed( documentBuilder, input ))
		{
			return populatePOIDataFromXMLSource( poiList );
		}

		return false;
	}

	/**
	 * Opens the document for reading as XML and sets the document root element.
	 * 
	 * @param db document builder
	 * @param input xml file as a string
	 * @return true if successful and false otherwise.
	 */
	private boolean isInputDocumentParsed( DocumentBuilder db, String input )
	{
		// File reader returns an empty string if it fails.
		if (input.length() == 0)
		{
			Log.e( TAG, "File was not read because it is empty." );
			return false;
		}

		InputSource is = new InputSource();
		is.setCharacterStream( new StringReader( input ) );
		try
		{
			this.doc = db.parse( is );
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Parses the XML file and fills in the data of the PoiList which will create new objects as necessary.
	 * 
	 * @param poiList the linked list of POIs to add new objects to.
	 * @return true if the parsing was completed successfully and false otherwise.
	 */
	private boolean populatePOIDataFromXMLSource( PoiList poiList )
	{
		Element documentRoot = this.doc.getDocumentElement();
		if (documentRoot == null)
		{
			Log.e( TAG, "Error parsing the blogs XML document -- no root element." );
			return false;
		}
		NodeList nodeList = documentRoot.getElementsByTagName( KML_DOCUMENT );

		if (nodeList != null && nodeList.getLength() > 0)
		{
			POI poi = poiList.getCurrent();
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				Element element = (Element) nodeList.item( i );
				poi.setTitle( getTextValue( element, KML_TITLE ) );
				poi.setDescription( getTextValue( element, KML_DESCRIPTION ) );
				setExtendedData( poi, element );
				switch (poiList.getType())
				{
				case TRACE:
					setTraceLocations( (Trace) poi, getTextValue( element, KML_COORDINATES ) );
					break;
				case BLOG: // do same things for both
				case INCIDENT:
					setBlogLocation( (Blog) poi, getTextValue( element, KML_COORDINATES ) );
					break;
				default:
					Log.e( TAG, "Unknown datatype." );
					return false;
				}
				// add a new node if needed.
				if (i < nodeList.getLength() - 1)
				{
					poi = poiList.add();
				}
			}
		}
		return true;
	}

	/**
	 * Sets the Blog's Location.
	 * 
	 * @param blog
	 * @param textValue the text from the coordinates tag.
	 */
	private void setBlogLocation( Blog blog, String textValue )
	{

		// for each pair split them on the ',' and apply to a new location.
		Location location = null;

		String[] thisCoordinate = textValue.split( "," );
		if (thisCoordinate.length > 1) // in the future this could be length 3 if altitude is captured too.
		{
			// this creates a new Location with no location provider. Normally when collecting data
			// you would specify GPS but not when you restore.
			location = new Location( (String) null );
			location.setLatitude( Double.parseDouble( thisCoordinate[LATITUDE] ) );
			location.setLongitude( Double.parseDouble( thisCoordinate[LONGITUDE] ) );
			blog.setLocation( location );
		}
		else
		{
			Log.e( TAG,
				"Failed to load Blog coordinates from media due to a formatting error of the coordinates pairs in XML document, or they are missing." );
		}

	}

	/**
	 * Reads all the extended data from the XML and populate the POI.
	 * 
	 * @param poi the poi to populate.
	 * @param element the ExtendedData tag or a child of it.
	 */
	private void setExtendedData( POI poi, Element element )
	{
		// look for the extended data type in the xml and extract the tags. Looks like:
		// <ExtendedData>
		// <Data name="imagePath">
		// <value>/path/to/image.jpg</value>
		// </Data>
		// <Data name="tag">
		// <value>LRT,LRT south,LRT downtown,#LRT Edmonton</value>
		// <!-- incidents use this flag -->
		// <Data name="isIncident">
		// <value>true</value>
		// </Data>
		// </ExtendedData>
		NodeList nodeList = element.getElementsByTagName( KML_DATA_TAG );

		if (nodeList != null && nodeList.getLength() > 0)
		{
			// search each of the child Data elements.
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				// get the Item element
				Element dataElement = (Element) nodeList.item( i );
				// get the Data tag's attribute "name"
				String whichDataElement = dataElement.getAttribute( KML_ATTRIB_NAME );
				// and see if it is "tag"
				if (whichDataElement.matches( KML_TAG_ATTRIB_NAME ))
				{
					Tags tags = new Tags( getTextValue( dataElement, KML_VALUE_TAG ) );
					poi.setTags( tags );
					return;
				}
				else
					if (whichDataElement.matches( KML_IMAGEPATH_ATTRIB_NAME ))
					{
						// this will throw a ClassCastException if the argument poi was not the expected type!
						( (Blog) poi ).setImagePath( getTextValue( dataElement, KML_IMAGEPATH_ATTRIB_NAME ) );
					}
					else
						if (whichDataElement.matches( KML_IS_INCIDENT_ATTRIB_NAME ))
						// You will need to extend this if statement if you decide to include more extended data.
						{
							poi.setIsIncident( getTextValue( dataElement, KML_IMAGEPATH_ATTRIB_NAME ) );
						}
			}
		}
	}

	/**
	 * Sets the locations for the argument trace.
	 * 
	 * @param trace the trace that needs locations added to it.
	 * @param textValue the value (child text node in DOM speak) of the coordinate tag.
	 */
	private void setTraceLocations( Trace trace, String textValue )
	{
		// the coordinates look like this:
		// -112.2549277039738,36.08117083492122
		// -112.2552505069063,36.08260761307279
		// -112.2564540158376,36.08395660588506
		// -112.2580238976449,36.08511401044813
		// ...
		String[] strCoordinates = textValue.split( "\\n" ); // breaks them into lines
		// for each pair split them on the ',' and apply to a new location.
		Location location = null;
		for (int i = 0; i < strCoordinates.length; i++)
		{
			String[] thisCoordinate = strCoordinates[i].split( "," );
			if (thisCoordinate.length > 1)
			{
				// this creates a new Location with no location provider. Normally when collecting data
				// you would specify GPS but not when you restore.
				location = new Location( (String) null );
				location.setLatitude( Double.parseDouble( thisCoordinate[LATITUDE] ) );
				location.setLongitude( Double.parseDouble( thisCoordinate[LONGITUDE] ) );
				trace.setLocation( location );
			}
			else
			{
				Log.e( TAG, "Failed to load trace coordinates from media due to a formatting error of the coordinates pairs in XML document." );
			}
		}

	}

	/**
	 * @param ele a parent element beneath which the tagName is to be found.
	 * @param tagName of the child element of ele.
	 * @return String of text of the element that contains a tag of tagName.
	 */
	private String getTextValue( Element ele, String tagName )
	{
		String text = null;
		NodeList nodeList = ele.getElementsByTagName( tagName );
		if (nodeList != null && nodeList.getLength() > 0)
		{
			Element element = (Element) nodeList.item( 0 );
			if (element.getFirstChild() != null)
			{
				text = element.getFirstChild().getNodeValue();
			}
		}

		return text;
	}

}
