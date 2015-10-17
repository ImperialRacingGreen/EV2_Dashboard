package imperial.racinggreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Dialog;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.io.FileOutputStream;

public class MainActivity extends Activity {
	
	public String newData = new String();
    private static final int ARDUINO_USB_VENDOR_ID = 0x2341;
    private static final int ARDUINO_DUE_USB_PRODUCT_ID = 0x3d;
    private static final int ARDUINO_UNO_USB_PRODUCT_ID = 0x01;
    private static final int ARDUINO_MEGA_2560_USB_PRODUCT_ID = 0x10;
    private static final int ARDUINO_MEGA_2560_R3_USB_PRODUCT_ID = 0x42;
    private static final int ARDUINO_MEGA_2560_ADK_R3_USB_PRODUCT_ID = 0x44;
    private static final int ARDUINO_MEGA_2560_ADK_USB_PRODUCT_ID = 0x3F;

	private double mRpm;
    private double mKph;
	private double mTorque;
	private double mAirTemp;
	private double mTemp;
	private double mContTemp;
	private int mVolt;
	private int mCurrent;
	private int mPower;
	private double mCoreStat;
	private double mError;
	private double mRfe;
	private double mFrg;
	private double mGo;
	
	private double bVolt;
	private double bCurrent;
	private double bSoc;
	private double bAveTemp;
	private double bTempMin;
	private double bTempMax;
	private int bFaultCode;
	private int bState;
	private double bCapacity;
	
	private int miscCarState;
	private int miscBatFault;
	private int miscIsoFault;
	private int miscThrottleOne;
	private int miscThrottleTwo;
	private int miscAveThrottle;
	private int miscBrake;
	private double miscLowVoltBatt;
	private int miscCarStateErr;
	private int miscVoltSense;
	//private int miscSysActive;
	private int miscShutdownLoopR;
	private int miscCurrentSense;
	private int miscPWM;
	private int miscButton;
	
	
    private final static String TAG = "MainActivity";
    private final static boolean DEBUG = false;
    
    private Boolean mIsReceiving;
    private Dashboard myDash;
    private View mDecorView;
	private boolean barHidden = false;
    private GestureDetector gestureDetector;

    private PendingIntent intent;

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
    ArrayList<String> values = new ArrayList<String>();
    String[] dataArray;
    int counter = 0;

