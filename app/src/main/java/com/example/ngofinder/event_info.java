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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class event_info extends AppCompatActivity{

    private static final int LOCATION_PERMISSION_REQUEST = 100;
    String NGOID, EVENTID, USERID, name, status;
    FirebaseDatabase database;
    FirebaseAuth auth;
    Button bt_Track;
    private GoogleMap miniMap;
    private MapView miniMapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        TextView eventname = (TextView) findViewById(R.id.eventname);
        TextView eventdescription=(TextView)findViewById(R.id.eventdescription);
        TextView eventdate=(TextView)findViewById(R.id.eventdate);
        Button b1 = (Button) findViewById(R.id.register);
        bt_Track = findViewById(R.id.bt_track);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        miniMapView = findViewById(R.id.miniMapView);
        miniMapView.onCreate(savedInstanceState);

        NGOID = getIntent().getExtras().get("ngo_id").toString();
        EVENTID = getIntent().getExtras().get("event_id").toString();
        USERID = auth.getUid();

        DatabaseReference ref = database.getReference().child("NGOS").child(NGOID).child("Events").child(EVENTID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    EventModel event = snapshot.getValue(EventModel.class);
                    eventname.setText(event.getEventname());
                    eventdescription.setText(event.getDescription());
                    eventdate.setText(event.getDate());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        miniMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                miniMap = googleMap;
                database.getReference().child("NGOS").child(NGOID).child("Events").child(EVENTID).child("location").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String destination = snapshot.getValue(String.class);
                        Geocoder geocoder = new Geocoder(event_info.this);
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocationName(destination, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (addresses != null && addresses.size() > 0) {
                            double latitude = addresses.get(0).getLatitude();
                            double longitude = addresses.get(0).getLongitude();
                            LatLng place = new LatLng(latitude, longitude);
                            miniMap.addMarker(new MarkerOptions().position(place).title(destination));
                            miniMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 12));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                    }
                });
            }
        });


        database.getReference().child("Users").child(USERID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                name = user.getName();
                status = "Approved";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisteredUserModel reg = new RegisteredUserModel(name, status);
                reg.setTimeStamp(System.currentTimeMillis());
                DatabaseReference eventRef = database.getReference().child("NGOS").child(NGOID).child("Events").child(EVENTID);

                eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        EventModel event = snapshot.getValue(EventModel.class);
                        long count = snapshot.child("Registered Users").getChildrenCount();
                        int limit = event.getLimit();

                        if (count < limit) {
                            RegisteredUserModel reg = new RegisteredUserModel(name, status);
                            reg.setTimeStamp(System.currentTimeMillis());
                            database.getReference().child("NGOS").child(NGOID).child("Events").child(EVENTID).child("Registered Users").child(USERID).setValue(reg);

                            database.getReference().child("NGOS").child(NGOID).child("Events").child(EVENTID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    EventModel event = snapshot.getValue(EventModel.class);
                                    eventname.setText(event.getEventname());
                                    database.getReference().child("Users").child(USERID).child("EventsRegistered").child(NGOID).child(EVENTID).setValue(event);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });

                            Toast.makeText(getApplicationContext(), "You have sucessfully registered", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Limit reached! Unsucessful", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle any errors
                    }
                });
            }

        });

        bt_Track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            database.getReference().child("NGOS").child(NGOID).child("Events").child(EVENTID).child("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                  String destination = snapshot.getValue(String.class);
                  getUserLocationAndDisplayTrack(destination);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
            });
            }
        }
        );
    }

    private void DisplayTrack(String sSource, String sDestination) {
        try {
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + sSource + "/" + sDestination);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");

            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            startActivity(intent);
        }
    }


    private void getUserLocationAndDisplayTrack(String sDestination) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return;
        }

        // Get the last known location
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            // Display track from user's current location to the final destination
            DisplayTrack(lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude(), sDestination);
        } else {
            // Request location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // Display track from user's current location to the final destination
                    DisplayTrack(location.getLatitude() + "," + location.getLongitude(), sDestination);

                    // Stop location updates after getting the user's location
                    locationManager.removeUpdates(this);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }
    }

}

