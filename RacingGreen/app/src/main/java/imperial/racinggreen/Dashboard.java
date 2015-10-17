package imperial.racinggreen;


import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.text.DecimalFormat;

import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

/**
 * Created by salmanarif on 16/12/14.
 */
public class Dashboard extends View {

    // fields
    private Double speed = 0.0;
    private Double rpm = 0.0;
    private Double torque = 0.0;
    private Double battery = 0.0;
    private Double maxRpm = 0.0;
    private Bitmap battIcon;
    private final Double maxSpeed = 120.0;
    private final float needle = 432;
    private boolean error = false;
    private boolean batteryFault = false;
    private boolean imdFault = false;
    private Double packVolt = 0.0;
    private Double packVoltLimit = 280.0;
    private Double lowVolt = 0.0;
    private Double lowVoltLimit = 11.8;
    private Double maxTemp = 0.0;
    private Double maxTempLimit = 60.0;
    private boolean tsStatus = false;
    private String errorMessage = "";
    private CarState state = CarState.IDLE;

    private Double mcTemp = 0.0;
    private Double mcTempLimit = 60.0;
    private Double mcPower = 0.0;

    // constructor
    public Dashboard(Context context, Double maxRpm) {
        super(context);
        this.maxRpm = maxRpm;
        battIcon = BitmapFactory.decodeResource(getResources(), R.drawable.battery);

    }

    // override draw method
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();

        /*-------- draw dial --------*/
        // calculate degrees offset
        float degrees = (float) (rpm.doubleValue() * (180.0/maxRpm));
        float x_offset = needle*(float)cos(toRadians(degrees));
        float y_offset = needle*(float)sin(toRadians(degrees));
        // draw needle
        paint.setColor(Color.parseColor("#D75050"));
        paint.setStrokeWidth(8);
        canvas.drawLine(1920/2,567,1920/2-x_offset,567-y_offset,paint);

        // draw rpm
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(243);
        canvas.drawText(String.format("%.1f",rpm.intValue()*0.032)+"", 1920/2, 567, paint);
        // draw rpm label
        paint.setStrokeWidth(0);
        paint.setColor(Color.WHITE);
        paint.setTextSize(65);
        canvas.drawText("kph", 1920/2, 641, paint);
        // draw rpm value labels
        int rpmCounter = 0;
        for (int i=0; i<=(maxRpm/500.0); i++) {
            drawRpmLabel(canvas, paint, rpmCounter);
            rpmCounter += 500;
        }

        /*-------- draw boxes --------*/
        DecimalFormat dfOne = new DecimalFormat("0.0");
        DecimalFormat dfTwo = new DecimalFormat("0.00");


        // pack volt
        if (packVolt < packVoltLimit) paint.setColor(Color.parseColor("#D75050"));
        else paint.setColor(Color.parseColor("#88A2D3"));
        canvas.drawRoundRect(256, 692, 494, 844, 8, 8, paint);
        paint.setTextSize(40);
        canvas.drawText("PACK VOLT", 375, 898, paint);
        paint.setColor(Color.parseColor("#12161E"));
        paint.setTextSize(65);
        canvas.drawText(""+packVolt.intValue(), 375, 796, paint);

        // low volt
        if (lowVolt < lowVoltLimit) paint.setColor(Color.parseColor("#D75050"));
        else paint.setColor(Color.parseColor("#88A2D3"));
        canvas.drawRoundRect(552, 692, 788, 844, 8, 8, paint);
        paint.setTextSize(40);
        canvas.drawText("LOW VOLT", 670, 898, paint);
        paint.setColor(Color.parseColor("#12161E"));
        paint.setTextSize(65);
        canvas.drawText(dfTwo.format(lowVolt.floatValue()), 670, 796, paint);

