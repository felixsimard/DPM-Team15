package ca.mcgill.ecse211.project;
//static import to avoid duplicating variables and make the code easier to read
import static ca.mcgill.ecse211.project.Resources.*;
import lejos.hardware.Button;

/**
* The main driver class for the search and rescue procedure.
*/
public class Main {

//-----------------------------------------------------------------------------------------------------------------------  
/**
 * The main entry point.
 * 
 * @param args not used
 */
public static void main(String[] args) {

  //start odometer thread
  new Thread(odometer).start();
  //start display thread
  new Thread(new Display()).start(); 
  //Starts the thread responsible for returning values from ultrasonic sensor
  Sensors.readUsDistance();
  //Starts the thread responsible for returning values from the left wheel colour sensor
  // and the right wheel colur sensor
  Sensors.getSamples_wheelColorSensors();
  // UltrasonicLocalizer insantiation
  UltrasonicLocalizer USLocalization = new UltrasonicLocalizer();
 // Performs ultrasonic localization
  USLocalization.usLocalize();
  
  }
//-----------------------------------------------------------------------------------------------------------------------
/**
 * Sleeps for the specified duration.
 * @param millis the duration in milliseconds
 */
public static void sleepFor(long millis) {           
  try {
    Thread.sleep(millis);
  } catch (InterruptedException e) {
    // Nothing to do here
  }
 }

}       //end of Main class 