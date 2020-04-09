package ca.mcgill.ecse211.project;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3ColorSensor;

import ca.mcgill.ecse211.playingfield.Point;
import ca.mcgill.ecse211.playingfield.Region;
import ca.mcgill.ecse211.wificlient.WifiConnection;
import java.math.BigDecimal;
import java.util.Map;

/**
 * This class is used to define static resources in one place for easy access and to avoid
 * cluttering the rest of the codebase. All resources can be imported at once like this:
 * 
 * <p>{@code import static ca.mcgill.ecse211.lab3.Resources.*;}
 */
public class Resources {
  
  // Set these as appropriate for your team and current situation
  /**
   * The default server IP used by the profs and TA's.
   */
  public static final String DEFAULT_SERVER_IP = "192.168.2.3";
  
  /**
   * The IP address of the server that transmits data to the robot. For the beta demo and
   * competition, replace this line with
   * 
   * <p>{@code public static final String SERVER_IP = DEFAULT_SERVER_IP;}
   */
  public static final String SERVER_IP = "192.168.2.3"; // = DEFAULT_SERVER_IP;
  
  /**
   * Your team number.
   */
  public static final int TEAM_NUMBER = 13;
  
  /** 
   * Enables printing of debug info from the WiFi class. 
   */
  public static final boolean ENABLE_DEBUG_WIFI_PRINT = true;
  
  /**
   * Enable this to attempt to receive Wi-Fi parameters at the start of the program.
   */
  public static final boolean RECEIVE_WIFI_PARAMS = true;
  
  
  // ----------------------------- DECLARE YOUR CURRENT RESOURCES HERE -----------------------------
  // ----------------------------- eg, motors, sensors, constants, etc -----------------------------
  
  
  /**
   * Container for the Wi-Fi parameters.
   */
  public static Map<String, java.lang.Object> wifiParameters;
  
  // This static initializer MUST be declared before any Wi-Fi parameters.
  static {
    receiveWifiParameters();
  }
  
  /** Red team number. */
  public static int redTeam = getWP("RedTeam");

  /** Red team's starting corner. */
  public static int redCorner = getWP("RedCorner");

  /** Green team number. */
  public static int greenTeam = getWP("GreenTeam");

  /** Green team's starting corner. */
  public static int greenCorner = getWP("GreenCorner");

  /** The Red Zone. */
  public static Region red = makeRegion("Red");

  /** The Green Zone. */
  public static Region green = makeRegion("Green");

  /** The Island. */
  public static Region island = makeRegion("Island");

  /** The red tunnel footprint. */
  public static Region tnr = makeRegion("TNR");

  /** The green tunnel footprint. */
  public static Region tng = makeRegion("TNG");

  /** The red search zone. */
  public static Region szr = makeRegion("SZR");

  /** The green search zone. */
  public static Region szg = makeRegion("SZG");
  
