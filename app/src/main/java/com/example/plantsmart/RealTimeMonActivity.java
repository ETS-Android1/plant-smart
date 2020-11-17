package com.example.plantsmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import maes.tech.intentanim.CustomIntent;

import static com.example.plantsmart.RegisterActivity.SHARED_PREFS;

public class RealTimeMonActivity extends AppCompatActivity {

    //Views
    ImageView logo, logo2, logo3, logo4, phPic, lightPic, temperaturePic, moisturePic, waterTankPic, ph, lightBulb, temperature, water, tank, StatusImage, StatusImage2, StatusImage3, StatusImage4;
    TextView plantName, SystemNameStatus, value, CurrValue, SystemNameStatus2, value2, CurrValue2, SystemNameStatus3, value3, CurrValue3, SystemNameStatus4, value4, CurrValue4;
    ProgressBar progressBar, progressBarTop, progressBar2, progressBarTop2, progressBar3, progressBarTop3, progressBar4, progressBarTop4;
    LinearLayout rel1, rel2, rel3, rel4;
    FrameLayout systemContainer, systemContainer2, systemContainer3, systemContainer4;
    //Values
    String SystemUsed = "PH";
    int progress = 0, progress2 = 0, progress3 = 0, progress4 = 0;
    int ScreenWidthPx = 0;
    SystemPlant systemPlant[] = new SystemPlant[10];
    boolean fan = false;


    public void PH(View view) {
        setLayout(phPic, "PH", R.drawable.phc, ph, R.drawable.phg, 1);
    }

    public void Light(View view) {
        setLayout(lightPic, "light", R.drawable.lightbulbc, lightBulb, R.drawable.lightbulbg, 2);
    }

    public void Temperature(View view) {
        setLayout(temperaturePic, "temperature", R.drawable.temperaturec, temperature, R.drawable.temperatureg, 3);
    }

    public void Moisture(View view) {
        setLayout(moisturePic, "moisture", R.drawable.waterc, water, R.drawable.waterg, 4);
    }

    public void rain(View view) {
        setLayout(waterTankPic, "tank water level", R.drawable.water_tankc, tank, R.drawable.water_tankg, 5);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);

        systemPlant[0] = new SystemPlant("PH sensor", 0);
        systemPlant[1] = new SystemPlant("LDR\n(LUX)", 0);
        systemPlant[2] = new SystemPlant("DHT 1\n(Temperature)", 0);
        systemPlant[3] = new SystemPlant("DHT 1\n(Humidity)", 0);
        systemPlant[4] = new SystemPlant("DHT 2\n(Temperature)", 0);
        systemPlant[5] = new SystemPlant("DHT 2\n(Humidity)", 0);
        systemPlant[6] = new SystemPlant("Moisture 1", 0);
        systemPlant[7] = new SystemPlant("Moisture 2", 0);
        systemPlant[8] = new SystemPlant("Ultra sonic 1\n(Height)", 0);
        systemPlant[9] = new SystemPlant("Ultra sonic 2\n(Height)", 0);

