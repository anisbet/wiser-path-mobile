package path.wiser.mobile.ui;

import java.util.List;

import path.wiser.mobile.R;
import path.wiser.mobile.geo.Blog;
import path.wiser.mobile.geo.Incident;
import path.wiser.mobile.geo.MapOverlayItems;
import path.wiser.mobile.geo.POI;
import path.wiser.mobile.geo.Trace;
import path.wiser.mobile.util.MapBlogMVC;
import path.wiser.mobile.util.MapIncidentMVC;
import path.wiser.mobile.util.MapTraceMVC;
import path.wiser.mobile.util.ModelViewController;
import path.wiser.mobile.util.PoiList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * This class is the UI for the Wiser Path tab of the application. Note that this class subclasses MapActivity, not
 * {@link path.wiser.mobile.ui.WiserPathHelper}.
 * 
 * @author andrewnisbet
 * 
 */
public class WiserPathActivity extends MapActivity
{

	private static final String	TAG	= "WiserPathActivity";
	private List<Overlay>		mapOverlays;
	private Drawable			drawable;
	private MapOverlayItems		itemizedOverlay;
	private boolean				useOnlineData;
	private boolean				useDeviceData;				// Selection from the map_controls menu.

	/** Called when the activity is first created. */
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.wiserpath_tab );
		MapView mapView = (MapView) findViewById( R.id.mapview );
		mapView.setBuiltInZoomControls( true );

		// now instantiate the map's overlay items.
		mapOverlays = mapView.getOverlays();
		drawable = this.getResources().getDrawable( R.drawable.ic_tee_poi_blue );
		itemizedOverlay = new MapOverlayItems( drawable );

		// this places a Wiser path POI 'tee' icon on west ed mall.
		GeoPoint point = new GeoPoint( 53522780, -113623052 ); // WGS84 * 1e6
		OverlayItem overlayitem = new OverlayItem( point, "West Edmonton Mall", "The greatest indoor show on Earth!" );
		itemizedOverlay.addOverlay( overlayitem );
		mapOverlays.add( itemizedOverlay );
	}

	@Override
	protected boolean isRouteDisplayed()
	{
		// TODO Auto-generated method stub
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
	 * @param isDisplayed TODO
	 * @return True if the request was successful and false otherwise.
	 */
	private boolean viewMobileData( boolean isDisplayed )
	{
		boolean result = false;
		if (isDisplayed == false)
		{
			return result;
		}
		// TODO get the data stored on the device.
		// to do that you need to get the saved POIs
		PoiList poiList = new PoiList( POI.Type.BLOG );
		result = poiList.deserialize();
		if (result == true)
		{
			display( poiList );
		}

		// poiList = new PoiList( POI.Type.TRACE );
		// result = poiList.deserialize();
		// if (result == true)
		// {
		// display( poiList );
		// }
		//
		// poiList = new PoiList( POI.Type.INCIDENT );
		// result = poiList.deserialize();
		// if (result == true)
		// {
		// display( poiList );
		// }

		// for each item on the list create a MVC to convert the POI to something useful on the map.

		return result;
	}

	private void display( PoiList poiList )
	{
		// get each poi and use appropriate MVC
		POI currentPoi = poiList.getCurrent();
		ModelViewController mvc = null;
		switch (poiList.getType())
		{
		case TRACE:
			mvc = new MapTraceMVC( this, (Trace) currentPoi );
			break;
		case BLOG:
			mvc = new MapBlogMVC( this, (Blog) currentPoi );
			break;
		case INCIDENT:
			mvc = new MapIncidentMVC( this, (Incident) currentPoi );
			break;
		default:
			Log.w( TAG, "Asked to display an unknown type of POI???" );
			return;
		}
		mvc.update();

		while (currentPoi.getNext() != null) // TODO will this work??
		{
			mvc = null;
			switch (poiList.getType())
			{
			case TRACE:
				mvc = new MapTraceMVC( this, (Trace) currentPoi );
				break;
			case BLOG:
				mvc = new MapBlogMVC( this, (Blog) currentPoi );
				break;
			case INCIDENT:
				mvc = new MapIncidentMVC( this, (Incident) currentPoi );
				break;
			default:
				Log.w( TAG, "Asked to display an unknown type of POI???" );
				return;
			}
			mvc.update();
		}
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
}
