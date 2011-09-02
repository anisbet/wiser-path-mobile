/**
 * 
 */
package path.wiser.mobile.ui;

import android.graphics.Point;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

/**
 * Converts Locations to Points.
 * 
 * @author andrew
 * 
 */
public class CanvasPoint extends Point
{

	/**
	 * @param location converted from degrees to integer.
	 */
	public CanvasPoint( Location location )
	{
		super( (int) ( location.getLatitude() * 1E6 ), (int) ( location.getLongitude() * 1E6 ) );
	}

	/**
	 * Creates a point with no conversion.
	 * 
	 * @param x
	 * @param y
	 */
	public CanvasPoint( int x, int y )
	{
		super( x, y );
	}

	public CanvasPoint()
	{
		super();
	}

	/**
	 * Converts the GeoPoint to a location on the screen.
	 * 
	 * @param mapView
	 * @param point
	 */
	public CanvasPoint( MapView mapView, GeoPoint point )
	{
		mapView.getProjection().toPixels( point, this );
	}

	/**
	 * Takes a location from the GPS and converts it to the screen pixel location.
	 * 
	 * @param mapView
	 * @param location
	 */
	public CanvasPoint( MapView mapView, Location location )
	{
		GeoPoint point = new GeoPoint( (int) ( location.getLatitude() * 1E6 ), (int) ( location.getLongitude() * 1E6 ) );
		mapView.getProjection().toPixels( point, this );
	}

	/**
	 * @return x coordinate of point.
	 */
	public float getX()
	{
		return (float) super.x;
	}

	/**
	 * @return y coordinate of point.
	 */
	public float getY()
	{
		return (float) super.y;
	}

}
