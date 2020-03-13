package ca.mcgill.ecse211.project;



import lejos.hardware.Sound;
import lejos.utility.Timer;
import lejos.utility.TimerListener;

import static ca.mcgill.ecse211.project.Resources.*;

public class UltrasonicLocalizer implements Runnable {
  
  /**
   * distance calculated by the Ultrasound sensor
   */
  private  int distance;     
  
  /**
   * Buffer (array) to store US samples. Declared as an instance variable to avoid creating a new
   * array each time {@code readUsSample()} is called.
   */
  private float[] usData = new float[usSensor.sampleSize()];                
 
  
  /**
   * The Localization type.
   */
  private String Localization_type;            
  
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
   * Constructor
   */
  public UltrasonicLocalizer(String type) {
  this.Localization_type = type;
  }
 //-------------------------------------------------------------------------------------------------------------------------
  public void run() {
    while (true) {
  
    readUsDistance();                           //this method fetches the distance read by the US sensor and stores it in the class variable called 'distance'
//   if (USLocalDone) {
    if (distance <= 7) {    
      obstacleAvoidanceInProgress = true;
      OBJECT_DETECTED = true; 
      leftMotor.stop(true);     
      rightMotor.stop(false);
      turnBy(90);
      Navigation.moveStraightFor(20);
      turnBy(-90);
      Navigation.moveStraightFor(10);
      turnBy(-90);
      Navigation.moveStraightFor(20);
      turnBy(90);
      OBJECT_DETECTED = false;
      obstacleAvoidanceInProgress = false;
      // travel to next point.
    } //end of inner if loop
//  } // end of outer if loop
    Main.sleepFor(POLL_SLEEP_TIME);              
    }       //end of while loop
  } //end of run method
  //-------------------------------------------------------------------------------------------------------------------------
  /**
   *    In this method, A rising edge is detected, its average theta is calculated,
   *    we then continue rotating in the same direction until a falling edge is etected,
   *    the average theta of the falling edge is then calculated. We then use the equation as explained
   *    in the tutorial notes to calculate delta theta which is then added to the angle of rotation at which the
   *    falling edge is detected ( new theta = delta theta + theta at which the falling edge is detected). We then
   *    turn by new theta in the opposite direction so that the EV3 facing in the correct direction , which is the 0 degree axis.                       
   *   
       In short : Detect a Rising edge, continue rotating in the same direction, detect a falling edge, rotate the EV3 to correct direction ( 0 degree axis)
   */
  public void Rising_Edge () {     
                           
    leftMotor.setSpeed(ROTATION_SPEED);      
    rightMotor.setSpeed(ROTATION_SPEED);       
                                               
    while (distance < D - K) {               //Detect rising edge
      leftMotor.forward();
      rightMotor.backward();
    }
    leftMotor.stop(true);
    rightMotor.stop(false);
     angle1 = odometer.getXyt()[2];         // returns the angle rotated by EV3 from its starting position
     Sound.beep();
    
    
    while (distance < D + K) {      
      leftMotor.forward();
      rightMotor.backward();
    }
    leftMotor.stop(true);
    rightMotor.stop(false);
     angle2 = odometer.getXyt()[2];      // returns the angle rotated by EV3 from its starting position
     Sound.beep();
     avg_angle_12 = (angle1+angle2)/2.0;
    
    
    while (distance > D + K) {             //Detect falling edge
      leftMotor.forward();
      rightMotor.backward();
    }
    leftMotor.stop(true);
    rightMotor.stop(false);
    angle3 = odometer.getXyt()[2];
    Sound.beep();
    
    while (distance > D - K) {     
      leftMotor.forward();
      rightMotor.backward();
    }
    leftMotor.stop(true);
    rightMotor.stop(false);
    angle4 = odometer.getXyt()[2];
    Sound.beep();
    
    avg_angle_34 = (angle3+angle4)/2.0;
    
    if (avg_angle_34 > avg_angle_12) {
      deltaTheta = 50 - (avg_angle_34+avg_angle_12) / 2.0;  //used
    } else {
      deltaTheta = 235 - (avg_angle_34+avg_angle_12) / 2.0;     
    }
           
    double newTheta = odometer.getXyt()[2] + deltaTheta;
    turnBy(-(newTheta));
    
    odometer.setTheta(0);       //EV3 informed that it is on the 0 degree axis 
    
    leftMotor.stop(true);
    rightMotor.stop(false);
    
 //   LightLocalizer.localizeDuringUltrasonicLocalization();  // go to (1,1) and face 0 degree axis
   
  } //end of rising edge method
  
