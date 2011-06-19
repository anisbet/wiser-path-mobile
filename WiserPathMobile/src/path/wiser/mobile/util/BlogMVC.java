/**
 * 
 */
package path.wiser.mobile.util;

import path.wiser.mobile.R;
import path.wiser.mobile.R.id;
import path.wiser.mobile.geo.Blog;
import path.wiser.mobile.ui.PointOfInterestActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * @author andrewnisbet
 * 
 */
public class BlogMVC implements ModelViewController
{

	private Blog					blog;
	private PointOfInterestActivity	poi;

	public BlogMVC( PointOfInterestActivity activity, Blog blog )
	{
		this.blog = blog;
		this.poi = activity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.ModelViewController#update()
	 */
	@Override
	public void update()
	{
		if (this.blog != null)
		{
			// take the values from the blog and write them to screen
			TextView textView = (TextView) poi.findViewById( R.id.Poi_Title );
			textView.setText( blog.getPoiTitle() );
			textView = (TextView) poi.findViewById( R.id.Poi_Blog );
			textView.setText( blog.getDescription() );
			textView = (TextView) poi.findViewById( R.id.Poi_Tag );
			Tags tag = blog.getTags();
			textView.setText( tag.toString() );
		}
		else
		{
			Log.e( this.getClass().getName(), "Unable to update because blog is null." );
		}
		// TODO also include images.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.ModelViewController#change()
	 */
	@Override
	public void change()
	{
		// TODO take the values from the screen and put them in the blog
		if (this.blog != null)
		{
			TextView textView = (TextView) poi.findViewById( R.id.Poi_Title );
			blog.setPoiTitle( (String) textView.getText() );
			textView = (TextView) poi.findViewById( R.id.Poi_Blog );
			blog.setDescription( (String) textView.getText() );
			textView = (TextView) poi.findViewById( R.id.Poi_Tag );
			blog.setTags( (String) textView.getText() );
			// TODO also include images.
		}
		else
		{
			Log.e( this.getClass().getName(), "Unable to update because blog is null." );
		}
	}

}
