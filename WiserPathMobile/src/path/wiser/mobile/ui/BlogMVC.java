/**
 * 
 */
package path.wiser.mobile.ui;

import path.wiser.mobile.R;
import path.wiser.mobile.WPEnvironment;
import path.wiser.mobile.geo.Blog;
import path.wiser.mobile.util.MediaReader;
import path.wiser.mobile.util.MediaWriter;
import path.wiser.mobile.util.ModelViewController;
import path.wiser.mobile.util.Tags;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author andrewnisbet
 * 
 */
public class BlogMVC implements ModelViewController
{

	private Blog					blog;
	private PointOfInterestActivity	poi;
	private Intent					data;

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
			textView.setText( blog.getTitle() );
			textView = (TextView) poi.findViewById( R.id.Poi_Blog );
			textView.setText( blog.getDescription() );
			textView = (TextView) poi.findViewById( R.id.Poi_Tag );
			Tags tag = blog.getTags();
			textView.setText( tag.toString() );
			// now set the image preview.
			MediaReader mediaReader = new MediaReader();
			Bitmap thumbnail = mediaReader.readImageFile( WPEnvironment.BLOG_PATH, this.blog.getImageName() );
			ImageView imageView = (ImageView) this.poi.findViewById( R.id.photo_preview );
			if (thumbnail != null)
			{
				imageView.setImageBitmap( thumbnail );
			}
			// else // set optional extra image if the first fails for some reason.
			// {
			// imageView.setImageBitmap( R.drawable.icon );
			// }
		}
		else
		{
			Log.e( this.getClass().getName(), "Unable to update because blog is null." );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see path.wiser.mobile.ModelViewController#change()
	 */
	@Override
	public void change()
	{
		// take the values from the screen and put them in the blog
		// this is called from a request to change the underlying model because
		// the user entered new data or navigated away from the this POI.
		if (this.blog != null)
		{
			TextView textView = (TextView) poi.findViewById( R.id.Poi_Title );
			blog.setTitle( textView.getText().toString() );
			textView = (TextView) poi.findViewById( R.id.Poi_Blog );
			blog.setDescription( textView.getText().toString() );
			textView = (TextView) poi.findViewById( R.id.Poi_Tag );
			blog.setTags( new Tags( textView.getText().toString() ) );
			// also include images.
			// the model can also change if the host activity received an Intent from the Camera activity.
			// in that case we need to save the image and update the blogs imageName.
			if (data != null) // this action was the result of an Intent ie image from the camera.
			{
				// now save the image for the PointOfInterestActivity.
				Bitmap image = (Bitmap) data.getExtras().get( "data" );
				MediaWriter mediaWriter = new MediaWriter();
				String fileName = getImageFileName();
				mediaWriter.writeImageFile( WPEnvironment.BLOG_PATH, fileName, image );
				// now update the name of the image in the POI
				this.blog.setImageName( fileName );
			}

		}
		else
		{
			Log.e( this.getClass().getName(), "Unable to update because blog is null." );
		}
	}

	/**
	 * @param data the data to set
	 */
	public final void setData( Intent data )
	{
		this.data = data;
	}

	/**
	 * This method makes an attempt to give the best name for an image. It chooses the title of
	 * the blog if it has been filled out and the current time in milliseconds if blog entry is empty.
	 * 
	 * @return file name for the current image.
	 */
	private String getImageFileName()
	{
		// TODO test if we can use titles because of spacing and possible unicode input from users.
		// POI currentPoi = this.blogs.getCurrent();
		// if (currentPoi.getTitle().length() > 0)
		// {
		// return currentPoi.getTitle() + ".jpg";
		// }
		// else
		// {
		return String.valueOf( System.currentTimeMillis() ) + ".jpg"; // for now give it a number.
		// }
	}

}
