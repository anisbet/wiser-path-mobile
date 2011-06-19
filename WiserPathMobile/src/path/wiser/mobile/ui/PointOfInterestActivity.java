/**
 * 
 */
package path.wiser.mobile.ui;

import java.util.Vector;

import path.wiser.mobile.R;
import path.wiser.mobile.geo.Blog;
import path.wiser.mobile.geo.GPS;
import path.wiser.mobile.services.HTTPService;
import path.wiser.mobile.util.BlogMVC;
import path.wiser.mobile.util.Selectable;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
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
		gps = new GPS( this );
		this.blogs = new Vector<Blog>();
		this.blog = new Blog();

		this.blog.setPoiTitle( "Andrew's test blog" );

		// test code
		BlogMVC mvc = new BlogMVC( this, this.blog );
		mvc.update();
		this.blog.setDescription( "Oh, how nice, a new blog!" );
		mvc.update();

		// test code

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
		BlogMVC mvc = new BlogMVC( this, this.blog );
		mvc.change();
		if (isSerialized())
		{
			text = String.format( res.getString( R.string.poi_blog_save_success_msg ) );
		}
	}

	private boolean isSerialized()
	{
		return true;
	}

	@Override
	protected void upload()
	{
		// Make sure the blog is ready to go.
		if (this.blog != null && this.blog.validate())
		{
			// get the HTTPService for posting data.
			HTTPService service = HTTPService.getInstance();
			// upload the blog.
			if (service.uploadBlog( this.blog ))
			{
				text = String.format( res.getString( R.string.poi_blog_post_success_msg ) );
			}
			else
			{
				text = String.format( res.getString( R.string.poi_blog_post_fail_msg ) );
			}
		}
		else
		{
			text = String.format( res.getString( R.string.poi_blog_post_invalid_msg ) );
		}
		msg = Html.fromHtml( text );
		showMessage( msg );
	}

	// Occurs when ever you move away from the screen. All objects that were created for
	// this activity have persistence but are suspended.
	@Override
	public void onPause()
	{
		// TODO store data before we move away from this screen.
		super.onPause();
		this.print( "onPause() called!" );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume()
	{
		//
		super.onResume();
		this.print( "onResume() called!" );
	}

	@Override
	public void onLocationChanged( Location location )
	{
		// TODO if a location has already been stored stop the GPS
		// we do this so the location doesn't keep updating after you leave the POI.
		if (this.blog != null && this.blog.needsLocation())
		{
			this.blog.setLocation( location );
			if (location != null)
			{
				this.print( "LOCATION CHANGED " + location.getLongitude() + " long " + location.getLatitude() + " lat." );
			}
			// turn off the gps.
			this.gps = null;
		}
	}
}
