package path.wiser.mobile.services;

public class WiserCookie 
{
	private String cookie;

	public WiserCookie( String cookie ) 
	{
		this.cookie = cookie;
	}

	@Override
	public String toString()
	{
		return cookie;
	}

}
