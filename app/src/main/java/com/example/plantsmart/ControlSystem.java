package com.example.plantsmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import static com.example.plantsmart.RegisterActivity.SHARED_PREFS;

public class ControlSystem extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_system);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                toolbar.setTitle("Ideal values");
            }
        });
        View headerView = navigationView.getHeaderView(0);
        TextView plantName = (TextView) headerView.findViewById(R.id.plantNamee);
        plantName.setText(loadData("Plant"));
        getPicForEdit((ImageView) headerView.findViewById(R.id.plantPic));

        navigationView.setItemIconTintList(null);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentAdjust(loadData("Plant"))).commit();
            navigationView.setCheckedItem(R.id.ThreeShot);
        }
    }
    public String loadData(String name) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if (sharedPreferences == null) {
            return "";
        }
        return sharedPreferences.getString(name, "");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ThreeShot:
                toolbar.setTitle("Ideal values");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentAdjust(loadData("Plant"))).commit();
                break;
            case R.id.ControlOnOff:
                toolbar.setTitle("Control");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentControl(loadData("Plant"))).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void getPicForEdit(final ImageView plant) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("Fruits").child(loadData("Plant").toLowerCase() + ".png");
        try {
            final File localFile = File.createTempFile("images", "png");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    try {
                        plant.setImageURI(Uri.fromFile(localFile));
                    } catch (Exception ignored) {

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide0,R.anim.slide_in_top);
    }

}
