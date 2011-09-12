/**
 * 
 */
package path.wiser.mobile.util;

import path.wiser.mobile.geo.Blog;
import path.wiser.mobile.geo.POI;
import path.wiser.mobile.geo.Trace;
import path.wiser.mobile.util.WPXMLDocument.Format;
import android.util.Log;

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

	private static final String	TAG		= "PoiList";
	private POI					head	= null;
	private POI.PoiType			myType;
	private int					count	= 0;
	private Format				format;

	/**
	 * @param type the type of list to create.
	 */
	public PoiList( POI.PoiType type )
	{
		this.format = Format.KML;
		this.myType = type;
		this.head = createNewEntry();
	}

	/**
	 * @param type the type of list to create.
	 */
	public PoiList( POI.PoiType type, Format format )
	{
		this.format = format;
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
			// count not effected.
		}
		else
			if (this.head.getPrevious() == null)
			{
				// this is the head of the list
				this.head = this.head.getNext();
				this.head.setPrevious( null );
				this.count--;
			}
			else
				if (this.head.getNext() == null)
				{
					// I am the end of the list
					this.head = this.head.getPrevious();
					this.head.setNext( null );
					this.count--;
				}
				else
				{
					// I am in the middle of the list
					this.head.getPrevious().setNext( this.head.getNext() );
					this.head.getNext().setPrevious( this.head.getPrevious() );
					this.head = this.head.getNext();
					this.count--;
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
		this.count++;

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
		WPXMLDocument doc = null;
		while (myHead.getPrevious() != null)
		{
			myHead = myHead.getPrevious();
		}
		// now at the head proceed through the list
		switch (format)
		{
		case GPX:
			doc = new GPXDocument( this.getType(), true );
			break;
		case KML:
			doc = new KMLDocument( this.getType(), true );
			break;
		default:
			Log.e( TAG, "Request to serialize a document in an unsupported format." );
		}

		doc.setOutput( myHead );
		while (myHead.getNext() != null)
		{
			myHead = myHead.getNext();
			doc.setOutput( myHead );
		}
		doc.serialize();
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
		WPXMLDocument doc = null;
		switch (format)
		{
		case GPX:
			doc = new GPXDocument( this.getType(), false );
			break;
		case KML:
			doc = new KMLDocument( this.getType(), false );
			break;
		default:
			Log.e( TAG, "Request to deserialize a document in an unsupported format." );
		}
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
		case INCIDENT: // essentially a blog.
		default:
			return new Blog();
		}
	}

	/**
	 * @return PoiType of object this is.
	 */
	public POI.PoiType getType()
	{
		return this.myType;
	}

	/**
	 * @return true if the list is empty and false otherwise.
	 */
	public boolean isEmpty()
	{
		return this.count < 2 && this.head.isValid() == false;
	}
}
