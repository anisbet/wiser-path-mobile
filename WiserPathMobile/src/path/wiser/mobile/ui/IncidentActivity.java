/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.R;
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

	/** Called when the activity is first created. */
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		greeting = "This is the Incident tab";
		TextView textview = new TextView( this );
		textview.setText( greeting );
		setContentView( textview );
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
	protected void delete()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.ui.PointOfInterestActivity#next()
	 * We don't save multiple incidents.
	 */
	@Override
	protected void next()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.ui.PointOfInterestActivity#previous()
	 * We don't save multiple incidents.
	 */
	@Override
	protected void previous()
	{
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
