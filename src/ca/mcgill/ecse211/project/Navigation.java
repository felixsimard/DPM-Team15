package ca.mcgill.ecse211.project;
import static ca.mcgill.ecse211.project.Resources.*;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import static ca.mcgill.ecse211.project.Main.sleepFor;

/**
 * Class to handle basic navigation methods and navigating between waypoints.
 */
public class Navigation extends Thread {

  //private UltrasonicSensor usSensor = new UltrasonicSensor();

  /**
   * the x displacement of our desired destination in feet
   */
  int xDest_FT;
  /**
   * the y displacement of our desired destination in feet  
   */
  int yDest_FT;  
  /**
   * the current x displacement of the EV3 in cm
   */
  double xCurrent_CM;
  /**
   * the current y displacement of the EV3 in cm
   */
  double yCurrent_CM;
  /**
   * the current clockwise angle of the EV3 from the 0 axis
   */
  double thetaCurrent_RAD; //angle from vertical CW
  double thetacurrent_RAD_Localizer;
  /**
   *  the displacement change required in the x-axis to reach our desired destination
   */
  double displX;
  /**
   *  the displacement change required in the y-axis to reach our desired destination
   */
  double displY;
  /**
   * the angle from the 0 axis that we need to rotate to in order to go in the direction of our desired destination
   */
  double displTheta_RAD;
  /**
   * the distance in cm we need to travel to reach our desired destination
   */
  double distance_needed_to_cover;  

  /**
   * Main run method of the navigation class.
   */
  public void run( ) {

    // Main run method


  }

  /**
   * This method takes as input the angle we wish to turn to, it then turns the EV3 to our desired angle
   * @param angle_RAD
   */
  public void turnTo(double angle_RAD) {           //has to turn by minimal angle


    double deltaT = angle_RAD*(180/Math.PI) -  thetaCurrent_RAD*(180/Math.PI);

    if (deltaT >= 0 && deltaT <= 180 ) {
      turnBy(deltaT);          
    }
    else if (deltaT > 180 ) {
      turnBy(deltaT -360 );
    }
    else if (deltaT < 0 && deltaT > -180 ) {
      turnBy(deltaT);
    }
    else if (deltaT < 0 && deltaT < -180 ) {
      turnBy(deltaT + 360);
    }

  }

  public void turnTo_Localizer(double angle_RAD) {           //has to turn by minimal angle

    thetacurrent_RAD_Localizer = odometer.getXyt()[2] * RADS_PER_1DEG;
    double deltaT = angle_RAD*(180/Math.PI) -  thetacurrent_RAD_Localizer*(180/Math.PI);

    if (deltaT >= 0 && deltaT <= 180 ) {
      turnBy(deltaT);          
    }
    else if (deltaT > 180 ) {
      turnBy(deltaT -360 );
    }
    else if (deltaT < 0 && deltaT > -180 ) {
      turnBy(deltaT);
    }
    else if (deltaT < 0 && deltaT < -180 ) {
      turnBy(deltaT + 360);
    }


  } 


