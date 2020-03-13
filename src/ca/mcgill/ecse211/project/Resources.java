package ca.mcgill.ecse211.project;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3ColorSensor;

/**
 * This class is used to define static resources in one place for easy access and to avoid
 * cluttering the rest of the codebase. All resources can be imported at once like this:
 * 
 * <p>{@code import static ca.mcgill.ecse211.lab3.Resources.*;}
 */
public class Resources {
  
  public static boolean USLocalDone = false;
  public static boolean OBJECT_DETECTED = false;
  public static boolean obstacleAvoidanceInProgress = false;
  public static boolean travelToNotCompleted = false;
  public static boolean reachedOneOne = false;
  /**
   * Time for which the navigation thread sleeps for
   */
  public static final int NAVIGATION_SLEEP = 55;
  
  /**
   * Time for which the method retrieving samples from the Light sensors sleeps for 
   */
  public static final int LIGHTLOCALIZER_SLEEP = 50;

  
  public static int xDest;
  public static int yDest;
  
  
  /*
   * Define the room type in which the lab is performed.
   */
  public static final String CURRENT_ROOM = "BIG";
  
  /*
   * Black line threshold (for big room).
   */
  public static final int BLACK_LINE_THRESHOLD = 45;
  
  /*
   * Blue line threshold (for small room).
   */
  public static final int BLUE_LINE_THRESHOLD_L = 30;
  public static final int BLUE_LINE_THRESHOLD_R = 40;
  
  
 //------------------------------------------------------------------------------------------------------ 
  
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
  public static final EV3UltrasonicSensor usSensor = new EV3UltrasonicSensor(SensorPort.S2);  
  
  /**
   * The color sensor.
   */
  public static final EV3ColorSensor colourSensor1 = new EV3ColorSensor(SensorPort.S3);
 public static final EV3ColorSensor colourSensor2 = new EV3ColorSensor(SensorPort.S4);
  
  
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
  public static final double BASE_WIDTH = 8.90;        //15.30

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
  public static final double DEGS_PER_1RAD= 57.2598;                    //new
  
  /**
   * Number of radians in one degree, equivalent approximately to Math.PI/180 (used to convert to radians)
   */
public static final double RADS_PER_1DEG=0.01745329251;                 //new
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