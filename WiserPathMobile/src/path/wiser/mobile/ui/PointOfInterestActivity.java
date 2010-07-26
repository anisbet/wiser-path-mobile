/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * This class represents the Point of Interest screen on the Android.
 * 
 * @author andrew nisbet
 * 
 */
public class PointOfInterestActivity extends WiserActivity
{
	protected String	greeting	= "This is the POI tab";

	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		// Context context = this.getBaseContext();
		// Drawable image = Drawable.createFromPath( "edmonton.png" );
		// ImageView imgView = new ImageView( context );
		// imgView = (ImageView) findViewById( R.id.Poi_Photo );
		// imgView.setImageDrawable( image );
		setContentView( R.layout.poi_tab );
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
}
