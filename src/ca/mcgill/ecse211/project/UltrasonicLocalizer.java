package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class to manage and record values of the ultrasonic sensor.
 * This class is also responsible for performing Ultrasonic Localization
 */
public class UltrasonicLocalizer {
  
  /**
   *  ArrayList to store all the readings object
   */
  public static  ArrayList<Reading> readings = new ArrayList<Reading>();

  /**
   * Buffer (array) to store US samples. Declared as an instance variable to avoid creating a new
   * array each time {@code readUsSample()} is called.
   */
           
  /**
   * To hold the x, y, t values of the odometer
   */
  private double[] position;

  /**
   * Offset angle, values between 0-359
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
  public UltrasonicLocalizer() {
  }


  /**
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
    stopMotors();

    Main.sleepFor(GENERAL_SLEEP);

    // Orient the robot in the direction pointing towards the minimum distance
    Navigation.turnBy(intialSweep.getDeltaAngle());

    Main.sleepFor(GENERAL_SLEEP);

    // Rotate the robot about 90 degrees
    Navigation.turnBy(RIGHT_ANGLE);

    // Now, here the logic of the following:
    // after having turned 90 degrees in the previous step, if the next US sensor reading is large, then the robot must
    // be oriented towards the 0 degree line. If not, then another clockwise 90 degree rotation must be made

    // Start by taking the average of two readings for accuracy
    int dist = Sensors.getDistance();
    if(dist < DIST_THRESHOLD) { // then we are facing against the other wall, at a very close similar minimum distance
      Navigation.turnBy(RIGHT_ANGLE);
      Main.sleepFor(GENERAL_SLEEP);
      stopMotors();
    } else { // if not, then we are currently facing the 0 degree line!
      stopMotors();  
    }

    Main.sleepFor(GENERAL_SLEEP); 

    // Note: at this point, the robot should be oriented towards the 0 degrees line (along the defined y-axis)

    // Angle at around 45 degrees away from the y-axis
    Navigation.turnBy(HALF_RIGHT_ANGLE);

    Main.sleepFor(GENERAL_SLEEP);

    // Get to (1, 1) point on board
    double distanceToTravel = computeDistanceToOneOne(minReading.getDistance());
    Navigation.moveStraightForWithoutStopping(distanceToTravel);

    // Now orient wheel towards 0 degrees
    Navigation.turnBy(-HALF_RIGHT_ANGLE);

    // Init odometer at (1, 1)
    odometer.setXyt(TILE_SIZE_cm,TILE_SIZE_cm, 0);

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
      int d = Sensors.getDistance();

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

  /**
   * Stops both the left wheel motor and the right wheel motor
   */
  public void stopMotors() {
    leftMotor.stop();
    rightMotor.stop();
  }
  
} //end of UltrasonicLocalizer class