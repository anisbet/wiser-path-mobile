/**
 * 
 */
package path.wiser.mobile.geo;

import android.os.Bundle;

/**
 * Blog is like a POI except that it includes an image.
 * 
 * @author andrewnisbet
 * 
 */
public class Blog extends POI
{
	// TODO include image

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		this.gps = new GPS( this );
	}

}
