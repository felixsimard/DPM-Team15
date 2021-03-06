
package ca.mcgill.ecse211.project;


import lejos.hardware.Sound;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

import static ca.mcgill.ecse211.project.Resources.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class to manage and record values of the ultrasonic sensor.
 */
public class UltrasonicSensor extends Thread {

  /**
   * distance calculated by the US sensor
   */
  private static int distance;     

  /**
   *  ArrayList to store all the readings object
   */
  public static  ArrayList<Reading> readings = new ArrayList<Reading>();

  /**
   * Buffer (array) to store US samples. Declared as an instance variable to avoid creating a new
   * array each time {@code readUsSample()} is called.
   */
  private float[] usData = new float[usSensor.sampleSize()];        

  private Navigation nav = new Navigation();       
  /**
   * Noise margin in cm
   */
  private final static double K = 2;         
  /**
   * the point at which the measured distance falls above D - K
   */
  private static double angle1=0;
  /**
   * the point at which the measured distance falls above D + K
   */
  private static double angle2=0;
  /**
   * average of angle1 and angle2
   */
  private static double avg_angle_12;
  /**
   * the point at which the measured distance falls below D + K
   */
  private static double angle3=0;
  /**
   * the point at which the measured distance falls below D - K
   */
  private static double angle4=0;
  /**
   * average of angle3 and angle4
   */
  private static double avg_angle_34;
  /**
   * angle calculated  by the equation given in the tutorial notes, this angle is used to correct the direction of the EV3
   */
  private static double deltaTheta;               

  /**
   * Instantiate ColorSensor object
   */
  private ColorSensor colorSensor = new ColorSensor();

  /**
   * To hold the x, y, t values of the odometer
   */
  private double[] position;

  /**
   * ffset angle, values between 0-359
   */
  private double deltaAngle;

  /**
   * Cummulative offset angle, values from 0-inf (used for US localization)
   */
  private double totalDeltaAngle;

  /**
   * Reading object which will store the minimum distance and the offset angle associated to it
   */
  private Reading minReading;


  /**
   * Constructor.
   */
  public UltrasonicSensor() {
  }

  /**
   * Main run method.
   */
  public void run() {

    /*
    usLocalize();

    while(!DONE_CORNER_LOCALIZATION) {
      continue;
    }

    Main.sleepFor(GENERAL_SLEEP);

    while(true) {
      detectObject();
    }
     */

  } 

  /**
   * Method to continuously check if our US sensor detects an object.
   * @return void
   */
  public void detectObject() {

    int d = 100;
    int min_distance = 2;
    int max_distance = 4;
    boolean hasDetectedObject = false;

    // while US sensor has not detected an object...
    while(!hasDetectedObject) {

      // get US sensor reading
      d = readUsDistance();

      // when we come within around 2-4 cm away from the ring
      if(d >= min_distance && d <= max_distance) {
        hasDetectedObject = true; // update local flag
        colorSensor.objectDetected(); // trigger the objectDetected method in ColorSensor class
        nav.stopMotors();
      }

      Main.sleepFor(POLL_SLEEP_TIME);

    }

    hasDetectedObject = false; // update local flag

    return;

  }

  /**
   * Returns the distance between the US sensor and an obstacle in cm.
   * 
   * @return the distance between the US sensor and an obstacle in cm
   */
  public int readUsDistance() {         
    // extract from buffer, convert to cm, cast to int, and filter
    usSensor.fetchSample(usData, 0);  
    int distance = ((int) (usData[0] * 100.0));
    //System.out.println("US distance: " + distance);
    return distance;
  }

  /**
   * 
   * @return Returns the distance calculated by the Ultrasonic sensor
   */
  public static int getDistance() {     
    return distance;
  }

  /**
   * Turns the robot by a specified angle. Note that this method is different from {@code Navigation.turnTo()}. For
   * example, if the robot is facing 90 degrees, calling {@code turnBy(90)} will make the robot turn to 180 degrees, but
   * calling {@code Navigation.turnTo(90)} should do nothing (since the robot is already at 90 degrees).
   * 
   * @param angle the angle by which to turn, in degrees
   */
  public static void turnBy(double angle) {
    leftMotor.rotate(convertAngle(angle), true);
    rightMotor.rotate(-convertAngle(angle), false);
  }

  /**
   * Converts input angle to the total rotation of each wheel needed to rotate the robot by that angle.
   * 
   * @param angle the input angle
   * @return the wheel rotations necessary to rotate the robot by the angle
   */
  public static int convertAngle(double angle) {
    return convertDistance(Math.PI * BASE_WIDTH * angle / 360.0);
  }

