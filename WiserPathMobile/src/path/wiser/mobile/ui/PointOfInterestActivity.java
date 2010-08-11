/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import path.wiser.mobile.db.Queryable;
import path.wiser.mobile.ui.WiserActivity.ClearTextView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * This class represents the Point of Interest screen on the Android.
 * 
 * @author anisbet
 * 
 */
public class PointOfInterestActivity extends Queryable
{

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
		// TODO add handling of images. Default image, From Camera and saving.
		// ImageView imageView = (ImageView) findViewById( R.id.Poi_Photo );

		// add a onClick listener to the text screens so we can remove the
		// existing text and allow the user to start entering text.
		TextView textView = (TextView) findViewById( R.id.Poi_Title );
		textView.setOnTouchListener( new ClearTextView() );
		// clear the blog text aswell.
		textView = (TextView) findViewById( R.id.Poi_Blog );
		textView.setOnTouchListener( new ClearTextView() );
		textView = (TextView) findViewById( R.id.Poi_Tag );
		textView.setOnTouchListener( new ClearTextView() );

		ImageButton cameraButton = (ImageButton) findViewById( R.id.Poi_Camera );
		cameraButton.setOnClickListener( new CameraActivity() );

	}

	public void onResume()
	{
		super.onResume();
		// TextView tv = (TextView) findViewById( R.id.Poi_Title );
		// tv.setText( "Changed Value" );
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
		// TODO Auto-generated method stub

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
