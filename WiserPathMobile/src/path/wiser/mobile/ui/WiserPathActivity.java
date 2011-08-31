package path.wiser.mobile.ui;

import java.util.List;

import path.wiser.mobile.R;
import path.wiser.mobile.geo.GPS;
import path.wiser.mobile.geo.MapMultiPointMVC;
import path.wiser.mobile.geo.MapSinglePointMVC;
import path.wiser.mobile.geo.POI;
import path.wiser.mobile.geo.WPMapLayerPoints;
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
	// 1 is world view.
	private static final int		ZOOM_SETTING	= 12;
	// City hall, Edmonton where map opens
	private static final GeoPoint	CENTER_POINT	= new GeoPoint( 53545556, -113490000 );
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.MapActivity#onCreate(android.os.Bundle)
	 */
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
		map = mapView.getOverlays();

		// for locating the user on the map.
		locationManager = new GPS( this );
	}

	/*
	 * 
	 * For accounting purposes, the server needs to know whether or not you are currently displaying any kind of route
	 * information, such as a set of driving directions. Subclasses must implement this method to truthfully report this
	 * information, or be in violation of our terms of use.
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	@Override
	protected boolean isRouteDisplayed()
	{
		return false;
	}

	// Menu activity methods here below
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate( R.menu.map_controls, menu );
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
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
		int failCount = 0;
		boolean result = poiList.deserialize();
		if (result == true)
		{
			display( poiList );
		}
		else
		{
			failCount++;
		}

		poiList = new PoiList( POI.Type.TRACE );
		result = poiList.deserialize();
		if (result == true)
		{
			display( poiList );
		}
		else
		{
			failCount++;
		}

		poiList = new PoiList( POI.Type.INCIDENT );
		result = poiList.deserialize();
		if (result == true)
		{
			display( poiList );
		}
		else
		{
			failCount++;
		}

		return failCount == 0;
	}

	/**
	 * @return true if the layers were removed and false otherwise.
	 */
	private boolean removeMobileDataLayers()
	{
		// to do this go through all the layers and see if they are mobile layers.
		for (Overlay layer : this.map)
		{
			switch (( (WPMapLayerPoints) layer ).getLayerType())
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
	 * @param poiList list of objects to draw on the map.
	 * @param whichLayer the layer we are adding the items of the poi list too.
	 */
	private void display( PoiList poiList )
	{
		ModelViewController mvc = null;
		switch (poiList.getType())
		{
		case TRACE:
			mvc = new MapMultiPointMVC( poiList, this );
			break;

		case INCIDENT:
		case BLOG:
			mvc = new MapSinglePointMVC( poiList, this );
			break;

		// case INCIDENT:
		// mvc = new MapIncidentMVC( poiList, this );
		// break;

		default:
			Log.w( TAG, "Asked for an unknown type of ModelViewController." );
			return;
		}
		mvc.update();
	}

	/**
	 * @param isDisplayed true if you want the layer's data displayed and false if you want it hidden.
	 * @return true if everything went well and false otherwise.
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
