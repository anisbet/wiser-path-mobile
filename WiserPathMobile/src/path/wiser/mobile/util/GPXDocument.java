/**
 * 
 */
package path.wiser.mobile.util;

import java.io.StringWriter;
import java.util.Vector;

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

import path.wiser.mobile.WPEnvironment;
import path.wiser.mobile.geo.POI;
import path.wiser.mobile.geo.POI.PoiType;
import path.wiser.mobile.geo.Trace;
import path.wiser.mobile.services.HTTPService;
import android.location.Location;
import android.util.Log;

/**
 * Saves and restores Wiser Path Data as GPX XML.
 * 
 * @author andrewnisbet
 * 
 */
public class GPXDocument implements WPXMLDocument
{

	private static final String	TAG					= "GPXDocument";
	private static final String	GPX_METADATA		= "metadata";
	private static final String	GPX_NAME			= "name";
	private static final String	GPX_DESCRIPTION		= "desc";
	private static final String	GPX_EXTENSIONS		= "extensions";
	private static final String	GPX_TRACK			= "trk";
	private static final String	GPX_TRACK_SEQUENCE	= "trkseg";
	private static final String	GPX_TRACK_POINT		= "trkpt";
	private static final String	GPX_ELEVATION		= "ele";
	private static final String	GPX_TIME			= "time";

	// private static final String WP_ACTIVITY = "WP:activity";
	private static final String	WP_RELATED_POI		= "WP:relatedPOI";
	private static final String	WP_APPLICATION		= "WP:application";
	private static final String	WP_IS_INCIDENT		= "WP:isIncident";
	private static final String	WP_TAGS				= "WP:tags";
	// private static final String TRACE_PATH = "/trace";
	public static final String	TRACE_FILENAME		= "trace.gpx";
	private static final String	BLOG_FILENAME		= "blog.gpx";
	private static final String	INCIDENT_FILENAME	= "incident.gpx";
	private PoiType				poiType;
	private boolean				isSerializing;
	private Node				doc;

	/**
	 * Used for uploading by {@link HTTPService}.
	 */
	public GPXDocument()
	{
		// Required but empty.
	}

