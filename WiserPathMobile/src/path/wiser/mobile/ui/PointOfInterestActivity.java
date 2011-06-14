/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import path.wiser.mobile.geo.Blog;
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
	protected Blog	blog	= null;

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

	}

	@Override
	protected void next()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void previous()
	{
		// this.db.deleteAll();
		// this.db.insert( "Zainia", "", null, "" );
		// this.db.insert( "Andrew", "", null, "" );
		//
		// List<String> names = this.db.selectAll();
		// TextView textView = (TextView) findViewById( R.id.Poi_Title );
		// if (names.size() > 0)
		// {
		// textView.setText( names.get( 0 ) + ", " + names.get( 1 ) );
		// }
	}

	@Override
	protected void save()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void upload()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause()
	{
		// TODO store data before we move away from this screen.
		super.onPause();
	}

}
