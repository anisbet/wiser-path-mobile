/**
 * 
 */
package path.wiser.mobile.services;

import java.io.File;
import java.net.URL;

import path.wiser.mobile.geo.Blog;
import path.wiser.mobile.util.ModelViewController;
import android.util.Log;

/**
 * @author anisbet
 * 
 */
public class HTTPBlogMVC implements ModelViewController
{

	private Blog		blog;
	private HTTPService	service;
	private boolean		isUploaded	= false;

	public HTTPBlogMVC( HTTPService service, Blog blog )
	{
		this.service = service;
		this.blog = blog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.util.ModelViewController#update()
	 */
	@Override
	public void update()
	{
		if (this.blog == null || this.service.isLoggedIn() == false)
		{
			return;
		}

		// Write blog to web site
		if (this.blog.hasImage())
		{
			isUploaded = postImage(); // posting images allows titles bodies and tags.
		}
		else
		// no image? Its a point of interest and needs to use a different interface.
		{
			isUploaded = createPointOfInterest();
		}
	}

	/**
	 * @return true if the poi is uploaded and false otherwise.
	 */
	private boolean createPointOfInterest()
	{
		// The goal of this test is to post an geotagged blog without image.
		// WiserPath has many conflicting names; this is one of them: POI == geoblog.
		// "http://wiserpath-dev.bus.ualberta.ca/node/add/geoblog?location=POINT(-12661258.674479%207092974.0387356)" );
		URL url = service.getGeoblogURL( this.blog );

		// Now test multipart POI we request the page so that we can get the form_id which is important to
		WiserPathConnection.GET( url );
		String formToken = WiserPathConnection.getFormToken( WiserPathConnection.getContent() );
		WiserPathConnection.setAllowRedirects( true );
		WiserPathConnection connection = WiserPathConnection.getInstance( url );
		connection.addFormData( "title", this.blog.getTitle() );
		connection.addFormData( "body", this.blog.getDescription() );
		connection.addFormData( "form_token", formToken );
		connection.addFormData( "form_id", "geoblog_node_form" );
		connection.addFormData( "op", "Save" );
		connection.POST();
		if (WiserPathConnection.getReturnCode() == HTTPService.SUCCESS_BLOG_UPLOAD)
		{
			return true;
		}

		return false;
	}

	/**
	 * The name of this method reflects the menu item this method immitates if you were
	 * using WiserPath online.
	 * 
	 * @return True if the Blog is uploaded and false otherwise.
	 */
	private boolean postImage()
	{
		// compute file size so we know if WiserPath will even permit the image upload.
		File f = new File( this.blog.getImagePath() );
		int len = (int) f.length(); // create a buffer big enough for the image.
		if (len > HTTPService.WISERPATH_MAX_IMAGE_SIZE)
		{
			Log.e( "HTTPBlogMVC:", "The image you want to upload is too big." );
			return false;
		}
		// The goal of this test is to post an geotagged image.
		WiserPathConnection.setAllowRedirects( true );
		URL url = this.service.getPostImageURL( this.blog ); // passes blog to get it's location which becomes part of
																// the URL.
		// Now test multipart POI we request the page so that we can get the form_id which is important to
		WiserPathConnection.setRequestProperty( "Referer", "http://wiserpath-dev.bus.ualberta.ca" ); // test if nec.
		WiserPathConnection.GET( url );
		String formToken = WiserPathConnection.getFormToken( WiserPathConnection.getContent() );
		// send the image data.
		WiserPathConnection connection = WiserPathConnection.getInstance( url );
		connection.addFormData( "title", this.blog.getTitle() );
		connection.addFormData( "field_photo[0][fid]", "0" );
		connection.addFormData( "field_photo[0][list]", "1" );
		// connection.addImageData( "files[field_photo_0]", "spitz.jpg", "/home/anisbet/Downloads/spitz.jpg" );
		connection.addImageData( "files[field_photo_0]", this.blog.getImageName(), this.blog.getImagePath() );
		connection.addFormData( "teaser_include", "1" );
		connection.addFormData( "body", this.blog.getDescription() );
		connection.addFormData( "format", "5" ); // TODO determine and add additional Image types.
		connection.addFormData( "changed", "" );
		// form build id.
		connection.addFormData( "form_token", formToken );
		connection.addFormData( "form_id", "geophoto_node_form" );
		connection.addFormData( "op", "Save" );

		connection.POST();
		// TODO remove for production, for testing results:
		System.out.println( "Connection results: \nreturn code:" + WiserPathConnection.getReturnCode() + "\n" + WiserPathConnection.getContent()
			+ "\n\nThe form token is: " + formToken );
		if (WiserPathConnection.getReturnCode() == HTTPService.SUCCESS_BLOG_UPLOAD)
		{
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.util.ModelViewController#change()
	 */
	@Override
	public void change()
	{
		if (this.blog != null)
		{
			this.blog.setUploaded( this.isUploaded );
		}
	}

}
