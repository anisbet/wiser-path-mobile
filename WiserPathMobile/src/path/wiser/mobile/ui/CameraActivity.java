/**
 * 
 */
package path.wiser.mobile.ui;

import java.io.IOException;

import path.wiser.mobile.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

// Note to programmer: you need only pass a new version of this object to the setOnClickListener()
// method of a button you wish to use to activate the camera.
/**
 * This class will take care of taking photos from the androids camera
 * and storing the picture in an appropriate place.
 * 
 * @author anisbet
 * 
 */
public class CameraActivity extends Activity implements OnClickListener, SurfaceHolder.Callback
{

	public static final int		FOTO_MODE		= 0;
	private static final String	TAG				= "CameraTest";
	private Camera				mCamera			= null;
	private boolean				mPreviewRunning	= false;
	private Context				mContext		= this;

	public void onCreate( Bundle icicle )
	{
		super.onCreate( icicle );

		Log.e( TAG, "onCreate" );

		Bundle extras = getIntent().getExtras();

		getWindow().setFormat( PixelFormat.TRANSLUCENT );
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
		setContentView( R.layout.camera_surface );
		mSurfaceView = (SurfaceView) findViewById( R.id.surface_camera );
		mSurfaceView.setOnClickListener( this );
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback( this );
		mSurfaceHolder.setType( SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS );
	}

	@Override
	protected void onRestoreInstanceState( Bundle savedInstanceState )
	{
		super.onRestoreInstanceState( savedInstanceState );
	}

	Camera.PictureCallback	mPictureCallback	= new Camera.PictureCallback()
												{
													public void onPictureTaken( byte[] imageData, Camera c )
													{

														if (imageData != null)
														{

															Intent mIntent = new Intent();

															// FileUtilities
															// .StoreByteImage(
															// mContext,
															// imageData,
															// 50,
															// "ImageName" );

															mCamera.startPreview();

															setResult( FOTO_MODE, mIntent );
															finish();

														}
													}
												};

	protected void onResume()
	{
		Log.e( TAG, "onResume" );
		super.onResume();
	}

	protected void onSaveInstanceState( Bundle outState )
	{
		super.onSaveInstanceState( outState );
	}

	protected void onStop()
	{
		Log.e( TAG, "onStop" );
		super.onStop();
	}

	public void surfaceCreated( SurfaceHolder holder )
	{
		Log.e( TAG, "surfaceCreated" );
		mCamera = Camera.open();

	}

	public void surfaceChanged( SurfaceHolder holder, int format, int w, int h )
	{
		Log.e( TAG, "surfaceChanged" );

		// XXX stopPreview() will crash if preview is not running
		if (mPreviewRunning)
		{
			mCamera.stopPreview();
		}

		Camera.Parameters p = mCamera.getParameters();
		p.setPreviewSize( w, h );
		mCamera.setParameters( p );
		try
		{
			mCamera.setPreviewDisplay( holder );
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mCamera.startPreview();
		mPreviewRunning = true;
	}

	public void surfaceDestroyed( SurfaceHolder holder )
	{
		Log.e( TAG, "surfaceDestroyed" );
		mCamera.stopPreview();
		mPreviewRunning = false;
		mCamera.release();
	}

	private SurfaceView		mSurfaceView;
	private SurfaceHolder	mSurfaceHolder;

	public void onClick( View arg0 )
	{

		mCamera.takePicture( null, mPictureCallback, mPictureCallback );

	}

}
