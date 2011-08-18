/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import path.wiser.mobile.geo.GPS;
import path.wiser.mobile.util.PoiList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * @author andrew nisbet
 * 
 */
public class IncidentActivity extends PointOfInterestActivity
{

	public static final int	CAMERA_PIC_REQUEST	= 1338;
	protected PoiList		traces				= null;
	protected GPS			gps					= null;

	public IncidentActivity()
	{
		super( "IncidentActivity" );
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		// set content view so you can grab stuff in it.
		setContentView( R.layout.incident_tab );
		// TODO add handling of images. Default image, From Camera and saving.
		// ImageView imageView = (ImageView) findViewById( R.id.Poi_Photo );

		// add a onClick listener to the text screens so we can remove the
		// existing text and allow the user to start entering text.
		TextView textView = (TextView) findViewById( R.id.Incident_Title );
		textView.setOnTouchListener( new ClearTextView() );
		// clear the blog text aswell.
		textView = (TextView) findViewById( R.id.Incident_Blog );
		textView.setOnTouchListener( new ClearTextView() );
		textView = (TextView) findViewById( R.id.Incident_Tag );
		textView.setOnTouchListener( new ClearTextView() );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate( R.menu.navigation, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		// Handle item selection
		switch (item.getItemId())
		{
		case R.id.Previous:
			previous();
			return true;
		case R.id.Upload:
			upload();
			return true;
		case R.id.Delete:
			delete();
			return true;
		case R.id.Save:
			save();
			return true;
		case R.id.Next:
			next();
			return true;
		default:
			return super.onOptionsItemSelected( item );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.ui.PointOfInterestActivity#delete()
	 */
	@Override
	protected boolean delete()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.ui.PointOfInterestActivity#next()
	 * We don't save multiple incidents.
	 */
	@Override
	protected boolean next()
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.ui.PointOfInterestActivity#previous()
	 * We don't save multiple incidents.
	 */
	@Override
	protected boolean previous()
	{
		return false;
	}

	@Override
	protected boolean save()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean upload()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
