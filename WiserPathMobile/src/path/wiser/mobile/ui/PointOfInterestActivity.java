/**
 * 
 */
package path.wiser.mobile.ui;

import java.util.Vector;

import path.wiser.mobile.R;
import path.wiser.mobile.geo.Blog;
import path.wiser.mobile.geo.GPS;
import path.wiser.mobile.services.HTTPService;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * This class represents the Point of Interest screen on the Android.
 * 
 * @author anisbet
 * @param <MainActivity>
 * 
 */
public class PointOfInterestActivity extends Selectable
{
	protected Blog			blog	= null;
	protected Vector<Blog>	blogs	= null;
	protected GPS			gps		= null;

	public PointOfInterestActivity()
	{
		super( "POIActivity" );
	}

	public PointOfInterestActivity( String tag )
	{
		super( tag );
	}

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		// set content view so you can grab stuff in it.
		setContentView( R.layout.poi_tab );
		// GPS gps = new GPS( this );
		this.blogs = new Vector<Blog>();
		// this.blog = new Blog();

		// add a onClick listener to the text screens so we can remove the
		// existing text and allow the user to start entering text.
		TextView textView = (TextView) findViewById( R.id.Poi_Title );
		ClearTextView titleClearTextView = new ClearTextView();
		textView.setOnTouchListener( titleClearTextView );
		textView.setOnFocusChangeListener( titleClearTextView );
		// clear the blog text aswell.
		textView = (TextView) findViewById( R.id.Poi_Blog );
		textView.setOnTouchListener( new ClearTextView() );
		textView = (TextView) findViewById( R.id.Poi_Tag );
		textView.setOnTouchListener( new ClearTextView() );

		// TODO add handling of images. Default image, From Camera and saving.
		// ImageView imageView = (ImageView) findViewById( R.id.Poi_Photo );
		// ImageButton cameraButton = (ImageButton) findViewById(
		// R.id.Poi_Camera );
		// cameraButton.setOnClickListener( new CameraActivity() );

		// Get the database
		// this.db = new WiserDatabase( this );

	}

	// This method gets run onCreate() as well.
	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.ui.WiserActivityHelper#onResume()
	 */
	// public void onResume()
	// {
	// super.onResume();
	// // load previous image.
	// previous();
	// }

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

	@Override
	protected void delete()
	{
		// TODO Auto-generated method stub
		// this.blogs.removeElement( blog );
		// this.blog = new Blog();
	}

	@Override
	protected void next()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void previous()
	{
		// TODO goto previous blog.
	}

	@Override
	protected void save()
	{
		// TODO Serialize the entire vector to disk.
	}

	@Override
	protected void upload()
	{
		// TODO Auto-generated method stub
		HTTPService service = HTTPService.getInstance();
		if (service.uploadBlog( this.blog ))
		{
			;
		}
	}

	@Override
	public void onPause()
	{
		// TODO store data before we move away from this screen.
		super.onPause();
	}

	@Override
	public void onLocationChanged( Location location )
	{
		// TODO if a location has already been stored stop the GPS
		// we do this so the location doesn't keep updating after you leave the POI.
		if (this.blog.needsLocation())
		{
			this.blog.setLocation( location );
			// this.gps = null;
		}
	}
}
