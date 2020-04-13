package ca.mcgill.ecse211.project;

import java.util.ArrayList;

/**
 * Class to hold a basic value object coming from the US sensor.
 * We record the distance read by the US sensor and the offset angle from the initial orientation at which the distance was
 * recorded.
 * 
 */
public class Reading implements Comparable {

  /**
   * Offset delta angle from initial orientation.
   */
  private double deltaAngle;
  
  /**
   * Measured distance returned from US sensor.
   */
  private int distance;

  /**
   * Constructor
   */
  public Reading(double deltaAngle, int distance) {
    this.deltaAngle = deltaAngle;
    this.distance = distance;
  }
  /**
   * 
   * @return the angle from the initial orientation of the robot
   */
  public double getDeltaAngle() {
    return this.deltaAngle;
  }
/**
 * 
 * @return the distance returned from US sensor
 */
  public int getDistance() {
    return this.distance;
  }
/**
 * the angle from the initial orientation of the robot is set to deltaAngle 
 * @param deltaAngle is the angle returned from the odometer (in degrees)
 */
  public void setDeltaAngle(double deltaAngle) {
    this.deltaAngle = deltaAngle;
  }
  /**
   * the distance returned from the US sensor for a specific angle from the initial orientation of the robot is set to distance
   * @param distance is the value returned from the US sensor (in cm)
   */
  public void setDistances(int distance) {
    this.distance = distance;
  }
 
  @Override
  public int compareTo(java.lang.Object arg0) {
    // TODO Auto-generated method stub
    return 0;
  }

//  @Override
//  public int compareTo(Object o) {
//    int compared=((Reading) o).getDistance();
//    /* For Ascending order*/
//    return this.distance-compared;
//  }

}
