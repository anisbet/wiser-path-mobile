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

	private CircularList<POI>	list		= null;
	private POI					currentPoi	= null;
	private Type				myType;

	/**
	 * @param type the type of list to create.
	 */
	public POIList( Type type )
	{
		this.myType = type;
		switch (type)
		{
		case TRACE:
			this.currentPoi = new Trace();
			break;
		default:
			this.currentPoi = new Blog();
		}
		this.list = new CircularList<POI>();
		this.list.pushTail( this.currentPoi );
	}

	/**
	 * @return the previous POI object (cast it to something useful).
	 */
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
				this.list.pushTail( this.currentPoi );
				this.currentPoi = (Trace) this.list.popHead();
				break;
			default:
				this.currentPoi = new Blog();
				this.list.pushTail( this.currentPoi );
				this.currentPoi = (Blog) this.list.popHead();
			}
		}
		else
		{
			switch (this.myType)
			{
			case TRACE:
				this.list.pushTail( this.currentPoi );
				this.currentPoi = (Trace) this.list.popHead();
				break;
			default:
				this.list.pushTail( this.currentPoi );
				this.currentPoi = (Blog) this.list.popHead();
			}
		}

		return this.currentPoi;
	}

	/**
	 * @return The next POI object (cast it to something useful).
	 */
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
				this.list.pushHead( this.currentPoi );
				this.currentPoi = (Trace) this.list.popTail();
				break;
			default:
				this.currentPoi = new Blog();
				this.list.pushHead( this.currentPoi );
				this.currentPoi = (Blog) this.list.popTail();
			}
		}
		else
		{
			switch (this.myType)
			{
			case TRACE:
				this.list.pushHead( this.currentPoi );
				this.currentPoi = (Trace) this.list.popTail();
				break;
			default:
				this.list.pushHead( this.currentPoi );
				this.currentPoi = (Blog) this.list.popTail();
			}
		}

		return this.currentPoi;
	}
}
