package com.example.voterapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class adapter extends ArrayAdapter<Items> {
    Intent homeIntent;
    public adapter(@NonNull Context context, ArrayList<Items> itemArrayList) {
        super(context, 0, itemArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listedView = convertView;
        if (listedView == null) {
            listedView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_layout, parent, false);
        }
        Items item = getItem(position);
        TextView pollName = listedView.findViewById(R.id.listItemText);
        pollName.setText(item.getPollName());
        listedView.setOnClickListener((View v) -> {
            homeIntent = new Intent(v.getContext(), HomeActivity.class);
            homeIntent.putExtra("phone", item.getPhone());
            homeIntent.putExtra("poll", item.getPollName());
            v.getContext().startActivity(homeIntent);
        });
        return  listedView;
    }
}
