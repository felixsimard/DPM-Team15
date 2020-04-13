package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;
/**
 *This class contains methods that are responsible for performing light localization at the current position of the robot
 */
public class LightLocalizer {
/**
 * This value is used by the left wheel colour sensor to determine whether it has detected a line or not, this value is different
 * depending on the colour of the line
 */
private static int line_threshold_L;
/**
 * This value is used by the right wheel colour sensor to determine whether it has detected a line or not, this value is different
 * depending on the colour of the line
 */
private static int line_threshold_R;

//-----------------------------------------------------------------------------------------------------------------------
/**
 * This method performs light localization at the robots current gridpoint position.
 * As light localization is a procedure with multiple steps, within this method, multiple methods are called to complete
 * light localization. Some of these methods include : turnTo_LightLocalizer() from the Navigation class, turnBy() from the
 * Navigation class, and Light_Localization_PartTwo() from the LightLocalizer class
 * 
 */
public static void Light_Localization_PartOne() {

  line_threshold_L = BLACK_LINE_THRESHOLD;           
  line_threshold_R = BLACK_LINE_THRESHOLD;          

  //turn to 0
  Navigation.turnTo_LightLocalizer(0);

  // first phase of localization   
  Light_Localization_PartTwo();

   //turn by 90;
   Navigation.turnBy(90);

   // Second phase of light localization
   Light_Localization_PartTwo();

   // turn to 0 degree axis
   Navigation.turnBy(-90);

//}).start();
} //end of localize2 method

//-----------------------------------------------------------------------------------------------------------------------
/**
 * In this method, the robot positions itself such that its left wheel colour sensor and its right wheel colour sensor
 * are directly above the line on the grid.
 */
public static void Light_Localization_PartTwo() {

 /*
  * neither sensors detect a line initially
  */
 leftMotor.setSpeed(50);
 rightMotor.setSpeed(50);
 leftMotor.rotate(-200,true);
 rightMotor.rotate(-200,false);

 while (!(Sensors.get_value_of_colour_leftwheel() <= line_threshold_L) && !(Sensors.get_value_of_colour_rightwheel() <= line_threshold_R)) { //while neither sensor on line
   leftMotor.setSpeed(50);
   rightMotor.setSpeed(50);
   leftMotor.forward();
   rightMotor.forward();
 
 } //end of while loop  , while loop ends when one of the sensors detect a line
  
/*
 * both sensors detect a line
 */
 if((Sensors.get_value_of_colour_leftwheel() <= line_threshold_L) && (Sensors.get_value_of_colour_rightwheel() <= line_threshold_R)) {  //both sensors detect a line
   leftMotor.stop(true);
   rightMotor.stop(false);
   return;
 } //end of if
 /*
  * the right sensor detects a line
  */
 else  if (!(Sensors.get_value_of_colour_leftwheel() <= line_threshold_L) && (Sensors.get_value_of_colour_rightwheel() <= line_threshold_R)) {      //right sensor detects line
   rightMotor.stop(false); //stop the right motor as the right sensor has detected a line
   while(!(Sensors.get_value_of_colour_leftwheel() <= line_threshold_R)) { //while the left sensor does not detect a line
     
   } //end of while loop
   leftMotor.stop(false); //stop the left motor from turning
   return;
 } //end of else if

 /*
  * the left sensor detects a line
  */
 else  if ((Sensors.get_value_of_colour_leftwheel() <= line_threshold_L) && !(Sensors.get_value_of_colour_rightwheel() <= line_threshold_R)) {      //left sensor detects line
   leftMotor.stop(false); //stop the right motor as the right sensor has detected a line
   while(!(Sensors.get_value_of_colour_leftwheel() <= line_threshold_R)) { //while the right sensor does not detect a line

     
   } //end of while loop
   rightMotor.stop(false); //stop the left motor from turning
   return;
 } //end of else if
       
//} //end of else
} //end of localize method

}
