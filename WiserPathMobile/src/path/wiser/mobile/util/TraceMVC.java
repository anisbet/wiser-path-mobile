/**
 * 
 */
package path.wiser.mobile.util;

import path.wiser.mobile.geo.Trace;
import path.wiser.mobile.ui.TraceActivity;

/**
 * @author andrewnisbet
 * 
 */
public class TraceMVC implements ModelViewController
{

	private TraceActivity	activity;
	private Trace			trace;

	/**
	 * @param activity
	 * @param trace
	 */
	public TraceMVC( TraceActivity activity, Trace trace )
	{
		this.activity = activity;
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
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.util.ModelViewController#change()
	 */
	@Override
	public void change()
	{
		// TODO Auto-generated method stub

	}

}
