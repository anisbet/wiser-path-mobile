package path.wiser.mobile.ui;

import java.util.List;

import path.wiser.mobile.R;
import path.wiser.mobile.geo.GPS;
import path.wiser.mobile.geo.POI;
import path.wiser.mobile.geo.WPMapLayerItems;
import path.wiser.mobile.util.MapBlogMVC;
import path.wiser.mobile.util.ModelViewController;
import path.wiser.mobile.util.PoiList;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * This class is the UI for the Wiser Path tab of the application. Note that this class subclasses MapActivity, not
 * {@link path.wiser.mobile.ui.WiserPathHelper}.
 * 
 * @author andrewnisbet
 * 
 */
public class WiserPathActivity extends MapActivity implements LocationListener
{

	private static final String		TAG				= "WiserPathActivity";
	private static final int		ZOOM_SETTING	= 12;									// 1 is world
																							// view.
	private static final GeoPoint	CENTER_POINT	= new GeoPoint( 53545556, -113490000 ); // City hall
																							// Edmonton
																							// where
																							// map opens
	private boolean					mobile			= false;
	private boolean					server			= false;
	private List<Overlay>			map;
	private MapController			mapController;
	@SuppressWarnings("unused")
	private GPS						locationManager;										// it is used it
																							// fires
																							// events for
																							// this
																							// screen.

	// from
	// the
	// map_controls
	// menu.

