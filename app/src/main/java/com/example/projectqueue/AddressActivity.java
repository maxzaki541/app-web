package com.example.projectqueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class AddressActivity extends FragmentActivity implements OnMapReadyCallback {
  DrawerLayout drawerLayout;
  //Location currentLocation;
  GoogleMap gmap;
  //SupportMapFragment mapFragment;
  //SearchView searchView;
  private MapView mapView;
  private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_address);

    drawerLayout = findViewById(R.id.drawer_layout);

    Bundle mapViewBundle = null;
    if (savedInstanceState != null) {
      mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
    }

    mapView = findViewById(R.id.mapView);
    mapView.onCreate(mapViewBundle);
    mapView.getMapAsync(this);

    //searchView = findViewById(R.id.sv_location);
    //mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.google_map);

        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if(location != null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(AddressActivity.this);
                    try{
                        addressList=geocoder.getFromLocationName(location,1);
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude() 14.0454636,101.3664964);
                    //map.addMarker(new MarkerOptions().position(latLng).title(location));
                    //map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });*/
//        mapFragment.getMapAsync(this);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
    if (mapViewBundle == null) {
      mapViewBundle = new Bundle();
      outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
    }

    mapView.onSaveInstanceState(mapViewBundle);
  }
  @Override
  public void onMapReady(GoogleMap googleMap) {
    gmap = googleMap;
    gmap.setMinZoomPreference(15);
    gmap.setIndoorEnabled(true);
    UiSettings uiSettings = gmap.getUiSettings();
    uiSettings.setIndoorLevelPickerEnabled(true);
    uiSettings.setMyLocationButtonEnabled(true);
    uiSettings.setMapToolbarEnabled(true);
    uiSettings.setCompassEnabled(true);
    uiSettings.setZoomControlsEnabled(true);

    LatLng ny = new LatLng(14.0454916,101.3686154);

    MarkerOptions markerOptions = new MarkerOptions();
    markerOptions.position(ny);
    gmap.addMarker(markerOptions);

    gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
  }


  public void ClickBack(View view) {
    Intent intent = new Intent(this, HomeActivity.class);
    finish();
    startActivity(intent);
  }

  protected  void onPause(){
    super.onPause();
    //HomeActivity.closeDrawer(drawerLayout);
    mapView.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  protected void onDestroy() {
    mapView.onDestroy();
    super.onDestroy();
  }
  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }


}
