/**
 * 
 */
package path.wiser.mobile.util;

import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import path.wiser.mobile.geo.POI;
import path.wiser.mobile.geo.Trace;
import android.util.Log;

/**
 * Saves and restores Wiser Path Data as GPX XML.
 * 
 * @author andrewnisbet
 * 
 */
public class GPXDocument implements WPXMLDocument
{

	private static final String	TAG				= "GPXDocument";
	private static final String	GPX_METADATA	= "metadata";
	private static final String	GPX_NAME		= "name";
	private static final String	GPX_DESCRIPTION	= "desc";
	private static final String	GPX_EXTENSIONS	= "extensions";
	private static final String	WP_APPLICATION	= "WP:application";
	private static final String	WP_ACTIVITY		= "WP:activity";
	private static final String	WP_RELATED_POI	= "WP:relatedPOI";

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.util.WPXMLDocument#setOutput(path.wiser.mobile.geo.POI)
	 */
	@Override
	public void setOutput( POI poi )
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.util.WPXMLDocument#serialize()
	 */
	@Override
	public boolean serialize()
	{
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getAsXMLContent( POI poi )
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
		return null;
	}

	private String createXML( Trace poi )
	{
		Document doc = getNewDoc();
		doc.appendChild( getMetaData( doc, poi ) );
		return null;
	}

	/**
	 * @param doc
	 * @param trace
	 * @return
	 */
	private Node getMetaData( Document doc, Trace trace )
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
		Element root = doc.getDocumentElement();
		Element elementMetaData = doc.createElement( GPX_METADATA );
		elementMetaData.appendChild( createNewElement( doc, GPX_NAME, trace.getTitle() ) );
		elementMetaData.appendChild( createNewElement( doc, GPX_DESCRIPTION, trace.getDescription() ) );
		// add the extensions
		Element elementExtensions = doc.createElement( GPX_EXTENSIONS );
		elementExtensions.appendChild( createNewElement( doc, WP_APPLICATION, "Wiser Path Mobile" ) );
		// TODO elementExtensions.appendChild( createNewElement( doc, WP_ACTIVITY, poi.getActivity() ) );
		setRelatedBlogs( doc, elementExtensions, trace ); // TODO add this kind of functionality to KMLDocuments.
		elementMetaData.appendChild( elementExtensions );
		// TODO create trip data section.
		root.appendChild( elementMetaData );

		return null;
	}

	/**
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
