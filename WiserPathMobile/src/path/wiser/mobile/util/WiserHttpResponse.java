/**
 * 
 */
package path.wiser.mobile.util;

/**
 * @author anisbet
 *         Encapsulates the response string and status of the transaction.
 */
public class WiserHttpResponse
{
	public enum ResponseType
	{
		UNSET, OK, FAIL_INVALID_URI, FAIL_COMMUNICATION_ERR;
	}

	private String			message	= null;
	private ResponseType	status	= ResponseType.UNSET;

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage( String message )
	{
		this.message = message;
	}

	/**
	 * @return the status
	 */
	public boolean isSuccessful()
	{
		if (status == ResponseType.OK) return true;
		return false;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus( ResponseType status )
	{
		this.status = status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "HttpResponse [status=" + status + "]";
	}

}