        // max temp
        if (maxTemp > maxTempLimit) paint.setColor(Color.parseColor("#D75050"));
        else paint.setColor(Color.parseColor("#88A2D3"));
        canvas.drawRoundRect(840, 692, 1076, 844, 8, 8, paint);
        paint.setTextSize(40);
        canvas.drawText("BMS MAX TEMP", 958, 898, paint);
        paint.setColor(Color.parseColor("#12161E"));
        paint.setTextSize(65);
        canvas.drawText(dfOne.format(maxTemp.floatValue()), 958, 796, paint);

        // car state
//        paint.setColor(Color.parseColor("#88A2D3"));
//        canvas.drawRoundRect(1134, 692, 1370, 844, 8, 8, paint);
//        paint.setTextSize(40);
//        canvas.drawText("CAR STATE", 1252, 898, paint);
//        paint.setColor(Color.parseColor("#12161E"));
//        paint.setTextSize(65);
//        String stateString = "";
//        stateString = String.valueOf(state);
//        canvas.drawText(stateString, 1252, 796, paint);

        // MC POWER
        paint.setColor(Color.parseColor("#88A2D3"));
        canvas.drawRoundRect(1134, 692, 1370, 844, 8, 8, paint);
        paint.setTextSize(40);
        canvas.drawText("MC POWER", 1252, 898, paint);
        paint.setColor(Color.parseColor("#12161E"));
        paint.setTextSize(65);
        String mcPowerString = "";
        mcPowerString = String.format("%.2f",mcPower/1000.00);
        mcPowerString += "kW";
        canvas.drawText(mcPowerString, 1252, 796, paint);

//        // ts status
//        String tsStatusString = "";
//        if (!tsStatus) {
//            paint.setColor(Color.parseColor("#D75050"));
//            tsStatusString = "OFF";
//        }
//        else {
//            paint.setColor(Color.parseColor("#88A2D3"));
//            tsStatusString = "ON";
//        }
//        canvas.drawRoundRect(1426, 692, 1662, 844, 8, 8, paint);
//        paint.setTextSize(40);
//        canvas.drawText("TS STATUS", 1544, 898, paint);
//        paint.setColor(Color.parseColor("#12161E"));
//        paint.setTextSize(65);
//        canvas.drawText(tsStatusString, 1544, 796, paint);


        // low volt
        if (mcTemp < mcTempLimit) paint.setColor(Color.parseColor("#D75050"));
        else paint.setColor(Color.parseColor("#88A2D3"));
        canvas.drawRoundRect(1426, 692, 1662, 844, 8, 8, paint);
        paint.setTextSize(40);
        canvas.drawText("MC TEMP", 1544, 898, paint);
        paint.setColor(Color.parseColor("#12161E"));
        paint.setTextSize(65);
        canvas.drawText(dfTwo.format(mcTemp.floatValue()), 1544, 796, paint);
//
//        // MC TEMP
//        String mcTempString = "";
//        mcTempString = String.valueOf(mcTemp);
//        canvas.drawRoundRect(1426, 692, 1662, 844, 8, 8, paint);
//        paint.setTextSize(40);
//        canvas.drawText("MC TEMP", 1544, 898, paint);
//        paint.setColor(Color.parseColor("#12161E"));
//        paint.setTextSize(65);
//        canvas.drawText(mcTempString, 1544, 796, paint);

        // Draw Car State
        String stateString = "";
        stateString = String.valueOf(state);
        switch (stateString) {
            case "IDLE":
                paint.setColor(Color.parseColor("#ECF0F1"));
                break;
            case "DRIVE":
                paint.setColor(Color.parseColor("#27AE60"));
                break;
            case "ERROR":
                paint.setColor(Color.parseColor("#D75050"));
                break;

        }
        paint.setTextSize(65);
        canvas.drawText(stateString, 959, 1008, paint);

        // Draw Error at Corner
        String errorString = errorMessage;
        if (errorString == "OK") {
            paint.setColor(Color.parseColor("#ECF0F1"));
        }
        else {
            paint.setColor(Color.parseColor("#D75050"));
        }
        paint.setTextSize(65);
        canvas.drawText(errorString, 150, 1008, paint);

