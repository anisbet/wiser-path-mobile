/**
 * 
 */
package path.wiser.mobile.geo;

import java.util.Vector;

import path.wiser.mobile.Units;
import android.location.Location;

/**
 * This class does the heavy lifting for computing the following metrics:
 * <ol>
 * <li>speed
 * <li>distance
 * <li>lat
 * <li>long
 * <li>time
 * <li>direction
 * <li>pace
 * </ol>
 * 
 * @author andrewnisbet
 * 
 */
public class TraceComputer
{
	private static Units	units;

	public TraceComputer( Units u, Vector<Location> locations )
	{
		units = u;
	}

}
