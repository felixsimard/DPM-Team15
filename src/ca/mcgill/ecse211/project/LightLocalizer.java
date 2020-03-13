package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;


public class LightLocalizer {

private static SampleProvider lightSensor1 = colourSensor1.getMode("Red");
private static float colour1[] = new float[lightSensor1.sampleSize()];

private static SampleProvider lightSensor2 = colourSensor2.getMode("Red");
private static float colour2[] = new float[lightSensor2.sampleSize()];

private static final int VALUE_OF_BLACK = 13;
private static float value_of_colour1;
private static float value_of_colour2;
private static int line_threshold_L;
private static int line_threshold_R;


//-----------------------------------------------------------------------------------------------------------------------
public static void getSample_localize() {

(new Thread() {
  public void run() {

    while(true) {

      lightSensor1.fetchSample(colour1, 0);
      value_of_colour1 =   colour1[0] * 100;
  //    System.out.println("value of left sensor: " + value_of_colour1);
      lightSensor2.fetchSample(colour2, 0);
      value_of_colour2 =   colour2[0] * 100;
  //    System.out.println("value of right sensor: " + value_of_colour2);
      try {
        Thread.sleep(LIGHTLOCALIZER_SLEEP);
      } catch (InterruptedException e) {
        //no code
      }
    } //end of while loop

  }     //end of run method
}).start();

} //end of localize method    

//-----------------------------------------------------------------------------------------------------------------------
public static void localizeDuringUltrasonicLocalization() {
  
    line_threshold_L = BLACK_LINE_THRESHOLD;            //Big room
    line_threshold_R = BLACK_LINE_THRESHOLD;            //Big room
    
  localize();                       //go straight
  
  UltrasonicLocalizer.turnBy(90);   // turn right
  
  localize();                      // go straight
  
  UltrasonicLocalizer.turnBy(-90);  //turn left
}
//-----------------------------------------------------------------------------------------------------------------------
public static void localizeDuringNavigation() {
  
  line_threshold_L = BLACK_LINE_THRESHOLD;            //Big room
  line_threshold_R = BLACK_LINE_THRESHOLD;            //Big room

  //turn to 0
  //........ (Done in Navigation class)

  // first part of localization   
   localize();

   // //turn by 90;
   UltrasonicLocalizer.turnBy(90);

   // //Second part of light localization
   localize();

   // turn to 0 degree axis
   UltrasonicLocalizer.turnBy(-90);

//}).start();
} //end of localize2 method

//-----------------------------------------------------------------------------------------------------------------------
public static void localize() {
  if ((LightLocalizer.get_value_of_colour1() <= line_threshold_L) && (LightLocalizer.get_value_of_colour2() <= line_threshold_R)) {       //both light sensors on line already
    return;
  }
  
  else if (!(LightLocalizer.get_value_of_colour1() <= line_threshold_L) && (LightLocalizer.get_value_of_colour2() <= line_threshold_R)) {  //right sensor on line already
      while(!(LightLocalizer.get_value_of_colour1() <= line_threshold_L)) { //while the left sensor does not detect a line
        leftMotor.setSpeed(50);
        leftMotor.forward();
      } //end of while loop
      leftMotor.stop(false); // stop the left motor as the left sensor has detected a line
      return;
  } //end of else if
  
else if ((LightLocalizer.get_value_of_colour1() <= line_threshold_L) && !(LightLocalizer.get_value_of_colour2() <= line_threshold_R)) {    //left sensor on line already
 while(!(LightLocalizer.get_value_of_colour2() <= line_threshold_R)) { //while the right sensor does not detect a line
   rightMotor.setSpeed(50);
   rightMotor.forward();
     } //end of while loop
 rightMotor.stop(false); //stop the right motor as the right sensor has detected a line
 return;
 } //end of else if

//--------------------------------------------------------------------------------------------------------------------------------------------    
else {   
 /*
  * neither sensors detect a line initially
  */
// if (reachedStartingPoint) {
 leftMotor.setSpeed(50);
 rightMotor.setSpeed(50);
 leftMotor.rotate(-200,true);
 rightMotor.rotate(-200,false);
// }
 while (!(LightLocalizer.get_value_of_colour1() <= line_threshold_L) && !(LightLocalizer.get_value_of_colour2() <= line_threshold_R)) { //while neither sensor on line
   leftMotor.setSpeed(50);
   rightMotor.setSpeed(50);
   leftMotor.forward();
   rightMotor.forward();
 
 } //end of while loop  , while loop ends when one of the sensors detect a line
  
/*
 * both sensors detect a line
 */
 if((LightLocalizer.get_value_of_colour1() <= line_threshold_L) && (LightLocalizer.get_value_of_colour2() <= line_threshold_R)) {  //both sensors detect a line
   leftMotor.stop(true);
   rightMotor.stop(false);
   return;
 } //end of if
 /*
  * the right sensor detects a line
  */
 else  if (!(LightLocalizer.get_value_of_colour1() <= line_threshold_L) && (LightLocalizer.get_value_of_colour2() <= line_threshold_R)) {      //right sensor detects line
   rightMotor.stop(false); //stop the right motor as the right sensor has detected a line
   while(!(LightLocalizer.get_value_of_colour1() <= line_threshold_R)) { //while the left sensor does not detect a line
     
   } //end of while loop
   leftMotor.stop(false); //stop the left motor from turning
   return;
 } //end of else if

 /*
  * the left sensor detects a line
  */
 else  if ((LightLocalizer.get_value_of_colour1() <= line_threshold_L) && !(LightLocalizer.get_value_of_colour2() <= line_threshold_R)) {      //left sensor detects line
   leftMotor.stop(false); //stop the right motor as the right sensor has detected a line
   while(!(LightLocalizer.get_value_of_colour2() <= line_threshold_R)) { //while the right sensor does not detect a line

     
   } //end of while loop
   rightMotor.stop(false); //stop the left motor from turning
   return;
 } //end of else if
       
} //end of else
} //end of localize method
//-----------------------------------------------------------------------------------------------------------------------
public static float get_value_of_colour1() {
return value_of_colour1;
  }
//-----------------------------------------------------------------------------------------------------------------------
public static float get_value_of_colour2() {
return value_of_colour2;
  }
}