	public GPXDocument( PoiType type, boolean openForWriting )
	{
		this.poiType = type;
		this.isSerializing = openForWriting;
		Document doc = getNewDoc();
		if (this.isSerializing)
		{
			// Start creating the xml tree.
			Element docRoot = doc.createElement( "gpx" );
			docRoot.setAttribute( "xmlns:WP", "http://wiserpath.org/xml_ns/1_0" );
			docRoot.setAttribute( "creator", "WiserPathMobile http://wiserpath.org" );
			doc.appendChild( docRoot );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.util.WPXMLDocument#setOutput(path.wiser.mobile.geo.POI)
	 */
	@Override
	public void setOutput( POI poi )
	{
		// TODO See KMLDocument example.

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.util.WPXMLDocument#serialize()
	 */
	@Override
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
		DOMSource source = new DOMSource( this.doc );
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
		switch (this.poiType)
		{
		case TRACE:
			mediaWriter.writeTextFile( WPEnvironment.TRACE_PATH, TRACE_FILENAME, sw.toString() );
			return true;
		case BLOG:
			mediaWriter.writeTextFile( WPEnvironment.BLOG_PATH, BLOG_FILENAME, sw.toString() );
			return true;
		case INCIDENT:
			mediaWriter.writeTextFile( WPEnvironment.INCIDENT_PATH, INCIDENT_FILENAME, sw.toString() );
			return true;
		default:
			Log.e( TAG, "Unknown document type request, contact developer!" );
			break;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.util.WPXMLDocument#deserialize(path.wiser.mobile.util.PoiList)
	 */
	@Override
	public boolean deserialize( PoiList poiList )
	{
		// TODO See KMLDocument example.
		return false;
	}

	@Override
	public String getXMLContent( POI poi )
	{
		switch (poi.getType())
		{
		case TRACE:
			return createXML( (Trace) poi );
		case BLOG:
			break;
		case INCIDENT:
			break;
		default:
			Log.e( TAG, "Undefined POI type." );
		}
		return "";
	}

	private String createXML( Trace poi )
	{
		Document doc = getNewDoc();
		doc.appendChild( setMetaData( doc, poi ) );
		doc.appendChild( getTripData( doc, poi ) );
		// convert the doc to a string.
		return getDocumentAsString( doc );
	}

	/**
	 * Returns the document as an XML string.
	 * 
	 * @param doc
	 * @return XML string of the document.
	 */
	private String getDocumentAsString( Document doc )
	{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = null;
		try
		{
			transformer = transformerFactory.newTransformer();
		}
		catch (TransformerConfigurationException e)
		{
			Log.e( TAG, "TransformerConfigurationException " );
			return "";
		}

		transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
		// serialize tree to string
		StringWriter stringWriter = new StringWriter();
		StreamResult result = new StreamResult( stringWriter );
		DOMSource source = new DOMSource( doc );
		try
		{
			transformer.transform( source, result );
		}
		catch (TransformerException e)
		{
			Log.e( TAG, "TransformerException " );
			return "";
		}
		// for testing
		return stringWriter.toString();
	}

	/**
	 * Converts and returns an element containing all the locations as XML.
	 * 
	 * @param doc
	 * @param poi
	 * @return
	 */
	private Node getTripData( Document doc, Trace poi )
	{
		// TODO create trip data section.
		// <trk>
		// <name>Lap 1</name>
		// <extensions>
		// <TO:calories>118</TO:calories>
		// </extensions>
		// <trkseg>
		// <trkpt lat="53.517711" lon="-113.545767">
		// <ele>626</ele>
		// <time>2010-07-14T09:15:30</time>
		// </trkpt>
		// <trkpt lat="53.517738" lon="-113.545757">
		// <ele>626</ele>
		// <time>2010-07-14T09:15:33</time>
		// <type>Resting</type>
		// </trkpt>
		// </trkseq>
		// </trk>
		Element elementTrip = doc.createElement( GPX_TRACK );
		elementTrip.appendChild( createNewElement( doc, GPX_NAME, poi.getTitle() ) );
		// <trkseg>
		Element elementTripSequence = doc.createElement( GPX_TRACK_SEQUENCE );
		Vector<android.location.Location> locations = poi.getLocations();
		for (Location location : locations)
		{
			elementTripSequence.appendChild( getTrackPoint( doc, location ) );
		}
		elementTrip.appendChild( elementTripSequence );

		return elementTrip;
	}

	/**
	 * Creates a trip sequence for each location.
	 * 
	 * @param doc
	 * @param location
	 * @return trkpt element.
	 */
	private Node getTrackPoint( Document doc, Location location )
	{
		// <trkpt lat="53.517711" lon="-113.545767">
		// <ele>626</ele>
		// <time>2010-07-14T09:15:30</time>
		// </trkpt>
		Element elementTrackPoint = doc.createElement( GPX_TRACK_POINT );
		elementTrackPoint.setAttribute( "lat", String.valueOf( location.getLatitude() ) );
		elementTrackPoint.setAttribute( "lon", String.valueOf( location.getLongitude() ) );
		elementTrackPoint.appendChild( createNewElement( doc, GPX_ELEVATION, String.valueOf( location.getAltitude() ) ) );
		elementTrackPoint.appendChild( createNewElement( doc, GPX_TIME, String.valueOf( location.getTime() ) ) );
		return elementTrackPoint;
	}

	/**
	 * @param doc
	 * @param poi
	 * @return Element metadata.
	 */
	private Node setMetaData( Document doc, POI poi )
	{
		// <metadata>
		// <name>Road Biking 14 Jul</name>
		// <desc />
		// <link href="http://www.trimbleoutdoors.com/ViewTrip/806143">
		// <text>View this Trip at TrimbleOutdoors.com!</text>
		// </link>
		// <time>2010-07-14T09:15:08</time>
		// <extensions>
		// <TO:app name="AllSportGPS" device="9700" carrier="Rogers Wireless" manufacturer="Research In Motion" />
		// <TO:tripID>806143</TO:tripID>
		// <TO:activity>Bicycling</TO:activity>
		// <TO:calories>118</TO:calories>
		// <TO:totalDistance>3.65</TO:totalDistance>
		// <TO:averageSpeed>12.54</TO:averageSpeed>
		// <TO:averagePace>PT4M47.083S</TO:averagePace>
		// <TO:durationTotal>PT23M17S</TO:durationTotal>
		// <TO:durationActive>PT16M22S</TO:durationActive>
		// <TO:durationResting>PT2M53S</TO:durationResting>
		// <TO:durationUnaccounted>PT4M2S</TO:durationUnaccounted>
		// <TO:elevationGain>81</TO:elevationGain>
		// <TO:elevationLoss>24</TO:elevationLoss>
		// <TO:shared>False</TO:shared>
		// </extensions>
		// </metadata>
		Element elementMetaData = doc.createElement( GPX_METADATA );
		elementMetaData.appendChild( createNewElement( doc, GPX_NAME, poi.getTitle() ) );
		elementMetaData.appendChild( createNewElement( doc, GPX_DESCRIPTION, poi.getDescription() ) );
		// add the extensions
		Element elementExtensions = doc.createElement( GPX_EXTENSIONS );
		elementExtensions.appendChild( createNewElement( doc, WP_APPLICATION, "Wiser Path Mobile" ) );
		// TODO elementExtensions.appendChild( createNewElement( doc, WP_ACTIVITY, poi.getActivity() ) );
		setTags( doc, elementExtensions, poi );
		elementExtensions.appendChild( createNewElement( doc, WP_IS_INCIDENT, String.valueOf( poi.isIncident() ) ) );
		// only traces can have associated blogs or incidents.
		if (poi.getType() == PoiType.TRACE)
		{
			// TODO add this kind of functionality to KMLDocuments.
			setRelatedBlogs( doc, elementExtensions, (Trace) poi );
		}
		elementMetaData.appendChild( elementExtensions );

		return elementMetaData;
	}

	/**
	 * Sets the document's tags.
	 * 
	 * @param doc
	 * @param elementExtensions
	 * @param poi
	 */
	private void setTags( Document doc, Element elementExtensions, POI poi )
	{
		Tags tags = poi.getTags();
		Node elementTags = createNewElement( doc, WP_TAGS, tags.toString() );
		elementExtensions.appendChild( elementTags );
	}

	/**
	 * Sets the reference values for Pois that were created during the trace.
	 * 
	 * @param document
	 * @param elementExtensions
	 * @param trace
	 */
	private void setRelatedBlogs( Document document, Element elementExtensions, Trace trace )
	{
		if (trace.hasRelatedPOIs() == false)
		{
			return; // nothing gets added.
		}

		Vector<POI> includedPois = trace.getPois();
		for (POI poi : includedPois)
		{
			Node elementPoi = createNewElement( document, WP_RELATED_POI, poi.getID() );
			elementExtensions.appendChild( elementPoi );
		}
	}

	/**
	 * @param document
	 * @param elementName of the element
	 * @param content to put in the element.
	 * @return Element
	 */
	private Node createNewElement( Document document, String elementName, String content )
	{
		Element element = document.createElement( elementName );
		// element.setAttribute( KML_ATTRIB_NAME, attribValue );
		if (content != null && content.length() > 0)
		{
			Text text = document.createTextNode( content );
			element.appendChild( text );
		}
		return element;
	}

	/**
	 * Creates a new gpx document.
	 * 
	 * @return Document
	 */
	private Document getNewDoc()
	{
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
		Document doc = docBuilder.newDocument();

		// Start creating the xml tree.
		// <gpx xmlns:TO="http://www.trimbleoutdoors.com/WebServices/Api/1/0" version="1.1"
		// xmlns="http://www.topografix.com/GPX/1/1">
		Element docRoot = doc.createElement( "gpx" );
		docRoot.setAttribute( "xmlns", "http://www.topografix.com/GPX/1/1" );
		docRoot.setAttribute( "creator", "Wiser Path Mobile - http://wiserpath.bus.ualberta.ca" );
		docRoot.setAttribute( "xmlns:WP", "http://wiserpath.bus.ualberta.ca/gpxns/1" );
		doc.appendChild( docRoot );

		return doc;
	}

}
