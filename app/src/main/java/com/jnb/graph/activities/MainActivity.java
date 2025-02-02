package com.jnb.graph.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jnb.graph.R;
import com.jnb.graph.common.MBox;
import com.jnb.graph.helpers.PreferenceHelper;
import com.jnb.graph.models.GraphSheet;
import com.jnb.graph.models.ReadingSpecialDetails;

public class MainActivity extends AppCompatActivity {
    EditText txtXSheetBoxes, txtYSheetBoxes, txtXMin, txtXMax, txtYMin, txtYMax;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        txtXSheetBoxes = findViewById(R.id.x_axis_boxes);
        txtYSheetBoxes = findViewById(R.id.y_axis_boxes);
        txtXMin = findViewById(R.id.x_min);
        txtXMax = findViewById(R.id.x_max);
        txtYMin = findViewById(R.id.y_min);
        txtYMax = findViewById(R.id.y_max);

        btnNext = findViewById(R.id.next);

        btnNext.setOnClickListener(view -> next());

        loadData();
    }

    private void next() {
        String xSheetBoxes = txtXSheetBoxes.getText().toString();
        String ySheetBoxes = txtYSheetBoxes.getText().toString();
        String xMin = txtXMin.getText().toString();
        String xMax = txtXMax.getText().toString();
        String yMin = txtYMin.getText().toString();
        String yMax = txtYMax.getText().toString();

        if (xSheetBoxes.isEmpty() || ySheetBoxes.isEmpty() || xMin.isEmpty() || xMax.isEmpty() || yMin.isEmpty() || yMax.isEmpty()) {
            MBox.showDialog(this, "Invalid Input", "All details are needed, please fill all fields to continue.", MBox.WARNING);
            return;
        }

        GraphSheet graphSheet = new GraphSheet(
                Integer.parseInt(xSheetBoxes),
                Integer.parseInt(ySheetBoxes)
        );

        ReadingSpecialDetails readingSpecialDetails = new ReadingSpecialDetails(
                Float.parseFloat(xMin),
                Float.parseFloat(xMax),
                Float.parseFloat(yMin),
                Float.parseFloat(yMax)
        );

        if (readingSpecialDetails.xMax > 0 && readingSpecialDetails.xMin < 0) {
            MBox.showDialog(this, "Under Developing", "This app under development for do this dual range calculation", MBox.WARNING);
            return;
        }
        else if (readingSpecialDetails.xMax < 0 && readingSpecialDetails.xMin > 0) {
            MBox.showDialog(this, "Under Developing", "This app under development for do this dual range calculation", MBox.WARNING);
            return;
        }

        if (readingSpecialDetails.yMax > 0 && readingSpecialDetails.yMin < 0) {
            MBox.showDialog(this, "Under Developing", "This app under development for do this dual range calculation", MBox.WARNING);
            return;
        }
        else if (readingSpecialDetails.yMax < 0 && readingSpecialDetails.yMin > 0) {
            MBox.showDialog(this, "Under Developing", "This app under development for do this dual range calculation", MBox.WARNING);
            return;
        }

        if (Math.abs(readingSpecialDetails.xMin) > Math.abs(readingSpecialDetails.xMax)) {
            MBox.showDialog(this, "Invalid Data", "X axis minimum value should be less than maximum value.", MBox.WARNING);
            return;
        }

        if (Math.abs(readingSpecialDetails.yMin) > Math.abs(readingSpecialDetails.yMax)) {
            MBox.showDialog(this, "Invalid Data", "Y axis minimum value should be less than maximum value.", MBox.WARNING);
            return;
        }

        PreferenceHelper.setGraphSheet(this, graphSheet);
        PreferenceHelper.setReadingSpecialDetails(this, readingSpecialDetails);

        Intent intent = new Intent(MainActivity.this, ReadingsActivity.class);
        startActivity(intent);
    }

    private void loadData() {
        GraphSheet graphSheet = PreferenceHelper.getGraphSheet(this);
        ReadingSpecialDetails readingSpecialDetails = PreferenceHelper.getReadingSpecialDetails(this);

        txtXSheetBoxes.setText(String.valueOf(graphSheet.xBoxes));
        txtYSheetBoxes.setText(String.valueOf(graphSheet.yBoxes));

        txtXMin.setText(String.valueOf(readingSpecialDetails.xMin));
        txtXMax.setText(String.valueOf(readingSpecialDetails.xMax));
        txtYMin.setText(String.valueOf(readingSpecialDetails.yMin));
        txtYMax.setText(String.valueOf(readingSpecialDetails.yMax));
    }
}