  /**
   * This method takes in the (x,y) coordinates of where we want to go, it then causes the EV3 to rotate and move to that specific
   * coordinate.
   * @param x
   * @param y
   * @return void
   */
  public void travelTo(int x,int y) {   

    // Update global waypoint destination coordinates
    NEXT_X = x;
    NEXT_Y = y;

    xDest_FT= x; //the x position (in feet) we want to reach
    yDest_FT= y; // the y position (in feet) we want to reach
    //get current position
    xCurrent_CM=odometer.getXyt()[0];   // our current x position in cm
    yCurrent_CM=odometer.getXyt()[1];   // our current y position in cm
    thetaCurrent_RAD=odometer.getXyt()[2] * RADS_PER_1DEG ;  // our current angle from the 0 degree axis

    displX= xDest_FT*TILE_SIZE_cm - xCurrent_CM;    //displX = the distance we need to travel in the x axis to reach where we want
    displY= yDest_FT*TILE_SIZE_cm - yCurrent_CM;    // displY = the distance we need to travel in the y axis to reach where we want

    if (displX != 0 && displY != 0)  {            // if we do not want to stay in the same position then..

      //1st quadrant 
      if (displX>0 && displY>0) {                                 
        displTheta_RAD=PI/2.0 - Math.atan(displY/displX);
        distance_needed_to_cover =  Math.sqrt((displX*displX) + (displY*displY));
        turnTo(displTheta_RAD);
        moveStraightFor(distance_needed_to_cover);
      }
      //2nd quadrant
      else if (displX<0 && displY>0)                             
      {
        displTheta_RAD=1.5*PI + Math.atan(Math.abs(displY/displX)); // pi + (pi/2+angle) 
        distance_needed_to_cover =  Math.sqrt((displX*displX) + (displY*displY));
        turnTo(displTheta_RAD);
        moveStraightFor(distance_needed_to_cover);
      }
      //3nd quadrant
      else if (displX<0 && displY<0)                             
      {
        displTheta_RAD =1.5*PI-Math.atan(Math.abs(displY/displX));  // pi + (pi/2-angle) 
        distance_needed_to_cover =  Math.sqrt((displX*displX) + (displY*displY));
        turnTo(displTheta_RAD);
        moveStraightFor(distance_needed_to_cover);
      }
      //4th quadrant 
      else                                                        
      {  
        displTheta_RAD=0.5*PI+ Math.atan(Math.abs(displY/displX));
        distance_needed_to_cover =  Math.sqrt((displX*displX) + (displY*displY));// (pi/2) + angle 
        turnTo(displTheta_RAD);
        moveStraightFor(distance_needed_to_cover);
      }

    } //end of if statement

    //vertical displacement 
    else if (displX==0)                                 
    {
      if     (displY>=0) displTheta_RAD=0;  //displacement forward
      else if(displY<0)  displTheta_RAD=PI; //displacement backward
      distance_needed_to_cover =  displY;
      turnTo(displTheta_RAD);
      moveStraightFor(distance_needed_to_cover);
    }
    //horizontal displacement
    else if (displY==0)                     
    {
      if     (displX>0)   displTheta_RAD=PI/2.0; //displacement to the right
      else if(displX<0)   displTheta_RAD=1.5*PI; //displacement to the left 
      distance_needed_to_cover =  displX;
      turnTo(displTheta_RAD);
      moveStraightFor(distance_needed_to_cover);
    }

  } //end of travelTo method

  /**
   * Moves the robot straight for the given distance.
   * 
   * @param distance in feet (tile sizes), may be negative
   */
  public static void moveStraightFor(double distance) {

    // get tacho count values into some variables
    int initialTacho = leftMotor.getTachoCount();
    int requiredTacho = convertDistance(distance);
    int currentTacho = initialTacho;

    // move straight until we cover the required distance to the next waypoint
    while((currentTacho - initialTacho < requiredTacho)) {

      // continuously check if we detect and object using the global flag
      while(HAS_DETECTED_OBJECT) {
        //stopMotors();
        continue;
      }

      leftMotor.setSpeed(MOTOR_LOW);
      rightMotor.setSpeed(MOTOR_LOW);
      leftMotor.forward();
      rightMotor.forward();

      // update current tacho count
      currentTacho = leftMotor.getTachoCount();

    }

  }

  /**
   * Stops both motors.
   */
  public static void stopMotors() {
    leftMotor.setSpeed(0);
    rightMotor.setSpeed(0);
    //    leftMotor.stop();
    //    rightMotor.stop();
  }

  /* 
   * Some basic navigation methods.
   */
  public static void turnBy(double angle) {
    leftMotor.rotate(convertAngle(angle), true);
    rightMotor.rotate(-convertAngle(angle), false);
  }
  
  /**
   * Convert angle to distance.
   * @param angle
   * @return
   */
  public static int convertAngle(double angle) {
    return convertDistance(Math.PI * BASE_WIDTH * angle / 360.0);
  }
  /**
   * Convert distance to angle.
   * @param distance
   * @return
   */
  public static int convertDistance(double distance) {
    return (int) ((180.0 * distance) / (Math.PI * WHEEL_RADIUS));
  }
  
  /**
   * Helper wrapper method to properly make the robot rotate by a certain angle.
   * @param angle
   */
  public void rotateRobotBy(double angle) {
    leftMotor.setSpeed(MOTOR_LOW);
    rightMotor.setSpeed(MOTOR_LOW);
    leftMotor.forward();
    rightMotor.backward();
    turnBy(angle); // angle in degrees
  }



}