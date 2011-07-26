/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import path.wiser.mobile.geo.Blog;
import path.wiser.mobile.geo.GPS;
import path.wiser.mobile.geo.POI;
import path.wiser.mobile.services.HTTPService;
import path.wiser.mobile.util.PoiList;
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
	protected PoiList	blogs	= null;
	protected GPS		gps		= null;

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
		this.gps = new GPS( this ); // TODO Use the new stopUpdatingLocation of GPS to turn off.
		// create the container for many blogs
		this.blogs = new PoiList( POI.Type.BLOG );
		Blog currentBlog = (Blog) blogs.getCurrent();
		currentBlog.setTitle( "Andrew's test currentBlog" );

		// test code
		BlogMVC mvc = new BlogMVC( this, currentBlog );
		mvc.update();
		currentBlog.setDescription( "Oh, how nice, a new currentBlog!" );
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
		Blog currentBlog = (Blog) this.blogs.deleteCurrent();
		BlogMVC mvc = new BlogMVC( this, currentBlog );
		mvc = new BlogMVC( this, currentBlog );
		mvc.update();
	}

	@Override
	protected void next()
	{
		Blog currentBlog = (Blog) this.blogs.getCurrent();
		BlogMVC mvc = new BlogMVC( this, currentBlog );
		// write data from UI to blog
		mvc.change();
		currentBlog = (Blog) this.blogs.next();
		mvc = new BlogMVC( this, currentBlog );
		// update the ui to the new blog
		mvc.update();
		// if this is a new blog you may need to add a location so check for GPS and if none start it.
		if (currentBlog.needsLocation())
		{
			this.gps = new GPS( this ); // this will run until a location is stored when locationChange fires.
		}
	}

	@Override
	protected void previous()
	{
		Blog currentBlog = (Blog) this.blogs.getCurrent();
		BlogMVC mvc = new BlogMVC( this, currentBlog );
		// write data from UI to blog
		mvc.change();
		currentBlog = (Blog) this.blogs.previous();
		mvc = new BlogMVC( this, currentBlog );
		// update the ui to the new blog
		mvc.update();
		// if this is a new blog you may need to add a location so check for GPS and if none start it.
		if (currentBlog.needsLocation())
		{
			this.gps = new GPS( this ); // this will run until a location is stored when locationChange fires.
		}
	}

	@Override
	protected void save()
	{
		Blog currentBlog = (Blog) this.blogs.getCurrent();
		BlogMVC mvc = new BlogMVC( this, currentBlog );
		// sync the data from the UI to the blog
		mvc.change();

		if (this.blogs.serialize())
		{
			text = String.format( res.getString( R.string.poi_blog_save_success_msg ) );
		}
		else
		{
			// text = String.format( res.getString( R.string.poi_blog_save_success_msg ) );
		}
	}

	@Override
	protected void upload()
	{
		Blog currentBlog = (Blog) this.blogs.getCurrent();
		BlogMVC mvc = new BlogMVC( this, currentBlog );
		// sync the data from the UI to the blog
		mvc.change();
		// Make sure the currentBlog is ready to go.
		if (currentBlog.validate())
		{
			// get the HTTPService for posting data.
			HTTPService service = HTTPService.getInstance();
			service.uploadBlog( currentBlog );
			// upload the currentBlog.
			if (currentBlog.isUploaded())
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
		// get the next Blog after deletion.
		currentBlog = (Blog) this.blogs.deleteCurrent();
		mvc = new BlogMVC( this, currentBlog );
		mvc.update();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	public void onStop()
	{
		super.onStop();
		this.blogs.serialize();
	}

	@Override
	public void onLocationChanged( Location location )
	{
		Blog currentBlog = (Blog) this.blogs.getCurrent();

		// we do this so the location doesn't keep updating after you leave the POI.
		if (currentBlog.needsLocation())
		{
			currentBlog.setLocation( location );
			if (location != null)
			{
				this.print( "LOCATION CHANGED " + location.getLongitude() + " long " + location.getLatitude() + " lat." );
			}
			// if a location has already been stored stop the GPS
			// turn off the gps.
			// this.gps.stopUpdatingLocation( this ); // TODO test this.
			this.gps = null;
		}
	}
}
