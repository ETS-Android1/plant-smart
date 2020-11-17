package com.example.plantsmart;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import maes.tech.intentanim.CustomIntent;
import static android.widget.Toast.LENGTH_SHORT;
import static com.example.plantsmart.ExampleJobService.started;
import static com.example.plantsmart.RegisterActivity.SHARED_PREFS;

public class DashBoardActivity extends AppCompatActivity {
    String Id = "";
    boolean doubleBackToExitPressedOnce = false;
    static boolean addPlant = false;
    DashBoardActivity dashBoardActivity = this;
    FloatingActionButton floating;
    Boolean EditMode = false;
    FragmentPickPlant fragmentPickPlant;
    Activity activity = this;
    BottomNavigationView bottomNav;

    public DashBoardActivity setFloating(FloatingActionButton floating, View toolbar, View toolbar2) {
        this.floating = floating;
        TypedValue tv = new TypedValue();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            params.height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        toolbar.setLayoutParams(params);
        toolbar2.setLayoutParams(params);
        return this;
    }

    public void addPic(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (result != null) {
                fragmentPickPlant.addPic(result.getUri());
            }
        }
    }

    public void save(View view) {
        fragmentPickPlant.save();
    }

    public void Edit(View view) {
        EditMode = true;
        fragmentPickPlant.EditModeFunc();
    }

    boolean select = false;

    public void addPlantHome(View view) {
        if (!loadData("Plant").isEmpty()) {
            addPlant = true;
            EditMode = true;
            bottomNav.setSelectedItemId(R.id.plant);
        } else {
            Toast.makeText(getApplicationContext(), "Choose plant first", LENGTH_SHORT).show();
            bottomNav.setSelectedItemId(R.id.plant);
        }
    }

    public void showPlant(View view) {
        if (!loadData("Plant").isEmpty()) {
            select = true;
            bottomNav.setSelectedItemId(R.id.plant);
        } else {
            Toast.makeText(getApplicationContext(), "Choose plant first", LENGTH_SHORT).show();
            bottomNav.setSelectedItemId(R.id.plant);
        }
    }

    public void maps(View view) {
        if (!loadData("Plant").isEmpty()) {
            startActivity(new Intent(DashBoardActivity.this, mapActivity.class));
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide0);
        } else {
            Toast.makeText(getApplicationContext(), "Choose plant first", LENGTH_SHORT).show();
            bottomNav.setSelectedItemId(R.id.plant);
        }
    }

    public void charts(View view) {
        if (!loadData("Plant").isEmpty()) {
            startActivity(new Intent(DashBoardActivity.this, ActivityCharts.class));
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide0);
        } else {
            Toast.makeText(getApplicationContext(), "Choose plant first", LENGTH_SHORT).show();
            bottomNav.setSelectedItemId(R.id.plant);
        }
    }

    public void logout(View view) {
        saveData("", "Id");
        saveData("", "Plant");
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
        CustomIntent.customType(this, "right-to-left");
    }

    public void RealTimeVal(View view) {
        if (!loadData("Plant").isEmpty()) {
            startActivity(new Intent(getApplicationContext(), RealTimeMonActivity.class));
            CustomIntent.customType(this, "left-to-right");
        } else {
            Toast.makeText(getApplicationContext(), "Choose plant first", LENGTH_SHORT).show();
            bottomNav.setSelectedItemId(R.id.plant);
        }

    }

    public void Control(View view) {
        if (!loadData("Plant").isEmpty()) {
            startActivity(new Intent(getApplicationContext(), ControlSystem.class));
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide0);
        } else {
            Toast.makeText(getApplicationContext(), "Choose plant first", LENGTH_SHORT).show();
            bottomNav.setSelectedItemId(R.id.plant);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        if (!started) {
            started=true;
            ContextCompat.startForegroundService(this, new Intent(this, ExampleJobService.class));
            Toast.makeText(this,"started",LENGTH_SHORT).show();
        }

        Id = loadData("Id");

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        fragmentPickPlant = new FragmentPickPlant();
        fragmentPickPlant.setDashBoardActivity(dashBoardActivity);
        fragmentPickPlant.setActivity(activity);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            if (loadData("Selection").compareTo("0") == 0) {

                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    if (bundle.getString("Edit").compareTo("Edit") == 0) {
                        fragmentPickPlant.setEditPlant(true);
                        fragmentPickPlant.setName(loadData("Plant"));
                    }
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        fragmentPickPlant).commit();
            } else if (loadData("Selection").compareTo("1") == 0) {
                EditMode = false;
                bottomNav.setSelectedItemId(R.id.home);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHome(loadData("Plant"))).commit();
            } else {
                EditMode = false;
                bottomNav.setSelectedItemId(R.id.home);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentHome(loadData("Plant"))).commit();
            }
        }


    }

    public void hideKeyBoard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.plant:

                            saveData("0", "Selection");
                            if (!addPlant) {
                                EditMode = false;
                            }
                            fragmentPickPlant = new FragmentPickPlant();
                            fragmentPickPlant.setDashBoardActivity(dashBoardActivity);
                            fragmentPickPlant.setActivity(activity);
                            if (select) {
                                fragmentPickPlant.setEditPlant(true);
                                select = false;
                            }
                            fragmentPickPlant.setName(loadData("Plant"));
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    fragmentPickPlant).commit();

                            break;
                        case R.id.home:
                            saveData("1", "Selection");
                            EditMode = false;
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    new FragmentHome(loadData("Plant"))).commit();
                            break;
                    }
                    return true;
                }
            };

    public void saveData(String data, String name) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, data);
        editor.apply();
    }

    public String loadData(String name) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if (sharedPreferences == null) {
            return "";
        }
        return sharedPreferences.getString(name, "");
    }

    public void back(View view) {
        onBackPressed();
        hideKeyBoard(view);
    }

    @Override
    public void onBackPressed() {
//        if (fragmentPickPlant.EditPlant) {
//            startActivity(new Intent(getApplicationContext(), plantActivity.class));
//            finish();
//            CustomIntent.customType(this, "left-to-right");
//        } else {
        if (EditMode) {
            EditMode = false;
            fragmentPickPlant.getPlants();
            fragmentPickPlant.SearchModeFunc();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
//        }
    }
}
