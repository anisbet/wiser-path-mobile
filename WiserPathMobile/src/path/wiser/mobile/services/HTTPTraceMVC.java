/**
 * 
 */
package path.wiser.mobile.services;

import java.net.URL;

import path.wiser.mobile.WPEnvironment;
import path.wiser.mobile.geo.Trace;
import path.wiser.mobile.util.GPXDocument;
import path.wiser.mobile.util.ModelViewController;

/**
 * @author andrewnisbet
 * 
 */
public class HTTPTraceMVC implements ModelViewController
{
	private HTTPService	service		= null;
	private Trace		trace		= null;
	private boolean		isUploaded	= false;

	public HTTPTraceMVC( HTTPService httpService, Trace trace )
	{
		this.service = httpService;
		this.trace = trace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.util.ModelViewController#update()
	 */
	@Override
	public void update()
	{
		if (this.trace == null || this.service.isLoggedIn() == false)
		{
			return;
		}

		isUploaded = createGeoTrace();

	}

	/**
	 * @return true if the Trace was created (uploaded) and false otherwise.
	 */
	private boolean createGeoTrace()
	{
		// The goal of this test is to post an geotrace GPX file.
		// "http://wiserpath-dev.bus.ualberta.ca/node/add/gpstrace" );
		// compute file size so we know if WiserPath will even permit the GPX upload.
		GPXDocument traceDocument = new GPXDocument();
		// The goal of this test is to post an geotagged image.
		WiserPathConnection.setAllowRedirects( true );
		URL url = this.service.getGeotraceURL( this.trace ); // passes blog to get it's location which becomes part of
																// the URL.
		// Now test multipart POI we request the page so that we can get the form_id which is important to
		WiserPathConnection.setRequestProperty( "Referer", "http://wiserpath-dev.bus.ualberta.ca" ); // test if nec.
		WiserPathConnection.GET( url );
		String formToken = WiserPathConnection.getFormToken( WiserPathConnection.getContent() );
		// send the image data.
		WiserPathConnection connection = WiserPathConnection.getInstance( url );
		connection.addFormData( "title", this.trace.getTitle() );
		connection.addFormData( "field_tracefile[0][fid]", "0" );
		connection.addFormData( "field_tracefile[0][list]", "1" );
		// connection.addImageData( "files[field_photo_0]", "spitz.jpg", "/home/anisbet/Downloads/spitz.jpg" );
		connection.addXMLData( "files[field_tracefile_0]", getFileName( this.trace ), traceDocument.getXMLContent( this.trace ) );
		connection.addFormData( "teaser_include", "1" );
		connection.addFormData( "body", this.trace.getDescription() );
		connection.addFormData( "changed", "" );
		connection.addFormData( "format", "5" ); // the geotrace uses the same format number.
		// form build id.
		connection.addFormData( "form_token", formToken );
		connection.addFormData( "form_id", "gpstrace_node_form" );
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

	/**
	 * Returns the name of the trace truncated to the Wiser Path size of 22.
	 * 
	 * @param trace
	 * @return String of the file name It is used in a field in the upload form.
	 */
	private String getFileName( Trace trace )
	{
		String name = "";
		int maxNameLength = 18;
		if (trace.getTitle().length() > maxNameLength)
		{
			name = trace.getTitle().substring( 0, maxNameLength - 1 ) + WPEnvironment.TRACE_UPLOAD_EXTENSION;
		}
		else
		{
			name = trace.getTitle() + WPEnvironment.TRACE_UPLOAD_EXTENSION;
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.util.ModelViewController#change()
	 */
	@Override
	public void change()
	{
		if (this.trace != null)
		{
			this.trace.setUploaded( this.isUploaded );
		}

	}

}
