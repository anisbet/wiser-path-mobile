/**
 * 
 */
package path.wiser.mobile.util;

import java.util.LinkedList;

/**
 * This is a simple list of POI objects with methods that allow for removing elements for use then pushing
 * them back onto the list when done. POI is the superclass of recorded objects in WiserPath.
 * 
 * @author anisbet
 * 
 */
public class CircularList<T>
{
	private LinkedList<T>	queue	= null;

	public CircularList()
	{
		this.queue = new LinkedList<T>();
	}

	/**
	 * @return the removed object that was on the head and null if the list is empty.
	 */
	public T popHead()
	{
		if (this.queue.size() > 0)
		{
			return this.queue.removeFirst();
		}
		return null;
	}

	/**
	 * @return the removed object that was on the tail of the list or null if the list is empty.
	 */
	public T popTail()
	{
		if (this.queue.size() > 0)
		{
			return this.queue.removeLast();
		}
		return null;
	}

	/**
	 * @param poi to be put on the head of the list.
	 */
	public void pushHead( T poi )
	{
		this.queue.addFirst( poi );
	}

	/**
	 * @param poi to be appended to the end of the list.
	 */
	public void pushTail( T poi )
	{
		this.queue.addLast( poi );
	}

	public int size()
	{
		return this.queue.size();
	}
}
