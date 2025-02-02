package com.jnb.graph.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.jnb.graph.R;
import com.jnb.graph.common.MBox;
import com.jnb.graph.helpers.PreferenceHelper;
import com.jnb.graph.models.GraphSheet;
import com.jnb.graph.models.ReadingSpecialDetails;

public class GraphActivity extends AppCompatActivity {

    TextView lblXUV, lblYUV, lblXValue, lblYValue;
    EditText txtXBoxes, txtYBoxes;
    CombinedChart chart;
    Button btnFind;
    ImageButton btnBack;

    GraphSheet graphSheet;
    ReadingSpecialDetails readingSpecialDetails;

    float xUV, yUV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        init();
    }

    private void init() {
        lblXUV = findViewById(R.id.x_uv);
        lblYUV = findViewById(R.id.y_uv);
        lblXValue = findViewById(R.id.x_value);
        lblYValue = findViewById(R.id.y_value);
        txtXBoxes = findViewById(R.id.x_boxes);
        txtYBoxes = findViewById(R.id.y_boxes);
        chart = findViewById(R.id.chart);
        btnFind = findViewById(R.id.find);
        btnBack = findViewById(R.id.back);

        graphSheet = PreferenceHelper.getGraphSheet(this);
        readingSpecialDetails = PreferenceHelper.getReadingSpecialDetails(this);

        setUnitValues(graphSheet, readingSpecialDetails);

        btnFind.setOnClickListener(view -> find());
        btnBack.setOnClickListener(view -> finish());
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

    private void find() {
        String xBoxesS = txtXBoxes.getText().toString();
        String yBoxesS = txtYBoxes.getText().toString();

        if (xBoxesS.isEmpty() || yBoxesS.isEmpty()) {
            MBox.showDialog(this, "Invalid Value", "Please enter valid values and try again", MBox.WARNING);
            return;
        }

        int xBoxes = Integer.parseInt(xBoxesS);
        int yBoxes = Integer.parseInt(yBoxesS);

        float xVal = (xBoxes * xUV) + Math.abs(readingSpecialDetails.xMin);
        float yVal = (yBoxes * yUV) + Math.abs(readingSpecialDetails.yMin);

        if (readingSpecialDetails.xMin < 0) {
            xVal = -xVal;
        }

        if (readingSpecialDetails.yMin < 0) {
            yVal = -yVal;
        }

        xVal = Math.round(xVal * 10000f) / 10000f;
        yVal = Math.round(yVal * 10000f) / 10000f;

        lblXValue.setText(String.valueOf(xVal));
        lblYValue.setText(String.valueOf(yVal));
    }
}