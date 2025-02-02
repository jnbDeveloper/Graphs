package com.jnb.graph.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jnb.graph.R;
import com.jnb.graph.adapters.ReadingAdapter;
import com.jnb.graph.common.MBox;
import com.jnb.graph.common.RemoveClickCallback;
import com.jnb.graph.helpers.DBHelper;
import com.jnb.graph.helpers.PreferenceHelper;
import com.jnb.graph.models.GraphSheet;
import com.jnb.graph.models.OtherData;
import com.jnb.graph.models.Reading;
import com.jnb.graph.models.ReadingSpecialDetails;

import java.util.ArrayList;
import java.util.List;

public class ReadingsActivity extends AppCompatActivity implements RemoveClickCallback {
    GraphSheet graphSheet;
    ReadingSpecialDetails readingSpecialDetails;
    ReadingAdapter readingAdapter;
    DBHelper dbHelper;
    OtherData otherData;

    ImageButton btnBack;
    EditText txtXValue, txtYValue;
    Button btnAddReading, btnNext;
    TextView lblXUV, lblYUV, lblXBarValue, lblYBarValue, lblXBarBoxes, lblYBarBoxes;
    ListView lvReadings;

    float xUV = 0, yUV = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readings);

        init();
    }

    private void init() {
        btnBack = findViewById(R.id.back);
        lblXUV = findViewById(R.id.x_uv);
        lblYUV = findViewById(R.id.y_uv);
        txtXValue = findViewById(R.id.x_value);
        txtYValue = findViewById(R.id.y_value);
        btnAddReading = findViewById(R.id.add_reading);
        lvReadings = findViewById(R.id.readings);
        btnNext = findViewById(R.id.next);
        lblXBarValue = findViewById(R.id.x_bar_value);
        lblXBarBoxes = findViewById(R.id.x_bar_boxes);
        lblYBarValue = findViewById(R.id.y_bar_value);
        lblYBarBoxes = findViewById(R.id.y_bar_boxes);

        btnBack.setOnClickListener(view -> finish());
        btnAddReading.setOnClickListener(view -> addReading());
        btnNext.setOnClickListener(view -> next());

        dbHelper = new DBHelper(this);
        otherData = new OtherData();

        readingAdapter = new ReadingAdapter(this, this);
        lvReadings.setAdapter(readingAdapter);

        graphSheet = PreferenceHelper.getGraphSheet(this);
        readingSpecialDetails = PreferenceHelper.getReadingSpecialDetails(this);

        setUnitValues(graphSheet, readingSpecialDetails);

        loadData();
    }

    private void addReading() {
        String xValueS = txtXValue.getText().toString();
        String yValueS = txtYValue.getText().toString();

        if (xValueS.isEmpty() || yValueS.isEmpty()) {
            MBox.showDialog(this, "Invalid Input", "X value and Y value may not be empty.", MBox.WARNING);
            return;
        }

        float xValue = Float.parseFloat(xValueS);
        float yValue = Float.parseFloat(yValueS);

        if (xValue > 0) {
            if (xValue < readingSpecialDetails.xMin || xValue > readingSpecialDetails.xMax) {
                MBox.showDialog(this, "Invalid X Value", "You entered x value not in valid range.", MBox.WARNING);
                return;
            }
        }
        else if (xValue < 0) {
            if (Math.abs(xValue) < Math.abs(readingSpecialDetails.xMin) || Math.abs(xValue) > Math.abs(readingSpecialDetails.xMax)) {
                MBox.showDialog(this, "Invalid X Value", "You entered x value not in valid range.", MBox.WARNING);
                return;
            }
        }

        if (yValue > 0) {
            if (yValue < readingSpecialDetails.yMin || yValue > readingSpecialDetails.yMax) {
                MBox.showDialog(this, "Invalid Y Value", "You entered y value not in valid range.", MBox.WARNING);
                return;
            }
        }
        else if (yValue < 0) {
            if (Math.abs(yValue) < Math.abs(readingSpecialDetails.yMin) || Math.abs(yValue) > Math.abs(readingSpecialDetails.yMax)) {
                MBox.showDialog(this, "Invalid Y Value", "You entered y value not in valid range.", MBox.WARNING);
                return;
            }
        }

        Reading reading = new Reading(
                xValue,
                getBox(xValue, readingSpecialDetails.xMin, xUV),
                yValue,
                getBox(yValue, readingSpecialDetails.yMin, yUV)
        );

        txtXValue.setText("");
        txtYValue.setText("");
        txtXValue.requestFocus();

        readingAdapter.add(reading);
        readingAdapter.notifyDataSetChanged();

        updateXYBar();
    }

    private void next() {
        List<Reading> readings = new ArrayList<>();

        for (int i = 0; i < readingAdapter.getCount(); i++)
            readings.add(readingAdapter.getItem(i));

        if (readings.size() < 2) {
            MBox.showDialog(this, "Not Enough Data", "Minimum two readings wan't to countinue.", MBox.WARNING);
            return;
        }

        dbHelper.insertData(readings);
        PreferenceHelper.setOtherData(this, otherData);

        Intent intent = new Intent(ReadingsActivity.this, GraphActivity.class);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void setUnitValues(@NonNull GraphSheet gs, @NonNull ReadingSpecialDetails readingSpecialDetails) {
        xUV = (readingSpecialDetails.xMax - readingSpecialDetails.xMin) / (float) gs.xBoxes;
        xUV = Math.round(xUV * 10000f) / 10000f;
        xUV = Math.abs(xUV);
        yUV = (readingSpecialDetails.yMax - readingSpecialDetails.yMin) / (float) gs.yBoxes;
        yUV = Math.round(yUV * 10000f) / 10000f;
        yUV = Math.abs(yUV);

        lblXUV.setText("X Unit Value:\n" + String.valueOf(xUV));
        lblYUV.setText("Y Unit Value:\n" + String.valueOf(yUV));
    }

    private int getBox(float value, float minVal, float uv) {
        float xbox = (value - minVal) / uv;
        return Math.abs(Math.round(xbox));
    }

    private void updateXYBar() {
        int count = 0;
        float totalX = 0;
        float totalY = 0;

        if (readingAdapter.getCount() == 0) {
            lblXBarValue.setText("");
            lblXBarBoxes.setText("");
            lblYBarValue.setText("");
            lblYBarBoxes.setText("");
            return;
        }

        for (int i = 0; i < readingAdapter.getCount(); i++) {
            Reading r = readingAdapter.getItem(i);
            totalX += r.xValue;
            totalY += r.yValue;
            count++;
        }

        otherData.xBar = totalX / (float) count;
        otherData.yBar = totalY / (float) count;
        otherData.xBox = getBox(otherData.xBar, readingSpecialDetails.xMin, xUV);
        otherData.yBox = getBox(otherData.yBar, readingSpecialDetails.yMin, yUV);

        lblXBarValue.setText(String.valueOf(otherData.xBar));
        lblXBarBoxes.setText(String.valueOf(otherData.xBox));
        lblYBarValue.setText(String.valueOf(otherData.yBar));
        lblYBarBoxes.setText(String.valueOf(otherData.yBox));
    }

    @Override
    public void removeClicked(int itemIndex) {
        readingAdapter.remove(readingAdapter.getItem(itemIndex));
        updateXYBar();
    }

    private void loadData() {
        Cursor cursor = dbHelper.getReadings();


        if (cursor.getCount() > 0) {
            while(cursor.moveToNext()){
                Reading r = new Reading(
                        cursor.getFloat(0),
                        cursor.getInt(1),
                        cursor.getFloat(2),
                        cursor.getInt(3)
                );
                readingAdapter.add(r);
            }

            updateXYBar();
            readingAdapter.notifyDataSetChanged();
        }
    }
}