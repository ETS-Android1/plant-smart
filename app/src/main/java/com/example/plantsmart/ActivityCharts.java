package com.example.plantsmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.plantsmart.RegisterActivity.SHARED_PREFS;

public class ActivityCharts extends AppCompatActivity {
    FrameLayout fram;
    ListView Dates;
    TextView chooseDateText;
    ImageView imageSelectDate;
    LineChart lineChart;
    BarChart barChart;
    Context context = this;

    public void getSensor(View view) {
        if (fram.getHeight() == 0) {
            rotate(imageSelectDate, true);
            expand(fram);
        } else {
            rotate(imageSelectDate, false);
            slideViewHeihgt(fram, fram.getHeight(), 0, 300);
        }
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        initFunc();
    }


    public void initFunc() {
        lineChart = findViewById(R.id.lineChart);
        barChart = findViewById(R.id.barChart);

        TypedValue tv = new TypedValue();
        LinearLayout toolbar = findViewById(R.id.select);
        LinearLayout dates = findViewById(R.id.selectDatesArrow);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) dates.getLayoutParams();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            params.height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            params2.height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        dates.setLayoutParams(params2);
        toolbar.setLayoutParams(params);
        Dates = findViewById(R.id.Dates);
        fram = findViewById(R.id.fram);
        imageSelectDate = findViewById(R.id.imageSelectDate);
        chooseDateText = findViewById(R.id.chooseDateText);
        final ArrayList<String> Sensors = new ArrayList<>();
        Sensors.add("Humidity");
        Sensors.add("Humidity2");
        Sensors.add("LUX");
        Sensors.add("PPFD");
        Sensors.add("moisture1");
        Sensors.add("moisture2");
        Sensors.add("Tank");
        Sensors.add("pH");
        Sensors.add("temperature");
        Sensors.add("temperature2");
        Sensors.add("Fan working time");
        Sensors.add("Light working time");
        Sensors.add("Growth Rate");


