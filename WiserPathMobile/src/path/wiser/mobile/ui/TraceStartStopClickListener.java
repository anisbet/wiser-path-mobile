/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.geo.Trace;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author andrewnisbet
 * 
 */
public class TraceStartStopClickListener implements OnClickListener
{

	private Trace	trace;

	public TraceStartStopClickListener( Trace trace )
	{
		this.trace = trace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick( View clickedView )
	{
		// Intent cameraIntent = new Intent( android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
		// startActivityForResult( cameraIntent, CAMERA_PIC_REQUEST );
		// TODO start the trace activity and register it as running so POIs and Incidents. This
		// should be done as a service so that it continues to run if the app is eventually minimized.

	}

}
