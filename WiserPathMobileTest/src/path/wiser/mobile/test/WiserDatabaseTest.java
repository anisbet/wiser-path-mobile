package path.wiser.mobile.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import path.wiser.mobile.db.PoiIncedent;
import path.wiser.mobile.db.WiserDatabase;
import android.test.AndroidTestCase;
import android.util.Log;
import junit.framework.TestCase;


public class WiserDatabaseTest extends AndroidTestCase {
	
	public final static String	TAG	= "WiserDBTest";
	private WiserDatabase		wdb	= null;
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		wdb = new WiserDatabase( this.getContext() );
		wdb.open();
		PoiIncedent poi = new PoiIncedent();
		poi.setBlog("This is the Blog string");
		poi.setTitle("Poi Title");
		poi.isIncident(true);
		wdb.insert(poi);
		
		assertNotNull( wdb );
	}
	
	@After
	protected void tearDown() {
		wdb.close();
	}

}
