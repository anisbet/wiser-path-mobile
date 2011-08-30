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
import android.graphics.BitmapFactory;
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
	private PointOfInterestActivity	activity;
	private Intent					cameraData; // used to retrieve images from the camera activity Intent.

	public BlogMVC( PointOfInterestActivity activity, Blog blog )
	{
		this.blog = blog;
		this.activity = activity;
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
			TextView textView = (TextView) activity.findViewById( R.id.Poi_Title );
			textView.setText( blog.getTitle() );
			textView = (TextView) activity.findViewById( R.id.Poi_Blog );
			textView.setText( blog.getDescription() );
			textView = (TextView) activity.findViewById( R.id.Poi_Tag );
			Tags tag = blog.getTags();
			textView.setText( tag.toString() );
			// now set the image preview.
			MediaReader mediaReader = new MediaReader();
			Bitmap thumbnail = mediaReader.readImageFile( WPEnvironment.BLOG_PATH, this.blog.getImageName() );
			ImageView imageView = (ImageView) this.activity.findViewById( R.id.photo_preview );
			if (thumbnail == null)
			// set optional extra image if the first fails for some reason.
			{
				thumbnail = BitmapFactory.decodeResource( activity.getResources(), R.drawable.no_photo );
				activity.print( activity.getString( R.string.camera_fail_msg ) );
			}
			imageView.setImageBitmap( thumbnail );
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
		if (blog != null)
		{
			TextView textView = (TextView) activity.findViewById( R.id.Poi_Title );
			blog.setTitle( textView.getText().toString() );
			textView = (TextView) activity.findViewById( R.id.Poi_Blog );
			blog.setDescription( textView.getText().toString() );
			textView = (TextView) activity.findViewById( R.id.Poi_Tag );
			blog.setTags( new Tags( textView.getText().toString() ) );
			// also include images.
			// the model can also change if the host activity received an Intent from the Camera activity.
			// in that case we need to save the image and update the blogs imageName.
			if (isFreshImage()) // this action was the result of an Intent ie image from the camera.
			{
				// now save the image for the PointOfInterestActivity.
				Bitmap image = (Bitmap) cameraData.getExtras().get( "data" );
				MediaWriter mediaWriter = new MediaWriter();
				String fileName = getImageFileName();
				mediaWriter.writeImageFile( WPEnvironment.BLOG_PATH, fileName, image );
				// now update the name of the image in the POI
				blog.setImageName( fileName );
				// reset the reference to the camera data so we don't keep reloading
				// a thumbnail every time the user enters new text.
				cameraData = null;
			}

		}
		else
		{
			Log.e( this.getClass().getName(), "Unable to update because blog is null." );
		}
	}

	/**
	 * This method will return true if data was returned from the camera activity. The result
	 * from the camera may be cancel but it was called and terminated with a non-crashing event.
	 * Classes that use this class should check that the result was ok before creating a model view controller.
	 * {@link path.wiser.mobile.ui.PointOfInterestActivity} is designed to check for success of image capture.
	 * 
	 * @return true if the camera activity has finished and false otherwise.
	 */
	private boolean isFreshImage()
	{
		return cameraData != null;
	}

	/**
	 * @param cameraData the cameraData to set
	 */
	public final void setData( Intent data )
	{
		this.cameraData = data;
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
		// for now give it a number.
		return String.valueOf( System.currentTimeMillis() ) + WPEnvironment.getImageExtension();
		// }
	}

}