    private void findDevice() {
		UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        UsbDevice usbDevice = null;
        HashMap<String, UsbDevice> usbDeviceList = usbManager.getDeviceList();
        if (DEBUG) Log.d(TAG, "length: " + usbDeviceList.size());
        Iterator<UsbDevice> deviceIterator = usbDeviceList.values().iterator();
        if (deviceIterator.hasNext()) {
            UsbDevice tempUsbDevice = deviceIterator.next();

            // Print device information. If you think your device should be able
            // to communicate with this app, add it to accepted products below.
            if (DEBUG) Log.d(TAG, "VendorId: " + tempUsbDevice.getVendorId());
            if (DEBUG) Log.d(TAG, "ProductId: " + tempUsbDevice.getProductId());
            if (DEBUG) Log.d(TAG, "DeviceName: " + tempUsbDevice.getDeviceName());
            if (DEBUG) Log.d(TAG, "DeviceId: " + tempUsbDevice.getDeviceId());
            if (DEBUG) Log.d(TAG, "DeviceClass: " + tempUsbDevice.getDeviceClass());
            if (DEBUG) Log.d(TAG, "DeviceSubclass: " + tempUsbDevice.getDeviceSubclass());
            if (DEBUG) Log.d(TAG, "InterfaceCount: " + tempUsbDevice.getInterfaceCount());
            if (DEBUG) Log.d(TAG, "DeviceProtocol: " + tempUsbDevice.getDeviceProtocol());

            if (tempUsbDevice.getVendorId() == ARDUINO_USB_VENDOR_ID) {
                if (DEBUG) Log.i(TAG, "Arduino device found!");

                switch (tempUsbDevice.getProductId()) {
                    case ARDUINO_DUE_USB_PRODUCT_ID:
                        Toast.makeText(getBaseContext(), "Arduino Due " + getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                    case ARDUINO_UNO_USB_PRODUCT_ID:
                        Toast.makeText(getBaseContext(), "Arduino Uno " + getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                    case ARDUINO_MEGA_2560_USB_PRODUCT_ID:
                        Toast.makeText(getBaseContext(), "Arduino Mega 2560 " + getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                    case ARDUINO_MEGA_2560_R3_USB_PRODUCT_ID:
                        Toast.makeText(getBaseContext(), "Arduino Mega 2560 R3 " + getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                    case ARDUINO_MEGA_2560_ADK_R3_USB_PRODUCT_ID:
                        Toast.makeText(getBaseContext(), "Arduino Mega 2560 ADK R3 " + getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                    case ARDUINO_MEGA_2560_ADK_USB_PRODUCT_ID:
                        Toast.makeText(getBaseContext(), "Arduino Mega 2560 ADK " + getString(R.string.found), Toast.LENGTH_SHORT).show();
                        usbDevice = tempUsbDevice;
                        break;
                }
            }
        }

        if (usbDevice == null) {
            if (DEBUG) Log.i(TAG, "No device found!");
            Toast.makeText(getBaseContext(), getString(R.string.no_device_found), Toast.LENGTH_LONG).show();
        } else {
            if (DEBUG) Log.i(TAG, "Device found!");
            Intent startIntent = new Intent(getApplicationContext(), ArduinoCommunicatorService.class);
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, startIntent, 0);
            usbManager.requestPermission(usbDevice, pendingIntent);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(onRuntimeError);
//        intent = PendingIntent.getActivity(YourApplication.getInstance().getBaseContext(), 0,
//                new Intent(getIntent()), getIntent().getFlags());
        if (DEBUG) Log.d(TAG, "onCreate()");

        IntentFilter filter = new IntentFilter();
        filter.addAction(ArduinoCommunicatorService.DATA_RECEIVED_INTENT);
        filter.addAction(ArduinoCommunicatorService.DATA_SENT_INTERNAL_INTENT);
        registerReceiver(mReceiver, filter);
		
		setContentView(R.layout.activity_main);
		
		// make new dashboard
        LinearLayout layout = (LinearLayout) findViewById(R.id.dashLayout);
        Bitmap bitmap = Bitmap.createBitmap(1920, 1080, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        myDash = new Dashboard(this, 5000.0);
        myDash.draw(canvas);
        myDash.setLayoutParams(new ViewGroup.LayoutParams(1920, 1080));
        layout.addView(myDash);

        // add touch-and-hold gesture listener to Dashboard view and load custom button grid view
        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                Dialog buttonGridDialog = new Dialog(myDash.getContext());
                ButtonGrid buttonGrid = new ButtonGrid(myDash.getContext(), buttonGridDialog);
                buttonGridDialog.setContentView(buttonGrid);
                buttonGridDialog.setTitle("Select action");
                buttonGridDialog.show();
            }
        };
        gestureDetector = new GestureDetector(myDash.getContext(), gestureListener);
        myDash.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });

        findDevice();
//        if(VERSION.SDK_INT >= 16) hideSystemUI();

    }

    private Thread.UncaughtExceptionHandler onRuntimeError= new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            //Try starting the Activity again

            Intent mStartActivity = new Intent(getApplicationContext(), MainActivity.class);
            int mPendingIntentId = 123456;
            PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
            System.exit(0);

        }
    };

	// @SuppressLint("LongLogTag")
	// @Override
    // protected void onListItemClick(ListView l, View v, int position, long id) {
        // super.onListItemClick(l, v, position, id);

        // if (DEBUG) Log.i(TAG, "onListItemClick() " + position + " " + id);
        // ByteArray transferedData = mTransferedDataList.get(position);
        // transferedData.toggleCoding();
        // mTransferedDataList.set(position, transferedData);
        // mDataAdapter.notifyDataSetChanged();
    // }

