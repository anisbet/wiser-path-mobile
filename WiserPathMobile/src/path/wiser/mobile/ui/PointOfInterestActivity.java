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
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
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
	public static final int	CAMERA_PIC_REQUEST	= 1337;
	protected PoiList		blogs				= null;
	protected GPS			gps					= null;

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
		// create the container for many blogs
		this.blogs = new PoiList( POI.PoiType.BLOG );
		Blog currentBlog = null;

		if (this.blogs.deserialize()) // there was no blog to deserialize.
		{
			currentBlog = (Blog) blogs.getCurrent();
			BlogMVC mvc = new BlogMVC( this, currentBlog );
			mvc.update();
		}

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

		// Invoke the camera when the button is clicked.
		ImageButton cameraButton = (ImageButton) findViewById( R.id.camera_button );
		cameraButton.setOnClickListener( new OnClickListener()
		{
			public void onClick( View touchedView )
			{
				Intent cameraIntent = new Intent( android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
				startActivityForResult( cameraIntent, CAMERA_PIC_REQUEST );
			}
		} );

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
			return previous();
		case R.id.Upload:
			return upload();
		case R.id.Delete:
			return delete();
		case R.id.Save:
			return save();
		case R.id.Next:
			return next();
		default:
			return super.onOptionsItemSelected( item );
		}
	}

	@Override
	protected boolean delete()
	{
		Blog currentBlog = (Blog) this.blogs.deleteCurrent();
		BlogMVC mvc = new BlogMVC( this, currentBlog );
		mvc.update();
		return true;
	}

	@Override
	protected boolean next()
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
		return true;
	}

	@Override
	protected boolean previous()
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
		return true;
	}

	@Override
	protected boolean save()
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
			text = String.format( res.getString( R.string.poi_blog_save_not_success_msg ) );
			return false;
		}
		return true;
	}

	@Override
	protected boolean upload()
	{
		boolean result = true;
		Blog currentBlog = (Blog) this.blogs.getCurrent();
		BlogMVC mvc = new BlogMVC( this, currentBlog );
		// sync the data from the UI to the blog
		mvc.change();
		// Make sure the currentBlog is ready to go.
		if (currentBlog.isValid())
		{
			// get the HTTPService for posting data.
			HTTPService service = HTTPService.getInstance();
			service.uploadBlog( currentBlog );
			// upload the currentBlog.
			if (currentBlog.isUploaded())
			{
				text = String.format( res.getString( R.string.poi_blog_post_success_msg ) );
			}
			else
			{
				text = String.format( res.getString( R.string.poi_blog_post_fail_msg ) );
				result = false;
			}
		}
		else
		{
			text = String.format( res.getString( R.string.poi_blog_post_invalid_msg ) );
			result = false;
		}
		this.showMessage( text );
		// get the next Blog after deletion.
		if (result == true)
		{
			currentBlog = (Blog) this.blogs.deleteCurrent();
		}
		mvc = new BlogMVC( this, currentBlog );
		mvc.update();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	public void onStop()
	{
		super.onStop();
		save();
	}

	@Override
	public void onLocationChanged( Location location )
	{
		Blog currentBlog = (Blog) this.blogs.getCurrent();

		// we do this so the location doesn't keep updating after you leave the POI.
		if (currentBlog.needsLocation())
		{
			currentBlog.setLocation( location );
			// TODO this is just for testing remove.
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

	// /////////////////// Camera activity methods ///////////////////
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data )
	{
		if (requestCode == CAMERA_PIC_REQUEST)
		{
			if (resultCode == RESULT_OK)
			{
				Blog currentBlog = (Blog) this.blogs.getCurrent();
				BlogMVC mvc = new BlogMVC( this, currentBlog );
				// It is important to pass on the intent to the MVC so it can save the file and update the blog with the
				// file name.
				mvc.setData( data );
				mvc.change();
				mvc.update(); // calling this will update the display the new image.
				showMessage( res.getString( R.string.camera_success_msg ) );
			}
		}
		// ignore others if any
	}

}
