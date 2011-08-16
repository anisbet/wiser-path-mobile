package path.wiser.mobile.util;

import path.wiser.mobile.geo.POI;

public interface WPXMLDocument
{

	/**
	 * Call this method for each POI and then call the {@link #serialize()} method to write to file.
	 * A POI is not serialized if the {@link POI#isValid()} returns false.
	 * 
	 * @param poi the object to output.
	 */
	public abstract void setOutput( POI poi );

	/**
	 * Writes the XML tree to media. To be called after the all the POI objects have {@link #setOutput(POI)}ed.
	 * 
	 * @return true if the document was successfully written and false otherwise.
	 */
	public abstract boolean serialize();

	/**
	 * Retrieves the list from disk.
	 * 
	 * @param poiList
	 * @return true if the operation was successful and false otherwise.
	 */
	public abstract boolean deserialize( PoiList poiList );

}