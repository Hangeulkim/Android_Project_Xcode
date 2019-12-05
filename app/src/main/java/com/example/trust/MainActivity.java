package com.example.trust;


import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import java.util.ArrayList;
import java.lang.String;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    LinearLayout Select_More_Layout;
    RelativeLayout Select_Start_Layout;
    LinearLayout Start_First_Layout;

    ImageButton Start_More;
    ImageButton Start_Start;

    public void Click_Select_Log(View view){

    }

    public void Click_Select_Setting(View view){

    }

    public void Click_Select_Makers(View view){

    }

    public void Click_More_Quit(View view){
        Select_More_Layout.setVisibility(View.INVISIBLE);
        Start_First_Layout.setVisibility(View.VISIBLE);
    }


    public void Click_Start_More(View view){
        Start_First_Layout.setVisibility(View.INVISIBLE);
        Select_More_Layout.setVisibility(View.VISIBLE);
    }

    public void Click_Start_Start(View view){
        Start_More.setVisibility(View.VISIBLE);
        Start_Start.setVisibility(View.INVISIBLE);

        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        else {
            final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);
        }

    }

    public void Click_Start_Timer(View view){

    }

    public void Click_Start_Select(View view){

    }

    public void Click_Start_Fast(View view){

    }

    private GoogleMap mMap;
    private GoogleMap gMap;
    private ArrayList<LatLng> arrayPoints;
    double p_lat, p_lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Select_More_Layout=(LinearLayout)findViewById(R.id.Select_More);
        Select_Start_Layout=(RelativeLayout)findViewById(R.id.Select_Start);
        Start_First_Layout=(LinearLayout)findViewById(R.id.Start_First);

        Start_More=(ImageButton)findViewById(R.id.Start_More);
        Start_Start=(ImageButton)findViewById(R.id.Start_Start);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        arrayPoints = new ArrayList<LatLng>();
    }

    public double CalcDistance(LatLng s_latlng, LatLng l_latlng){
        double dDistance = 0;
        double dLat1Rad = ((double)s_latlng.latitude)*(Math.PI/180.0);
        double dLong1Rad = ((double)s_latlng.longitude)*(Math.PI/180.0);
        double dLat2Rad = ((double)l_latlng.latitude)*(Math.PI/180.0);
        double dLong2Rad = ((double)l_latlng.longitude)*(Math.PI/180.0);

        double dLongitude = dLong2Rad - dLong1Rad;
        double dLatitude = dLat2Rad - dLat1Rad;
        double a = Math.pow(Math.sin(dLatitude/2.0), 2.0) + Math.cos(dLat1Rad) * Math.cos(dLat2Rad) * Math.pow(Math.sin(dLongitude/2.0),2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0-a));
        double kEarth = 6376.5;
        dDistance = kEarth * c;

        return dDistance;
    }

    final LocationListener gpsLocationListener = new LocationListener(){
        public void onLocationChanged(Location location){
            // String provider = location.getProvider();
            p_lng = location.getLongitude();
            p_lat = location.getLatitude();
            Double latitude = p_lat;
            Double longitude = p_lng;
            LatLng p_latlng = new LatLng(p_lat, p_lng);

            if(arrayPoints.size()>=1){
                arrayPoints.add(p_latlng);
                LatLng s_latlng = arrayPoints.get(arrayPoints.size()-2);
                LatLng l_latlng = arrayPoints.get(arrayPoints.size()-1);

                if(CalcDistance(s_latlng, l_latlng) < 0.01) {

                    gMap.clear();

                    MarkerOptions mOptions = new MarkerOptions();
                    mOptions.title("마커 좌표");


                    mOptions.snippet(String.valueOf(CalcDistance(s_latlng, l_latlng)));
                    mOptions.position(p_latlng);

                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(p_latlng, 18));
                    gMap.addMarker(mOptions);


                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.color(Color.RED);
                    polylineOptions.width(5);
                    polylineOptions.addAll(arrayPoints);
                    gMap.addPolyline(polylineOptions);
                }else{
                    arrayPoints.remove(arrayPoints.size()-1);
                }

        }else{
                gMap.clear();

                MarkerOptions mOptions = new MarkerOptions();
                mOptions.title("마커 좌표");


                mOptions.snippet(latitude.toString() + "," + longitude.toString());
                mOptions.position(p_latlng);

                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(p_latlng, 18));
                gMap.addMarker(mOptions);

                arrayPoints.add(p_latlng);

                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.RED);
                polylineOptions.width(5);
                polylineOptions.addAll(arrayPoints);
                gMap.addPolyline(polylineOptions);
        }

            // Circle circle = mMap.addCircle(new CircleOptions().center(new LatLng(p_lat, p_lng)).radius(3).strokeColor(Color.RED).fillColor(Color.BLUE));

        }
        public void onStatusChanged(String provider, int status, Bundle extras){
        }
        public void onProviderEnabled(String provider){
        }
        public void onProviderDisabled(String provider){
        }

    };

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        gMap = googleMap;


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                mOptions.title("마커 좌표");
                Double latitude = point.latitude;
                Double longitude = point.longitude;

                mOptions.snippet(latitude.toString() + "," + longitude.toString());
                mOptions.position(new LatLng(latitude, longitude));

                mMap.addMarker(mOptions);

//
//                Double latitude = point.latitude;
//                Double longitude = point.longitude;
//                LatLng p_latlng = new LatLng(latitude, longitude);

//                if(arrayPoints.size()>=1){
//                    arrayPoints.add(p_latlng);
//                    LatLng s_latlng = arrayPoints.get(arrayPoints.size()-2);
//                    LatLng l_latlng = arrayPoints.get(arrayPoints.size()-1);
//
//                    if(CalcDistance(s_latlng, l_latlng) < 0.1) {
//
//                        //gMap.clear();
//
//                        MarkerOptions mOptions = new MarkerOptions();
//                        mOptions.title("마커 좌표");
//
//
//                        mOptions.snippet(String.valueOf(CalcDistance(s_latlng, l_latlng)));
//                        mOptions.position(p_latlng);
//
//                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(p_latlng, 18));
//                        gMap.addMarker(mOptions);
//
//
//
//                        PolylineOptions polylineOptions = new PolylineOptions();
//                        polylineOptions.color(Color.RED);
//                        polylineOptions.width(5);
//                        polylineOptions.addAll(arrayPoints);
//                        gMap.addPolyline(polylineOptions);
//                    }else{
//                        arrayPoints.remove(arrayPoints.size()-1);
//                    }
//
//                }else{
//                    //gMap.clear();
//
//                    MarkerOptions mOptions = new MarkerOptions();
//                    mOptions.title("마커 좌표");
//
//
//                    mOptions.snippet(latitude.toString() + "," + longitude.toString());
//                    mOptions.position(p_latlng);
//
//                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(p_latlng, 18));
//                    gMap.addMarker(mOptions);
//
//                    arrayPoints.add(p_latlng);
//
//                    PolylineOptions polylineOptions = new PolylineOptions();
//                    polylineOptions.color(Color.RED);
//                    polylineOptions.width(5);
//                    polylineOptions.addAll(arrayPoints);
//                    gMap.addPolyline(polylineOptions);
//                }

            }

        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener(){
            public void onMapLongClick(LatLng point){
                mMap.clear();
                arrayPoints.clear();
            }
        });



        // Add a marker in start and move the camera
        LatLng start = new LatLng(37.5193, 126.9778);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));

    }
}
