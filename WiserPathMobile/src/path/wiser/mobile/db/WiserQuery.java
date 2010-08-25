/**
 * 
 */
package path.wiser.mobile.db;

/**
 * This object houses all the queries you can do with the {@link WiserDatabase}.
 * To simplify what you have to do, there are a set of template queries that are
 * used a lot. There is a special USER_DEFinded query type that allows you to
 * specify a non-template query. Use the default constructor and setters to
 * populate your query.
 * 
 * @author anisbet
 * 
 */
public class WiserQuery
{

	// public final static String SELECT_STAR_POIS =
	// "POI_INCIDENT_TABLE, new String[] { KEY_ROWID, KEY_ISBN, KEY_TITLE, KEY_PUBLISHER }, null, null, null, null, null";

	public static enum QueryType
	{
		T_LAST_LOC, T_FIRST_LOC, T_ALL, P_ALL, I_ALL, P_ALL_BY_T_ID, I_ALL_BY_T_ID, P_DEL, T_DEL, I_DEL, USER_DEF
	}

	private boolean		isDistinct	= false;
	private String[]	columns		= null;
	private String		table		= "null";
	private String		whereClause	= "null";
	private String[]	whereArgs	= null;
	private String		groupBy		= "null";
	private String		having		= "null";
	private String		orderBy		= "null";
	private String		limit		= "null";

	/**
	 * Use this query if you are defining your own query and use the setters to
	 * populate your query.
	 */
	public WiserQuery()
	{
	}

	public WiserQuery( QueryType queryType, Queryable queryingActivity )
	{
		setWiserQyery( queryType, queryingActivity.getId() );
	}

	/**
	 * for testing so we don't need to create an activity
	 * 
	 * @param queryType
	 * @param rowId
	 */
	public WiserQuery( QueryType queryType, long rowId )
	{
		setWiserQyery( queryType, rowId );
	}

	private void setWiserQyery( QueryType queryType, long rowId )
	{

		switch (queryType)
		{
		case T_LAST_LOC: // last location
			return;
		case T_FIRST_LOC:
			return;
		case P_ALL_BY_T_ID:
			return;
		case I_ALL_BY_T_ID:
			return;
		case P_ALL:
			this.table = PoiIncedent.TABLE_NAME;
			this.columns = PoiIncedent.COLUMNS;
			this.whereClause = " ROWID='" + "%" /* String.valueOf( rowId ) */+ "'";
		case P_DEL:
			return;
		case I_DEL:
			return;
		case T_DEL:
			return;
		case USER_DEF:
		default:
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sBuff = new StringBuffer();
		sBuff.append( "query( " );
		if (isDistinct) sBuff.append( "true" );
		sBuff.append( table + ", " );
		if (columns != null)
		{
			sBuff.append( " {" );
			for (int i = 0; i < columns.length; i++)
			{
				sBuff.append( " \"" + columns[i] + "\", " );
			}
			sBuff.append( "}, " );
		}
		sBuff.append( whereClause + ", " );
		if (whereArgs != null)
		{
			sBuff.append( " {" );
			for (int i = 0; i < whereArgs.length; i++)
			{
				sBuff.append( " \"" + whereArgs[i] + "\", " );
			}
			sBuff.append( "}, " );
		}
		sBuff.append( groupBy + ", " );
		sBuff.append( having + ", " );
		sBuff.append( orderBy + ", " );
		sBuff.append( limit );
		sBuff.append( " )" );

		return sBuff.toString();
	}

	/**
	 * @return the columns
	 */
	public String[] getColumns()
	{
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns( String[] columns )
	{
		this.columns = columns;
	}

	/**
	 * @return the table
	 */
	public String getTable()
	{
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable( String table )
	{
		this.table = table;
	}

	/**
	 * @return the selection
	 */
	public String getWhereClause()
	{
		return whereClause;
	}

	/**
	 * @param selection the selection to set
	 */
	public void setWhereClause( String selection )
	{
		this.whereClause = selection;
	}

	/**
	 * @return the selectionArgs
	 */
	public String[] getWhereArgs()
	{
		return whereArgs;
	}

	/**
	 * @param whereArgs the selectionArgs to set
	 */
	public void setWhereArgs( String[] whereArgs )
	{
		this.whereArgs = whereArgs;
	}

	/**
	 * @return the groupBy
	 */
	public String getGroupBy()
	{
		return groupBy;
	}

	/**
	 * @param groupBy the groupBy to set
	 */
	public void setGroupBy( String groupBy )
	{
		this.groupBy = groupBy;
	}

	/**
	 * @return the having
	 */
	public String getHaving()
	{
		return having;
	}

	/**
	 * @param having the having to set
	 */
	public void setHaving( String having )
	{
		this.having = having;
	}

	/**
	 * @return the orderBy
	 */
	public String getOrderBy()
	{
		return orderBy;
	}

	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy( String orderBy )
	{
		this.orderBy = orderBy;
	}

	/**
	 * @return the isDistinct
	 */
	public boolean isDistinct()
	{
		return isDistinct;
	}

	/**
	 * @param isDistinct the isDistinct to set
	 */
	public void setDistinct( boolean isDistinct )
	{
		this.isDistinct = isDistinct;
	}

	/**
	 * @return the limit
	 */
	public String getLimit()
	{
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit( String limit )
	{
		this.limit = limit;
	}

}
