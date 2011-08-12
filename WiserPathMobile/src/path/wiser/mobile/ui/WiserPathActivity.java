package path.wiser.mobile.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import path.wiser.mobile.R;
import path.wiser.mobile.geo.Blog;
import path.wiser.mobile.geo.GPS;
import path.wiser.mobile.geo.Incident;
import path.wiser.mobile.geo.POI;
import path.wiser.mobile.geo.POI.Type;
import path.wiser.mobile.geo.Trace;
import path.wiser.mobile.geo.WPMapLayerItems;
import path.wiser.mobile.util.MapBlogMVC;
import path.wiser.mobile.util.MapIncidentMVC;
import path.wiser.mobile.util.MapTraceMVC;
import path.wiser.mobile.util.ModelViewController;
import path.wiser.mobile.util.PoiList;
import android.graphics.drawable.Drawable;
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

	private static final String				TAG				= "WiserPathActivity";
	private static final int				ZOOM_SETTING	= 14;									// 1 is world
																									// view.
	private static final GeoPoint			CENTER_POINT	= new GeoPoint( 53545556, -113490000 ); // City hall
																									// Edmonton
																									// where
																									// map opens
	private List<Overlay>					map;
	// this is a collection of objects references of WPMapLayerItems. We keep it for convenience because
	// we manipulate the layer items by layer and I don't know if Google adds its own layers.
	private HashMap<Type, WPMapLayerItems>	mobileLayers;
	private MapController					mapController;
	@SuppressWarnings("unused")
	private GPS								locationManager;										// it is used it
																									// fires
																									// events for
																									// this
																									// screen.
	private boolean							useOnlineData;
	private boolean							useDeviceData;											// Selection
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

		// now make the container to store the overlayItems.
		mobileLayers = new HashMap<POI.Type, WPMapLayerItems>();
		// now create an overlay with a specific Icon. Make new mobileLayers for incidents and traces as well as data
		// from
		// server.
		map = mapView.getOverlays();

		// assign an icon to this overlay.
		Drawable blogIcon = this.getResources().getDrawable( R.drawable.ic_tee_poi_blue );
		WPMapLayerItems blogOverlay = new WPMapLayerItems( blogIcon );
		// now keep a reference to the mobileLayers.
		mobileLayers.put( POI.Type.BLOG, blogOverlay );
		map.add( blogOverlay );

		// Now the Incidents
		// change this to the appropriate icon for an incident.
		Drawable incidentIcon = this.getResources().getDrawable( R.drawable.icon );
		WPMapLayerItems incidentsOverlay = new WPMapLayerItems( incidentIcon );
		mobileLayers.put( POI.Type.INCIDENT, incidentsOverlay );
		map.add( incidentsOverlay );

		// Now the Incidents
		// change this to the appropriate icon for an incident.
		Drawable traceIcon = this.getResources().getDrawable( R.drawable.icon );
		WPMapLayerItems traceOverlay = new WPMapLayerItems( traceIcon );
		mobileLayers.put( POI.Type.TRACE, traceOverlay );
		map.add( traceOverlay );

		// this places a Wiser path POI 'tee' icon on west ed mall.
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
		this.useDeviceData = menu.getItem( R.id.use_mobile_data ).isChecked();
		this.useOnlineData = menu.getItem( R.id.use_server_data ).isChecked();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		// Handle item selection
		switch (item.getItemId())
		{
		case R.id.use_server_data:
			this.useOnlineData = !this.useOnlineData;
			return viewOnlineData( this.useOnlineData );

		case R.id.use_mobile_data:
			this.useDeviceData = !this.useDeviceData;
			return viewMobileData( this.useDeviceData );

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
		boolean result = false;
		if (isDisplayed == false)
		{
			// this.mobileLayers.remove( Type.BLOG );
			return removeMobileDataLayers( true );
		}
		// get the data stored on the device to do that you need to get the saved POIs
		PoiList poiList = new PoiList( POI.Type.BLOG );
		result = poiList.deserialize();
		if (result == true)
		{
			WPMapLayerItems currentLayer = clearOverlayItemsFromMap( poiList.getType() );
			display( poiList, currentLayer );
		}

		// for each item on the list create a MVC to convert the POI to something useful on the map.
		// TODO test the serialization and deserialization of Traces and Incidents and then un comment this code.
		// poiList = new PoiList( POI.Type.TRACE );
		// result = poiList.deserialize();
		// if (result == true)
		// {
		// display( poiList );
		// }
		//
		poiList = new PoiList( POI.Type.INCIDENT );
		result = poiList.deserialize();
		if (result == true)
		{
			// by clearing and passing the layer here we give flexibility later to mixed types on one layer.
			// and you don't necessarily need to clear the list, just add to it or subtract from it.
			WPMapLayerItems currentLayer = clearOverlayItemsFromMap( poiList.getType() );
			display( poiList, currentLayer );
		}

		return result;
	}

	/**
	 * @param flushCache TODO
	 * @return true always.
	 */
	private boolean removeMobileDataLayers( boolean flushCache )
	{
		Iterator<Type> it = mobileLayers.keySet().iterator();
		while (it.hasNext())
		{
			POI.Type thisLayerType = it.next();
			// get rid of local reference or just get rid of map reference maybe we should just clear them or even
			// better just remove them from the map but keep them in local store to restore for efficiency.
			// TODO test implications for updating data.
			WPMapLayerItems thisLayer = null;
			if (flushCache)
			{
				thisLayer = mobileLayers.remove( thisLayerType );
			}
			else
			{
				thisLayer = mobileLayers.get( thisLayerType );
			}

			if (thisLayer == null)
			{
				continue;
			}

			this.map.remove( thisLayer );
		}
		return true;
	}

	/**
	 * Displays all the POI objects stored on the argument PoiList for reuse.
	 * 
	 * @param poiList
	 * @param whichLayer the layer we are adding the items of the poi list too.
	 */
	private void display( PoiList poiList, WPMapLayerItems whichLayer )
	{
		// get each poi and use appropriate MVC
		POI currentPoi = poiList.getCurrent();
		// get a model view controller.
		ModelViewController mvc = getMVC( poiList.getType(), currentPoi, whichLayer );
		if (mvc == null) // can happen for an undefined type.
		{
			return;
		}
		mvc.update(); // Reads the items to the overlay.

		while (currentPoi.getNext() != null) // Loop through the rest.
		{
			currentPoi = poiList.next();
			mvc = getMVC( poiList.getType(), currentPoi, whichLayer );
			if (mvc == null) // can happen for an undefined type.
			{
				return;
			}
			mvc.update();
		}
	}

	/**
	 * Clears the requested layer of items from map and returns a fresh empty layer.
	 * 
	 * @param type
	 * @return an empty list ready to be repopulated with new items.
	 */
	private WPMapLayerItems clearOverlayItemsFromMap( POI.Type type )
	{
		WPMapLayerItems thisLayer = mobileLayers.get( type );
		thisLayer.clear();
		return thisLayer;
	}

	/**
	 * Returns the appropriate ModelViewController for displaying a certain type of POI in the WiserPathActivity
	 * (Google) Map.
	 * 
	 * @param type
	 * @param poi
	 * @param currentOverlay which layer to draw items on.
	 * @return ModelViewController for the type of poi we need to display
	 */
	private ModelViewController getMVC( Type type, POI poi, WPMapLayerItems currentOverlay )
	{

		switch (type)
		{
		case TRACE:
			return new MapTraceMVC( (Trace) poi, currentOverlay );
		case BLOG:
			return new MapBlogMVC( (Blog) poi, currentOverlay );
		case INCIDENT:
			return new MapIncidentMVC( (Incident) poi, currentOverlay );
		default:
			Log.w( TAG, "Asked for an unknown type of ModelViewController." );
		}
		return null;
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
}
