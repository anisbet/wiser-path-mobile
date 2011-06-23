package path.wiser.mobile.services;

public class WiserCookie
{
	private String	cookie;
	private String	node;

	public WiserCookie( )
	{
		this.cookie = new String();
		this.node = "-1";
	}
	
	public WiserCookie( String cookie )
	{
		this.cookie = cookie;
	}

	@Override
	public String toString()
	{
		return cookie;
	}

	public void setLocation( String value )
	{
		String[] pathParts = value.split( "/" );
		this.node = pathParts[pathParts.length - 1];
	}

	public String getUserNodeNumber()
	{
		return node;
	}

}