    @Override
    protected void onNewIntent(Intent intent) {
        if (DEBUG) Log.d(TAG, "onNewIntent() " + intent);
        super.onNewIntent(intent);

        if (UsbManager.ACTION_USB_DEVICE_ATTACHED.contains(intent.getAction())) {
            if (DEBUG) Log.d(TAG, "onNewIntent() " + intent);
            findDevice();
        }
    }

	@SuppressLint("LongLogTag")
    @Override
    protected void onDestroy() {
        if (DEBUG) Log.d(TAG, "onDestroy()");
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
	
	@SuppressLint("LongLogTag")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
	BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @SuppressLint("LongLogTag")
        private void handleTransferedData(Intent intent, boolean receiving) {
            if (mIsReceiving == null || mIsReceiving != receiving) {
                mIsReceiving = receiving;
            }

            final byte[] newTransferedData = intent.getByteArrayExtra(ArduinoCommunicatorService.DATA_EXTRA);

            StringBuilder tempData = new StringBuilder();
            for (byte aNewTransferedData : newTransferedData) {
                tempData.append(new String(new byte[]{aNewTransferedData}));
            }

            newData = newData.concat(tempData.toString());
//            newData = tempData.toString();

			if(newData.contains("@") && newData.contains("#")) {
				int startFrameIndex = newData.indexOf('@');
				int endFrameIndex = newData.lastIndexOf('#');
				if (endFrameIndex-startFrameIndex > 0) {
                    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.UK);
					String timeStamp = timeFormat.format(cal.getTime());

                    String temp1 = newData.replace("@", timeStamp + ",");
                    temp1 = temp1.replace("#","");

                    String temp2 = newData.replace("@","");
                    temp2 = temp2.replace("#", "");

//                    newData = newData.delete(0,endFrameIndex);
                    String toBeReplaced = newData.substring(0, endFrameIndex);
                    newData = newData.replace(toBeReplaced, "");
                    dataArray = temp2.split(",");

                    if (dataArray.length == variables.length) {


                        values.add(temp1);
//                        if (values.size() != 0) {
//                            String temp = values.get(0);
//                        Toast.makeText(getBaseContext(),String.valueOf(values.size()), Toast.LENGTH_SHORT).show();
//                        }
                        if (counter++ == 3){
                            new DemoTask().execute(timeStamp, dataArray[0], dataArray[2], dataArray[8], dataArray[15], dataArray[26], dataArray[35]);
                            counter = 0;
                        }

                        Double rpm = Double.valueOf(dataArray[0]);
                        myDash.setRpm(rpm);

                        boolean batFault = false;
                        boolean imdFault = false;
                        boolean tSysActive = false;
                        CarState carStateError = CarState.IDLE;

                        int batFaultVal = Integer.parseInt(dataArray[27]);
                        if (batFaultVal == 0)batFault = true;
                        myDash.setBatteryFault(batFault);

                        int imdFaultVal = Integer.parseInt(dataArray[28]);
                        if (imdFaultVal == 0)imdFault = true;
                        myDash.setImdFault(imdFault);
                        /**/
                        int carStateVal = Integer.parseInt(dataArray[26]);
                        switch (carStateVal){
                            case 0:
                                carStateError = CarState.IDLE;
                                break;
                            case 1:
                                carStateError = CarState.DRIVE;
                                break;
                            case 2:default:
                                carStateError = CarState.ERROR;
                                break;
                        }
                        myDash.setState(carStateError);

                        Double packVolt = Double.valueOf(dataArray[15]);
                        myDash.setPackVolt(packVolt);

                        Double mcPower = Double.valueOf(dataArray[8]);
                        myDash.setMcPower(mcPower);

                        Double maxTemp = Double.valueOf(dataArray[22]);
                        myDash.setMaxTemp(maxTemp);

                        Double torque = Double.valueOf(dataArray[2]);
                        myDash.setTorque(torque);

//                        int TractiveSysActive = Integer.parseInt(dataArray[37]);
//                        tSysActive = (TractiveSysActive == 1);
//                        myDash.setTsStatus(tSysActive);
                        Double mcTemp = Double.valueOf(dataArray[4]);
                        myDash.setMcTemp(mcTemp);

                        Double LowVolt = Double.valueOf(dataArray[33]);
                        myDash.setLowVolt(LowVolt);

                        String errorMessage = dataArray[35];
                        myDash.setErrorMessage(errorMessage);
                        myDash.redraw();
                    }
                }
            }
//			if(newData.contains("@") && newData.contains("#")){
//				int startFrameIndex = newData.indexOf('@');
//				int endFrameIndex = newData.lastIndexOf('#');
//				if (endFrameIndex-startFrameIndex > 0) {
//                    /**
//                     Possible to use String.substring(String, beginIndex, endIndex)
//                     instead of char. (Line 184)
//                     newData = newData.substring(startFrameIndex, endFrameIndex);
//                     */
//                    char[] charArray = newData.toCharArray();
//                    char[] tempCharArray = new char[1000];
//                    int charIndex = 0;
//                    for (int i = startFrameIndex + 1; i < endFrameIndex; i++) {
//                        tempCharArray[charIndex] = charArray[i];
//                        charIndex++;
//                    }
//                    newData = new String(tempCharArray);
//                    newData = newData.replace(" ", "");
//                    newData = newData.replace("@", "");
//                    newData = newData.replace("#", "");
//                    String[] dataArray = newData.split(",");
//
//                    /**
//                     Data being sent to Dashboard UI  goes here
//                     */
//					// For debugging
//                    // myDash.setError(true);
//
//
//                    Double rpm = Double.valueOf(dataArray[0]);
//                    myDash.setRpm(rpm);
//
//                    boolean batFault = false;
//                    boolean imdFault = false;
//                    boolean tSysActive = false;
//                    CarState carStateError = CarState.IDLE;
//
//                    int batFaultVal = Integer.parseInt(dataArray[14]);
//                    if (batFaultVal == 0)batFault = true;
//                    myDash.setBatteryFault(batFault);
//
//                    int imdFaultVal = Integer.parseInt(dataArray[15]);
//                    if (imdFaultVal == 0)imdFault = true;
//                    myDash.setImdFault(imdFault);
//                    /**/
//                    int carStateVal = Integer.parseInt(dataArray[22]);
//                    switch (carStateVal){
//                        case 0:
//                            carStateError = CarState.IDLE;
//                            break;
//                        case 1:
//                            carStateError = CarState.DRIVE;
//                            break;
//                        case 2:default:
//                            carStateError = CarState.ERROR;
//                            break;
//                    }
//                    myDash.setState(carStateError);
//                    /**
//                    */
//
//                    Double packVolt = Double.valueOf(dataArray[9]);
//                    myDash.setPackVolt(packVolt);
//
//                    Double LowVolt = Double.valueOf(dataArray[18]);
//                    myDash.setLowVolt(LowVolt);
//
//                    Double maxTemp = Double.valueOf(dataArray[11]);
//                    myDash.setMaxTemp(maxTemp);
//
//                    int TractiveSysActive = Integer.parseInt(dataArray[21]);
//                    tSysActive = (TractiveSysActive == 1);
//                    myDash.setTsStatus(tSysActive);
//                    /**
//                    */
//					myDash.redraw();
//					/**
//					Function for logging data in database also goes here
//					*/
//
//					mRpm 				= Double.parseDouble(dataArray[0]);
//                                                             mKph 				= Double.parseDouble(dataArray[1]);
//					mTorque 			= Double.parseDouble(dataArray[2]);
//					mAirTemp 			= Double.parseDouble(dataArray[3]);
//					mContTemp 			= Double.parseDouble(dataArray[4]);
//                                                             mTemp 			= Double.parseDouble(dataArray[5]);
//					mVolt 				= Integer.parseInt(dataArray[6]);
//					mCurrent 			= Integer.parseInt(dataArray[7]);
//					mPower	 		= Integer.parseInt(dataArray[8]);
//					mCoreStat 			= Double.parseDouble(dataArray[8]);
//					mError 				= Double.parseDouble(dataArray[9]);
//					mRfe 				= Double.parseDouble(dataArray[7]);
//					mFrg 				= Double.parseDouble(dataArray[8]);
//					mGo 				= Double.parseDouble(dataArray[12]);
//					bVolt 				= Double.parseDouble(dataArray[9]);
//					bCurrent 			= Double.parseDouble(dataArray[14]);
//					bSoc 				= Double.parseDouble(dataArray[15]);
//					bAveTemp 			= Double.parseDouble(dataArray[10]);
//					bTempMin 			= Double.parseDouble(dataArray[11]);
//					bTempMax 			= Double.parseDouble(dataArray[12]);
//					bState 				= Integer.parseInt(dataArray[13]);
//					bFaultCode 			= Integer.parseInt(dataArray[20]);
//					bCapacity 			= Integer.parseInt(dataArray[21]);
//					miscBatFault 		= Integer.parseInt(dataArray[14]);
//					miscIsoFault 		= Integer.parseInt(dataArray[15]);
//					miscThrottleOne 	= Integer.parseInt(dataArray[24]);
//					miscThrottleTwo 	= Integer.parseInt(dataArray[25]);
//					miscAveThrottle 	= Integer.parseInt(dataArray[16]);
//					miscBrake		 	= Integer.parseInt(dataArray[17]);
//					miscLowVoltBatt 	= Double.parseDouble(dataArray[18]);
//					miscCarStateErr 	= Integer.parseInt(dataArray[29]);
//					miscVoltSense 		= Integer.parseInt(dataArray[19]);
//                    TractiveSysActive 	= Integer.parseInt(dataArray[20]);
//					miscShutdownLoopR 	= Integer.parseInt(dataArray[21]);
//					miscCurrentSense 	= Integer.parseInt(dataArray[33]);
//					miscPWM 			= Integer.parseInt(dataArray[34]);
//					miscButton 			= Integer.parseInt(dataArray[35]);
//					miscCarState 		= Integer.parseInt(dataArray[22]);
//
//                    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
//					SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
//					SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.UK);
//
//					String testDate = dateFormat.format(cal.getTime());
//					String timeStamp = timeFormat.format(cal.getTime());
//                    // myDash.setErrorMessage(timeStamp);
//                    /**/
//					EVTULog evDataLog = new EVTULog(
//                            mRpm,						// Double.parseDouble(dataArray[0])
//                            mTorque,                    // Double.parseDouble(dataArray[1])
//                            mAirTemp,                   // Double.parseDouble(dataArray[2])
//                            mTemp,                      // Double.parseDouble(dataArray[3])
//                            mContTemp,                  // Double.parseDouble(dataArray[4])
//                            mVolt,                      // Integer.parseInt(dataArray[5])
//                            mCurrent,                   // Integer.parseInt(dataArray[6])
//                            mPower,                   	// Integer.parseInt(dataArray[7])
//                            mCoreStat,                  // Double.parseDouble(dataArray[8])
//                            mError,                     // Double.parseDouble(dataArray[9])
//                            mRfe,                       // Double.parseDouble(dataArray[10])
//                            mFrg,  						// Double.parseDouble(dataArray[11])
//                            mGo,  						// Double.parseDouble(dataArray[12])
//                            bVolt,  					// Double.parseDouble(dataArray[13])
//                            bCurrent,  					// Double.parseDouble(dataArray[14])
//                            bSoc,  						// Double.parseDouble(dataArray[15])
//                            bAveTemp,  					// Double.parseDouble(dataArray[16])
//                            bTempMin,  					// Double.parseDouble(dataArray[17])
//                            bTempMax,  					// Double.parseDouble(dataArray[18])
//                            bState,    					// Integer.parseInt(dataArray[19])
//                            bFaultCode,    				// Integer.parseInt(dataArray[20])
//                            bCapacity,  				// Double.parseDouble(dataArray[21])
//                            miscBatFault,    			// Integer.parseInt(dataArray[22])
//                            miscIsoFault,    			// Integer.parseInt(dataArray[23])
//                            miscThrottleOne,    		// Integer.parseInt(dataArray[24])
//                            miscThrottleTwo,    		// Integer.parseInt(dataArray[25])
//                            miscAveThrottle,    		// Integer.parseInt(dataArray[26])
//                            miscBrake,    				// Integer.parseInt(dataArray[27])
//                            miscLowVoltBatt,  			// Double.parseDouble(dataArray[28])
//                            miscCarStateErr,    		// Integer.parseInt(dataArray[29])
//                            miscVoltSense,    			// Integer.parseInt(dataArray[30])
//                            TractiveSysActive,    		// Integer.parseInt(dataArray[31])
//                            miscShutdownLoopR,	    	// Integer.parseInt(dataArray[32])
//							miscCurrentSense,    		// Integer.parseInt(dataArray[33])
//                            miscPWM,    				// Integer.parseInt(dataArray[34])
//                            miscButton,    				// Integer.parseInt(dataArray[35])
//                            miscCarState,    			// Integer.parseInt(dataArray[36])
//                            testDate,
//                            timeStamp
//                    );
//
//					evDataLog.save();
//                    /**
//                    */
//				}
////				else{
////					throw new IllegalArgumentException("Data transfer error");
////				}
//				if((newData.length() - endFrameIndex) > 0 ){
//					String tempString = newData;
//					newData = tempString.substring(endFrameIndex);
//				}
//			}
        }

