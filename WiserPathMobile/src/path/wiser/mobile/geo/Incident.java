/**
 * 
 */
package path.wiser.mobile.geo;

/**
 * @author andrewnisbet
 * 
 */
public class Incident extends Blog
{

	/**
	 * Creates a new Incident object from an existing Blog object.
	 * 
	 * @param poi
	 */
	public Incident( Blog poi )
	{
		this.setType( POI.Type.INCIDENT );
		// you do this when you read the extended data and discover you are recording a Incident instead of a Blog.
		this.description = poi.description;
		this.isUploaded = poi.isUploaded;
		this.tags = poi.tags;
		this.title = poi.title;
	}

	@Override
	public boolean isIncident()
	{
		return true;
	}

}