  /**
   * Converts input distance to the total rotation of each wheel needed to cover that distance.
   * 
   * @param distance the input distance
   * @return the wheel rotations necessary to cover the distance
   */
  public static int convertDistance(double distance) {
    return (int) ((180.0 * distance) / (Math.PI * WHEEL_RADIUS));
  }


  /*
   * 
   * Method to perform the corner US localization. 
   * Essentially, sweep 360 degrees, record all distances read by the US sensor. Point the EV3 towards the minimal distance.
   * Rotate 90 degrees, check if still facing a wall, if so rotate another 90 degrees, if not, then facing the 0 degree axis.
   * 
   * Then, rotate 45 degrees, compute distance required to travel to get to the "1,1" mark using trigonometry.
   * Travel there, rotate -45 degrees. Done. Light localize afterwards for greater accuracy.
   */
  public void usLocalize() {
    Reading intialSweep = sweep();

    // Now that we have exited the main loop, cut the motors
    nav.stopMotors();

    Main.sleepFor(GENERAL_SLEEP);

    // Orient the robot in the direction pointing towards the minimum distance
    nav.rotateRobotBy(intialSweep.getDeltaAngle());

    Main.sleepFor(GENERAL_SLEEP);

    // Rotate the robot about 90 degrees
    nav.rotateRobotBy(RIGHT_ANGLE);

    // Now, here the logic of the following:
    // after having turned 90 degrees in the previous step, if the next US sensor reading is large, then the robot must
    // be oriented towards the 0 degree line. If not, then another clockwise 90 degree rotation must be made

    // Start by taking the average of two readings for accuracy
    int dist = readUsDistance();
    if(dist < DIST_THRESHOLD) { // then we are facing against the other wall, at a very close similar minimum distance
      nav.rotateRobotBy(RIGHT_ANGLE);
      Main.sleepFor(GENERAL_SLEEP);
      nav.stopMotors();
    } else { // if not, then we are currently facing the 0 degree line!
      nav.stopMotors();  
    }

    Main.sleepFor(GENERAL_SLEEP); 

    // Note: at this point, the robot should be oriented towards the 0 degrees line (along the defined y-axis)

    // Angle at around 45 degrees away from the y-axis
    nav.rotateRobotBy(HALF_RIGHT_ANGLE);

    Main.sleepFor(GENERAL_SLEEP);

    // Get to (1, 1) point on board
    double distanceToTravel = computeDistanceToOneOne(minReading.getDistance());
    nav.moveStraightFor(distanceToTravel);

    // Now orient wheel towards 0 degrees
    nav.rotateRobotBy(-HALF_RIGHT_ANGLE);

    // Init odometer at (1, 1)
    odometer.setXyt(TILE_SIZE_cm,TILE_SIZE_cm, 0);
    LightLocalization.localize(); //starts taking samples
    Main.sleepFor(GENERAL_SLEEP);
    nav.turnTo_Localizer(0);
    LightLocalization.localize2();

    // done corner localization
    DONE_CORNER_LOCALIZATION = true;

    // We should be all good at this point!
  }

  /**
   * Method to perform a 360 degrees sweep and record each distance readings in a Reading.java object.
   * Used in the usLocalize() method.
   */
  public Reading sweep() {

    while(totalDeltaAngle <= (DEGREES_MAX)) { // use the <= 360 degree angle condition if it works

      leftMotor.setSpeed(MOTOR_LOW);
      rightMotor.setSpeed(MOTOR_LOW);
      leftMotor.forward();
      rightMotor.backward();

      // Setup inputs for Reading object creation
      int d = readUsDistance();

      // Retrieve the angle
      position = odometer.getXyt();
      deltaAngle = position[2];
      totalDeltaAngle = position[3];

      // Initialize a Reading object
      Reading r = new Reading(deltaAngle, d);
      readings.add(r); // append reading data to local arraylist

      minReading = findMinDistance(readings); // find the minimum reading out of the arraylist of Reading objects

    }

    return minReading;
  }

  /**
   * Method to compute the distance remaining to travel by the robot to the (1, 1) point
   */
  public double computeDistanceToOneOne(int minDistance) {
    double hyp = ( (TILE_SIZE_cm - (minDistance + SENSOR_TO_CENTER_DIST))/ ( Math.sin( PI / 4 )) );
    return hyp;
  }

  /**
   * Method to find the minimum reading out of the arraylist of Reading objects.
   * Idea is simply to sort the arraylist by distance attribute and then pick the first element in the list.
   */
  public Reading findMinDistance(ArrayList<Reading> readings) {
    Collections.sort(readings);
    return readings.get(0);
  }

}