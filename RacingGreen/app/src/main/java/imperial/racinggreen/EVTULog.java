package imperial.racinggreen;
import com.orm.SugarRecord;

/**
 * Created by JohnD on 21-Mar-15.
 */
public class EVTULog extends SugarRecord<EVTULog> {
    // Motor Controller variables
	double rpm;		// [00] RPM	1
	double tqe;		// [01] Torque	1
	double atmp;	// [02] Air Temperature 0
	double mtmp;	// [03] Motor Temperature 1
	double mctmp;	// [04] Motor Controller Temperature 1 
	int mcv;		// [05] Voltage 1
	int mcc;		// [06] Current 1
	int mcp;		// [07] Power 1
	double mcstat;	// [08] Core Status 0
	double mcerr;	// [09] Errors 0
	double mcrfe;	// [10] rfe 1
	double mcfrg;	// [11] frg 1
	double go;		// [12] GO 0
	
	//BMS variables
	double bvolt; 	// [13] Battery Pack Voltage 1
	double bcur;	// [14] Current 0
	double soc;		// [15] SOC 0
	double btmp;	// [16] Average Temperature 1
	double bmintmp;	// [17] Min Temperature 1
	double bmaxtmp;	// [18] Max Temperature 1
	int bstat;		// [19] State	1
	int bflt;		// [20] Fault Code 0
	double bcap;	// [21] Capacity 0
	
	//Misc variables
	
	int batteryFault;		// [22] Battery Fault 1
	int isolationFault;		// [23] Isolation Fault 1
	int throttleOne;		// [24] Throttle 1 0
	int throttleTwo;		// [25] Throttle 2 0
	int aveThrottle;		// [26] Average Throttle 1
	int miscBrake;			// [27] Brake 1
	double lowVoltBatt;		// [28] Low Voltage Battery 1
	int carStateErr;	    // [29] Error State of Car 0
	int hVoltSense;			// [30] High Voltage Sensor 1
	int tractiveSysActive;	// [31] Tractive System Active 1
	int shutdownLoopRelay;	// [32] Shutdown Loop Relay 1
	int hCurrentSense;		// [33] High Current Sensor 0
	int insulationPWM;		// [34] Insulation PWM 0
	int dashStartButton;	// [35] Dashboard Start Button 0
	int carState;			// [36] Car State 1
	
	String testDate;        //Test Date
	String timeStamp; 		//Record of time
    

	public EVTULog(){
	}
	
	public EVTULog(double rpm, double tqe, double atmp,double mtmp,double mctmp, int mcv,int mcc,int mcp,
					double mcstat, double mcerr, double mcrfe, double mcfrg, double go,double bvolt,
					double bcur, double soc, double btmp, double bmintmp, double bmaxtmp, int bstat,
					int bflt, double bcap, int batteryFault, int isolationFault,
					int throttleOne, int throttleTwo, int aveThrottle, int miscBrake, double lowVoltBatt, int carStateErr,
					int hVoltSense, int tractiveSysActive, int shutdownLoopRelay, int hCurrentSense,
					int insulationPWM, int dashStartButton, int carState, String testDate, String timeStamp){
	this.rpm = rpm;				
	this.tqe = tqe;
	this.atmp = atmp;
	this.mtmp = mtmp;
	this.mctmp = mctmp;
	this.mcv = mcv;
	this.mcc = mcc;
	this.mcp = mcp;
	this.mcstat = mcstat;
	this.mcerr = mcerr;
	this.mcrfe = mcrfe;
	this.mcfrg = mcfrg;
    this.mcp = mcp;
	this.go = go;
	this.bvolt = bvolt;
	this.bcur = bcur;
	this.soc = soc;
	this.btmp = btmp;
	this.bmintmp = bmintmp;
	this.bmaxtmp = bmaxtmp;
	this.bstat = bstat;
	this.bflt = bflt;
	this.bcap = bcap;
	this.batteryFault = batteryFault;
	this.isolationFault = isolationFault;
	this.throttleOne = throttleOne;
	this.throttleTwo = throttleTwo;
	this.aveThrottle = aveThrottle;
	this.miscBrake = miscBrake;
	this.lowVoltBatt = lowVoltBatt;
	this.carStateErr = carStateErr;
	this.hVoltSense = hVoltSense;
	this.tractiveSysActive = tractiveSysActive;
	this.shutdownLoopRelay = shutdownLoopRelay;
	this.hCurrentSense = hCurrentSense;
	this.insulationPWM = insulationPWM;
	this.dashStartButton = dashStartButton;
	this.carState = carState;
    this.testDate =  testDate;
	this.timeStamp = timeStamp;
	}
}
