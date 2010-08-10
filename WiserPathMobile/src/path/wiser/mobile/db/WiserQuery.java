/**
 * 
 */
package path.wiser.mobile.db;

/**
 * This object houses all the queries you can do with the {@link WiserDatabase}.
 * To simplify what you have to do, there are a set of template queries that are
 * used a lot. There is a special USER_DEFINED query type that allows you to
 * specify a
 * special query.
 * 
 * @author anisbet
 * 
 */
public class WiserQuery
{

	public final static String	SELECT_STAR_POIS	= "POI_INCIDENT_TABLE, new String[] { KEY_ROWID, KEY_ISBN, KEY_TITLE, KEY_PUBLISHER }, null, null, null, null, null";

	public static enum QueryType
	{
		USER_DEFINED, GET_ALL_POIS, GET_ALL_TRACES, GET_ALL_INCIDENTS, GET_TRACE_LOCATIONS
	}

	private QueryType	queryType			= null;
	private String		userDefinedQuery	= "";	// for testing. Stores a
													// user

	// defined query.

	/**
	 * @param query The query string to execute.
	 * @param description
	 */
	public WiserQuery( QueryType queryType )
	{
		this.queryType = queryType;
	}

	public void setQuery( String query )
	{
		this.queryType = QueryType.USER_DEFINED;
		this.userDefinedQuery = query;
	}

	/**
	 * @return query string formatted in SQL.
	 */
	public String getQuery()
	{
		switch (this.queryType)
		{
		case USER_DEFINED:
			return this.userDefinedQuery;
		case GET_ALL_POIS:
			return "";
		default:
			break;
		}
		return userDefinedQuery;
	}
}