        initfunc();
    }

    int prog = 0;

    private void setProgressValue(final int progres) {
        prog = progres;
        if (progres > 100) {
            progressBar.setProgress(100);
            progressBarTop.setProgress(prog - 100);
        } else {
            progressBarTop.setProgress(0);
            progressBar.setProgress(prog);
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (prog < progress) {
                    setProgressValue(prog + 1);
                } else if (prog > progress) {
                    setProgressValue(prog - 1);
                } else {
                    setProgressValue(prog);
                }
            }
        });
        thread.start();
    }

    int prog2 = 0;

    private void setProgressValue2(final int progres) {
        prog2 = progres;
        if (progres > 100) {
            progressBar2.setProgress(100);
            progressBarTop2.setProgress(prog2 - 100);
        } else {
            progressBarTop2.setProgress(0);
            progressBar2.setProgress(prog2);
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (prog2 < progress2) {
                    setProgressValue2(prog2 + 1);
                } else if (prog2 > progress2) {
                    setProgressValue2(prog2 - 1);
                } else {
                    setProgressValue2(prog2);
                }
            }
        });
        thread.start();
    }


    int prog3 = 0;

    private void setProgressValue3(final int progres) {
        prog3 = progres;
        if (progres > 100) {
            progressBar3.setProgress(100);
            progressBarTop3.setProgress(prog3 - 100);
        } else {
            progressBarTop3.setProgress(0);
            progressBar3.setProgress(prog3);
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (prog3 < progress3) {
                    setProgressValue3(prog3 + 1);
                } else if (prog3 > progress3) {
                    setProgressValue3(prog3 - 1);
                } else {
                    setProgressValue3(prog3);
                }
            }
        });
        thread.start();
    }


    int prog4 = 0;

    private void setProgressValue4(final int progres) {
        prog4 = progres;
        if (progres > 100) {
            progressBar4.setProgress(100);
            progressBarTop4.setProgress(prog4 - 100);
        } else {
            progressBarTop4.setProgress(0);
            progressBar4.setProgress(prog4);
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (prog4 < progress4) {
                    setProgressValue4(prog4 + 1);
                } else if (prog4 > progress4) {
                    setProgressValue4(prog4 - 1);
                } else {
                    setProgressValue4(prog4);
                }
            }
        });
        thread.start();
    }


    public void initfunc() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ScreenWidthPx = displayMetrics.widthPixels;

        progressBar = findViewById(R.id.progressBar);
        progressBarTop = findViewById(R.id.progressBarTop);

        progressBar2 = findViewById(R.id.progressBar2);
        progressBarTop2 = findViewById(R.id.progressBarTop2);

        progressBar3 = findViewById(R.id.progressBar3);
        progressBarTop3 = findViewById(R.id.progressBarTop3);

        progressBar4 = findViewById(R.id.progressBar4);
        progressBarTop4 = findViewById(R.id.progressBarTop4);

        setProgressValue(0);
        setProgressValue2(0);
        setProgressValue3(0);
        setProgressValue4(0);

        logo = findViewById(R.id.logo);
        logo2 = findViewById(R.id.logo2);
        logo3 = findViewById(R.id.logo3);
        logo4 = findViewById(R.id.logo4);

        rel1 = findViewById(R.id.rel1);
        rel2 = findViewById(R.id.rel2);
        rel3 = findViewById(R.id.rel3);
        rel4 = findViewById(R.id.rel4);

        plantName = findViewById(R.id.PlantName);

        StatusImage = findViewById(R.id.StatusImage);
        StatusImage2 = findViewById(R.id.StatusImage2);
        StatusImage3 = findViewById(R.id.StatusImage3);
        StatusImage4 = findViewById(R.id.StatusImage4);

        systemContainer = findViewById(R.id.systemContainer);
        systemContainer2 = findViewById(R.id.systemContainer2);
        systemContainer3 = findViewById(R.id.systemContainer3);
        systemContainer4 = findViewById(R.id.systemContainer4);

        CurrValue = findViewById(R.id.CurrValue);
        CurrValue2 = findViewById(R.id.CurrValue2);
        CurrValue3 = findViewById(R.id.CurrValue3);
        CurrValue4 = findViewById(R.id.CurrValue4);

        SystemNameStatus = findViewById(R.id.SystemNameStatus);
        SystemNameStatus2 = findViewById(R.id.SystemNameStatus2);
        SystemNameStatus3 = findViewById(R.id.SystemNameStatus3);
        SystemNameStatus4 = findViewById(R.id.SystemNameStatus4);

        value = findViewById(R.id.value);
        value2 = findViewById(R.id.value2);
        value3 = findViewById(R.id.value3);
        value4 = findViewById(R.id.value4);

        int icon = ((ScreenWidthPx - dpToPx(40)) / 5);
        phPic = findViewById(R.id.phPic);
        setView(phPic, icon, icon);
        lightPic = findViewById(R.id.lightPic);
        setView(lightPic, icon, icon);
        moisturePic = findViewById(R.id.moisturePic);
        setView(moisturePic, icon, icon);
        temperaturePic = findViewById(R.id.tempPic);
        setView(temperaturePic, icon, icon);
        waterTankPic = findViewById(R.id.waterTankPic);
        setView(waterTankPic, icon, icon);

        ph = findViewById(R.id.ph);
        setView(ph, icon - dpToPx(35), icon - dpToPx(35));
        lightBulb = findViewById(R.id.lightBulb);
        setView(lightBulb, icon - dpToPx(35), icon - dpToPx(35));
        temperature = findViewById(R.id.temperature);
        setView(temperature, icon - dpToPx(35), icon - dpToPx(35));
        water = findViewById(R.id.water);
        setView(water, icon - dpToPx(35), icon - dpToPx(35));
        tank = findViewById(R.id.tank);
        setView(tank, icon - dpToPx(35), icon - dpToPx(35));

        //to set the layout
        plantName.setText(loadData("Plant"));
        setLayout(phPic, "PH", R.drawable.phc, ph, R.drawable.phg, 1);

        //startListening
        FirebaseDatabase.getInstance().getReference().child("plants").child(loadData("Plant")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                systemPlant[0].setVal(snapshot.child("RealTimeVal/pH").getValue(), snapshot.child("IdealValues/PH").getValue());
                systemPlant[1].setVal(snapshot.child("RealTimeVal/LUX").getValue(), snapshot.child("IdealValues/light").getValue());
                systemPlant[2].setVal(snapshot.child("RealTimeVal/temperature").getValue(), snapshot.child("IdealValues/temperature").getValue());
                systemPlant[3].setVal(snapshot.child("RealTimeVal/Humidity").getValue(), "100");
                systemPlant[4].setVal(snapshot.child("RealTimeVal/temperature2").getValue(), snapshot.child("IdealValues/temperature").getValue());
                systemPlant[5].setVal(snapshot.child("RealTimeVal/Humidity2").getValue(), "100");
                systemPlant[6].setVal(snapshot.child("RealTimeVal/moisture1").getValue(), snapshot.child("IdealValues/moisture").getValue());
                systemPlant[7].setVal(snapshot.child("RealTimeVal/moisture2").getValue(), snapshot.child("IdealValues/moisture").getValue());
                systemPlant[8].setVal(snapshot.child("RealTimeVal/Tank 1").getValue(), snapshot.child("IdealValues/tank water level").getValue());
                systemPlant[9].setVal(snapshot.child("RealTimeVal/Tank 2").getValue(), "5");

                setValues();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setView(View view, int WidthPx, int HeightPx) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (WidthPx > 0) {
            params.height = WidthPx;
        }
        if (HeightPx > 0) {
            params.width = HeightPx;
        }
        view.setLayoutParams(params);
    }

    public boolean isFound(String p, String hph) {
        return hph.indexOf(p) != -1 ? true : false;
    }

    public void setLayout(ImageView clickedImage, String systemName, int drawable, ImageView clickedImage2, int drawable2, int index) {

        progressBar.setProgress(0);
        progressBarTop.setProgress(0);
        progress=0;
        prog=0;

        progressBar2.setProgress(0);
        progressBarTop2.setProgress(0);
        progress2=0;
        prog2=0;

        progressBar3.setProgress(0);
        progressBarTop3.setProgress(0);
        progress3=0;
        prog3=0;

        progressBar4.setProgress(0);
        progressBarTop4.setProgress(0);
        progress4=0;
        prog4=0;

        logo.setImageResource(drawable);
        if (index==3) {
            logo2.setImageResource(R.drawable.humidity);
            logo4.setImageResource(R.drawable.humidity);
        } else {
            logo2.setImageResource(drawable);
            logo4.setImageResource(drawable);
        }
        logo3.setImageResource(drawable);

        phPic.setImageResource(R.drawable.button_off);
        lightPic.setImageResource(R.drawable.button_off);
        temperaturePic.setImageResource(R.drawable.button_off);
        moisturePic.setImageResource(R.drawable.button_off);
        waterTankPic.setImageResource(R.drawable.button_off);

        ph.setImageResource(R.drawable.phc);
        lightBulb.setImageResource(R.drawable.lightbulbc);
        temperature.setImageResource(R.drawable.temperaturec);
        water.setImageResource(R.drawable.waterc);
        tank.setImageResource(R.drawable.water_tankc);

        clickedImage2.setImageResource(drawable2);
        clickedImage.setImageResource(R.drawable.button_on);

        SystemUsed = systemName;
        int icon = ((ScreenWidthPx - dpToPx(40)) / 20);
        int framSize = (int) (((double) ScreenWidthPx) / 2.6);
        int textSize = (int) (((double) ScreenWidthPx) / 120);


        if (index == 1) {
            icon *= 2;
            framSize *= 2;
            textSize *= 2;
            rel1.setVisibility(View.VISIBLE);
            setView(systemContainer, framSize, framSize);
            SystemNameStatus.setTextSize(textSize * 2);
            setView(StatusImage, icon, icon);
            CurrValue.setTextSize(textSize);
            setView(logo, icon * 2, icon * 2);
            rel2.setVisibility(View.GONE);
            rel3.setVisibility(View.GONE);
            rel4.setVisibility(View.GONE);
        } else if (index == 2) {
            icon *= 2;
            framSize *= 2;
            textSize *= 2;
            rel1.setVisibility(View.VISIBLE);
            setView(systemContainer, framSize, framSize);
            SystemNameStatus.setTextSize(textSize * 2);
            setView(StatusImage, icon, icon);
            CurrValue.setTextSize(textSize);
            setView(logo, icon * 2, icon * 2);
            rel2.setVisibility(View.GONE);
            rel3.setVisibility(View.GONE);
            rel4.setVisibility(View.GONE);
        } else if (index == 3) {
            rel1.setVisibility(View.VISIBLE);
            rel2.setVisibility(View.VISIBLE);
            rel3.setVisibility(View.VISIBLE);
            rel4.setVisibility(View.VISIBLE);

            setView(systemContainer, framSize, framSize);
            SystemNameStatus.setTextSize(textSize * 2);
            setView(StatusImage, icon, icon);
            CurrValue.setTextSize(textSize);
            setView(logo, icon * 2, icon * 2);

            setView(systemContainer2, framSize, framSize);
            SystemNameStatus2.setTextSize(textSize * 2);
            setView(StatusImage2, icon, icon);
            CurrValue2.setTextSize(textSize);
            setView(logo2, icon * 2, icon * 2);

            setView(systemContainer3, framSize, framSize);
            SystemNameStatus3.setTextSize(textSize * 2);
            setView(StatusImage3, icon, icon);
            CurrValue3.setTextSize(textSize);
            setView(logo3, icon * 2, icon * 2);

            setView(systemContainer4, framSize, framSize);
            SystemNameStatus4.setTextSize(textSize * 2);
            setView(StatusImage4, icon, icon);
            CurrValue4.setTextSize(textSize);
            setView(logo4, icon * 2, icon * 2);


        } else if (index == 4) {
            rel1.setVisibility(View.VISIBLE);
            rel2.setVisibility(View.VISIBLE);
            rel3.setVisibility(View.GONE);
            rel4.setVisibility(View.GONE);

            setView(systemContainer, framSize, framSize);
            SystemNameStatus.setTextSize(textSize * 2);
            setView(StatusImage, icon, icon);
            CurrValue.setTextSize(textSize);
            setView(logo, icon * 2, icon * 2);

            setView(systemContainer2, framSize, framSize);
            SystemNameStatus2.setTextSize(textSize * 2);
            setView(StatusImage2, icon, icon);
            CurrValue2.setTextSize(textSize);
            setView(logo2, icon * 2, icon * 2);

        } else if (index == 5) {
            rel1.setVisibility(View.VISIBLE);
            rel2.setVisibility(View.VISIBLE);
            rel3.setVisibility(View.GONE);
            rel4.setVisibility(View.GONE);

            setView(systemContainer, framSize, framSize);
            SystemNameStatus.setTextSize(textSize * 2);
            setView(StatusImage, icon, icon);
            CurrValue.setTextSize(textSize);
            setView(logo, icon * 2, icon * 2);

            setView(systemContainer2, framSize, framSize);
            SystemNameStatus2.setTextSize(textSize * 2);
            setView(StatusImage2, icon, icon);
            CurrValue2.setTextSize(textSize);
            setView(logo2, icon * 2, icon * 2);
        }


        setValues();
    }

    public void setValues() {
        int i;
        if (SystemUsed.compareTo("PH") == 0) {
            i = 0;
            SystemNameStatus.setText(systemPlant[i].getSystemName());
            value.setText(systemPlant[i].getIdealValue());
            CurrValue.setText(systemPlant[i].getOutPut());
            progress = systemPlant[i].getProgress();
            if (systemPlant[i].isOnline()) {
                StatusImage.setImageResource(R.drawable.on);
                SystemNameStatus.setTextColor(Color.parseColor("#068958"));
            } else {
                StatusImage.setImageResource(R.drawable.off);
                SystemNameStatus.setTextColor(Color.parseColor("#ff320a"));
            }
        } else if (SystemUsed.compareTo("light") == 0) {
            i = 1;
            SystemNameStatus.setText(systemPlant[i].getSystemName());
            value.setText(systemPlant[i].getIdealValue());
            CurrValue.setText(systemPlant[i].getOutPut());
            progress = systemPlant[i].getProgress();
            if (systemPlant[i].isOnline()) {
                StatusImage.setImageResource(R.drawable.on);
                SystemNameStatus.setTextColor(Color.parseColor("#068958"));
            } else {
                StatusImage.setImageResource(R.drawable.off);
                SystemNameStatus.setTextColor(Color.parseColor("#ff320a"));
            }
        } else if (SystemUsed.compareTo("moisture") == 0) {
            i = 6;
            SystemNameStatus.setText(systemPlant[i].getSystemName());
            value.setText(systemPlant[i].getIdealValue());
            CurrValue.setText(systemPlant[i].getOutPut());
            progress = systemPlant[i].getProgress();
            if (systemPlant[i].isOnline()) {
                StatusImage.setImageResource(R.drawable.on);
                SystemNameStatus.setTextColor(Color.parseColor("#068958"));
            } else {
                StatusImage.setImageResource(R.drawable.off);
                SystemNameStatus.setTextColor(Color.parseColor("#ff320a"));
            }
            i = 7;
            SystemNameStatus2.setText(systemPlant[i].getSystemName());
            value2.setText(systemPlant[i].getIdealValue());
            CurrValue2.setText(systemPlant[i].getOutPut());
            progress2 = systemPlant[i].getProgress();
            if (systemPlant[i].isOnline()) {
                StatusImage2.setImageResource(R.drawable.on);
                SystemNameStatus2.setTextColor(Color.parseColor("#068958"));
            } else {
                StatusImage2.setImageResource(R.drawable.off);
                SystemNameStatus2.setTextColor(Color.parseColor("#ff320a"));
            }
        } else if (SystemUsed.compareTo("tank water level") == 0) {
            i = 8;
            SystemNameStatus.setText(systemPlant[i].getSystemName());
            value.setText(systemPlant[i].getIdealValue());
            CurrValue.setText(systemPlant[i].getOutPut());
            progress = systemPlant[i].getProgress();
            if (systemPlant[i].isOnline()) {
                StatusImage.setImageResource(R.drawable.on);
                SystemNameStatus.setTextColor(Color.parseColor("#068958"));
            } else {
                StatusImage.setImageResource(R.drawable.off);
                SystemNameStatus.setTextColor(Color.parseColor("#ff320a"));
            }
            i = 9;
            SystemNameStatus2.setText(systemPlant[i].getSystemName());
            value2.setText(systemPlant[i].getIdealValue());
            CurrValue2.setText(systemPlant[i].getOutPut());
            progress2 = systemPlant[i].getProgress();
            if (systemPlant[i].isOnline()) {
                StatusImage2.setImageResource(R.drawable.on);
                SystemNameStatus2.setTextColor(Color.parseColor("#068958"));
            } else {
                StatusImage2.setImageResource(R.drawable.off);
                SystemNameStatus2.setTextColor(Color.parseColor("#ff320a"));
            }
        } else if (SystemUsed.compareTo("temperature") == 0) {
            i = 2;
            SystemNameStatus.setText(systemPlant[i].getSystemName());
            value.setText(systemPlant[i].getIdealValue());
            CurrValue.setText(systemPlant[i].getOutPut());
            progress = systemPlant[i].getProgress();
            if (systemPlant[i].isOnline()) {
                StatusImage.setImageResource(R.drawable.on);
                SystemNameStatus.setTextColor(Color.parseColor("#068958"));
            } else {
                StatusImage.setImageResource(R.drawable.off);
                SystemNameStatus.setTextColor(Color.parseColor("#ff320a"));
            }
            i = 3;
            SystemNameStatus2.setText(systemPlant[i].getSystemName());
            value2.setText(systemPlant[i].getIdealValue());
            CurrValue2.setText(systemPlant[i].getOutPut());
            progress2 = systemPlant[i].getProgress();
            if (systemPlant[i].isOnline()) {
                StatusImage2.setImageResource(R.drawable.on);
                SystemNameStatus2.setTextColor(Color.parseColor("#068958"));
            } else {
                StatusImage2.setImageResource(R.drawable.off);
                SystemNameStatus2.setTextColor(Color.parseColor("#ff320a"));
            }
            i = 4;
            SystemNameStatus3.setText(systemPlant[i].getSystemName());
            value3.setText(systemPlant[i].getIdealValue());
            CurrValue3.setText(systemPlant[i].getOutPut());
            progress3 = systemPlant[i].getProgress();
            if (systemPlant[i].isOnline()) {
                StatusImage3.setImageResource(R.drawable.on);
                SystemNameStatus3.setTextColor(Color.parseColor("#068958"));
            } else {
                StatusImage3.setImageResource(R.drawable.off);
                SystemNameStatus3.setTextColor(Color.parseColor("#ff320a"));
            }
            i = 5;
            SystemNameStatus4.setText(systemPlant[i].getSystemName());
            value4.setText(systemPlant[i].getIdealValue());
            CurrValue4.setText(systemPlant[i].getOutPut());
            progress4 = systemPlant[i].getProgress();
            if (systemPlant[i].isOnline()) {
                StatusImage4.setImageResource(R.drawable.on);
                SystemNameStatus4.setTextColor(Color.parseColor("#068958"));
            } else {
                StatusImage4.setImageResource(R.drawable.off);
                SystemNameStatus4.setTextColor(Color.parseColor("#ff320a"));
            }
        }
    }

    public String loadData(String name) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if (sharedPreferences == null) {
            return "";
        }
        return sharedPreferences.getString(name, "");
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public int dpToPx(int dip) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
        return (int) px;
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }
}
