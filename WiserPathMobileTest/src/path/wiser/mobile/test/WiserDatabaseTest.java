/**
 * 
 */
package path.wiser.mobile.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import path.wiser.mobile.db.WiserDatabase;

import android.app.Activity;
import android.os.Bundle;
import android.test.AndroidTestCase;
import android.util.Log;
import android.widget.TextView;

/**
 * @author andrewnisbet
 * 
 */
public class WiserDatabaseTest extends AndroidTestCase
{
	public final static String	TAG	= "WiserDBTest";

	private WiserDatabase		wdb	= null;


	/**
	 * Test method for
	 * {@link path.wiser.mobile.db.WiserDatabase#WiserDatabase(android.content.Context)}
	 * .
	 */
	@Test
	public void testWiserDatabase()
	{
		WiserDatabase db = new WiserDatabase( null );
		assertNotNull( db );
		db.open();
		Log.i( TAG, "Print something will ya?" );
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		wdb = new WiserDatabase( this.getContext() );
		assertNotNull( wdb );
		wdb.close();
		
	}

}
