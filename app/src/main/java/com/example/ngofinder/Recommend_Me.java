package com.example.ngofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ngofinder.Model.EventModel;

import com.example.ngofinder.Model.RegisteredUserModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Recommend_Me extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private GoogleMap miniMap;
    private MapView miniMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_me);

        miniMapView = findViewById(R.id.miniMapView);
        miniMapView.onCreate(savedInstanceState);
        // Get a handle to the LocationManager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


// Check if the user has granted permission for location access
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Request location updates
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // Update the map to center on the user's current location
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    miniMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

                    // Add a marker for the user's current location
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(currentLocation)
                            .title("You are here")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    miniMap.addMarker(markerOptions);
                }
            });
        } else {
            // Request permission for location access
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }


        miniMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                miniMap = googleMap;
                DatabaseReference ngosRef = FirebaseDatabase.getInstance().getReference().child("NGOS");
                ngosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ngoSnapshot : snapshot.getChildren()) {
                            String ngoId = ngoSnapshot.getKey();
                            DatabaseReference eventsRef = ngosRef.child(ngoId).child("Events");
                            eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                                        String eventId = eventSnapshot.getKey();
                                        DatabaseReference locationRef = eventsRef.child(eventId).child("location");
                                        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String location = snapshot.getValue(String.class);
                                                Geocoder geocoder = new Geocoder(Recommend_Me.this);
                                                List<Address> addresses = null;
                                                try {
                                                    addresses = geocoder.getFromLocationName(location, 1);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                if (addresses != null && addresses.size() > 0) {
                                                    double latitude = addresses.get(0).getLatitude();
                                                    double longitude = addresses.get(0).getLongitude();
                                                    LatLng place = new LatLng(latitude, longitude);
                                                    miniMap.addMarker(new MarkerOptions().position(place).title(location));
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                // Handle database error
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle database error
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        miniMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        miniMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        miniMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        miniMapView.onLowMemory();
    }
}