	/** Called when the activity is first created. */
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.wiserpath_tab );
		MapView mapView = (MapView) findViewById( R.id.mapview );
		mapView.setBuiltInZoomControls( true );

		mapView.setStreetView( true );
		mapController = mapView.getController();
		mapController.setZoom( ZOOM_SETTING );
		mapController.setCenter( CENTER_POINT );
		locationManager = new GPS( this );
		// now create an overlay with a specific Icon. Make new mobileLayers for incidents and traces as well as data
		// from
		// server.
		map = mapView.getOverlays();

		// assign an icon to this overlay.
		// Drawable blogIcon = this.getResources().getDrawable( R.drawable.ic_tee_poi_blue );
		// WPMapLayerItems blogOverlay = new WPMapLayerItems( blogIcon );
		// now keep a reference to the mobileLayers.
		// mobileLayers.put( POI.Type.BLOG, blogOverlay );
		// map.add( blogOverlay );

		// // Now the Incidents
		// // change this to the appropriate icon for an incident.
		// Drawable incidentIcon = this.getResources().getDrawable( R.drawable.icon );
		// WPMapLayerItems incidentsOverlay = new WPMapLayerItems( incidentIcon );
		// mobileLayers.put( POI.Type.INCIDENT, incidentsOverlay );
		// map.add( incidentsOverlay );
		//
		// // Now the Incidents
		// // change this to the appropriate icon for an incident.
		// Drawable traceIcon = this.getResources().getDrawable( R.drawable.icon );
		// WPMapLayerItems traceOverlay = new WPMapLayerItems( traceIcon );
		// mobileLayers.put( POI.Type.TRACE, traceOverlay );
		// map.add( traceOverlay );

		// this places a Wiser path POI 'tee' icon on west ed mall.
		// Drawable blogIcon = this.getResources().getDrawable( R.drawable.ic_tee_poi_blue );
		// WPMapLayerItems blogOverlay = new WPMapLayerItems( blogIcon, MapLayerType.MOBILE_BLOG );
		// GeoPoint point = new GeoPoint( 53522780, -113623052 ); // WGS84 * 1e6
		// OverlayItem overlayitem = new OverlayItem( point, "West Edmonton Mall", "The greatest indoor show on Earth!"
		// );
		// blogOverlay.addOverlayItem( overlayitem );
		// map.add( blogOverlay );

	}

	@Override
	protected boolean isRouteDisplayed()
	{
		// TODO resolve what this does and implement with Trace.
		return false;
	}

	// Menu activity methods here below
	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate( R.menu.map_controls, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		// Handle item selection
		switch (item.getItemId())
		{
		case R.id.use_server_data:
			server = !server;
			item.setChecked( server );
			return viewOnlineData( server );

		case R.id.use_mobile_data:
			mobile = !mobile;
			item.setChecked( mobile );
			return viewMobileData( mobile );

		default:
			return super.onOptionsItemSelected( item );
		}
	}

	/**
	 * @param isDisplayed flag to switch layer on and off.
	 * @return True if the request was successful and false otherwise.
	 */
	private boolean viewMobileData( boolean isDisplayed )
	{
		if (isDisplayed == false)
		{
			return removeMobileDataLayers();
		}
		// get the data stored on the device to do that you need to get the saved POIs
		PoiList poiList = new PoiList( POI.Type.BLOG );
		boolean result = poiList.deserialize();
		if (result == true)
		{
			display( poiList );
		}

		// for each item on the list create a MVC to convert the POI to something useful on the map.
		// TODO test the serialization and deserialization of Traces and Incidents and then un comment this code.
		// poiList = new PoiList( POI.Type.TRACE );
		// result = poiList.deserialize();
		// if (result == true)
		// {
		// display( poiList );
		// }

		poiList = new PoiList( POI.Type.INCIDENT );
		result = poiList.deserialize();
		if (result == true)
		{
			display( poiList );
		}

		return result; // TODO if one fails return false otherwise true. Fix so that result is not reset by previous
						// display calls.
	}

	/**
	 * @return
	 */
	private boolean removeMobileDataLayers()
	{
		// to do this go through all the layers and see if they are mobile layers.
		for (Overlay layer : this.map)
		{
			switch (( (WPMapLayerItems) layer ).getLayerType())
			{
			case MOBILE_BLOG:
			case MOBILE_INCIDENT:
			case MOBILE_TRACE:
				return this.map.remove( layer );
			default:
				return false;
			}
		}
		return false;
	}

	/**
	 * Displays all the POI objects stored on the argument PoiList for reuse.
	 * 
	 * @param poiList
	 * @param whichLayer the layer we are adding the items of the poi list too.
	 */
	private void display( PoiList poiList )
	{
		ModelViewController mvc = null;
		switch (poiList.getType())
		{
		// case TRACE:
		// return new MapTraceMVC( (Trace) poi, this );
		case BLOG:
			mvc = new MapBlogMVC( poiList, this );
			break;
		// case INCIDENT:
		// return new MapIncidentMVC( poiList, this );
		default:
			Log.w( TAG, "Asked for an unknown type of ModelViewController." );
			return;
		}
		mvc.update();
	}

	/**
	 * @param isDisplayed TODO
	 * @return
	 */
	private boolean viewOnlineData( boolean isDisplayed )
	{
		if (isDisplayed == false)
		{
			return false;
		}
		// TODO get data from Wiser Path Online
		// to accomplish that you will have to query Wiser Path for Traces and POIs via the HTTPService
		// You will have to figure out what Wiser wants for input to get the results you want to display.
		// Then you have to parse the return HTML extract the information and convert it to a displayable
		// result in the Google map.

		return false;
	}

	// got this code snippet from http://www.vogella.de/articles/AndroidLocationAPI/article.html#googlemaps_library
	@Override
	public void onLocationChanged( Location location )
	{
		int lat = (int) ( location.getLatitude() * 1E6 );
		int lng = (int) ( location.getLongitude() * 1E6 );
		GeoPoint point = new GeoPoint( lat, lng );
		mapController.animateTo( point ); // mapController.setCenter(point);
	}

	@Override
	public void onProviderDisabled( String provider )
	{
	}

	@Override
	public void onProviderEnabled( String provider )
	{
	}

	@Override
	public void onStatusChanged( String provider, int status, Bundle extras )
	{
	}

	/**
	 * @return the map
	 */
	public final List<Overlay> getMap()
	{
		// This method is used for Model View Controllers.
		return map;
	}
}
