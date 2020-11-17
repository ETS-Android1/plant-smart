package com.example.plantsmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.plantsmart.RegisterActivity.SHARED_PREFS;

public class mapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    LatLng skypark = new LatLng(2.947348, 101.654612);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        animateTolocation(null);
    }


    public void animateTolocation(View view){

        FirebaseDatabase.getInstance().getReference().child("plants").child(loadData("Plant")).child("RealTimeVal").child("Real Time GPS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    String[] arr=snapshot.getValue().toString().split(",",2);
                    if(arr.length>1){
                        skypark = new LatLng(Double.parseDouble(arr[0]), Double.parseDouble(arr[1]));
                        mMap.addMarker(new MarkerOptions()
                                .position(skypark)
                                .title("Lettuce"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(skypark,16));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String loadData(String name) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if (sharedPreferences == null) {
            return "";
        }
        return sharedPreferences.getString(name, "");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide0,R.anim.slide_in_top);
    }
}
