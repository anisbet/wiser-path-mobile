/**
 * 
 */
package path.wiser.mobile.geo;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * @author andrewnisbet
 * 
 */
public abstract class POI extends Activity implements LocationListener
{
	protected GPS		gps		= null;

	protected String	title	= "";
	protected String	tags	= "";

	private String		blog	= "";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged( Location arg0 )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
	 */
	@Override
	public void onProviderDisabled( String arg0 )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	@Override
	public void onProviderEnabled( String arg0 )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
	 */
	@Override
	public void onStatusChanged( String arg0, int arg1, Bundle arg2 )
	{

	}

	/**
	 * @return the title
	 */
	public String getPoiTitle()
	{
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setPoiTitle( String title )
	{
		this.title = title;
	}

	/**
	 * @return the tags
	 */
	public String getTags()
	{
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags( String tags )
	{
		this.tags = tags;
	}

	public void setBlog( String blog )
	{
		this.blog = blog;
	}

	public String getBlog()
	{
		return this.blog;
	}

}
