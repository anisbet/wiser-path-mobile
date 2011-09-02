/**
 * 
 */
package path.wiser.mobile.geo;

import java.util.Vector;

import path.wiser.mobile.ui.CanvasPoint;
import path.wiser.mobile.ui.LineStyle;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * Draws the lines of a polyline as a Overlay. Each trace is in effect an overlay in the map.
 * 
 * @author andrew
 * 
 */
public class Polyline extends Overlay
{
	private static final float	TEXT_OFFSET	= 9;
	private LineStyle			style		= null;
	private Trace				trace		= null;

	/**
	 * @param trace to convert to polyline
	 */
	public Polyline( Trace trace )
	{
		init( null );
		this.trace = trace;
	}

	/**
	 * @param style of the polyline.
	 * @param trace
	 */
	public Polyline( LineStyle style, Trace trace )
	{
		init( style );
		this.trace = trace;
	}

	/**
	 * Creates a polyline with a specific line style.
	 * 
	 * @param style
	 */
	private void init( LineStyle style )
	{
		if (style != null)
		{
			this.style = style;
		}
		else
		{
			this.style = new LineStyle();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.Overlay#draw(android.graphics.Canvas, com.google.android.maps.MapView, boolean)
	 */
	@Override
	public void draw( Canvas canvas, MapView mapView, boolean shadow )
	{
		super.draw( canvas, mapView, shadow );
		// Setup our "brush"/"pencil"/ whatever...
		Paint paint = new Paint();
		this.style.setPaintAttributes( paint );

		// Draw our title
		// create an empty point the mapview will create the conversion for projection to the screen.
		// Converts lat/lng-Point to OUR coordinates on the screen.
		CanvasPoint title = new CanvasPoint( mapView, trace.getInitialLocation() );
		canvas.drawText( this.trace.title, title.getX() + TEXT_OFFSET, title.getY(), paint );

		// now draw the segments of the polyline.
		Vector<Location> locations = trace.getLocations();
		CanvasPoint previous = new CanvasPoint( mapView, locations.firstElement() );
		CanvasPoint current = null;
		for (int i = 1; i < locations.size(); i++)
		{
			current = new CanvasPoint( mapView, locations.get( i ) );
			canvas.drawLine( previous.getX(), previous.getY(), current.getX(), current.getY(), paint );
			previous = current;
		}
	}
}