        @SuppressLint("LongLogTag")
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (DEBUG) Log.d(TAG, "onReceive() " + action);

            if (ArduinoCommunicatorService.DATA_RECEIVED_INTENT.equals(action)) {
                handleTransferedData(intent, true);
            } 
			else if (ArduinoCommunicatorService.DATA_SENT_INTERNAL_INTENT.equals(action)) {
                handleTransferedData(intent, false);
            }
        }
    };

    class DemoTask extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String... params) {
            //Your implementation
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();

            HttpParams httpParameters = httpclient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 10);
            HttpConnectionParams.setSoTimeout(httpParameters, 0);
            HttpConnectionParams.setTcpNoDelay(httpParameters, true);

            HttpPost httppost = new HttpPost("https://imperialev2.appspot.com/_ah/api/ev2Api/v1/uploadData");



            JSONObject json = new JSONObject();
            try {
                json.put("date",params[0]);
                json.put("rpm",params[1]);
                json.put("torque",params[2]);
                json.put("mcpower",params[3]);
                json.put("bmsVoltage",params[4]);
                json.put("carState",params[5]);
                json.put("error", params[6]);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                StringEntity se = new StringEntity(json.toString());
                Log.w("DATA", json.toString());
                httppost.setEntity(se);
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("Content-type", "application/json");
            }
            catch (Exception e) {
                Log.w("",e);
            }

            HttpResponse response = null;

            try {
                response = httpclient.execute(httppost);
                Log.w("RESP", String.valueOf(response));
            }
            catch (Exception e) {
                Log.w("",e);
            }

            return null;
        }

        protected void onPostExecute(String result) {
            // TODO: do something with the feed
            Log.w("",result);
        }
    }

