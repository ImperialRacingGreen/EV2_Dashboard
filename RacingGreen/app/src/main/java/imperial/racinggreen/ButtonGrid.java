package imperial.racinggreen;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import android.content.Intent;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Java class to act as View Controller for Dashboard utility button grid
 * Created by salmanarif on 30/06/15.
 */
public class ButtonGrid extends FrameLayout {

    String variables[] = {
            "RPM",
            "SPEED",
            "TORQUE",
            "AIR TEMP",
            "MC TEMP",
            "MOTOR TEMP",
            "MC VOLTAGE",
            "MC CURRENT",
            "MC POWER",
            "MC CORE STATUS",
            "MC ERROR",
            "MC MESSAGE COUNT",
            "RFE",
            "FRG",
            "MC GO",
            "BMS VOLTAGE",
            "BMS MIN VOLTAGE",
            "BMS MAX VOLTAGE",
            "BMS CURRENT",
            "BMS SOC",
            "BMS TEMP",
            "BMS MINTEMP",
            "BMS MAXTEMP",
            "BMS STATUS",
            "BMS STATE",
            "BMS CAPACITY",
            "CAR_STATE",
            "BATT FAULT",
            "ISO FAULT",
            "THROTTLE 1",
            "THROTTLE 2",
            "AVE THROTTLE",
            "BRAKE",
            "LV",
            "HV",
            "ERROR",
            "TSA",
            "RELAY",
            "HIGH CURRENT",
            "INSULATION PWM",
            "START SWITCH"
    };

    /**
     * Parent dialog used to display the button grid.
     * Required here so that it can be closed by buttons.
     */
    private Dialog parentDialog;

    /**
     * Creates new ButtonGrid view with the given context, attributes and style
     * @param context The context for the ButtonGrid
     */
    public ButtonGrid(Context context, Dialog parentDialog) {
        super(context);
        this.parentDialog = parentDialog;
        initView();
        initButtons();
    }

    /**
     * Initialises and adds the ButtonGrid layout to this view
     */
    private void initView() {
        View view = inflate(getContext(), R.layout.button_grid, null);
        addView(view);
    }

    /**
     * Initialises buttons in GridView
     * Add custom buttons for other actions here
     */
    private void initButtons() {
        Button btnSaveToCsv = (Button) findViewById(R.id.btnSaveToCsv);
        btnSaveToCsv.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                parentDialog.hide(); // add this to each button that you think should close the parent dialog
                // TODO add code to perform save to CSV operation
                final MainActivity mainActivity = (MainActivity) getContext();
                Toast.makeText(getContext(), "Saving... ", Toast.LENGTH_SHORT).show();
                mainActivity.exportCSV();
                Log.w("","buttonCSV");
                return true;
            }
        });
    }



}