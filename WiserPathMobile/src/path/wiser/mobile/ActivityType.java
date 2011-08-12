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
	// these activity types are used to set the granularity of location recording of the GPS.
	// It is important that this list remain synchronized with the /res/values/array_list.xml
	// and /res/xml/preferences.xml
	WALK,
	RUN,
	HIKE, // uses altitude
	BIKE,
	DRIVE
}
