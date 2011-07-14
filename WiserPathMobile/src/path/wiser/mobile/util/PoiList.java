/**
 * 
 */
package path.wiser.mobile.util;

import path.wiser.mobile.geo.Blog;
import path.wiser.mobile.geo.POI;
import path.wiser.mobile.geo.Trace;

/**
 * PoiList is a linked list build with a difference. For one manages the creation and deletion of POI objects.
 * This abstracts that process away from the caller class that has many more things to do than worry about
 * how to create push pop destroy and link to the next poi.
 * 
 * @author andrewnisbet
 * 
 */
public class PoiList
{

	private POI			head	= null;
	private POI.Type	myType;

	/**
	 * @param type the type of list to create.
	 */
	public PoiList( POI.Type type )
	{
		this.myType = type;
		this.head = createNewEntry();
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
			// you can keep adding incomplete nodes but you cannot upload them.
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
			this.head = createNewEntry();
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
		POI p = createNewEntry();
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
		// go to the head of the list but use a local head so we don't lose our place
		POI myHead = this.head;
		while (myHead.getPrevious() != null)
		{
			myHead = myHead.getPrevious();
		}
		// now at the head proceed through the list
		KMLDocument doc = new KMLDocument();
		doc.output( myHead );
		while (myHead.getNext() != null)
		{
			myHead = myHead.getNext();
			doc.output( myHead );
		}
		doc.write(/* some file stream or what have you */);
		return false;
	}

	/**
	 * Serialize the list of items to media. This object acts as a document container for the KML file.
	 * 
	 * @return true if the items were serialized to media and false otherwise.
	 */
	public boolean deserialize()
	{
		// go to the head of the list but use a local head so we don't lose our place
		KMLDocument doc = new KMLDocument();
		return doc.deserialize( this );
	}

	/**
	 * @return The current head position
	 */
	public POI getCurrent()
	{
		return this.head;
	}

	private POI createNewEntry()
	{
		switch (myType)
		{
		case TRACE:
			return new Trace();

		default:
			return new Blog();
		}
	}

	public POI.Type getType()
	{
		return this.myType;
	}
}
