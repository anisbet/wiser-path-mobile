/**
 * 
 */
package path.wiser.mobile.util;

import java.util.Vector;

import path.wiser.mobile.ui.BlogMVC;

/**
 * This class is the listing of user defined tags.
 * 
 * @author andrewnisbet
 * 
 */
public class Tags
{
	private Vector<String>		tags		= null;
	public final static String	DELIMITER	= ", ";

	/**
	 * Constructor that takes a single string of , separated tags.
	 * 
	 * @param tag
	 */
	public Tags( String tags )
	{
		init();
		if (tags != null)
		{
			setTags( tags );
		}
	}

	public Tags()
	{
		init();
	}

	/**
	 * @param tag
	 * @return
	 */
	public boolean contains( String tag )
	{
		return tags.contains( tag );
	}

	/**
	 * 
	 */
	private void init()
	{
		tags = new Vector<String>();
	}

	/**
	 * @return String array of all the tags.
	 */
	public String[] getTags()
	{
		String[] retTags = new String[tags.size()];
		for (int i = 0; i < tags.size(); i++)
		{
			retTags[i] = tags.get( i );
		}
		return retTags;
	}

	/**
	 * This method is typically called from the {@link BlogMVC#change()} method.
	 * It parses the text it finds there and populates the tag object.
	 * 
	 * @param textViewTags
	 * @return true if all the tags were added and false if at least one was already a member of the tags list.
	 */
	public boolean setTags( String textViewTags )
	{
		String[] myTags = textViewTags.split( DELIMITER );
		boolean result = true;
		for (String tag : myTags)
		{
			if (tags.contains( tag.trim() )) // don't add tags if they already exist.
			{
				result = false;
			}
			tags.add( tag.trim() );
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer out = new StringBuffer();
		// for (String tag : tags)
		for (int i = 0; i < tags.size(); i++)
		{
			if (tags.size() > 0)
			{
				if (i < tags.size() - 1)
				{
					out.append( tags.get( i ) + DELIMITER );
				}
				else
				{
					out.append( tags.get( i ) );
				}
			}
		}

		return out.toString();
	}

}