//    public void hideTab(){
//        if(!barHidden) {
//            hideSystemUI();
//        }
//        else{
//            showSystemUI();
//        }
//        barHidden = !barHidden;
//    }

    public void exportCSV() {
        // setup csv file
        if (values.size() != 0) {
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH_mm_ss", Locale.UK);

            String testDate = dateFormat.format(cal.getTime());
            String timeStamp = timeFormat.format(cal.getTime());
            String filename = "EV2 " + testDate + " " + timeStamp + ".csv";

            File file = new File(Environment.getExternalStorageDirectory() + File.separator + filename);

            ArrayList<String> tempValues = new ArrayList<String>();
            tempValues = values;

            String output = "TIME,";
            for (String x: variables) {
                output += x;
                output += ",";

            }
            output += "\n";
//            for(String x : tempValues) {
//                output += x;
//            }
            StringBuilder strBuilder = new StringBuilder();
            for(String x : tempValues) {
                strBuilder.append(x + "\n");
            }
            output += strBuilder.toString();

            try {
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                // print headers into csv file
                outputStream.write(output.getBytes());
                outputStream.close();

                values.clear();

                Log.w("", "CSV Export");

                Toast.makeText(getApplicationContext(), "Saved data to CSV successfully!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
	// This snippet shows the system bars. It does this by removing all the flags
	// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}
