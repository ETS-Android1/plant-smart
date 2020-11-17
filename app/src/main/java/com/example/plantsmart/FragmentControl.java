package com.example.plantsmart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentControl extends Fragment {
    String plant;
    ImageView StatusFan, StatusLed, StatusPH1, StatusLDR, StatusDht, StatusDht2, StatusMois, StatusMois2, StatusUltra, StatusUltra2, rainTank;
    Switch SwitchFan, SwitchLed, rainTankP;
    TextView OutPH1, OutLdr, OutTemp1, OutHum1, OutTemp2, OutHum2, OutMois, OutMois2, OutUltra1, OutUltra2;
    boolean BoolPH1 = false, BoolLDR = false, BoolDht = false, BoolDht2 = false, BoolMois = false, BoolMois2 = false, BoolUltra = false, BoolUltra2 = false;

    public FragmentControl(String plant) {
        this.plant = plant;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_control_on_off, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getView() != null) {
            initFunc(getView());
            startListining();
        }
    }

    private void initFunc(View v) {
        rainTankP = v.findViewById(R.id.rainTankP);
        rainTank = v.findViewById(R.id.rainTank);
        StatusFan = v.findViewById(R.id.StatusFan);
        StatusLed = v.findViewById(R.id.StatusLed);
        StatusPH1 = v.findViewById(R.id.StatusPH1);
        StatusLDR = v.findViewById(R.id.StatusLDR);
        StatusDht = v.findViewById(R.id.StatusDht);
        StatusDht2 = v.findViewById(R.id.StatusDht2);
        StatusMois = v.findViewById(R.id.StatusMois);
        StatusMois2 = v.findViewById(R.id.StatusMois2);
        StatusUltra = v.findViewById(R.id.StatusUltra);
        StatusUltra2 = v.findViewById(R.id.StatusUltra2);
        SwitchFan = v.findViewById(R.id.SwitchFan);
        SwitchLed = v.findViewById(R.id.SwitchLed);
        OutPH1 = v.findViewById(R.id.OutPH1);
        OutLdr = v.findViewById(R.id.OutLdr);
        OutTemp1 = v.findViewById(R.id.OutTemp1);
        OutHum1 = v.findViewById(R.id.OutHum1);
        OutTemp2 = v.findViewById(R.id.OutTemp2);
        OutHum2 = v.findViewById(R.id.OutHum2);
        OutMois = v.findViewById(R.id.OutMois);
        OutMois2 = v.findViewById(R.id.OutMois2);
        OutUltra1 = v.findViewById(R.id.OutUltra1);
        OutUltra2 = v.findViewById(R.id.OutUltra2);
        SwitchFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("FanManual").setValue(true);
                    }else {
                    FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("FanManual").setValue(false);
                }
            }
        });
        rainTankP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("TankManual").setValue(true);
                    rainTank.setImageResource(R.drawable.on);
                }else {
                    FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("TankManual").setValue(false);
                    rainTank.setImageResource(R.drawable.off);
                }
            }
        });
        SwitchLed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("lightManual").setValue(true);
                }else {
                    FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("lightManual").setValue(false);
                }
            }
        });
    }

    private void startListining() {
        FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("FanManual").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (snapshot.getValue().toString().compareTo("true") == 0) {
                        SwitchFan.setChecked(true);
                    } else if (snapshot.getValue().toString().compareTo("false") == 0) {
                        SwitchFan.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("TankManual").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (snapshot.getValue().toString().compareTo("true") == 0) {
                        rainTankP.setChecked(true);
                        rainTank.setImageResource(R.drawable.on);
                    } else if (snapshot.getValue().toString().compareTo("false") == 0) {
                        rainTankP.setChecked(false);
                        rainTank.setImageResource(R.drawable.off);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("lightManual").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (snapshot.getValue().toString().compareTo("true") == 0) {
                        SwitchLed.setChecked(true);
                    } else if (snapshot.getValue().toString().compareTo("false") == 0) {
                        SwitchLed.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("fan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (snapshot.getValue().toString().compareTo("1") == 0) {
                        StatusFan.setImageResource(R.drawable.on);
                    } else if (snapshot.getValue().toString().compareTo("0") == 0) {
                        StatusFan.setImageResource(R.drawable.off);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("light").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (snapshot.getValue().toString().compareTo("1") == 0) {
                        StatusLed.setImageResource(R.drawable.on);
                    } else if (snapshot.getValue().toString().compareTo("0") == 0) {
                        StatusLed.setImageResource(R.drawable.off);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("pH").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (BoolPH1) {
                        StatusPH1.setImageResource(R.drawable.on);
                    }
                    OutPH1.setText("Output: " + snapshot.getValue().toString());
                    BoolPH1 = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("LUX").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (BoolLDR) {
                        StatusLDR.setImageResource(R.drawable.on);
                    }
                    OutLdr.setText("Output: " + snapshot.getValue().toString() + " LUX");
                    BoolLDR = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("temperature").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (BoolDht) {
                        StatusDht.setImageResource(R.drawable.on);
                    }
                    OutTemp1.setText("Temp: " + snapshot.getValue().toString() + " °C");
                    BoolDht = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("Humidity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (BoolDht) {
                        StatusDht.setImageResource(R.drawable.on);
                    }
                    OutHum1.setText("Humidity: " + snapshot.getValue().toString() + " %");
                    BoolDht = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("LUX2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (BoolDht2) {
                        StatusDht2.setImageResource(R.drawable.on);
                    }
                    BoolDht2 = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("moisture1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (BoolMois) {
                        StatusMois.setImageResource(R.drawable.on);
                    }
                    OutMois.setText("Output: " + snapshot.getValue().toString() + " %");
                    BoolMois = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("moisture2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (BoolMois2) {
                        StatusMois2.setImageResource(R.drawable.on);
                    }
                    OutMois2.setText("Output: " + snapshot.getValue().toString() + " %");
                    BoolMois2 = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("Tank 1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (BoolUltra) {
                        StatusUltra.setImageResource(R.drawable.on);
                    }
                    OutUltra1.setText("Height: " + snapshot.getValue().toString() + " cm");
                    BoolUltra = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        FirebaseDatabase.getInstance().getReference().child("plants").child(plant).child("RealTimeVal").child("Tank 2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    if (BoolUltra2) {
                        StatusUltra2.setImageResource(R.drawable.on);
                    }
                    OutUltra2.setText("Height: " + snapshot.getValue().toString() + " cm");
                    BoolUltra2 = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }


}