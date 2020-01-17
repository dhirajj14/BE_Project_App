package com.example.smartdustbin;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A styled map using JSON styles from a raw resource.
 */
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    FirebaseDatabase database;
    Location mLastLocation;
    String url;
    Polyline polylineFinal;
    Marker mCurrLocationMarker;
    LatLng olatLng;
    LatLng dest;
    DatabaseReference myRef ;
    private static final String TAG = MapsActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        // Get the SupportMapFragment and register for the callback
        // when the map is ready for use.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready for use.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location location) {
                        mLastLocation = location;
                        if (mCurrLocationMarker != null)
                        {
                            mCurrLocationMarker.remove();
                        }

                        //Place current location marker
                        olatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(olatLng);
                        markerOptions.title("Current Location");
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.truck));
                        mCurrLocationMarker = googleMap.addMarker(markerOptions);
                        }
                });

            }
        }
        else {
            googleMap.setMyLocationEnabled(true);
        }
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.



            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Dustbin_status");


            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Map_marker_info value = child.getValue(Map_marker_info.class);
                        String area= value.getArea();
                        String distance = value.getDistance();
                        float latitude= value.getLatitude();
                        float longitude = value.getLongitude();
                        Log.d("ADebugTag", "Value: " + latitude);
                        LatLng locate= new LatLng(latitude,longitude);
                        MarkerOptions marker = new MarkerOptions().position(locate).title(area+ " Status: "+distance+"% Filled");
                        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.trash_map_marker));
                        googleMap.addMarker(marker).showInfoWindow();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locate,15));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });



            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(polylineFinal !=null){
                    polylineFinal.remove();
                }

                dest=marker.getPosition();
                // Getting URL to the Google Directions API
                url = getUrl(olatLng, dest);
                Log.d("onMapClick", url.toString());
                FetchUrl FetchUrl = new FetchUrl();

                // Start downloading json data from Google Directions API
                FetchUrl.execute(url);
                //move map camera
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(olatLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                return false;
            }

            private String getUrl(LatLng origin, LatLng dest) {

                // Origin of route
                String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

                // Destination of route
                String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


                // Sensor enabled
                String sensor = "sensor=false";

                // Building the parameters to the web service
                String parameters = str_origin + "&" + str_dest + "&" + sensor;

                // Output format
                String output = "json";

                // Building the url to the web service
                String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" +"AIzaSyBuDvcJR01n0qHLqB4oYLYmrkl-kFpZmW0";


                return url;
            }

            /**
             * A method to download json data from url
             */
            private String downloadUrl(String strUrl) throws IOException {
                String data = "";
                InputStream iStream = null;
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(strUrl);

                    // Creating an http connection to communicate with url
                    urlConnection = (HttpURLConnection) url.openConnection();

                    // Connecting to url
                    urlConnection.connect();

                    // Reading data from url
                    iStream = urlConnection.getInputStream();

                    BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                    StringBuffer sb = new StringBuffer();

                    String line = "";
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    data = sb.toString();
                    Log.d("downloadUrl", data.toString());
                    br.close();

                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                } finally {
                    iStream.close();
                    urlConnection.disconnect();
                }
                return data;
            }

            // Fetches data from url passed
            class FetchUrl extends AsyncTask<String, Void, String> {

                @Override
                protected String doInBackground(String... url) {

                    // For storing data from web service
                    String data = "";

                    try {
                        // Fetching the data from web service
                        data = downloadUrl(url[0]);
                        Log.d("Background Task data", data.toString());
                    } catch (Exception e) {
                        Log.d("Background Task", e.toString());
                    }
                    return data;
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);

                    ParserTask parserTask = new ParserTask();

                    // Invokes the thread for parsing the JSON data
                    parserTask.execute(result);

                }
            }

            /**
             * A class to parse the Google Places in JSON format
             */
            class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

                // Parsing the data in non-ui thread
                @Override
                protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

                    JSONObject jObject;
                    List<List<HashMap<String, String>>> routes = null;

                    try {
                        jObject = new JSONObject(jsonData[0]);
                        Log.d("ParserTask",jsonData[0].toString());
                        DataParser parser = new DataParser();
                        Log.d("ParserTask", parser.toString());

                        // Starts parsing data
                        routes = parser.parse(jObject);
                        Log.d("ParserTask","Executing routes");
                        Log.d("ParserTask",routes.toString());

                    } catch (Exception e) {
                        Log.d("ParserTask",e.toString());
                        e.printStackTrace();
                    }
                    return routes;
                }

                // Executes in UI thread, after the parsing process
                @Override
                protected void onPostExecute(List<List<HashMap<String, String>>> result) {
                    ArrayList<LatLng> points;
                    PolylineOptions lineOptions = null;

                    // Traversing through all the routes
                    for (int i = 0; i < result.size(); i++) {
                        points = new ArrayList<>();
                        lineOptions = new PolylineOptions();

                        // Fetching i-th route
                        List<HashMap<String, String>> path = result.get(i);

                        // Fetching all the points in i-th route
                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);

                            points.add(position);
                        }

                        // Adding all the points in the route to LineOptions
                        lineOptions.addAll(points);
                        lineOptions.width(10);
                        lineOptions.color(getResources().getColor(R.color.map_track));
                        Log.d("onPostExecute","onPostExecute lineoptions decoded");

                    }

                    // Drawing polyline in the Google Map for the i-th route
                    if(lineOptions != null) {
                       polylineFinal = googleMap.addPolyline(lineOptions);
                    }
                    else {
                        Log.d("onPostExecute","without Polylines drawn");
                    }
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}