package com.example.plantsmart;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.IOException;

public class FragmentHome extends Fragment {
    private String plant = "";

    FragmentHome(String plant) {
        this.plant = plant;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dashboard_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getView() != null) {
            TextView plantName = getView().findViewById(R.id.plantName);
            ImageView plantImage = getView().findViewById(R.id.plantImage);


            if (!plant.isEmpty()) {
                plantName.setText("(" + plant + ")");
                plantName.setTextColor(Color.parseColor("#08F100"));
                getPicForEdit(plantImage);
            }
        }
    }

    public void getPicForEdit(final ImageView plantImage) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("Fruits").child(plant.toLowerCase() + ".png");
        try {
            final File localFile = File.createTempFile("images", "png");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    try {
                        plantImage.setImageURI(Uri.fromFile(localFile));
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
}