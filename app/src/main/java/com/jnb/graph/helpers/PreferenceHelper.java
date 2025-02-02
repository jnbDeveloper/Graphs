package com.jnb.graph.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import com.jnb.graph.models.GraphSheet;
import com.jnb.graph.models.OtherData;
import com.jnb.graph.models.ReadingSpecialDetails;

public class PreferenceHelper {
    public final static String APP_PREFERENCE_FILE_NAME = "graph_eusl";
    public static SharedPreferences sharedPreferences;

    public static void setGraphSheet(@NonNull Context context, @NonNull GraphSheet graphSheet) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(APP_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("graph_sheet_x_boxes", graphSheet.xBoxes);
        editor.putInt("graph_sheet_y_boxes", graphSheet.yBoxes);

        editor.apply();
    }

    @NonNull
    public static GraphSheet getGraphSheet(@NonNull Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(APP_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return new GraphSheet(
                sharedPreferences.getInt("graph_sheet_x_boxes", 0),
                sharedPreferences.getInt("graph_sheet_y_boxes", 0)
        );
    }

    public static void setReadingSpecialDetails(@NonNull Context context, @NonNull ReadingSpecialDetails readingSpecialDetails) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(APP_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putFloat("x_min", readingSpecialDetails.xMin);
        editor.putFloat("x_max", readingSpecialDetails.xMax);
        editor.putFloat("y_min", readingSpecialDetails.yMin);
        editor.putFloat("y_max", readingSpecialDetails.yMax);
        editor.apply();
    }

    @NonNull
    public static ReadingSpecialDetails getReadingSpecialDetails(@NonNull Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(APP_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return new ReadingSpecialDetails(sharedPreferences.getFloat("x_min", 0), sharedPreferences.getFloat("x_max", 0), sharedPreferences.getFloat("y_min", 0), sharedPreferences.getFloat("y_max", 0));
    }

    public static void setOtherData(@NonNull Context context, @NonNull OtherData otherData) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(APP_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putFloat("x_bar", otherData.xBar);
        editor.putFloat("x_box", otherData.xBox);
        editor.putFloat("y_bar", otherData.yBar);
        editor.putFloat("y_box", otherData.yBox);
        editor.apply();
    }

    public static OtherData getOtherData(@NonNull Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(APP_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return new OtherData(sharedPreferences.getFloat("x_bar", 0), sharedPreferences.getFloat("x_box", 0), sharedPreferences.getFloat("y_bar", 0), sharedPreferences.getFloat("y_box", 0));
    }
}
