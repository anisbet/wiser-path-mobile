/**
 * 
 */
package path.wiser.mobile.geo;

/**
 * Blog is like a POI except that it includes an image.
 * 
 * @author andrewnisbet
 * 
 */
public class Blog extends POI // implements ScreenRefreshListener
{
	// TODO include image
	public Blog()
	{
		this.gps = new GPS( this );
	}

}
