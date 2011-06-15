/**
 * 
 */
package path.wiser.mobile;

import java.util.Vector;

/**
 * This class is the listing of user defined tags.
 * 
 * @author andrewnisbet
 * 
 */
public class Tags
{
	private Vector<String>	tags	= null;

	/**
	 * @param tag
	 */
	public Tags( String tag )
	{
		init();
		tags.add( tag );
	}

	/**
	 * @param tag
	 */
	public void addTag( String tag )
	{
		if (tags.contains( tag )) // don't add tags if they already exist.
		{
			return;
		}
		tags.add( tag );
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
			out.append( tag + ", " );
		}
		return out.toString();
	}

}
