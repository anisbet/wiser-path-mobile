This page includes notes for implementing the map in the `WiserPathActivity` tab.

# Introduction #

Based on the techniques found on the tutorial at [Android Developers](http://developer.android.com/guide/tutorials/views/hello-mapview.html), I put together a simple project to test the concept, and then moved the code to WiserPathMobile.


# Details #
  1. Get an API Key from Google that can be done [here](http://code.google.com/android/add-ons/google-apis/).
  1. Changed the project > Properties > Android target application to Google-API (Android + Google-API).
  1. Added the `<uses-library android:name="com.google.android.maps" />` to the `<application>` tag.
  1. Made changes to the `wiserpathactivity-tab.xml` resource to include the API key and fill the parent container.
  1. Extended **WiserPathActivity** class to extend **MapActivity** rather than **Activity**.
  1. added
```
  setContentView( R.layout.wiserpath_tab );
  MapView mapView = (MapView) findViewById( R.id.mapview );
  mapView.setBuiltInZoomControls( true );
```
to `onCreate()` and implemented
```
@Override
protected boolean isRouteDisplayed()
{
	return false;
}
```

  1. When you ship a release you will need a new 'release' API key from Google. This is done in a similar way to the debug key and is described [here](http://my.safaribooksonline.com/book/programming/android/9781449309473/getting-your-application-into-users-hands/ch04_id221336#X2ludGVybmFsX0ZsYXNoUmVhZGVyP3htbGlkPTk3ODE0NDkzMDk0NzMvMTA4).

## Feature Request ##
  1. Give the Wiser Map a menu to select and control data from WP and/or WPM.

## Future Implementation ##
  * The team leader would like to see the map pan as the user moves and the GPS updates.
  * There should be a limit to the zoom level so that we can simplify data display. The problem is that as the we move across the map the desired POIs should be loaded as we move into a new reference frame (as defined by the bounding box of the tab screen boundaries), and the POIs that are no longer visible should be dumped from cache.