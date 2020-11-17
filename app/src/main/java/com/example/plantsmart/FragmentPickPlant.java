package com.example.plantsmart;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static com.example.plantsmart.DashBoardActivity.addPlant;

public class FragmentPickPlant extends Fragment {
    ListView fruit;
    EditText fruitSearch;
    EditText phMax, phMin, lightMax, lightMin, tempMax, tempMin, moisMax, moisMin, tankMax, tankMin,FruitName;
    ImageView plantEditPic;
    ArrayList<plant> plants = new ArrayList<>();
    plant choosenPlant;
    ArrayList<plant> currfruits = new ArrayList<>();
    DashBoardActivity dashBoardActivity;
    LinearLayout layout1, layout2;
    ScrollView editMode;
    FloatingActionButton floating;
    FragmentPickPlant fragmentPickPlant = this;
    Activity activity;
    boolean EditPlant = false;
    String name;

    public FragmentPickPlant setName(String name) {
        this.name = name;
        return this;
    }

    public FragmentPickPlant setEditPlant(boolean editPlant) {
        EditPlant = editPlant;
        return this;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public FragmentPickPlant setDashBoardActivity(DashBoardActivity dashBoardActivity) {
        this.dashBoardActivity = dashBoardActivity;
        return this;
    }

    public void deletePost(int pos) {
        FirebaseDatabase.getInstance().getReference().child("plants").child(currfruits.get(pos).getName()).child("IdealValues").removeValue();
        FirebaseStorage.getInstance().getReference("Fruits").child(currfruits.get(pos).getName().toLowerCase() + ".png").delete();
        Toast.makeText(getContext(), "Plant has been deleted successfully", LENGTH_LONG).show();
        getPlants();
    }

    public void ecitPost(final int pos) {
        dashBoardActivity.EditMode = true;
        EditModeFunc();
        choosenPlant = currfruits.get(pos);
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("plants").child(currfruits.get(pos).getName()).child("IdealValues");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FruitName.setText(currfruits.get(pos).getName());

                if (dataSnapshot.child("PHRange").getValue() != null) {
                    phMax.setText(dataSnapshot.child("PHRange").child("max").getValue().toString());
                    phMin.setText(dataSnapshot.child("PHRange").child("min").getValue().toString());
                }
                if (dataSnapshot.child("lightRange").getValue() != null) {
                    lightMax.setText(dataSnapshot.child("lightRange").child("max").getValue().toString());
                    lightMin.setText(dataSnapshot.child("lightRange").child("min").getValue().toString());
                }
                if (dataSnapshot.child("temperatureRange").getValue() != null) {
                    tempMax.setText(dataSnapshot.child("temperatureRange").child("max").getValue().toString());
                    tempMin.setText(dataSnapshot.child("temperatureRange").child("min").getValue().toString());
                }
                if (dataSnapshot.child("moistureRange").getValue() != null) {
                    moisMax.setText(dataSnapshot.child("moistureRange").child("max").getValue().toString());
                    moisMin.setText(dataSnapshot.child("moistureRange").child("min").getValue().toString());
                }

                if (dataSnapshot.child("tankRange").getValue() != null) {
                    tankMax.setText(dataSnapshot.child("tankRange").child("max").getValue().toString());
                    tankMin.setText(dataSnapshot.child("tankRange").child("min").getValue().toString());
                }

                getPicForEdit(currfruits.get(pos).getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void EditPlant() {
        dashBoardActivity.EditMode = true;
        EditModeFunc();
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("plants").child(name).child("IdealValues");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FruitName.setText(name);

                if (dataSnapshot.child("PHRange").getValue() != null) {
                    phMax.setText(dataSnapshot.child("PHRange").child("max").getValue().toString());
                    phMin.setText(dataSnapshot.child("PHRange").child("min").getValue().toString());
                }
                if (dataSnapshot.child("lightRange").getValue() != null) {
                    lightMax.setText(dataSnapshot.child("lightRange").child("max").getValue().toString());
                    lightMin.setText(dataSnapshot.child("lightRange").child("min").getValue().toString());
                }
                if (dataSnapshot.child("temperatureRange").getValue() != null) {
                    tempMax.setText(dataSnapshot.child("temperatureRange").child("max").getValue().toString());
                    tempMin.setText(dataSnapshot.child("temperatureRange").child("min").getValue().toString());
                }
                if (dataSnapshot.child("moistureRange").getValue() != null) {
                    moisMax.setText(dataSnapshot.child("moistureRange").child("max").getValue().toString());
                    moisMin.setText(dataSnapshot.child("moistureRange").child("min").getValue().toString());
                }

                if (dataSnapshot.child("tankRange").getValue() != null) {
                    tankMax.setText(dataSnapshot.child("tankRange").child("max").getValue().toString());
                    tankMin.setText(dataSnapshot.child("tankRange").child("min").getValue().toString());
                }

                getPicForEdit(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    Uri addPic;

    public void addPic(Uri Pic) {
        addPic = Pic;
        plantEditPic.setImageURI(Pic);
    }

    public void save() {
        if (TextUtils.isEmpty(FruitName.getText().toString())) {
            FruitName.setError("Fruit name is Required.");
            return;
        }

        if (TextUtils.isEmpty(phMax.getText().toString())) {
            phMax.setError("Ph is Required.");
            return;
        }
        if (TextUtils.isEmpty(phMin.getText().toString())) {
            phMin.setError("Ph is Required.");
            return;
        }

        if (TextUtils.isEmpty(lightMax.getText().toString())) {
            lightMax.setError("Light is Required.");
            return;
        }
        if (TextUtils.isEmpty(lightMin.getText().toString())) {
            lightMin.setError("Light is Required.");
            return;
        }

        if (TextUtils.isEmpty(tempMax.getText().toString())) {
            tempMax.setError("Temperature is Required.");
            return;
        }
        if (TextUtils.isEmpty(tempMin.getText().toString())) {
            tempMin.setError("Temperature is Required.");
            return;
        }

        if (TextUtils.isEmpty(moisMax.getText().toString())) {
            moisMax.setError("Moisture is Required.");
            return;
        }
        if (TextUtils.isEmpty(moisMin.getText().toString())) {
            moisMin.setError("Moisture is Required.");
            return;
        }

        if (TextUtils.isEmpty(tankMax.getText().toString())) {
            tankMax.setError("tank water level is Required.");
            return;
        }
        if (TextUtils.isEmpty(tankMin.getText().toString())) {
            tankMin.setError("tank water level is Required.");
            return;
        }

        boolean success = false;
        if (choosenPlant != null) {
            if (addPic != null) {
                FirebaseStorage.getInstance().getReference("Fruits").child(choosenPlant.getName().toLowerCase() + ".png").delete();
                StorageReference fileReference = FirebaseStorage.getInstance().getReference("Fruits").child(FruitName.getText().toString().toLowerCase() + ".png");
                fileReference.putFile(addPic)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(getContext(), "Error while uploading Fruit picture", LENGTH_SHORT).show();
                            }
                        });
            }
            FirebaseDatabase.getInstance().getReference().child("plants").child(choosenPlant.getName()).child("IdealValues").child("PHRange").removeValue();
            FirebaseDatabase.getInstance().getReference().child("plants").child(choosenPlant.getName()).child("IdealValues").child("lightRange").removeValue();
            FirebaseDatabase.getInstance().getReference().child("plants").child(choosenPlant.getName()).child("IdealValues").child("temperatureRange").removeValue();
            FirebaseDatabase.getInstance().getReference().child("plants").child(choosenPlant.getName()).child("IdealValues").child("moistureRange").removeValue();
            FirebaseDatabase.getInstance().getReference().child("plants").child(choosenPlant.getName()).child("IdealValues").child("tankRange").removeValue();

            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("PHRange").child("max").setValue(phMax.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("lightRange").child("max").setValue(lightMax.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("moistureRange").child("max").setValue(moisMax.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("temperatureRange").child("max").setValue(tempMax.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("tankRange").child("max").setValue(tankMax.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("PHRange").child("min").setValue(phMin.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("lightRange").child("min").setValue(lightMin.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("moistureRange").child("min").setValue(moisMin.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("temperatureRange").child("min").setValue(tempMin.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("tankRange").child("min").setValue(tankMin.getText().toString());


            success = true;
            Toast.makeText(getContext(), "Plant has been uploaded successfully", LENGTH_LONG).show();
        } else {
            if (addPic != null) {
                StorageReference fileReference = FirebaseStorage.getInstance().getReference("Fruits").child(FruitName.getText().toString().toLowerCase() + ".png");
                fileReference.putFile(addPic)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(getContext(), "Error while uploading Fruit picture", LENGTH_SHORT).show();
                            }
                        });
            } else {
                StorageReference fileReference = FirebaseStorage.getInstance().getReference("Fruits").child(FruitName.getText().toString().toLowerCase() + ".png");
                fileReference.putFile(Uri.parse("android.resource://com.example.plantsmart/drawable/plant2"))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(getContext(), "Error while uploading Fruit picture", LENGTH_SHORT).show();
                            }
                        });
            }
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("PHRange").child("max").setValue(phMax.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("lightRange").child("max").setValue(lightMax.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("moistureRange").child("max").setValue(moisMax.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("temperatureRange").child("max").setValue(tempMax.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("tankRange").child("max").setValue(tankMax.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("PHRange").child("min").setValue(phMin.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("lightRange").child("min").setValue(lightMin.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("moistureRange").child("min").setValue(moisMin.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("temperatureRange").child("min").setValue(tempMin.getText().toString());
            FirebaseDatabase.getInstance().getReference().child("plants").child(FruitName.getText().toString()).child("IdealValues").child("tankRange").child("min").setValue(tankMin.getText().toString());


            success = true;
            Toast.makeText(getContext(), "Plant has been uploaded successfully", LENGTH_LONG).show();
        }

        if (success) {
            dashBoardActivity.onBackPressed();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_select_plant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getView() != null) {
            fruit = getView().findViewById(R.id.Fruits);
            fruitSearch = getView().findViewById(R.id.fruitSearch);
            floating = getView().findViewById(R.id.edit);
            floating.setImageResource(R.drawable.ic_add_black_24dp);
            layout1 = getView().findViewById(R.id.select);
            layout2 = getView().findViewById(R.id.toolbar);
            editMode = getView().findViewById(R.id.editMode);
            FruitName = getView().findViewById(R.id.FruitName);

            phMax = getView().findViewById(R.id.maxPh);
            phMin = getView().findViewById(R.id.minPh);
            lightMax = getView().findViewById(R.id.maxLight);
            lightMin = getView().findViewById(R.id.minLight);
            tempMax = getView().findViewById(R.id.maxTemp);
            tempMin = getView().findViewById(R.id.minTemp);
            moisMax = getView().findViewById(R.id.maxMois);
            moisMin = getView().findViewById(R.id.minMois);
            tankMax = getView().findViewById(R.id.maxWaterL);
            tankMin = getView().findViewById(R.id.minWaterL);

            plantEditPic = getView().findViewById(R.id.plantEditPic);

            dashBoardActivity.setFloating(floating, layout1, layout2);
            getPlants();


            fruit.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    dashBoardActivity.hideKeyBoard(fruitSearch);
                    fruitSearch.clearFocus();
                    return false;
                }
            });

            fruitSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!fruitSearch.getText().toString().isEmpty()) {

                        ArrayList<plant> fruitsFound = new ArrayList<>();
                        for (int i = 0; i < plants.size(); i++) {
                            if (isFound(fruitSearch.getText().toString().trim().toLowerCase(), plants.get(i).getName().toLowerCase())) {
                                fruitsFound.add(plants.get(i));
                            }
                        }
                        currfruits.clear();
                        currfruits.addAll(fruitsFound);
                        plantAdapter plantAdapter = new plantAdapter(getContext(), R.layout.fruit, fruitsFound);
                        plantAdapter.setFragmentPickPlant(fragmentPickPlant);
                        plantAdapter.setDashBoardActivity(dashBoardActivity);
                        fruit.setAdapter(plantAdapter);
                    } else {
                        getPlants();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            if (EditPlant) {
                EditPlant();
            }

            if (addPlant) {
                addPlant = false;
                EditModeFunc();
            }
        }
    }

    public void EditModeFunc() {
        plantEditPic.setImageURI(Uri.parse("android.resource://com.example.plantsmart/drawable/plant2"));
        FruitName.setText("");
        phMax.setText("");
        lightMax.setText("");
        tempMax.setText("");
        moisMax.setText("");
        tankMax.setText("");
        phMin.setText("");
        lightMin.setText("");
        tempMin.setText("");
        moisMin.setText("");
        tankMin.setText("");

        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.VISIBLE);
        editMode.setVisibility(View.VISIBLE);
        fruit.setVisibility(View.GONE);
        floating.setVisibility(View.GONE);
    }

    public void SearchModeFunc() {
        choosenPlant = null;
        addPic = null;
        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.GONE);
        editMode.setVisibility(View.GONE);
        fruit.setVisibility(View.VISIBLE);
        floating.setVisibility(View.VISIBLE);
    }

    public void getPlants() {
//        if (fruits.size() == 0) {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("plants");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                plants.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    plants.add(new plant(ds.getKey(), getPic(ds.getKey(), plants.size())));
                }
                currfruits.clear();
                currfruits.addAll(plants);
                plantAdapter plantAdapter = new plantAdapter(getContext(), R.layout.fruit, plants);
                plantAdapter.setFragmentPickPlant(fragmentPickPlant);
                plantAdapter.setDashBoardActivity(dashBoardActivity);
                fruit.setAdapter(plantAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        } else {
//            Currfruits.clear();
//            Currfruits.addAll(fruits);
//            fruitAdapter fruitAdapter = new fruitAdapter(getContext(), R.layout.fruit, fruits);
//            fruitAdapter.setFragmentPickPlant(fragmentPickPlant);
//            fruit.setAdapter(fruitAdapter);
//        }
//        fruits.add(new fruit("Lettuce", Uri.parse("android.resource://com.example.plantsmart/drawable/salad")));
//        fruits.add(new fruit("Apple", Uri.parse("android.resource://com.example.plantsmart/drawable/apple")));
//        fruits.add(new fruit("Marijuana", Uri.parse("android.resource://com.example.plantsmart/drawable/marijuana")));
//        fruits.add(new fruit("Mango", Uri.parse("android.resource://com.example.plantsmart/drawable/mango")));
    }

    Uri FruitPic;

    public Uri getPic(final String plantName, final int index) {
        FruitPic = null;
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("Fruits").child(plantName.toLowerCase() + ".png");
        try {
            final File localFile = File.createTempFile("images", "png");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    FruitPic = Uri.fromFile(localFile);
                    plants.set(index, new plant(plantName, FruitPic));
                    try {
                        currfruits.clear();
                        currfruits.addAll(plants);
                        plantAdapter plantAdapter = new plantAdapter(getContext(), R.layout.fruit, plants);
                        plantAdapter.setFragmentPickPlant(fragmentPickPlant);
                        plantAdapter.setDashBoardActivity(dashBoardActivity);
                        fruit.setAdapter(plantAdapter);
                    } catch (Exception ignored) {

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });

            return FruitPic;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getPicForEdit(final String plantName) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("Fruits").child(plantName.toLowerCase() + ".png");
        try {
            final File localFile = File.createTempFile("images", "png");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    try {
                        plantEditPic.setImageURI(Uri.fromFile(localFile));
                        addPic = Uri.fromFile(localFile);
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

    public boolean isFound(String p, String hph) {
        boolean Found = hph.indexOf(p) != -1 ? true : false;
        return Found;
    }
}
