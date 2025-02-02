package com.jnb.graph.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jnb.graph.R;
import com.jnb.graph.common.RemoveClickCallback;
import com.jnb.graph.models.Reading;

public class ReadingAdapter extends ArrayAdapter<Reading> {
    private final RemoveClickCallback removeClickCallback;

    public ReadingAdapter(@NonNull Context context, RemoveClickCallback removeClickCallback) {
        super(context, R.layout.reading);
        this.removeClickCallback = removeClickCallback;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.reading, parent, false);
        Reading r = getItem(position);

        if (r != null) {
            TextView xValue = convertView.findViewById(R.id.x_value);
            TextView xBoxes= convertView.findViewById(R.id.x_boxes);
            TextView yValue = convertView.findViewById(R.id.y_value);
            TextView yBoxes = convertView.findViewById(R.id.y_boxes);
            ImageButton remove = convertView.findViewById(R.id.remove);

            xValue.setText(String.valueOf(r.xValue));
            xBoxes.setText(String.valueOf(r.xBoxes));
            yValue.setText(String.valueOf(r.yValue));
            yBoxes.setText(String.valueOf(r.yBoxes));

            remove.setOnClickListener(view -> {
                removeClickCallback.removeClicked(getPosition(r));
            });
        }

        return convertView;
    }
}
