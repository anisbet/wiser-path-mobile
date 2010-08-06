package path.wiser.mobile.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import path.wiser.mobile.db.WiserDatabase;
import android.test.AndroidTestCase;
import android.util.Log;
import junit.framework.TestCase;

//public class WiserDatabaseTest extends AndroidTestCase
//{
//	public final static String	TAG	= "WiserDBTest";
//
//	private WiserDatabase		wdb	= null;
//
//
//	/**
//	 * Test method for
//	 * {@link path.wiser.mobile.db.WiserDatabase#WiserDatabase(android.content.Context)}
//	 * .
//	 */
//	@Test
//	public void testWiserDatabase()
//	{
//		WiserDatabase db = new WiserDatabase( null );
//		assertNotNull( db );
//		db.open();
//		Log.i( TAG, "Print something will ya?" );
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@Before
//	public void setUp() throws Exception
//	{
//		wdb = new WiserDatabase( this.getContext() );
//		assertNotNull( wdb );
//		wdb.close();
//		
//	}
//
//}



public class WiserDatabaseTest extends AndroidTestCase {
	
	public final static String	TAG	= "WiserDBTest";
	private WiserDatabase		wdb	= null;
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		wdb = new WiserDatabase( this.getContext() );
		assertNotNull( wdb );
	}
	
	@After
	protected void tearDown() {
		wdb.close();
	}

}