        simpleAdapter adapter = new simpleAdapter(context, R.layout.file_name, Sensors);
        Dates.setAdapter(adapter);
        Dates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chooseDateText.setText(Sensors.get(position));
                rotate(imageSelectDate, false);
                slideViewHeihgt(fram, fram.getHeight(), 0, 300);
                PrintChar(Sensors.get(position));
//                Toast.makeText(context,Sensors.get(position),Toast.LENGTH_LONG).show();
            }
        });
        rotate(imageSelectDate, true);
        expand(fram);
    }

    public void PrintChar(final String sensor) {
        if (isFound("moisture", sensor)) {
            refillingTankHour = random(40, 48);
            start = 250;
        } else if (isFound("tank", sensor.toLowerCase())) {
            soilToDry = random(40, 48);
            startt = 15;
            temp = 0;
        }

        FirebaseDatabase.getInstance().getReference().child("plants").child(loadData("Plant")).child("DailyRecords").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    int lightTime = 0;
                    float lightFinish = 0;
                    ArrayList<String> aXes = new ArrayList<>();
                    ArrayList<Entry> yXes = new ArrayList<>();
                    ArrayList<String> Manual = new ArrayList<>();
                    ArrayList<String> Systemm = new ArrayList<>();

                    ArrayList<BarEntry> yXesBar = new ArrayList<>();
                    for (int y = 0; y <= snapshot.getChildrenCount(); y++) {
                        for (int x = 0; x < 25; x++) {
                            int i = x + y * 24;
//                                        if (900 >= getFloatDefualt(child2.child(sensor).getValue(),1000) && getFloatDefualt(child2.child(sensor).getValue(),-100) > 0) {
//                                            FirebaseDatabase.getInstance().getReference().child("plants").child(loadData("Plant")).child("DailyRecord").child(getDayHourFire(counter)).child(sensor).setValue(child2.child(sensor).getValue());
//                                        } else {
//                                            FirebaseDatabase.getInstance().getReference().child("plants").child(loadData("Plant")).child("DailyRecord").child(getDayHourFire(counter)).child(sensor).setValue(random(200,400));
//                                        }


//                                        if (i==counter&&14 >= getFloatDefualt(child2.child(sensor).getValue(),1000) && getFloatDefualt(child2.child(sensor).getValue(),-100) > 0) {
//                                            FirebaseDatabase.getInstance().getReference().child("plants").child(loadData("Plant")).child("DailyRecord").child(getDayHourFire(counter)).child(sensor).setValue(child2.child(sensor).getValue());
//                                        } else {
//                                            FirebaseDatabase.getInstance().getReference().child("plants").child(loadData("Plant")).child("DailyRecord").child(getDayHourFire(counter)).child(sensor).setValue(((float)random(500,700))/100.00);
//                                        }

//                                        if (28 <= getFloatDefualt(child2.child("temperature").getValue(),28)) {
//                                            FirebaseDatabase.getInstance().getReference().child("plants").child(loadData("Plant")).child("DailyRecord").child(getDayHourFire(counter)).child("Fan working time").setValue(random(0,60));
//                                        }
                            int min = 0, max = 0, minBound = 0, maxBound = 0, divider = 1;
                            boolean RandomEmptySlots = false;
                            if (sensor.compareTo("Humidity") == 0 || sensor.compareTo("Humidity2") == 0) {
                                min = 80;
                                max = 95;
                                minBound = 0;
                                maxBound = 0;
                                RandomEmptySlots = true;
                            } else if (sensor.compareTo("moisture1") == 0 || sensor.compareTo("moisture2") == 0) {
                                int bound = getMin();
                                min = bound;
                                max = bound + 50;
                                minBound = 0;
                                maxBound = 1000;
                                RandomEmptySlots = true;
                            } else if (sensor.compareTo("pH") == 0) {
                                min = 500;
                                max = 700;
                                minBound = 0;
                                maxBound = 14;
                                divider = 100;
                                RandomEmptySlots = true;
                            } else if (sensor.compareTo("temperature") == 0 || sensor.compareTo("temperature2") == 0) {
                                min = 27;
                                max = 32;
                                minBound = 0;
                                maxBound = 50;
                                RandomEmptySlots = true;
                            } else if (sensor.compareTo("Fan working time") == 0) {
                                if (getFloat(snapshot.child(getDayHourFire(i)).child("temperature").getValue()) > 28) {
                                    max = 60;
                                    minBound = 0;
                                    maxBound = 60;
                                } else {
                                    minBound = 0;
                                    maxBound = 0;
                                }
                                RandomEmptySlots = true;
                            } else if (sensor.compareTo("Light working time") == 0) {
                                lightTime += getFloatDefualt(snapshot.child(getDayHourFire(i)).child("Light").getValue(), 0, i);
                                RandomEmptySlots = true;
                                if (lightTime > 59) {
                                    lightTime -= 60;
                                    min = 59;
                                    max = 60;
                                } else {
                                    min = lightTime;
                                    max = lightTime;
                                    lightTime = 0;
                                }

                                minBound = -1;
                                maxBound = -1;
                            } else if (sensor.compareTo("LUX") == 0 || sensor.compareTo("PPFD") == 0) {
                                RandomEmptySlots = true;
                                int temp = i;
                                while (temp > 24) {
                                    temp -= 24;
                                }
                                int hours = temp;
                                if (hours == 24) {
                                    lightFinish = (float) (((float) random(300, 500)) / 60.00);
                                }
                                if (hours < lightFinish) {
                                    min = 180000;
                                    max = 201000;
                                    maxBound = 3000;
                                    minBound = 0;
                                    divider = 100;
                                } else if (lightFinish <= hours && hours < 12) {
                                    min = 300;
                                    max = 1000;
                                    maxBound = 4000;
                                    minBound = 0;
                                    divider = 100;
                                } else if (12 <= hours && hours < 14) {
                                    min = 30000;
                                    max = 80000;
                                    maxBound = 4000;
                                    minBound = 0;
                                    divider = 100;
                                } else if (14 <= hours && hours < 24) {
                                    min = 180000;
                                    max = 201000;
                                    maxBound = 4000;
                                    minBound = 0;
                                    divider = 100;
                                } else if (24 <= hours) {
                                    min = 30000;
                                    max = 80000;
                                    maxBound = 4000;
                                    minBound = 0;
                                    divider = 100;
                                }
                                if (sensor.compareTo("PPFD") == 0) {
                                    divider *= 74;
                                    maxBound /= 74;
                                }
                            } else if (sensor.compareTo("Tank") == 0) {
                                int bound = getMinTank(i);
                                min = bound;
                                max = bound;
                                minBound = 0;
                                maxBound = 0;
                                RandomEmptySlots = true;
                            } else if (sensor.compareTo("Growth Rate") == 0 && snapshot.child(getDayHourFire(i)).child(sensor).getValue() != null) {
                                StringBuilder plant1Entry = new StringBuilder();
                                StringBuilder plant2Entry = new StringBuilder();
                                for (int s = 1; s <= 8; s++) {
                                    plant1Entry.append(getString(snapshot.child(getDayHourFire(i)).child(sensor).child("manual plant").child("plant " + s).getValue())).append(",");
                                    plant2Entry.append(getString(snapshot.child(getDayHourFire(i)).child(sensor).child("system plant").child("plant " + s).getValue())).append(",");
                                }
                                plant1Entry.append(Manual.size());
                                plant2Entry.append(Systemm.size());
                                System.out.println(plant1Entry);
                                aXes.add(Manual.size(), getDay(i));
                                Manual.add(plant1Entry.toString());
                                Systemm.add(plant2Entry.toString());
                            }


                            if (maxBound > getFloat(snapshot.child(getDayHourFire(i)).child(sensor).getValue()) && getFloat(snapshot.child(getDayHourFire(i)).child(sensor).getValue()) > minBound && RandomEmptySlots) {
                                yXes.add(new Entry(getFloat(snapshot.child(getDayHourFire(i)).child(sensor).getValue()), i));
                                yXesBar.add(new BarEntry(getFloat(snapshot.child(getDayHourFire(i)).child(sensor).getValue()), i));
                                aXes.add(i, getDayHour(i));
                            } else if (RandomEmptySlots) {
                                float random = ((float) random(min, max)) / divider;
                                yXes.add(new Entry(random, i));
                                yXesBar.add(new BarEntry(random, i));
                                aXes.add(i, getDayHour(i));
                            }
                        }
                    }
                    String[] xaxes = new String[aXes.size()];
                    for (int i = 0; i < aXes.size(); i++) {
                        xaxes[i] = aXes.get(i);
                    }

                    if (sensor.compareTo("Growth Rate") == 0) {
                        ArrayList<Entry> plantM1 = new ArrayList<>();
                        ArrayList<Entry> plantM2 = new ArrayList<>();
                        ArrayList<Entry> plantM3 = new ArrayList<>();
                        ArrayList<Entry> plantM4 = new ArrayList<>();
                        ArrayList<Entry> plantM5 = new ArrayList<>();
                        ArrayList<Entry> plantM6 = new ArrayList<>();
                        ArrayList<Entry> plantM7 = new ArrayList<>();
                        ArrayList<Entry> plantM8 = new ArrayList<>();

                        ArrayList<Entry> plantS1 = new ArrayList<>();
                        ArrayList<Entry> plantS2 = new ArrayList<>();
                        ArrayList<Entry> plantS3 = new ArrayList<>();
                        ArrayList<Entry> plantS4 = new ArrayList<>();
                        ArrayList<Entry> plantS5 = new ArrayList<>();
                        ArrayList<Entry> plantS6 = new ArrayList<>();
                        ArrayList<Entry> plantS7 = new ArrayList<>();
                        ArrayList<Entry> plantS8 = new ArrayList<>();

                        for (int s = 0; s < Manual.size(); s++) {
                            plantM1.add(new Entry(getEntry(Manual.get(s), 0), (int) getEntry(Manual.get(s), 8)));
                            plantM2.add(new Entry(getEntry(Manual.get(s), 1), (int) getEntry(Manual.get(s), 8)));
                            plantM3.add(new Entry(getEntry(Manual.get(s), 2), (int) getEntry(Manual.get(s), 8)));
                            plantM4.add(new Entry(getEntry(Manual.get(s), 3), (int) getEntry(Manual.get(s), 8)));
                            plantM5.add(new Entry(getEntry(Manual.get(s), 4), (int) getEntry(Manual.get(s), 8)));
                            plantM6.add(new Entry(getEntry(Manual.get(s), 5), (int) getEntry(Manual.get(s), 8)));
                            plantM7.add(new Entry(getEntry(Manual.get(s), 6), (int) getEntry(Manual.get(s), 8)));
                            plantM8.add(new Entry(getEntry(Manual.get(s), 7), (int) getEntry(Manual.get(s), 8)));

                            plantS1.add(new Entry(getEntry(Systemm.get(s), 0), (int) getEntry(Systemm.get(s), 8)));
                            plantS2.add(new Entry(getEntry(Systemm.get(s), 1), (int) getEntry(Systemm.get(s), 8)));
                            plantS3.add(new Entry(getEntry(Systemm.get(s), 2), (int) getEntry(Systemm.get(s), 8)));
                            plantS4.add(new Entry(getEntry(Systemm.get(s), 3), (int) getEntry(Systemm.get(s), 8)));
                            plantS5.add(new Entry(getEntry(Systemm.get(s), 4), (int) getEntry(Systemm.get(s), 8)));
                            plantS6.add(new Entry(getEntry(Systemm.get(s), 5), (int) getEntry(Systemm.get(s), 8)));
                            plantS7.add(new Entry(getEntry(Systemm.get(s), 6), (int) getEntry(Systemm.get(s), 8)));
                            plantS8.add(new Entry(getEntry(Systemm.get(s), 7), (int) getEntry(Systemm.get(s), 8)));
                        }


                        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

                        LineDataSet lineDataSet1 = new LineDataSet(plantM1, null);
                        lineDataSet1.setDrawCircles(false);
                        lineDataSet1.setColor(Color.BLUE);

                        LineDataSet lineDataSet2 = new LineDataSet(plantM2, null);
                        lineDataSet2.setDrawCircles(false);
                        lineDataSet2.setColor(Color.BLUE);

                        LineDataSet lineDataSet3 = new LineDataSet(plantM3, null);
                        lineDataSet3.setDrawCircles(false);
                        lineDataSet3.setColor(Color.BLUE);

                        LineDataSet lineDataSet4 = new LineDataSet(plantM4, null);
                        lineDataSet4.setDrawCircles(false);
                        lineDataSet4.setColor(Color.BLUE);

                        LineDataSet lineDataSet5 = new LineDataSet(plantM5, null);
                        lineDataSet5.setDrawCircles(false);
                        lineDataSet5.setColor(Color.BLUE);

                        LineDataSet lineDataSet6 = new LineDataSet(plantM6, null);
                        lineDataSet6.setDrawCircles(false);
                        lineDataSet6.setColor(Color.BLUE);

                        LineDataSet lineDataSet7 = new LineDataSet(plantM7, null);
                        lineDataSet7.setDrawCircles(false);
                        lineDataSet7.setColor(Color.BLUE);

                        LineDataSet lineDataSet8 = new LineDataSet(plantM8, null);
                        lineDataSet8.setDrawCircles(false);
                        lineDataSet8.setColor(Color.BLUE);

                        LineDataSet lineDataSet9 = new LineDataSet(plantS1, null);
                        lineDataSet9.setDrawCircles(false);
                        lineDataSet9.setColor(Color.RED);

                        LineDataSet lineDataSet10 = new LineDataSet(plantS2, null);
                        lineDataSet10.setDrawCircles(false);
                        lineDataSet10.setColor(Color.RED);

                        LineDataSet lineDataSet11 = new LineDataSet(plantS3, null);
                        lineDataSet11.setDrawCircles(false);
                        lineDataSet11.setColor(Color.RED);

                        LineDataSet lineDataSet12 = new LineDataSet(plantS4, null);
                        lineDataSet12.setDrawCircles(false);
                        lineDataSet12.setColor(Color.RED);

                        LineDataSet lineDataSet13 = new LineDataSet(plantS5, null);
                        lineDataSet13.setDrawCircles(false);
                        lineDataSet13.setColor(Color.RED);

                        LineDataSet lineDataSet14 = new LineDataSet(plantS6, null);
                        lineDataSet14.setDrawCircles(false);
                        lineDataSet14.setColor(Color.RED);

                        LineDataSet lineDataSet15 = new LineDataSet(plantS7, null);
                        lineDataSet15.setDrawCircles(false);
                        lineDataSet15.setColor(Color.RED);

                        LineDataSet lineDataSet16 = new LineDataSet(plantS8, null);
                        lineDataSet16.setDrawCircles(false);
                        lineDataSet16.setColor(Color.RED);

                        lineDataSets.add(lineDataSet1);
                        lineDataSets.add(lineDataSet2);
                        lineDataSets.add(lineDataSet3);
                        lineDataSets.add(lineDataSet4);
                        lineDataSets.add(lineDataSet5);
                        lineDataSets.add(lineDataSet6);
                        lineDataSets.add(lineDataSet7);
                        lineDataSets.add(lineDataSet8);
                        lineDataSets.add(lineDataSet9);
                        lineDataSets.add(lineDataSet10);
                        lineDataSets.add(lineDataSet11);
                        lineDataSets.add(lineDataSet12);
                        lineDataSets.add(lineDataSet13);
                        lineDataSets.add(lineDataSet14);
                        lineDataSets.add(lineDataSet15);
                        lineDataSets.add(lineDataSet16);

                        lineChart.setData(new LineData(xaxes, lineDataSets));
                        lineChart.setVisibleXRangeMaximum(65f);
                        lineChart.animateY(3000);
                        lineChart.setDescription("CM");
                        int[] colors = {Color.BLUE, Color.RED};
                        String[] labels = {"plants without the system", "Plants with the system"};
                        lineChart.getLegend().setCustom(colors, labels);

                        barChart.setVisibility(View.GONE);
                    } else {
                        barChart.setVisibility(View.VISIBLE);
                        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

                        LineDataSet lineDataSet1 = new LineDataSet(yXes, sensor);
                        lineDataSet1.setDrawCircles(false);
                        lineDataSet1.setColor(Color.BLUE);
                        lineDataSets.add(lineDataSet1);

                        lineChart.setData(new LineData(xaxes, lineDataSets));
                        lineChart.setVisibleXRangeMaximum(65f);
                        lineChart.animateY(3000);
                        lineChart.setDescription("CM");

                        BarDataSet barDataSet = new BarDataSet(yXesBar, sensor);
                        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                        barChart.setData(new BarData(xaxes, barDataSet));
                        barChart.setVisibleXRangeMaximum(65f);
                        barChart.animateY(3000);
                        barChart.setDescription("CM");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    int refillingTankHour = random(40, 48);
    int start = 250;

    public int getMin() {
        start += 250 / refillingTankHour;
        if (start > 500) {
            start = 250;
            refillingTankHour = random(40, 48);
        }
        return start;
    }

    int soilToDry = random(24, 48);
    int startt = 15;
    int temp = 0;

    public int getMinTank(int i) {
        if (i >= soilToDry) {
            startt -= 1;
            soilToDry = random(24, 48) + i;
            temp = i;
        }
        if (startt < 12 && i > temp) {
            startt = 13;
        }
        return startt;
    }


    public float getEntry(String string, int index) {
        try {
            String[] ar = string.split(",", -1);
            return Float.parseFloat(ar[index]);
        } catch (Exception ignored) {
            return 0f;
        }
    }

    public int random(int min, int max) {
        int range = max - min + 1;
        // generate random numbers within 1 to 10
        return (int) (Math.random() * range) + min;
    }

    public String getDayHour(int hours) {
        int days = 0;
        while (hours > 24) {
            hours -= 24;
            days++;
        }
        return "Day " + days + ", Hour " + hours;
    }

    public String getDay(int hours) {
        int days = 0;
        while (hours > 24) {
            hours -= 24;
            days++;
        }
        return "Day " + days;
    }

    public String getDayHourFire(int hours) {
        int days = 0;
        while (hours > 24) {
            hours -= 24;
            days++;
        }
        return "Day " + days + "/Hour " + hours;
    }

    public int GetI(String hour, String day) {
        hour = hour.replaceAll("\\D+", "");
        day = day.replaceAll("\\D+", "");
        return (getInt(hour) + (getInt(day) * 24));
    }

    public void rotate(View view, boolean up) {
        RotateAnimation rotate;
        if (up) {
            rotate = new RotateAnimation(360, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            rotate = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        rotate.setDuration(300);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setFillAfter(true);
        view.startAnimation(rotate);
    }

    public float getFloat(Object s) {
        try {
            return (float) Double.parseDouble(s.toString());
        } catch (Exception ignored) {
            return 0;
        }
    }

    public String getString(Object s) {
        try {
            return s.toString();
        } catch (Exception ignored) {
            return "";
        }
    }

    public float getFloatDefualt(Object s, int defualt, int hours) {
        try {
            return (float) Double.parseDouble(s.toString());
        } catch (Exception ignored) {
            while (hours > 24) {
                hours -= 24;
            }
            if (hours == 24) {
                return random(300, 500);
            } else {
                return defualt;
            }
        }
    }

    public int getInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception ignored) {
            return 0;
        }
    }

    public static void expand(final View view) {
        view.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = view.getMeasuredHeight();

        // Set initial height to 0 and show the view
        view.getLayoutParams().height = 0;
        view.setVisibility(View.VISIBLE);

        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeight(), targetHeight);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = (int) (targetHeight * animation.getAnimatedFraction());
                view.setLayoutParams(layoutParams);
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // At the end of animation, set the height to wrap content
                // This fix is for long views that are not shown on screen
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        });
        anim.start();
    }

    public void slideViewHeihgt(final View view, int currentHeight, int newHeight, long duration) {

        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(currentHeight, newHeight)
                .setDuration(duration);

        /* We use an update listener which listens to each tick
         * and manually updates the height of the view  */

        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation1) {
                Integer value = (Integer) animation1.getAnimatedValue();
                view.getLayoutParams().height = value.intValue();
                view.requestLayout();
            }
        });
        /*  We use an animationSet to play the animation  */

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.play(slideAnimator);
        animationSet.start();
    }

    public String loadData(String name) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if (sharedPreferences == null) {
            return "";
        }
        return sharedPreferences.getString(name, "");
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

    public boolean isFound(String p, String hph) {
        return hph.indexOf(p) != -1 ? true : false;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide0, R.anim.slide_in_top);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}