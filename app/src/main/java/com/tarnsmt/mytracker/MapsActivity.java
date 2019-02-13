package com.tarnsmt.mytracker;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Bundle b=getIntent().getExtras();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        LoadLocation(b.getString("PhoneNumber"));

    }

    void  LoadLocation(String PhoneNumber){

        databaseReference.child("Users").child(PhoneNumber).
                child("Location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();
                if (td==null)return;
                double lat = Double.parseDouble(td.get("lat").toString());
                double lag = Double.parseDouble(td.get("lag").toString());
            

                sydney = new LatLng(lat, lag);
                  LastDateOnline= td.get("LastOnlineDate").toString();
                LoadMap();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value


            }
        });

    }


    void LoadMap(){
   


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    LatLng bangkok ;
    String LastDateOnline;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;




        mMap.addMarker(new MarkerOptions().position(bangkok).title("last online:"+ LastDateOnline));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bangkok,15));
    }





}