  /**
   * Receives Wi-Fi parameters from the server program.
   */
  public static void receiveWifiParameters() {
    // Only initialize the parameters if needed
    if (!RECEIVE_WIFI_PARAMS || wifiParameters != null) {
      return;
    }
    System.out.println("Waiting to receive Wi-Fi parameters.");

    // Connect to server and get the data, catching any errors that might occur
    try (WifiConnection conn =
        new WifiConnection(SERVER_IP, TEAM_NUMBER, ENABLE_DEBUG_WIFI_PRINT)) {
      /*
       * getData() will connect to the server and wait until the user/TA presses the "Start" button
       * in the GUI on their laptop with the data filled in. Once it's waiting, you can kill it by
       * pressing the back/escape button on the EV3. getData() will throw exceptions if something
       * goes wrong.
       */
      wifiParameters = conn.getData();
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }
  
  /**
   * Returns the Wi-Fi parameter int value associated with the given key.
   * 
   * @param key the Wi-Fi parameter key
   * @return the Wi-Fi parameter int value associated with the given key
   */
  public static int getWP(String key) {
    if (wifiParameters != null) {
      return ((BigDecimal) wifiParameters.get(key)).intValue();
    } else {
      return 0;
    }
  }
  
  /** 
   * Makes a point given a Wi-Fi parameter prefix.
   */
  public static Point makePoint(String paramPrefix) {
    return new Point(getWP(paramPrefix + "_x"), getWP(paramPrefix + "_y"));
  }
  
  /**
   * Makes a region given a Wi-Fi parameter prefix.
   */
  public static Region makeRegion(String paramPrefix) {
    return new Region(makePoint(paramPrefix + "_LL"), makePoint(paramPrefix + "_UR"));
  }
 
 //----------------------------------------------------------------------------------------------------------------------------
 //----------------------------------------------------------------------------------------------------------------------------
 //----------------------------------------------------------------------------------------------------------------------------

/**
* General sleep time.
*/
public static final int GENERAL_SLEEP = 250; 

/**
* Threshold to determine if looking at the corner
*/
public static final int DIST_THRESHOLD = 30;

/**
* A 90 degree (right angle).
*/
public static final int RIGHT_ANGLE = 90;

/**
* A 45 degree angle.
*/
public static final int HALF_RIGHT_ANGLE = 45;

/**
* To know when first corner localization is done.
*/
public static boolean DONE_CORNER_LOCALIZATION = false;

/**
* A 360 degree angle.
*/
public static final int DEGREES_MAX = 360;

/**
* Slow speed.
*/
public static final int MOTOR_LOW = 90;

/**
* Distance from end of US sensor to center of robot
*/
public static final int SENSOR_TO_CENTER_DIST = 7;

/**
 * Whether Ultrasonic localization has been completed or not
 */
public static boolean USLocalDone = false;
/**
 * Whether an object has been detected by the ultrasonic sensor
 */
public static boolean OBJECT_DETECTED = false;
/**
 * Whether obstacle avoidance is in progress
 */
public static boolean obstacleAvoidanceInProgress = false;
/**
 * Whether the robot has reached the (x,y) coordinate passed to the travelTo(int x,int y) method in the Navigation class
 */
public static boolean travelToNotCompleted = false;

/**
* Time for which the navigation thread sleeps for
*/
public static final int NAVIGATION_SLEEP = 55;

/**
* Time for which the method retrieving samples from the Light sensors sleeps for 
*/
public static final int LIGHT_LOCALIZER_SLEEP = 50;
/**
* Sleep time for the ultrasonic sensor
*/
public static final int US_LOCALIZER_SLEEP = 50;
/**
* Sleep time for the front colour sensor used in colour detection
*/
public static final int COLOUR_DETECTOR_SLEEP = 50;
/**
* The x coordinate of the desired destination of the robot. 
* This variable is used in the completeTravelTo() method in the Navigation class.
*/
public static int xDest;
/**
* The y coordinate of the desired destination of the robot. 
* This variable is used in the completeTravelTo() method in the Navigation class.
*/
public static int yDest;


/**
* Define the room type in which the lab is performed.
*/
public static final String CURRENT_ROOM = "BIG";

/**
* Black line threshold for the left wheel colour sensor and the right wheel colour sensor (for big room).
*/
public static final int BLACK_LINE_THRESHOLD = 45;

/**
* Blue line threshold  for the left wheel colour sensor (for small room).
*/
public static final int BLUE_LINE_THRESHOLD_L = 30;

/**
 * Blue line threshold  for the right wheel colour sensor (for small room).
 */
public static final int BLUE_LINE_THRESHOLD_R = 40;


/**
*  90 degrees
*/
public static final int NINETY_DEGREES = 90;
   
/** Period of sampling f (ms). */
public static final int SAMPLING_INTERVAL_US = 25;       

/** Period of display update (ms). */
public static final int DISPLAY_SLEEP_PERIOD = 100; 

/**
* Time for which the method that returns the colour calculated by the colour sensor sleeps for (ms)
*/
public static final int COLOUR_SENSOR_SLEEP = 50;

/**
* PI
*/
public static final double PI=3.1415927; 
/**
* The ultrasonic sensor.
*/
public static final EV3UltrasonicSensor usSensor = new EV3UltrasonicSensor(SensorPort.S1);  

/**
* the right wheel colour sensor used in light localization
*/
public static final EV3ColorSensor colourSensor_leftwheel = new EV3ColorSensor(SensorPort.S2);
/**
* the left wheel colour sensor used in light localization
*/
public static final EV3ColorSensor colourSensor_rightwheel = new EV3ColorSensor(SensorPort.S3);
/**
* The colour sensor at the front of the robot used for finding the vertical pin of the stranded vehicle via colour detection
*/
public static final EV3ColorSensor colourSensor_front = new EV3ColorSensor(SensorPort.S4);

/**
* The poll sleep time, in milliseconds.
*/
public static final int POLL_SLEEP_TIME = 40; 

/**
* the line y = D as instructed in the tutorial notes
*/
public static final int D = 35;


/**
* The wheel radius in centimeters.
*/
public static final double WHEEL_RADIUS = 2.098;

/**
* The robot width in centimeters.
*/
public static final double BASE_WIDTH = 9.72;        //15.30

/**
* The speed at which the robot moves forward in degrees per second.
*/
public static final int FWD_SPEED = 100;

/**
* The speed at which the robot rotates in degrees per second.
*/
public static final int ROTATION_SPEED = 90;          

/**
* The motor acceleration in degrees per second squared.
*/
public static final int ACCELERATION_deg=3000;

/**
* Timeout period in milliseconds.
*/
public static final int TIMEOUT_PERIOD_ms = 3000;

/**
* The tile size in centimeters. Note that 30.48 cm = 1 ft.
*/
public static final double TILE_SIZE_cm = 30.48;
/**
* Number of degrees in one radian, equivalent approximately to 180/Math.PI.(used to convert to degrees)
*/
public static final double DEGS_PER_1RAD= 57.2598;                   

/**
* Number of radians in one degree, equivalent approximately to Math.PI/180 (used to convert to radians)
*/
public static final double RADS_PER_1DEG=0.01745329251;                
/**
* The left motor.
*/
public static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.D);

/**
* The right motor.
*/
public static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.A);

/**
* The LCD.
*/
public static final TextLCD lcd = LocalEV3.get().getTextLCD();

/**
* The odometer.
*/
public static Odometer odometer = Odometer.getOdometer();

}