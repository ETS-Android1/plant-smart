package com.example.plantsmart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentAdjust extends Fragment {
    String plant;
    TextView CurrPH, CurrLight, CurrTemp, CurrMois, Currheight;
    TextView CurrPHMin, CurrLightMin, CurrTempMin, CurrMoisMin, CurrheightMin, CurrPHMax, CurrLightMax, CurrTempMax, CurrMoisMax, CurrheightMax;
    SeekBar ph, light, temp, mois, height;
    int phProgress = 0, lightProgress = 0, tempProgress = 0, moisProgress = 0, heightProgress = 0, CurrPHMinI = 0, CurrLightMinI = 0, CurrTempMinI = 0, CurrMoisMinI = 0, CurrheightMinI = 0, CurrPHMaxI = 0, CurrLightMaxI = 0, CurrTempMaxI = 0, CurrMoisMaxI = 0, CurrheightMaxI = 0;

    public FragmentAdjust(String plant) {
        this.plant = plant;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_adjust_three_shot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getView() != null) {
            initFunc(getView());

            FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("IdealValues").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("PHRange").getValue() != null) {
                        CurrPHMax.setText("Max:   " + dataSnapshot.child("PHRange").child("max").getValue().toString());
                        CurrPHMin.setText("Min:   " + dataSnapshot.child("PHRange").child("min").getValue().toString());
                        CurrPHMaxI = getInt(dataSnapshot.child("PHRange").child("max").getValue().toString());
                        CurrPHMinI = getInt(dataSnapshot.child("PHRange").child("min").getValue().toString());
                        if (dataSnapshot.child("PH").getValue() != null) {
                            int phD = (int) (((getDouble(dataSnapshot.child("PH").getValue().toString()) - ((double) CurrPHMinI)) / ((double) (CurrPHMaxI - CurrPHMinI))) * 100.0);
                            if (100 >= phD && phD > 0)
                                ph.setProgress(phD);
                        }
                    }
                    if (dataSnapshot.child("lightRange").getValue() != null) {
                        CurrLightMax.setText("Max:   " + dataSnapshot.child("lightRange").child("max").getValue().toString() + " DLI");
                        CurrLightMin.setText("Min:   " + dataSnapshot.child("lightRange").child("min").getValue().toString() + " DLI");
                        CurrLightMaxI = getInt(dataSnapshot.child("lightRange").child("max").getValue().toString());
                        CurrLightMinI = getInt(dataSnapshot.child("lightRange").child("min").getValue().toString());
                        if (dataSnapshot.child("light").getValue() != null) {
                            int phD = (int) (((getDouble(dataSnapshot.child("light").getValue().toString()) - ((double) CurrLightMinI)) / ((double) (CurrLightMaxI - CurrLightMinI))) * 100.0);
                            if (100 >= phD && phD > 0)
                                light.setProgress(phD);
                        }
                    }
                    if (dataSnapshot.child("temperatureRange").getValue() != null && dataSnapshot.child("temperature").getValue() != null) {
                        CurrTempMax.setText("Max:   " + dataSnapshot.child("temperatureRange").child("max").getValue().toString() + " °C");
                        CurrTempMin.setText("Min:   " + dataSnapshot.child("temperatureRange").child("min").getValue().toString() + " °C");
                        CurrTempMaxI = getInt(dataSnapshot.child("temperatureRange").child("max").getValue().toString());
                        CurrTempMinI = getInt(dataSnapshot.child("temperatureRange").child("min").getValue().toString());
                        if (dataSnapshot.child("temperature").getValue() != null) {
                            int phD = (int) (((getDouble(dataSnapshot.child("temperature").getValue().toString()) - ((double) CurrTempMinI)) / ((double) (CurrTempMaxI - CurrTempMinI))) * 100.0);
                            if (100 >= phD && phD > 0)
                                temp.setProgress(phD);
                        }
                    }
                    if (dataSnapshot.child("moistureRange").getValue() != null) {
                        CurrMoisMax.setText("Max:   " + dataSnapshot.child("moistureRange").child("max").getValue().toString() + " %");
                        CurrMoisMin.setText("Min:   " + dataSnapshot.child("moistureRange").child("min").getValue().toString() + " %");
                        CurrMoisMaxI = getInt(dataSnapshot.child("moistureRange").child("max").getValue().toString());
                        CurrMoisMinI = getInt(dataSnapshot.child("moistureRange").child("min").getValue().toString());
                        if (dataSnapshot.child("moisture").getValue() != null) {
                            int phD = (int) (((getDouble(dataSnapshot.child("moisture").getValue().toString()) - ((double) CurrMoisMinI)) / ((double) (CurrMoisMaxI - CurrMoisMinI))) * 100.0);
                            if (100 >= phD && phD > 0)
                                mois.setProgress(phD);
                        }
                    }

                    if (dataSnapshot.child("tankRange").getValue() != null) {
                        CurrheightMax.setText("Max:   " + dataSnapshot.child("tankRange").child("max").getValue().toString() + " cm");
                        CurrheightMin.setText("Min:   " + dataSnapshot.child("tankRange").child("min").getValue().toString() + " cm");
                        CurrheightMaxI = getInt(dataSnapshot.child("tankRange").child("max").getValue().toString());
                        CurrheightMinI = getInt(dataSnapshot.child("tankRange").child("min").getValue().toString());
                        if (dataSnapshot.child("tank water level").getValue() != null) {
                            int phD = (int) (((getDouble(dataSnapshot.child("tank water level").getValue().toString()) - ((double) CurrheightMinI)) / ((double) (CurrheightMaxI - CurrheightMinI))) * 100.0);
                            if (100 >= phD && phD > 0)
                                height.setProgress(phD);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public int getInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception ig) {
            return 0;
        }
    }

    public double getDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception ig) {
            return 0;
        }
    }

    private void initFunc(View v) {
        CurrPH = v.findViewById(R.id.CurrPH);
        CurrLight = v.findViewById(R.id.CurrLight);
        CurrTemp = v.findViewById(R.id.CurrTemp);
        CurrMois = v.findViewById(R.id.CurrMois);
        Currheight = v.findViewById(R.id.CurrHeight);
        CurrPHMin = v.findViewById(R.id.phMin);
        CurrLightMin = v.findViewById(R.id.LightMin);
        CurrTempMin = v.findViewById(R.id.tempMin);
        CurrMoisMin = v.findViewById(R.id.MoisMin);
        CurrheightMin = v.findViewById(R.id.HeightMin);
        CurrPHMax = v.findViewById(R.id.phMax);
        CurrLightMax = v.findViewById(R.id.LightMax);
        CurrTempMax = v.findViewById(R.id.tempMax);
        CurrMoisMax = v.findViewById(R.id.MoisMax);
        CurrheightMax = v.findViewById(R.id.HeightMax);
        ph = v.findViewById(R.id.seekBarPH);
        light = v.findViewById(R.id.seekBarLight);
        temp = v.findViewById(R.id.seekBarTemp);
        mois = v.findViewById(R.id.seekBarMois);
        height = v.findViewById(R.id.seekBarHeight);
        ph.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                phProgress = progress;
                int diff = CurrPHMaxI - CurrPHMinI;
                double newThre =round( (((double) phProgress) / 100.00) * ((double) diff) + ((double) CurrPHMinI),2);
                if (diff > 0) {
                    CurrPH.setText("Threshohld :" + newThre);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int diff = CurrPHMaxI - CurrPHMinI;
                double newThre = round((((double) phProgress) / 100.00) * ((double) diff) + ((double) CurrPHMinI),2);
                if (diff > 0) {
                    FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("IdealValues").child("PH").setValue(newThre);
                    CurrPH.setText("Threshohld :" + newThre);
                }
            }
        });


        light.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lightProgress = progress;
                int diff = CurrLightMaxI - CurrLightMinI;
                double newThre = round((((double) lightProgress) / 100.00) * ((double) diff) + ((double) CurrLightMinI),2);
                if (diff > 0) {
                    CurrLight.setText("Threshohld :" + newThre + " DLI");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int diff = CurrLightMaxI - CurrLightMinI;
                double newThre =round( (((double) lightProgress) / 100.00) * ((double) diff) + ((double) CurrLightMinI),2);
                if (diff > 0) {
                    FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("IdealValues").child("light").setValue(newThre);
                    CurrLight.setText("Threshohld :" + newThre + " DLI");
                }
            }
        });
        temp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tempProgress = progress;
                int diff = CurrTempMaxI - CurrTempMinI;
                double newThre =round( (((double) tempProgress) / 100.00) * ((double) diff) + ((double) CurrTempMinI),2);
                if (diff > 0) {
                    CurrTemp.setText("Threshohld :" + newThre + " °C");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int diff = CurrTempMaxI - CurrTempMinI;
                double newThre = round((((double) tempProgress) / 100.00) * ((double) diff) + ((double) CurrTempMinI),2);
                if (diff > 0) {
                    FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("IdealValues").child("temperature").setValue(newThre);
                    FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("IdealValues").child("temperatureBackup").setValue(newThre);
                    CurrTemp.setText("Threshohld :" + newThre + " °C");
                }
            }
        });

        mois.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                moisProgress = progress;
                int diff = CurrMoisMaxI - CurrMoisMinI;
                double newThre =round( (((double) moisProgress) / 100.00) * ((double) diff) + ((double) CurrMoisMinI),2);
                if (diff > 0) {
                    CurrMois.setText("Threshohld :" + newThre + " %");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int diff = CurrMoisMaxI - CurrMoisMinI;
                double newThre = round((((double) moisProgress) / 100.00) * ((double) diff) + ((double) CurrMoisMinI),2);
                if (diff > 0) {
                    FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("IdealValues").child("moisture").setValue(newThre);
                    CurrMois.setText("Threshohld :" + newThre + " %");
                }
            }
        });

        height.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                heightProgress = progress;
                int diff = CurrheightMaxI - CurrheightMinI;
                double newThre = round((((double) heightProgress) / 100.00) * ((double) diff) + ((double) CurrheightMinI),2);
                if (diff > 0) {
                    Currheight.setText("Threshohld :" + newThre + " cm");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int diff = CurrheightMaxI - CurrheightMinI;
                double newThre = round((((double) heightProgress) / 100.00) * ((double) diff) + ((double) CurrheightMinI),2);
                if (diff > 0) {
                    FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("IdealValues").child("tank water level").setValue(newThre);
                    Currheight.setText("Threshohld :" + newThre + " cm");
                }
            }
        });

    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}