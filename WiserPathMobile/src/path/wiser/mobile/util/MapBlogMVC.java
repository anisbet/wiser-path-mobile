package path.wiser.mobile.util;

import path.wiser.mobile.geo.Blog;
import path.wiser.mobile.ui.WiserPathActivity;

public class MapBlogMVC implements ModelViewController
{

	private WiserPathActivity	activity;
	private Blog				blog;

	public MapBlogMVC( WiserPathActivity wiserPathActivity, Blog currentPoi )
	{
		this.activity = wiserPathActivity;
		this.blog = currentPoi;
	}

	@Override
	public void update()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void change()
	{
		// TODO Auto-generated method stub

	}

}
