/**
 * 
 */
package path.wiser.mobile.db;

/**
 * This object houses all the queries you can do with the {@link WiserDatabase}.
 * To simplify what you have to do, there are a set of template queries that are
 * used a lot. There is a special TEST query type that allows you to specify a
 * special query.
 * 
 * @author anisbet
 * 
 */
public class WiserQuery
{

	public static enum QueryType
	{
		TEST, GET_ALL_POIS, GET_ALL_TRACES, GET_ALL_INCIDENTS, GET_TRACE_LOCATIONS
	}

	private String	query		= "";
	private String	description	= "";

	/**
	 * @param query The query string to execute.
	 * @param description
	 */
	public WiserQuery( QueryType queryType )
	{
		switch (queryType)
		{
		case TEST:
			System.out.println( "test type selected." );
			break;
		default:

		}
	}

	public void setQuery( String query )
	{
		this.query = query;
	}

	/**
	 * @return
	 */
	public String getQuery()
	{
		return query;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return description;
	}
}
