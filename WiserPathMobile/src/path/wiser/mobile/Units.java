/**
 * 
 */
package path.wiser.mobile;

/**
 * @author andrewnisbet
 * 
 */
public class Units
{

	private static MeasurementSystem	measurementSystem	= MeasurementSystem.METRIC;
	private static ActivityType			activityType		= ActivityType.WALK;

	private Units()
	{

	}

	public enum MeasurementSystem
	{
		METRIC, IMPERIAL
	}

	public static void setMeasurementSystem( MeasurementSystem system )
	{
		measurementSystem = system;
	}

	public static void setActivityType( ActivityType type )
	{
		activityType = type;
	}

	public static ActivityType getActivityType()
	{
		return activityType;
	}

	/**
	 * @return Standard unit of measure for speed based on measurement system.
	 */
	public static String speed()
	{
		switch (measurementSystem)
		{
		case IMPERIAL:
			return " MPH";
		default:
			return " KPH";
		}
	}

	public static double convertToImperialDistance( double measure )
	{
		// TODO compute imperial distance from metric value.
		return 0.0;
	}

	/**
	 * @return
	 */
	public static String distance()
	{
		switch (measurementSystem)
		{
		case IMPERIAL:
			return " mi";
		default:
			return " kms";
		}
	}

	/**
	 * @param value the value the user will show on the screen.
	 * @param isLatitude if treu will put E/W and if false will put N/S as appropriate.
	 * @return N, S, E, or W.
	 */
	public static String coordinate( double value, boolean isLatitude )
	{
		// TODO Convert to the desired location and append type.
		return " deg N";
	}

	/**
	 * @return Units of time -- seconds.
	 */
	public static String time()
	{
		return " sec";
	}

	/**
	 * @return units for bearing.
	 */
	public static String bearing()
	{
		// TODO Consults Arie about what this would be.
		return " deg";
	}

	public static String pace()
	{
		// TODO also account for activity type for more useful units.
		switch (measurementSystem)
		{
		case IMPERIAL:
			return " feet/sec";
		default:
			return " m/s";
		}
	}

}
