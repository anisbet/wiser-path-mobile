/**
 * 
 */
package path.wiser.mobile;

/**
 * The types of activities controls things like if we use altitude and how frequently we
 * we want the GPS to record points.
 * 
 * @author andrewnisbet
 * 
 */
public enum ActivityType
{
	WALK, RUN, HIKE, // uses altitude
	BIKE,
	DRIVE
}
