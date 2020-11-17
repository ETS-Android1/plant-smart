package com.example.plantsmart;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class plantAdapter extends ArrayAdapter<plant> {

    private Context mContext;
    private int mResource;
    FragmentPickPlant fragmentPickPlant;
    DashBoardActivity dashBoardActivity = new DashBoardActivity();

    public plantAdapter setDashBoardActivity(DashBoardActivity dashBoardActivity) {
        this.dashBoardActivity = dashBoardActivity;
        return this;
    }

    public plantAdapter setFragmentPickPlant(FragmentPickPlant fragmentPickPlant) {
        this.fragmentPickPlant = fragmentPickPlant;
        return this;
    }

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        ImageView Pic, image;
        TextView FruitName;
        ImageButton deletePost, ecitPost;
    }

    /**
     * Default constructor for the PersonListAdapter
     * //     *
     * //     * @param context
     * //     * @param resource
     * //     * @param objects
     * //
     */
    public plantAdapter(Context context, int resource, ArrayList<plant> items) {
        super(context, resource, items);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Uri pic = getItem(position).getPic();
        String FruitName = getItem(position).getName();
        final plant plant = new plant(FruitName, pic);
        final View result;

        //ViewHolder object
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.FruitName = (TextView) convertView.findViewById(R.id.FruitName);
            holder.Pic = (ImageView) convertView.findViewById(R.id.FruitPic);
            holder.deletePost = convertView.findViewById(R.id.deletePost);
            holder.ecitPost = convertView.findViewById(R.id.ecitPost);
            holder.image = convertView.findViewById(R.id.image);

            result = convertView;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        if (holder.deletePost != null) {
            holder.deletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentPickPlant.deletePost(position);
                }
            });
        }
        if (holder.ecitPost != null) {
            holder.ecitPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        fragmentPickPlant.ecitPost(position);
                    } catch (Exception ignored) {

                    }
                }
            });
        }

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashBoardActivity.saveData(plant.getName(), "Plant");
                dashBoardActivity.Control(null);
                Toast.makeText(getContext(), plant.getName()+" picked",Toast.LENGTH_LONG).show();
//                dashBoard.startActivityForResult(new Intent(getContext(), plantActivity.class),17);
//                dashBoard.finish();
//                CustomIntent.customType(getContext(), "left-to-right");
            }
        });

        if (plant.getPic() != null) {
            holder.Pic.setImageURI(plant.getPic());
        }
        holder.FruitName.setText(plant.getName());

        return convertView;
    }
}
