/**
 * 
 */
package path.wiser.mobile.util;

import path.wiser.mobile.geo.Blog;
import path.wiser.mobile.geo.POI;
import path.wiser.mobile.geo.Trace;

/**
 * @author andrewnisbet
 * 
 */
public class POIList
{
	public enum Type
	{
		BLOG, TRACE
	}

	@SuppressWarnings("rawtypes")
	private CircularList	list		= null;
	private POI				currentPoi	= null;
	private Type			myType;

	/**
	 * @param type the type of list to create.
	 */
	public POIList( Type type )
	{
		this.myType = type;
		switch (type)
		{
		case TRACE:
			this.list = new CircularList<Trace>();
			this.currentPoi = new Trace();
			break;
		default:
			this.list = new CircularList<Blog>();
			this.currentPoi = new Blog();
		}
		this.list.pushTail( this.currentPoi );
	}

	/**
	 * @return the previous POI object (cast it to something useful).
	 */
	@SuppressWarnings("unchecked")
	public POI previous()
	{
		// if the currentBlog is not null push it on the tail and get the head.
		// if the poi is not valid but user requests previous create new object store it and fetch previous.
		if (this.currentPoi.validate() == false)
		{
			switch (this.myType)
			{
			case TRACE:
				this.currentPoi = new Trace();
				break;
			default:
				this.currentPoi = new Blog();
			}
		}

		this.list.pushTail( this.currentPoi );
		this.currentPoi = (POI) this.list.popHead();

		return this.currentPoi;
	}

	/**
	 * @return The next POI object (cast it to something useful).
	 */
	@SuppressWarnings("unchecked")
	public POI next()
	{
		// if the currentBlog is not null push it on the tail and get the head.
		// if the poi is not valid but user requests previous create new object store it and fetch previous.
		if (this.currentPoi.validate() == false)
		{
			switch (this.myType)
			{
			case TRACE:
				this.currentPoi = new Trace();
				break;
			default:
				this.currentPoi = new Blog();
			}
		}

		this.list.pushHead( this.currentPoi );
		this.currentPoi = (POI) this.list.popTail();

		return this.currentPoi;
	}
}
