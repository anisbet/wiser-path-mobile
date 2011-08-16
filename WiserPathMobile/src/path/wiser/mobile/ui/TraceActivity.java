package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import path.wiser.mobile.util.Selectable;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * @author anisbet
 *         This class represents the trace screen.
 */
public class TraceActivity extends Selectable
{
	public TraceActivity()
	{
		super( "TraceActivity" );
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.trace_tab );

		TextView textView = (TextView) findViewById( R.id.Trace_Blog );
		textView.setOnTouchListener( new ClearTextView() );
		textView = (TextView) findViewById( R.id.Trace_Title );
		textView.setOnTouchListener( new ClearTextView() );
		textView = (TextView) findViewById( R.id.Trace_Tag );
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

	@Override
	public void onPause()
	{
		// TODO Save current state to the database when the user selects moves
		// away from this screens
		super.onPause();

	}

	@Override
	protected boolean previous()
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

	@Override
	protected boolean delete()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean save()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean next()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLocationChanged( Location location )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setCurrentPOIImage( String imagePath )
	{
		// TODO Auto-generated method stub

	}
}