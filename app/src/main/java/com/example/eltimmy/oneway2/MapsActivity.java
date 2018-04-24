package com.example.eltimmy.oneway2;

import android.*;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
        , LocationListener {

    private GoogleMap mMap;

  private FusedLocationProviderClient mFusedLocationClient;//will be used to get my location

 /*   private FusedLocationProviderApi locationProviderApi = LocationServices.FusedLocationApi;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;


  public static final int MINUTE_PER_SECOND = 1000;
    public static final int MINUTE = 5 * MINUTE_PER_SECOND;
    private Marker marker=null;
    private TextView textView;
*/

    private Object dataTransfer[];//will be used to transfer my objects to 'GetDirecionsData' class
    private String url;//url requst
    private int waypoints;//number of markers

    JSONArray jsonArray=null;
    ArrayList<Marker> markers;
    ArrayList<LatLng> latLngs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

             /* googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();

        locationRequest.setInterval(MINUTE);
        locationRequest.setFastestInterval(1 * MINUTE_PER_SECOND);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
*/
    }


    private String getDirctionsUrl(ArrayList<LatLng> latLng) {
//creating url request for google api
        StringBuilder googleDirectionsUrl=new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+latLng.get(0).latitude+","+latLng.get(0).longitude);
        googleDirectionsUrl.append("&destination="+latLng.get(0).latitude+","+latLng.get(0).longitude);
        googleDirectionsUrl.append("&waypoints=optimize:true|");
        for(int i=0;i<latLng.size();i++)
        {
            googleDirectionsUrl.append(+latLng.get(i).latitude+","+latLng.get(i).longitude+"|");
        }

        googleDirectionsUrl.append("&key=AIzaSyBlAFd3r2H62WgQNnmDMTMmiS7F9I-va5Q");

        return googleDirectionsUrl.toString();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap = googleMap;


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Task location = mFusedLocationClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {


                latLngs=new ArrayList<LatLng>();
                markers = new ArrayList<Marker>();

                latLngs.add(new LatLng(31.2468149,32.3195051));
                latLngs.add(new LatLng(31.241462, 32.316670));
                latLngs.add(new LatLng(31.2438146,32.3165811));

                markers.add(mMap.addMarker(new MarkerOptions().position(latLngs.get(0)).title("Marker 1")));
                markers.add(mMap.addMarker(new MarkerOptions().position(latLngs.get(1)).title("Marker 2")));
                markers.add(mMap.addMarker(new MarkerOptions().position(latLngs.get(2)).title("Marker 3")));

//my location:
                Location location1 = (Location) task.getResult();
                    LatLng latLng = new LatLng(location1.getLatitude(), location1.getLongitude());
                    latLngs.add(latLng);
                    MarkerOptions MO = new MarkerOptions();
                    MO.position(latLng);
                    MO.title("current Location");
                    MO.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    markers.add(mMap.addMarker(MO));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));

//.






        dataTransfer= new Object[4];
        url=getDirctionsUrl(latLngs);
        GetDirectionsData getDirctionsData=new GetDirectionsData();
        waypoints=markers.size();

        dataTransfer[0]=mMap;
        dataTransfer[1]=url;
        dataTransfer[2]=markers;
        dataTransfer[3]=waypoints;
        getDirctionsData.execute(dataTransfer);



            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
  /*      if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    */}

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
/* if (marker!=null)
        {
            marker.remove();
        }

        //latLng= new LatLng(location.getLatitude(),location.getLongitude());
        //marker=mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
*/
    }


    /*@Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient.isConnected())
        {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
    }
*/}
