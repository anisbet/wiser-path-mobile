/**
 * 
 */
package path.wiser.mobile.util;

import java.util.Vector;


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
	 * @param tag
	 */
	public Tags( String tag )
	{
		init();
		tags.add( tag );
	}

	public Tags()
	{
		init();
	}

	/**
	 * @param tag
	 * @return TODO
	 */
	public boolean addTag( String tag )
	{
		if (tags.contains( tag )) // don't add tags if they already exist.
		{
			return false;
		}
		tags.add( tag );
		return true;
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
	 */
	public void setTags( String textViewTags )
	{
		String[] myTags = textViewTags.split( DELIMITER );
		for (String tag : myTags)
		{
			this.addTag( tag );
		}
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
		for (String tag : tags)
		{
			out.append( tag + DELIMITER );
		}
		return out.toString();
	}

}