  //--------------------------------------------------------------------------------------

  /**
   *     
   In this method, A falling edge is detected, its average theta is calculated,
   we then continue rotating in the same direction until a rising edge is detected,
   the average theta of the rising edge is then calculated. We then use the equation as explained
   in the tutorial notes to calculate delta theta which is then added to the angle of rotation at which the
   rising edge is detected ( new theta = delta theta + theta at which the rising edge is detected). We then
   turn by new theta in the opposite direction so that the EV3 facing in the correct direction , which is the 0 degree axis.
    
   // In short : Detect a Falling edge, continue rotating in the same direction, detect a rising edge, rotate the EV3 to correct direction ( 0 degree axis)
   */
  public void Falling_Edge () {      
    leftMotor.setSpeed(ROTATION_SPEED);
    rightMotor.setSpeed(ROTATION_SPEED);
    
    while (distance > D + K) {              //Detect falling edge
      leftMotor.forward();
      rightMotor.backward();
    }
    leftMotor.stop(true);
    rightMotor.stop(false);
     angle3 = odometer.getXyt()[2];
     Sound.beep();
    
    
    while (distance > D - K) {      
      leftMotor.forward();
      rightMotor.backward();
    }
    leftMotor.stop(true);
    rightMotor.stop(false);
     angle4 = odometer.getXyt()[2];
     Sound.beep();
     
     avg_angle_34 = (angle3+angle4)/2.0;
    
    
    while (distance < D - K) {             //Detect rising edge
      leftMotor.forward();
      rightMotor.backward();
    }
    leftMotor.stop(true);
    rightMotor.stop(false);
    angle1 = odometer.getXyt()[2];
    Sound.beep();
    
    while (distance < D + K) {     
      leftMotor.forward();
      rightMotor.backward();
    }
    leftMotor.stop(true);
    rightMotor.stop(false);
    angle2 = odometer.getXyt()[2];
    Sound.beep();
    
    avg_angle_12 = (angle1+angle2)/2.0;
    
    if (avg_angle_34 > avg_angle_12) {
      deltaTheta = 52 - (avg_angle_34 + avg_angle_12) / 2.0;
    } else {
      deltaTheta = 230 - ((avg_angle_34 + avg_angle_12)) / 2.0;     //used
    }
    double newTheta = odometer.getXyt()[2] + deltaTheta;
    turnBy(-(newTheta));
    
    odometer.setTheta(0);          //EV3 informed that it is on the 0 degree axis 
   
    leftMotor.stop(true);
    rightMotor.stop(false);   
    
//    LightLocalizer.localizeDuringUltrasonicLocalization();  // go to (1,1) and face 0 degree axis
    
  }     //end of Falling edge method
  
  //------------------------------------------------------------------------------------------------------------------------- 
  /**
   * Returns the distance between the US sensor and an obstacle in cm.
   * 
   * @return the distance between the US sensor and an obstacle in cm
   */
  public int readUsDistance() {         
    usSensor.fetchSample(usData, 0);  
    // extract from buffer, convert to cm, cast to int, and filter
    return distance = ((int) (usData[0] * 100.0));
  }
  //-------------------------------------------------------------------------------------------------------------------------
  /**
   * 
   * @return Returns the distance calculated by the Ultrasonic sensor
   */
  public  int getDistance() {     
    return distance;
  }
  //-------------------------------------------------------------------------------------------------------------------------
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
  //------------------------------------------------------------------------------------------------------------------------- 
  /**
   * Converts input angle to the total rotation of each wheel needed to rotate the robot by that angle.
   * 
   * @param angle the input angle
   * @return the wheel rotations necessary to rotate the robot by the angle
   */
  public static int convertAngle(double angle) {
    return convertDistance(Math.PI * BASE_WIDTH * angle / 360.0);
  }
  //-------------------------------------------------------------------------------------------------------------------------
  /**
   * Converts input distance to the total rotation of each wheel needed to cover that distance.
   * 
   * @param distance the input distance
   * @return the wheel rotations necessary to cover the distance
   */
  public static int convertDistance(double distance) {
    return (int) ((180.0 * distance) / (Math.PI * WHEEL_RADIUS));
  }
}