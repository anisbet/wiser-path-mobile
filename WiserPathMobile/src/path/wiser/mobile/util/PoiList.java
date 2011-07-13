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
public class PoiList
{
	public enum Type
	{
		BLOG, TRACE
	}

	private POI		head	= null;
	private Type	myType;

	/**
	 * @param type the type of list to create.
	 */
	public PoiList( Type type )
	{
		this.myType = type;
		switch (type)
		{
		case TRACE:
			this.head = new Trace();
			break;
		default:
			this.head = new Blog();
		}
	}

	/**
	 * @return the previous POI object (cast it to something useful).
	 */
	public POI previous()
	{
		if (this.head.getPrevious() != null)
		{
			this.head = this.head.getPrevious();
		}
		return this.head;
	}

	/**
	 * @return The next POI object (cast it to something useful).
	 */
	public POI next()
	{
		// if there are items on the list and we are in the middle get the next.
		if (this.head.getNext() != null)
		{
			this.head = this.head.getNext();
		}
		else
		{
			// add a new node
			this.head = add();
		}
		return this.head;
	}

	/**
	 * @return The next POI that comes after one being deleted, or the previous if there are no more, and
	 *         in the case where this is only one POI, clear the data in the POI
	 */
	public POI deleteCurrent()
	{
		// if we are the last on the list back up and delete the last.
		if (this.head.getNext() == null && this.head.getPrevious() == null)
		{
			// There is only a head so clear me.
			switch (myType)
			{
			case TRACE:
				this.head = new Trace();
				break;
			default:
				this.head = new Blog();
			}
		}
		else
			if (this.head.getPrevious() == null)
			{
				// this is the head of the list
				this.head = this.head.getNext();
				this.head.setPrevious( null );

			}
			else
				if (this.head.getNext() == null)
				{
					// I am the end of the list
					this.head = this.head.getPrevious();
					this.head.setNext( null );

				}
				else
				{
					// I am in the middle of the list
					this.head.getPrevious().setNext( this.head.getNext() );
					this.head.getNext().setPrevious( this.head.getPrevious() );
					this.head = this.head.getNext();
				}
		return this.head;
	}

	/**
	 * Adds a new POI node and returns a reference to it. This method is implied
	 * if you {@link#next()} past the end of the list.
	 * 
	 * @return a new POI.
	 */
	public POI add()
	{
		// go to the end of the list.
		while (this.head.getNext() != null)
		{
			this.head = this.head.getNext();
		}
		// now create a new item
		POI p = null;
		switch (myType)
		{
		case TRACE:
			p = new Trace();
			break;
		default:
			p = new Blog();
		}
		p.setPrevious( this.head );
		this.head.setNext( p );
		this.head = this.head.getNext();

		return this.head;
	}

	/**
	 * Serialize the list of items to media. This object acts as a document container for the KML file.
	 * 
	 * @return true if the items were serialized to media and false otherwise.
	 */
	public boolean serialize()
	{
		return false;
	}
}
