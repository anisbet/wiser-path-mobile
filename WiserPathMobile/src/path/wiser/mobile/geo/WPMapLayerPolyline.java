/**
 * 
 */
package path.wiser.mobile.geo;

import android.graphics.drawable.Drawable;

import com.google.android.maps.OverlayItem;

/**
 * At the time of the writing of this application Google does not have a polyline API for Android
 * and much of the research that I found avoided the topic by giving solutions in Javascript. There is an
 * API for that. Anyway this draws points along the line between two POIs using Bresenham (1962).
 * 
 * @author andrew nisbet
 * 
 */
public class WPMapLayerPolyline extends WPMapLayerPoints
{

	private static final int	FIRST_ITEM_INDEX	= 0;

	/**
	 * @param defaultMarker
	 * @param type
	 */
	public WPMapLayerPolyline( Drawable defaultMarker, MapLayerType type )
	{
		super( defaultMarker, type ); // there is error of 2 px because the super class binds icon bottom center.
	}

	@Override
	public void addOverlayItem( OverlayItem item )
	{
		int last = overlayItems.size() - 1;
		// get the last item on the list and if there is one fill the space between with points
		// to make it look like a line.
		if (last >= FIRST_ITEM_INDEX)
		{
			OverlayItem lastItem = overlayItems.get( last );
			drawLine( lastItem, item );
		}
		else
		{
			super.addOverlayItem( item );
		}
	}

	private void drawLine( OverlayItem previousPoint, OverlayItem currentPoint )
	{

	}

	// //////////////////////// Naive Implementation with points. //////////////////////////////////

	// /**
	// * Draws a line of GeoPoints on the map because the Android API doesn't support
	// * polylines as of version 8 of Google API. It uses the Bresenham optimized
	// * line drawing algorithm.
	// *
	// * @param previousPoint
	// * @param currentPoint
	// */
	// private void drawLine( OverlayItem previousPoint, OverlayItem currentPoint )
	// {
	// // compute diagonal between two point with reasonable zoom factor so we don't draw too many points.
	// // for now just draw a bunch of points.
	// int x0 = previousPoint.getPoint().getLatitudeE6();
	// int y0 = previousPoint.getPoint().getLongitudeE6();
	// int x1 = currentPoint.getPoint().getLatitudeE6();
	// int y1 = currentPoint.getPoint().getLongitudeE6();
	// // perform Bresenham (1962).
	// int dx = Math.abs( x1 - x0 );
	// int dy = Math.abs( y1 - y0 );
	// int sx, sy;
	// if (x0 < x1)
	// {
	// sx = 1;
	// }
	// else
	// {
	// sx = -1;
	// }
	//
	// if (y0 < y1)
	// {
	// sy = 1;
	// }
	// else
	// {
	// sy = -1;
	// }
	//
	// int error = dx - dy;
	//
	// while (true)
	// {
	// setPixel( x0, y0 );
	//
	// if (( x0 == x1 ) && ( y0 == y1 ))
	// {
	// break;
	// }
	// int e2 = 2 * error;
	// if (e2 > -dy)
	// {
	// error = error - dy;
	// x0 = x0 + sx;
	// }
	// if (e2 < dx)
	// {
	// error = error + dx;
	// y0 = y0 + sy;
	// }
	// }
	// }
	//
	// private void setPixel( int x, int y )
	// {
	// GeoPoint point = new GeoPoint( x, y );
	// super.addOverlayItem( new OverlayItem( point, "", "" ) );
	// }
}
