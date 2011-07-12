/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import path.wiser.mobile.geo.Blog;
import path.wiser.mobile.geo.GPS;
import path.wiser.mobile.services.HTTPService;
import path.wiser.mobile.util.CircularList;
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
	protected Blog			currentBlog	= null;
	protected CircularList<Blog>	blogs		= null;
	protected GPS			gps			= null;

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
		this.gps = new GPS( this );
		this.blogs = new CircularList<Blog>();
		this.currentBlog = new Blog();

		this.currentBlog.setPoiTitle( "Andrew's test currentBlog" );

		// test code
		BlogMVC mvc = new BlogMVC( this, this.currentBlog );
		mvc.update();
		this.currentBlog.setDescription( "Oh, how nice, a new currentBlog!" );
		mvc.update();

		// test code

		// add a onClick listener to the text screens so we can remove the
		// existing text and allow the user to start entering text.
		TextView textView = (TextView) findViewById( R.id.Poi_Title );
		ClearTextView titleClearTextView = new ClearTextView();
		textView.setOnTouchListener( titleClearTextView );
		textView.setOnFocusChangeListener( titleClearTextView );
		// clear the currentBlog text aswell.
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
		// this.blogs.removeElement( currentBlog );
		// this.blog = new Blog();
	}

	@Override
	protected void next()
	{
		// if the currentBlog is not null push it on the tail and get the head.
		if (this.currentBlog != null)
		{
			this.blogs.pushTail( this.currentBlog );
			this.currentBlog = this.blogs.popHead();
		}
	}

	@Override
	protected void previous()
	{
		// if the currentBlog is not null push it on the tail and get the head.
		if (this.currentBlog != null)
		{
			this.blogs.pushHead( this.currentBlog );
			this.currentBlog = this.blogs.popTail();
		}
	}

	@Override
	protected void save()
	{
		// TODO Serialize the entire vector to disk.
		BlogMVC mvc = new BlogMVC( this, this.currentBlog );
		mvc.change(); // sync the data from the UI to the blog
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
		// Make sure the currentBlog is ready to go.
		if (this.currentBlog != null && this.currentBlog.validate())
		{
			// get the HTTPService for posting data.
			HTTPService service = HTTPService.getInstance();
			service.uploadBlog( this.currentBlog );
			// upload the currentBlog.
			if (this.currentBlog.isUploaded())
			{
				text = String.format( res.getString( R.string.poi_blog_post_success_msg ) );
				// TODO delete currentBlog.
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
		if (this.currentBlog != null && this.currentBlog.needsLocation())
		{
			this.currentBlog.setLocation( location );
			if (location != null)
			{
				this.print( "LOCATION CHANGED " + location.getLongitude() + " long " + location.getLatitude() + " lat." );
			}
			// turn off the gps.
			this.gps = null;
		}
	}
}
