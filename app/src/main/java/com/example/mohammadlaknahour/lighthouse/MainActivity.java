package com.example.mohammadlaknahour.lighthouse;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohammadlaknahour.lighthouse.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    public final static  String EXTERA_MESSAGE="com.example.mohammadlaknahour.lighthouse.MESSAGE";

    private static final int ERROR_DIALOG_REQUEST = 9001;
    GoogleMap mMap;
    private static final double
            CSUN_LAT = 34.240059,
            CSUN_LNG = -118.529436;


    AutoCompleteTextView acTextView;


    String[] building = {
            "Bayramian Hall", "BH North", "BH East", "BH south", "BH West",/*End-BH */
            "Jacaranda Hall", "JD North", "JD South_01", "JD south_02", "JD West",/*End-JD */
            "Juniper Hall", "JH south", "JH East",/*End-JH */
            "Jerome Richfield Hall", "JR North", "JR East", "JR south",/*End-JR */
            "Live Oak Hall", "LO North", "LO EAST", "LO south", "LO West",/*End-LO */
            "Manzanita Hall", "MZ North", "MZ South",/*End-MZ */
            "Chaparral Hall", "CH North", "CH East", "CH south", "CH West",/*End-CH */
            "Eucalyptus Hall", "EH North", "EH East", "EH south", "EH West",/*End-EH */
            "Sierra Hall", "BH North_01", "BH North_02", "BH south",/*End-SH */
            "Sierra Tower","ST Eeat", "ST South",/*End-ST */
            "Oviatt Library", "OV North", "OV South"/*End-OV */
    };
    double locations[][] = {
            {34.240403, -118.530858}, {34.240641, -118.530697}, {34.240208, -118.530899},
            {34.239995, -118.531012}, {34.240327, -118.531359}, /*end_bh*/

            {34.241576, -118.528255}, {34.242075, -118.528347}, {34.241037, -118.528430},
            {34.241111, -118.528732}, {34.241828, -118.529250},/*end_JD*/


            {34.242035, -118.530606}, {34.241651, -118.530157}, {34.242152, -118.530500},/*end_JH*/

           {34.238891, -118.530660}, {34.239067, -118.530545}, {34.238864, -118.530933}, {34.238717, -118.530808},/*end_JR*/

            {34.238310, -118.528123}, {34.238380, -118.528306}, {34.238363, -118.528760},
            {34.238179, -118.528301}, {34.238325, -118.527671},/*end_LO*/

            {34.237765, -118.529786}, {34.237822, -118.529833}, {34.236890, -118.530428},/*end_MZ*/

            {34.242035, -118.527023}, {34.238558, -118.527209}, {34.237937, -118.527010},
            {34.238482, -118.526722}, {34.238139, -118.527176},/*end_CH*/


            {34.238643, -118.528198}, {34.238763, -118.528300},
            {34.238683, -118.527656}, {34.238541, -118.528262}, {34.238687, -118.528799},/*end_EH*/

            {34.238288, -118.530821}, {34.238453, -118.530197}, {34.238450, -118.531070}, {34.238112, -118.530768},/*end_SH*/

            {34.238772, -118.530199}, {34.238789, -118.530156}, {34.238656, -118.530220},/*end_ST*/

           {34.240059, -118.529436}, {34.240405, -118.529323}, {34.239775, -118.529315},/*end_OV*/
    };


    private GoogleApiClient mLocationClient;
    private Marker marker1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        if (servicesOK()) {
            setContentView(R.layout.map_activity);
            acTextView = (AutoCompleteTextView) findViewById(R.id.AutoCompeleteTextView1);
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.select_dialog_item, building);
            acTextView.setThreshold(1);
            acTextView.setAdapter(adapter);

            if (initMap()) {
                gotoLocation(CSUN_LAT, CSUN_LNG, 17);

                mLocationClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();

                mLocationClient.connect();

               // mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Map not connected!", Toast.LENGTH_SHORT).show();
            }

        } else {
            setContentView(R.layout.activity_main);
        }


    }


    private LocationManager locMan;
    private Marker userMarker;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Add menu handling code
        switch (id) {
            case R.id.mapTypeNone:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public boolean servicesOK() {

        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog =
                    GooglePlayServicesUtil.getErrorDialog(isAvailable, this, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to mapping service", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private boolean initMap() {




        if (mMap == null) {


            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFragment.getMap();


            mMap.addMarker(new MarkerOptions().position(new LatLng(34.240403, -118.530858))
                    .title("Bayramian Hall")
                    .snippet("consider yourself located")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));


            mMap.addMarker(new MarkerOptions().position(new LatLng(34.241576, -118.528255))
                    .title("Jacaranda Hall")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));


            mMap.addMarker(new MarkerOptions().position(new LatLng(34.242035, -118.530606))
                    .title("Juniper Hall")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));


            mMap.addMarker(new MarkerOptions().position(new LatLng(34.238891, -118.530660))
                    .title("Jerome Richfield Hall")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));


            mMap.addMarker(new MarkerOptions().position(new LatLng(34.238288, -118.530821))
                    .title("Sierra Hall")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));


            mMap.addMarker(new MarkerOptions().position(new LatLng(34.238643, -118.528198))
                    .title("Eucalyptus Hall")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));


            mMap.addMarker(new MarkerOptions().position(new LatLng(34.240059, -118.529436))
                    .title("Oviatt")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));


            mMap.addMarker(new MarkerOptions().position(new LatLng(34.238643, -118.528198))
                    .title("Eucalyptus Hall")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));


            mMap.addMarker(new MarkerOptions().position(new LatLng(34.237765, -118.529786))
                    .title("Manzanita Hall")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));

            mMap.addMarker(new MarkerOptions().position(new LatLng(34.238772, -118.530199))
                    .title("Sierra Tower")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));



            mMap.addMarker(new MarkerOptions().position(new LatLng(34.238310, -118.528123))
                    .title("Live Oak Hall")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));



            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(34.2417, -118.5283), 12));


            if (mMap != null) {
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker1) {
                        return null;
                    }

                    @Override

                    public View getInfoContents(Marker marker1) {
                        View v = getLayoutInflater().inflate(R.layout.window, null);
                        TextView tvLocality = (TextView) v.findViewById(R.id.tvLocality);
                        TextView tvLat = (TextView) v.findViewById(R.id.tvLat);
                        TextView tvLng = (TextView) v.findViewById(R.id.tvLng);
                        TextView tvSnippet = (TextView) v.findViewById(R.id.tvSnippet);

                        LatLng latLng = marker1.getPosition();
                        tvLocality.setText(marker1.getTitle());
                        tvLat.setText("Latitude: " + latLng.latitude);
                        tvLng.setText("Longitude: " + latLng.longitude);

                        tvSnippet.setText(marker1.getSnippet());

                        return v;
                    }
                });





                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override


                    public void onMapLongClick(LatLng latLng) {
                        Geocoder gc = new Geocoder(MainActivity.this);
                        List<Address> list = null;

                        try {
                            list = gc.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }


                        Address add = list.get(0);
                        MainActivity.this.addMarker(add, latLng.latitude, latLng.longitude);
                    }
                });

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker1) {
                                String msg = marker1.getTitle() + " (" +
                                marker1.getPosition().latitude + ", " +
                                 marker1.getPosition().longitude + ")";
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });




                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker marker1) {


                        Intent intent = new Intent(MainActivity.this, view.class);

                        intent.putExtra("location", marker1.getTitle() + " (" +
                                marker1.getPosition().latitude + ", " +
                                marker1.getPosition().longitude + ")");


                        startActivity(intent);
                    }
                });

            }

        }
        return (mMap != null);
    }

    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(update);


    }


    private void hideSoftKeyboard(View v) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void geoLocate(View v) throws IOException {

        hideSoftKeyboard(v);

        TextView tv = (TextView) findViewById(R.id.AutoCompeleteTextView1);
        String searchString = tv.getText().toString();

        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(searchString, 1);




        hideSoftKeyboard(v);

        int index = 0;
        for (int i = 0; i < 10; i++)
            if (searchString.equals(building[i]))
                index = i;

        double lat1 = locations[index][0];
        double lon = locations[index][1];
        System.out.println("lat1 is " + lat1 + " lon is " + lon);
        final LatLng PERTH1 = new LatLng(lat1, lon);

          Toast.makeText(this,"searching for:"+ searchString ,Toast.LENGTH_LONG).show();

         Marker perth = mMap.addMarker(new MarkerOptions().position(PERTH1).draggable(true));

        Toast.makeText(this, "Found: " + locations[index][0], Toast.LENGTH_SHORT).show();


        if (list.size() > 0) {
            Address add = list.get(0);
            String locality = add.getLocality();
            Toast.makeText(this, "Found: " + locality, Toast.LENGTH_SHORT).show();

            double lat = add.getLatitude();
            double lng = add.getLongitude();
            gotoLocation(lat, lng, 15);


        }
    }
    //moe

    public void addMarker(Address add, double lat, double lng) {
        mMap.addMarker(new MarkerOptions() //moe
                .title(add.getLocality())
                .position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_final))
                .draggable(true));

    }



    //(34.2417 ,-118.5283)


    public void showCurrentLocation(MenuItem item) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        Location currentLocation = LocationServices.FusedLocationApi
                .getLastLocation(mLocationClient);
        if (currentLocation == null) {
            Toast.makeText(this, "Couldn't connect!", Toast.LENGTH_SHORT).show();
        } else {
            LatLng latLng = new LatLng(
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude()
            );
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
                    latLng, 15
            );
            mMap.animateCamera(update);
        }

    }



    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Ready to map!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }





}
