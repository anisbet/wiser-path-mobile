/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.geo.Trace;
import path.wiser.mobile.util.ModelViewController;

/**
 * @author andrew nisbet
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
		// first handle the title and description and tags
		// this.activity.

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