        if (error) {
            paint.setColor(Color.parseColor("#D75050"));
            canvas.drawRoundRect(766, 954, 1152, 1026, 8, 8, paint);
            paint.setColor(Color.parseColor("#12161E"));
            paint.setTextSize(40);
            canvas.drawText(errorMessage, 959, 1008, paint);
        }
        if (batteryFault) {
            paint.setColor(Color.parseColor("#D75050"));
            paint.setTextSize(65);
            canvas.drawText("BATTERY FAULT", 452, 1008, paint);
        }
        if (imdFault) {
            paint.setColor(Color.parseColor("#D75050"));
            paint.setTextSize(65);
            canvas.drawText("I.M.D. FAULT", 1420, 1008, paint);
        }
        // Draw Torque at Corner
        String torqueString = String.valueOf(torque) + "%";
        paint.setColor(Color.parseColor("#ECF0F1"));
        paint.setTextSize(100);
        canvas.drawText(torqueString, 150, 150, paint);
    }

    private void drawRpmLabel(Canvas canvas, Paint paint, int value) {
        // calculate degrees offset
        float degrees = (float) (value * (180.0/maxRpm));
        float x_offset = (needle+24)*(float)cos(toRadians(degrees));
        float y_offset = (needle+24)*(float)sin(toRadians(degrees));
        paint.setColor(Color.WHITE);
        paint.setTextSize(32);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(""+value,1920/2-x_offset,567-y_offset,paint);
    }

    // redraw method
    public void redraw() {
        this.invalidate();
    }

    // color interpolation method
    public int interpolateRgb(String start, String end, double fraction) {
        int startColor = (int)Long.parseLong(start, 16);
        int endColor = (int)Long.parseLong(end, 16);
        int r = (int) ((((endColor >> 16) & 0xFF) - ((startColor >> 16) & 0xFF)) * fraction) + ((startColor >> 16) & 0xFF);
        int g = (int) ((((endColor >> 8) & 0xFF) - ((startColor >> 8) & 0xFF)) * fraction) + ((startColor >> 8) & 0xFF);
        int b = (int) ((((endColor >> 0) & 0xFF) - ((startColor >> 0) & 0xFF)) * fraction) + ((startColor >> 0) & 0xFF);
        return Color.argb(255,r,g,b);
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public void setRpm(Double rpm) {
        this.rpm = rpm;
    }

    public void setTorque(Double torque) {
        this.torque = torque;
    }

    public void setMcTemp(Double mcTemp) {
        this.mcTemp = mcTemp;
    }

    public void setMcPower(Double mcPower) {
        this.mcPower = mcPower;
    }

    public void setBattery(Double battery) {
        this.battery = battery;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Double getSpeed() {
        return this.speed;
    }

    public Double getRpm() {
        return this.rpm;
    }

    public Double getBattery() {
        return this.battery;
    }

    public boolean isError() {
        return error;
    }

    public boolean isBatteryFault() {
        return batteryFault;
    }

    public void setBatteryFault(boolean batteryFault) {
        this.batteryFault = batteryFault;
    }

    public boolean isImdFault() {
        return imdFault;
    }

    public void setImdFault(boolean imdFault) {
        this.imdFault = imdFault;
    }

    public Double getPackVolt() {
        return packVolt;
    }

    public void setPackVolt(Double packVolt) {
        this.packVolt = packVolt;
    }

    public Double getLowVolt() {
        return lowVolt;
    }

    public void setLowVolt(Double lowVolt) {
        this.lowVolt = lowVolt;
    }

    public Double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public boolean isTsStatus() {
        return tsStatus;
    }

    public void setTsStatus(boolean tsStatus) {
        this.tsStatus = tsStatus;
    }

    public CarState getState() {
        return state;
    }

    public void setState(CarState state) {
        this.state = state;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}