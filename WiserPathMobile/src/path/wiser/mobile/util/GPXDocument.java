/**
 * 
 */
package path.wiser.mobile.util;

import path.wiser.mobile.geo.POI;

/**
 * Saves and restores Wiser Path Data as GPX XML.
 * 
 * @author andrewnisbet
 * 
 */
public class GPXDocument implements WPXMLDocument
{

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

}
